package mapDrawer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	
	@Deprecated
	public static void main(String[] args) {        
	    createJFrame();
	} 
	
	@Deprecated
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

	public MapPanel(JFrame jf, double width, double height) {
		this.jf = jf;
		this.height = height;
		this.width = width;
		System.out.println("height: " + height + " width: " + width);
		preferredSize = setNewPreferredSize((int)width, (int)height);
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
	    
	    String file = "resources/denmark_coastline_fullres_shore.xyz_convertedJCOORD.txt";

	    ArrayList<EdgeLine> list = new ArrayList<EdgeLine>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
		    String line1 = reader.readLine();
		    String line2 = reader.readLine();

			while(line1 != null && line2 != null)
			{
				String[] coordFrom = line1.split("\\s+");
				String[] coordTo = line2.split("\\s+");
				double coordFromX = coordConverter.KrakToDrawCoordX(Double.parseDouble(coordFrom[0]));
				double coordFromY = coordConverter.KrakToDrawCoordY(Double.parseDouble(coordFrom[1]));
				double coordToX = coordConverter.KrakToDrawCoordX(Double.parseDouble(coordTo[0]));
				double coordToY = coordConverter.KrakToDrawCoordY(Double.parseDouble(coordTo[1]));

				double deltaX = Double.parseDouble(coordFrom[0])-Double.parseDouble(coordTo[0]);
				double deltaY = Double.parseDouble(coordFrom[1])-Double.parseDouble(coordTo[1]);
				double distanceBetweenPoints = Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));

				//If the points are not unreasonably far away from each other, then make a new line
				if(distanceBetweenPoints < 7000)
					list.add(new EdgeLine(coordFromX, coordFromY, coordToX, coordToY, 1));

				line2 = line1;
				line1 = reader.readLine();

			}

			EdgeLine[] newLinesOfEdges = new EdgeLine[linesOfEdges.length + list.size()];
			for(int i = 0; i < newLinesOfEdges.length; i++)
			{
				if(i < linesOfEdges.length)
						newLinesOfEdges[i] = linesOfEdges[i];
				else
						newLinesOfEdges[i] = list.get(i-linesOfEdges.length);
			}

			linesOfEdges = newLinesOfEdges;

			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
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
