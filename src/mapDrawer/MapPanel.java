package mapDrawer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class MapPanel extends JPanel {

	private Dimension preferredSize;
	private RectZoomer rectZoomer;
	private AreaToDraw area;
	private EdgeLine[] linesOfEdges; 
	private JFrame jf;
	private double height, width;

	/**
	 * The constructor of MapPanel. Initializes the MapPanel, size and lines for the map.
	 *  
	 * @param jf - This is the JFrame which the MapPanel works with.
	 * @param width - The width of the panel.
	 * @param height - The heigth of the panel
	 */
	public MapPanel(JFrame jf, double width, double height) {
		this.jf = jf;
		this.height = height;
		this.width = width;
		preferredSize = setNewPreferredSize((int)width, (int)height);
		rectZoomer = new RectZoomer(this);
	    makeLinesForMap();
        setBorderForPanel(this);
        addMouseListener(rectZoomer);
        addMouseMotionListener(rectZoomer);
	}
	
	/**
	 * Draws the lines for the map. 
	 * Saves all the edges and converts the coordinates and saves them in an array.
	 *  
	 */
	private void makeLinesForMap() {
		if(area == null)
			area = new AreaToDraw();
	    HashSet<Edge> edgeSet = FindRelevantNodes.findNodesToDraw(area);
	    Iterator<Edge> edgeSetIterator = edgeSet.iterator();
	    linesOfEdges = new EdgeLine[edgeSet.size()];
	    //setPanelDimensions(new Dimension());
	    System.out.println();
	    CoordinateConverter coordConverter = new CoordinateConverter((int)preferredSize.getWidth(), (int)preferredSize.getHeight(), area);

	    int numberOfEdges = 0;
	    while(edgeSetIterator.hasNext() == true) {
	    	Edge edge = edgeSetIterator.next();
	    	int fromNode = edge.getFromNode();
	    	int toNode = edge.getToNode();
	    	Double[] fromNodeCoords = FindRelevantNodes.getNodeCoordinatesMap().get(fromNode);
	    	Double[] toNodeCoords = FindRelevantNodes.getNodeCoordinatesMap().get(toNode);
	    	double drawFromCoordX = coordConverter.UTMToPixelCoordX(fromNodeCoords[0]);
	    	double drawFromCoordY = coordConverter.UTMToPixelCoordY(fromNodeCoords[1]);
	    	double drawToCoordX = coordConverter.UTMToPixelCoordX(toNodeCoords[0]);
	    	double drawToCoordY = coordConverter.UTMToPixelCoordY(toNodeCoords[1]);

	    	linesOfEdges[numberOfEdges++] = new EdgeLine(drawFromCoordX, drawFromCoordY, drawToCoordX, drawToCoordY, edge.getRoadType());
	    }

	    //String file = "resources/denmark_coastline_fullres_shore.xyz_convertedJCOORD.txt";
	    //String file = ("resources/osm_modified.txt_convertedJCOORD.txt");
	    String file = ("resources/denmark_coastline_fullres_shore_waaaaay_to_largeOfAnArea_shore.xyz_convertedJCOORD.txt");

	    ArrayList<EdgeLine> list = new ArrayList<EdgeLine>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
		    String line1 = reader.readLine();
		    String line2 = reader.readLine();

			while(line1 != null && line2 != null)
			{
				String[] coordFrom = line1.split("\\s+");
				String[] coordTo = line2.split("\\s+");
				double coordFromX = coordConverter.UTMToPixelCoordX(Double.parseDouble(coordFrom[0]));
				double coordFromY = coordConverter.UTMToPixelCoordY(Double.parseDouble(coordFrom[1]));
				double coordToX = coordConverter.UTMToPixelCoordX(Double.parseDouble(coordTo[0]));
				double coordToY = coordConverter.UTMToPixelCoordY(Double.parseDouble(coordTo[1]));

				double deltaX = Double.parseDouble(coordFrom[0])-Double.parseDouble(coordTo[0]);
				double deltaY = Double.parseDouble(coordFrom[1])-Double.parseDouble(coordTo[1]);
				double distanceBetweenPoints = Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));

				//If the points are not unreasonably far away from each other, then make a new line
				if(distanceBetweenPoints < 7000)
					list.add(new EdgeLine(coordFromX, coordFromY, coordToX, coordToY, 100));

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
	private Line2D line = new Line2D.Double();
	public void paint(Graphics g) {
	    super.paint(g);
	    for (EdgeLine edgeLine : linesOfEdges) {
	        line.setLine(edgeLine.getStartX(), edgeLine.getStartY(), edgeLine.getEndX(), edgeLine.getEndY());
	        int roadType = edgeLine.getRoadType();
	        g.setColor(RoadType.getColor(roadType));
	        ((Graphics2D)g).setStroke(new BasicStroke(RoadType.getStroke(roadType)));
	        ((Graphics2D)g).draw(line); 								  									 
	    }
	    	    
	    Graphics2D g2 = (Graphics2D) g;
	    if (rectZoomer.getRect() == null) {
	         return; 
	      } 
	      else if (rectZoomer.isDrawing() == true) {
	    	 g2.setStroke(new BasicStroke(1));
	         g2.setColor(Color.red);
	         g2.draw(rectZoomer.getRect());
	      } 
	}
	/**
	 * Sets the area to be used for drawing the map.
	 */
	public void setArea(AreaToDraw area) {
		this.area = area;
	}

	/**
	 * Returns the area used for drawing the map.
	 */
	public AreaToDraw getArea() {
		return area;
	}

	/**
	 * Returns the JFrame which the MapPanel is contained within.
	 */
	public JFrame getParentFrame() {
		return jf;
	}

	/**
	 * Sets the width of the MapPanel. Is used when resizing.
	 * @param width is the width-to-be.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Sets the height of the MapPanel. Is used when resizing.
	 * @param height is the height-to-be.
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * Creates a border around the MapPanel.
	 * @param mp is the MapPanel you're drawing a border around.
	 */
	private void setBorderForPanel(MapPanel mp) {
		Dimension d = setNewPreferredSize((int)mp.getMapWidth(), (int)getMapHeight());
		d = mp.setPanelDimensions(new Dimension());
        mp.setMaximumSize(d);
        mp.setBorder(new LineBorder(Color.black));

	}
	/*
	 * Takes a Dimension and makes it's width and height match the relation between area's width and height.
	 * Is also used to adjust the size of the map to a size that matches this relation.
	 * 
	 * @param d - Takes a dimension for the panel. 
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

	/**
	 * Draws the map. Is used when resizing and zooming.
	 */
	public void setLinesForMap() {
		preferredSize = setNewPreferredSize((int)width, (int)height);
		makeLinesForMap();
	}

	/*
	 * Is used for setting the initial size of the map.
	 */
	private static Dimension setNewPreferredSize(int w, int h) {
		Dimension tmpSize = new Dimension(w, h);
		return tmpSize;
	}

	/**
	 * Returns the dimensions of the MapPanel.
	 */
	public Dimension getPreferredSize() {
	    return preferredSize;
	}
	
	/**
	 * Returns the width of the map.
	 */
	public double getMapWidth() {
		return width;
	}
	
	/**
	 * Returns the height of the  map.
	 */
	public double getMapHeight() {
		return height;
	}
}