package mapDrawer.drawing;

import gui.MainGui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import mapDrawer.AreaToDraw;
import mapDrawer.RoadType;
import mapDrawer.dataSupplying.CoastLineMaker;
import mapDrawer.dataSupplying.CoordinateConverter;
import mapDrawer.dataSupplying.DataHolding;
import mapDrawer.dataSupplying.FindRelevantEdges;
import mapDrawer.drawing.mutators.MapMouseZoomAndPan;
import routing.DijkstraSP;
import routing.EdgeWeightedDigraph;
import sun.awt.image.OffScreenImage;

@SuppressWarnings("serial")
/**
 * MapPanel is an extension of JPanel. It draws the map of Denmark on itself.
 *
 */
public class MapPanel extends JPanel {

	private MapMouseZoomAndPan mapMouseZoomAndPan;
	private AreaToDraw area;
	private ArrayList<Edge> edgesToDraw; 
	private Line2D[] line2DsToDraw;
	
	//HashMap, where the value is the ID of the drawn line, and the key is the value of it's corresponding Edge
	private HashMap<Integer, Integer> iDHashMap;
	private GeneralPath[] coastLineToDraw;
	private CoordinateConverter coordConverter;
	private double mapHeight, mapWidth;
	private Stack<Edge> pathTo;

	/**
	 * The constructor of MapPanel. Initializes the MapPanel, size and lines for the map.
	 *  
	 * @param width - The width of the panel.
	 * @param height - The height of the panel
	 */
	public MapPanel(double width, double height) {
		mapHeight = height;
		mapWidth = width;
		mapMouseZoomAndPan = new MapMouseZoomAndPan(this);
		//markOgKasperTester();
		makeLinesForMap();
		setBorderForPanel();
		addMouseListener(mapMouseZoomAndPan);
		addMouseMotionListener(mapMouseZoomAndPan);
		setFocusable(true);
	}
	
	public MapMouseZoomAndPan getMapMouseZoomAndPan(){
		return mapMouseZoomAndPan;
	}

	
	public void markOgKasperTester() {
		DijkstraSP dip = new DijkstraSP(new EdgeWeightedDigraph(675902), "Aavej");
		pathTo = (Stack<Edge>) dip.pathTo("Følfodvej");
	}
	/**
	 * Draws the lines for the map. 
	 * Saves all the edges and converts the coordinates and saves them in an array. 
	 */
	private void makeLinesForMap() {
		if(area == null)
			area = new AreaToDraw();
		edgesToDraw = FindRelevantEdges.findEdgesToDraw(area);
		coordConverter = new CoordinateConverter((int)mapWidth, (int)mapHeight, area);
		if(MainGui.coastlinesWanted)
			makeCoastLineForMap();
	}
	
	private void makeCoastLineForMap()
	{
		coastLineToDraw = CoastLineMaker.getCoastLineToDraw((int)mapWidth, (int)mapHeight, area);
	}
	
	/**
	 * Draws all the lines for the map. Also, draws the rectangle used by the user
	 * to see where you are about to zoom.
	 * @param g The graphics object which is used.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		//Only do this, if coastlines should be drawn
		if(MainGui.coastlinesWanted){
			//Makes sure the inner coastlines gets drawn
			g2.setXORMode(getBackground());

			//Drawing coastline
			for (GeneralPath path : coastLineToDraw) 
			{
				g2.setColor(Color.lightGray);
				g2.fill(path);
			}

			//Cancels setXORMode()
			g2.setPaintMode();
		}
		
		line2DsToDraw = new Line2D.Double[edgesToDraw.size()];
		int indexToInsert = 0;
		iDHashMap = new HashMap<>();
		
		//Drawing roads
		Line2D line = new Line2D.Double();
		for (Edge edge : edgesToDraw) 
		{
			line.setLine(edge.getLine2DToDraw(coordConverter));
			line2DsToDraw[indexToInsert++] = line;
			iDHashMap.put(indexToInsert, edge.getiD());
			int roadType = edge.getRoadType();						
			g2.setColor(RoadType.getColor(roadType));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setStroke(new BasicStroke(RoadType.getStroke(roadType)));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.draw(line); 			
		}		
		
		if(pathTo != null) 
		{
			Stack<Edge> drawPath = pathTo;
			while(!drawPath.empty()) 
			{
				g2.setColor(Color.YELLOW);
				line.setLine(drawPath.pop().getLine2DToDraw(coordConverter));
				g2.setStroke(new BasicStroke(1000));
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.draw(line); 
			}
		}
		setBounds(new Rectangle((int)mapWidth, (int) mapHeight));

		if (mapMouseZoomAndPan.getRect() == null) {
			return; 
		} 
		else if (mapMouseZoomAndPan.isDrawing() == true) {
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.red);
			g2.draw(mapMouseZoomAndPan.getRect());
		} 
	}
	
	/**
	 * Repaints the map to the new AreaToDraw.
	 */
	
	public void repaintMap(AreaToDraw area) {
		this.area = area;
		makeLinesForMap();
		repaint();
	}
	
	/**
	 * Repaints the map with same AreaToDraw. Should be used if the window has been resized.
	 */
	public void repaintMap() {
		makeLinesForMap();
		repaint();
	}

	/**
	 * Returns the area used for drawing the map.
	 */
	public AreaToDraw getArea() {
		return area;
	}
	
	/**
	 * Sets the width of the MapPanel. Is used when resizing.
	 * @param width is the width-to-be.
	 */
	public void setMapWidth(double width) {
		mapWidth = width;
	}

	/**
	 * Sets the height of the MapPanel. Is used when resizing.
	 * @param height is the height-to-be.
	 */
	public void setMapHeight(double height) {
		mapHeight = height;
	}

	/**
	 * Creates a border around the MapPanel.
	 */
	private void setBorderForPanel() {
		setBorder(new LineBorder(Color.black));

	}


	/**
	 * Returns the width of the map.
	 */
	public double getMapWidth() {
		return mapWidth;
	}

	/**
	 * Returns the height of the  map.
	 */
	public double getMapHeight() {
		return mapHeight;
	}
	
	/**
	 * Returns an Edge, which lies near the given mouse coordinates.
	 * @param mouseX The x coordinate of the mouse
	 * @param mouseY The y coordinate of the mouse
	 * @return An Edge, which lies near the given mouse coordinates.
	 */
	public Edge getHitEdge(double mouseX, double mouseY)
	{
		double squareSideLength = area.getWidth()/100;
		Rectangle2D square = new Rectangle2D.Double(mouseX-(squareSideLength/2), mouseY-(squareSideLength/2), squareSideLength, squareSideLength);		
		for(Edge edge : edgesToDraw)
			if(edge.intersects(square))
				return edge;
		
		return null;
	}
}