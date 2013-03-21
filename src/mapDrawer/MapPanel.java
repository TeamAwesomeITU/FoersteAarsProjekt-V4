package mapDrawer;

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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MapPanel extends JPanel {

	private Dimension preferredSize = setPreferredSize();
	private RectZoomer rectZoomer;
	private AreaToDraw area;
	private EdgeLine[] linesOfEdges; 
	private JFrame jf;
	
	
	public static void main(String[] args) {        
	    createJFrame();
	} 
	
	private static JFrame createJFrame() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		JFrame jf = new JFrame("test");
	    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jf.setSize(screenSize);
	    jf.setExtendedState(Frame.MAXIMIZED_BOTH);
	    jf.add(new JScrollPane(new MapPanel(jf)));
	    
	    jf.setVisible(true);
	    return jf;
	}

	public MapPanel(JFrame jf) {
		this.jf = jf;
		rectZoomer = new RectZoomer(this);
	    makeLinesForMap();
	    // mouse listener to detect scrollwheel events
        addMouseListener(rectZoomer);
        addMouseMotionListener(rectZoomer);
	}
	
	private void makeLinesForMap() {
		if(area == null)
			area = new AreaToDraw();
	    HashSet<Edge> edgeSet = FindRelevantNodes.findNodesToDraw(area);
	    Iterator<Edge> edgeSetIterator = edgeSet.iterator();
	    linesOfEdges = new EdgeLine[edgeSet.size()];
	    setPanelDimensions();
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
	
	private void setPanelDimensions() {
		double whRelation = area.getWidthHeightRelation();
		double width = preferredSize.getHeight()*(whRelation);
		preferredSize.setSize(width, preferredSize.getHeight());
	}
	
	private static Dimension setPreferredSize() {
		Dimension tmpSize = Toolkit.getDefaultToolkit().getScreenSize();
		double w = tmpSize.getWidth() * 0.90;
		double h = tmpSize.getHeight() * 0.90;
		tmpSize.setSize(w, h);
		return tmpSize;
	}
	
	public Dimension getPreferredSize() {
	    return preferredSize;
	}
	
	private void updatePreferredSize(int n, Point p) {
	    double d = (double) n * 1.08;
	    d = (n > 0) ? 1 / d : -d;
	
	    int w = (int) (getWidth() * d);
	    int h = (int) (getHeight() * d);
	    preferredSize.setSize(w, h);
	    
	    int offX = (int)(p.x * d) - p.x;
	    int offY = (int)(p.y * d) - p.y;
	    setLocation(getLocation().x-offX,getLocation().y-offY);
	    getParent().doLayout();
	}
	
	/**
	 * Updates the size of the rectangles to match the zoom level.
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
	        ((Graphics2D)g).draw(line); 								  									  //Tegner en linje med koordinaterne (x1,y1,x2,y2)
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
	
	public void setLinesForMap() {
		makeLinesForMap();
	}
	
	public JFrame getParentFrame() {
		return jf;
	}
}
