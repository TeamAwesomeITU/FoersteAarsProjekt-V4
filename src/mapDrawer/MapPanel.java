package mapDrawer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class MapPanel extends JPanel {

	private Dimension preferredSize;
	private RectZoomer rectZoomer;
	private AreaToDraw area;
	private EdgeLine[] linesOfEdges; 
	private JFrame jf;
	private double height, width;
	
	public static void main(String[] args) {        
	    createJFrame();
	} 
	
	private static JFrame createJFrame() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		JFrame jf = new JFrame();
	    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jf.setSize(screenSize);
	    jf.setExtendedState(Frame.MAXIMIZED_BOTH);
	    BoxLayout boxL = new BoxLayout(jf.getContentPane(), BoxLayout.X_AXIS);
        jf.getContentPane().setLayout(boxL); 
        MapPanel mp = new MapPanel(jf, 600, 800);
        mp.setAlignmentY(0);
		jf.add(mp, 0);
		System.out.println("Height: "+ mp.getHeight());
		System.out.println("Width: " + mp.getWidth());
		jf.pack();
	    jf.setVisible(true);
	    return jf;
	}

	public MapPanel(JFrame jf, int height, double width) {
		this.jf = jf;
		this.height = height;
		this.width = width;
		System.out.println("height: " + height + " width: " + width);
		preferredSize = setNewPreferredSize(width, height);
		rectZoomer = new RectZoomer(this);
	    makeLinesForMap();
        setBorderForPanel(this);
        addMouseListener(rectZoomer);
        addMouseMotionListener(rectZoomer);
	}
	
	private void makeLinesForMap() {
		if(area == null)
			area = new AreaToDraw();
	    HashSet<Edge> edgeSet = FindRelevantNodes.findNodesToDraw(area);
	    Iterator<Edge> edgeSetIterator = edgeSet.iterator();
	    linesOfEdges = new EdgeLine[edgeSet.size()];
	    setPanelDimensions(new Dimension());
	    CoordinateConverter coordConverter = new CoordinateConverter((int)preferredSize.getWidth(), (int)preferredSize.getHeight(), area);
	    
	    int numberOfEdges = 0;
	    while(edgeSetIterator.hasNext() == true) {
	    	Edge edge = edgeSetIterator.next();
	    	int fromNode = edge.getFromNode();
	    	int toNode = edge.getToNode();
	    	Double[] fromNodeCoords = FindRelevantNodes.getNodeCoordinatesMap().get(fromNode);
	    	Double[] toNodeCoords = FindRelevantNodes.getNodeCoordinatesMap().get(toNode);
	    	double drawFromCoordX = coordConverter.KrakToDrawCoordX(fromNodeCoords[0]);
	    	double drawFromCoordY = coordConverter.KrakToDrawCoordY(fromNodeCoords[1]);
	    	double drawToCoordX = coordConverter.KrakToDrawCoordX(toNodeCoords[0]);
	    	double drawToCoordY = coordConverter.KrakToDrawCoordY(toNodeCoords[1]);
	    	
	    	linesOfEdges[numberOfEdges++] = new EdgeLine(drawFromCoordX, drawFromCoordY, drawToCoordX, drawToCoordY, edge.getRoadType());
	    }
	}

	/**
	 * Draws all the lines for the map. Also, draws the rectangle used by the user
	 * to see where you are about to zoom.
	 */
	private Line2D line = new Line2D.Float();
	public void paint(Graphics g) {
	    super.paint(g);
	    for (EdgeLine edgeLine : linesOfEdges) {
	        line.setLine(edgeLine.getStartX(), edgeLine.getStartY(), edgeLine.getEndX(), edgeLine.getEndY());
	        int roadType = edgeLine.getRoadType();
	        
	        if(roadType == 1 || roadType == 21 || roadType == 31 || roadType == 41 
	            || roadType == 2 || roadType == 22 || roadType == 32 || roadType == 42) 
	        	g.setColor(Color.red);
	        else if(roadType == 3 || roadType == 23 || roadType == 33 || roadType == 4 || roadType == 24 || roadType == 34) 
	        	g.setColor(Color.blue);
	        else if(roadType == 8 || roadType == 10 || roadType == 11)
	        	g.setColor(Color.green);
	        else 
	        	g.setColor(Color.black);
	        ((Graphics2D)g).draw(line); 								  									 
	    }
	    Graphics2D g2 = (Graphics2D) g;
	    if (rectZoomer.getRect() == null) {
	         return; 
	      } 
	      else if (rectZoomer.isDrawing() == true) {
	         g2.setColor(Color.red);
	         g2.draw(rectZoomer.getRect());
	      } 
	}
	
	public void setArea(AreaToDraw area) {
		this.area = area;
	}
	
	public AreaToDraw getArea() {
		return area;
	}
	
	
	public JFrame getParentFrame() {
		return jf;
	}
	
	private void setBorderForPanel(MapPanel mp) {
		Dimension d = setNewPreferredSize((int)mp.getMapWidth(), (int)getMapHeight());
		d = mp.setPanelDimensions(new Dimension());
        d.setSize(d.getWidth()*1.02, d.getHeight()*1.02);
        mp.setMaximumSize(d);
        mp.setBorder(new LineBorder(Color.black));
		
	}
	/*
	 * Takes a Dimension and makes it's width and height match the relation between area's width and height.
	 * Is also used to adjust the size of the map to a size that matches this relation.
	 */
	private Dimension setPanelDimensions(Dimension d) {
		if(height < width) {
			double whRelation = area.getWidthHeightRelation();
			double newWidth = height*(whRelation);
			if(newWidth > width) {
				height = height*0.9;
				width = width*0.9;
				setPanelDimensions(d);
			}
			else {
				d.setSize(newWidth, height);
				return d;
			}
		}
		
		else {
			double newHeight = (width*area.getHeight())/area.getWidth();
			if(newHeight > height) {
				height = height*0.9;
				width = width*0.9;
				setPanelDimensions(d);
			}
			else {
				d.setSize(width, newHeight);
				return d;
			}
		}
		return d;
	}
	
	public void setLinesForMap() {
		makeLinesForMap();
	}
	
	//TODO Make it get proper width and height.
	/*
	 * Is used for setting the initial size of the map.
	 */
	private static Dimension setNewPreferredSize(int w, int h) {
		Dimension tmpSize = new Dimension(w, h);
		return tmpSize;
	}
	
	public Dimension getPreferredSize() {
	    return preferredSize;
	}
	public double getMapWidth() {
		return width;
	}
	public double getMapHeight() {
		return height;
	}
}