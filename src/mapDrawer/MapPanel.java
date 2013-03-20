package mapDrawer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MapPanel extends JPanel {

	private Dimension preferredSize = setPreferredSize();
	private Rectangle2D[] rects = new Rectangle2D[50];
	private RectZoomer rectZoomer;
	private AreaToDraw area;
	private Line2D[] linesOfEdges; 
	private JFrame jf;
	
	public static void main(String[] args) {        
	    JFrame jf = createJFrame();
	}    

	public MapPanel(JFrame jf) {
		this.jf = jf;
		rectZoomer = new RectZoomer(this);
	    for (int i=0; i<rects.length; i++) {
	        rects[i] = new Rectangle2D.Double(
	                Math.random()*.8, Math.random()*.8, 
	                Math.random()*.2, Math.random()*.2);
	    }
	    area = new AreaToDraw();
	    HashSet<Edge> edgeSet = FindRelevantNodes.findNodesToDraw(area);
	    Iterator<Edge> edgeSetIterator = edgeSet.iterator();
	    linesOfEdges = new Line2D[edgeSet.size()];
	    //System.out.println(linesOfEdges.length);
	    //System.out.println(preferredSize.getWidth() + " " + preferredSize.getHeight());
	    CoordinateConverter coordConverter = new CoordinateConverter((int)preferredSize.getWidth(), (int)preferredSize.getHeight(), area);
	    
	    int numberOfEdges = 0;
	    while(edgeSetIterator.hasNext() == true) {
	    	Edge edge = edgeSetIterator.next();
	    	//System.out.println(edge.getRoadName());
	    	int fromNode = edge.getFromNode();
	    	int toNode = edge.getToNode();
	    	Double[] fromNodeCoords = FindRelevantNodes.getNodeCoordinatesMap().get(fromNode);
	    	Double[] toNodeCoords = FindRelevantNodes.getNodeCoordinatesMap().get(toNode);
	    	
	    	double drawFromCoordX = coordConverter.KrakToDrawCoordX(fromNodeCoords[0]);
	    	double drawFromCoordY = coordConverter.KrakToDrawCoordY(fromNodeCoords[1]);
	    	
	    	double drawToCoordX = coordConverter.KrakToDrawCoordX(toNodeCoords[0]);
	    	double drawToCoordY = coordConverter.KrakToDrawCoordY(toNodeCoords[1]);
	    	
	    	System.out.println("startX: " + drawFromCoordX);
	    	System.out.println("startY: " + drawFromCoordY);
	    	System.out.println("endX: " + drawToCoordX);
	    	System.out.println("endY: " + drawToCoordY);
	    	
	    	
	    	linesOfEdges[numberOfEdges++] = new Line2D.Double(drawFromCoordX, drawFromCoordY, drawToCoordX, drawToCoordY);
	    }
	    // mouse listener to detect scrollwheel events
        addMouseListener(rectZoomer);
        addMouseMotionListener(rectZoomer);
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
	
	private void drawLinesFromEdges(double[] linesOfEdges) {
		for (int i=0; i<linesOfEdges.length; i++) {
	       
		}
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
	
	public Dimension getPreferredSize() {
	    return preferredSize;
	}
	
	private static Dimension setPreferredSize() {
		Dimension tmpSize = Toolkit.getDefaultToolkit().getScreenSize();
		double w = tmpSize.getWidth() * 0.90;
		double h = tmpSize.getHeight() * 0.90;
		tmpSize.setSize(w, h);
		return tmpSize;
	}
	
	/**
	 * Updates the size of the rectangles to match the zoom level.
	 */
	private Line2D line = new Line2D.Float();
	public void paint(Graphics g) {
	    super.paint(g);
	    g.setColor(Color.black);
	    for (Line2D edgeLine : linesOfEdges) {
	        line.setLine(edgeLine.getX1(), edgeLine.getY1(), edgeLine.getX2(), edgeLine.getY2());      
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
}
