package mapCreationAndFunctions;

import gui.MainGui;
import gui.settingsAndPopUp.ColorTheme;

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
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import mapCreationAndFunctions.data.CoastLineMaker;
import mapCreationAndFunctions.data.CoordinateConverter;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.FindRelevantEdges;

@SuppressWarnings("serial")
/**
 * MapPanel is an extension of JPanel. It draws the map of Denmark on itself.
 *
 */
public class MapPanel extends JPanel {

	private MapMouseZoomAndPan mapMouseZoomAndPan;
	private AreaToDraw area;
	private ArrayList<Edge> edgesToDraw; 
	private GeneralPath[] coastLineToDraw;
	private CoordinateConverter coordConverter;
	private double mapHeight, mapWidth;
	private Stack<Edge> pathTo;
	private Edge[] fromEdgesToHighlight, toEdgesToHighlight;
	private Color fromColor = new Color(204,0,102), toColor = new Color(204,0,102);

	/**
	 * The constructor of MapPanel. Initializes the MapPanel, size and lines for the map.
	 *  
	 * @param width - The width of the panel.
	 * @param height - The height of the panel
	 */
	public MapPanel(double width, double height) {
		mapHeight = height;
		mapWidth = width;

		Color waterColor = new Color(160, 228, 253);;
		if(MainGui.colorFollowTheme){
			if(ColorTheme.springTheme)
				waterColor = ColorTheme.BUTTON_CLICKED_COLOR;
			if(ColorTheme.summerTheme || ColorTheme.autumnTheme)
				waterColor = ColorTheme.BACKGROUND_COLOR;
			if(ColorTheme.winterTheme)
				waterColor = ColorTheme.TEXT_COLOR;
		}
		if(!MainGui.coastlinesWanted)
			waterColor = Color.white;
		setBackground(waterColor);

		makeLinesForMap();
		setBorderForPanel();
		mapMouseZoomAndPan = new MapMouseZoomAndPan(this);
		addMouseListener(mapMouseZoomAndPan);
		addMouseMotionListener(mapMouseZoomAndPan);
		setFocusable(true);

	}

	public MapMouseZoomAndPan getMapMouseZoomAndPan(){
		return mapMouseZoomAndPan;
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
	@SuppressWarnings("unchecked")
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//Only do this, if coastlines should be drawn
		if(MainGui.coastlinesWanted){
			//Makes sure the inner coastlines gets drawn
			g2.setXORMode(getBackground());
			Color landMass; 
			if(MainGui.colorFollowTheme){
				landMass = ColorTheme.DARK_COLOR;
				if(ColorTheme.autumnTheme)
					landMass = ColorTheme.BUTTON_CLICKED_COLOR;
			} else
				landMass = new Color(255,255,210);
			//Drawing coastline
			for (GeneralPath path : coastLineToDraw) 
			{
				g2.setColor(landMass);
				g2.fill(path);
			}

			//Cancels setXORMode()
			g2.setPaintMode();
		}

		//Drawing roads
		Line2D line = new Line2D.Double();
		for (Edge edge : edgesToDraw) 
		{
			line.setLine(edge.getLine2DToDraw(coordConverter));
			int roadType = edge.getRoadType();	
			g2.setColor(RoadType.getColor(roadType));
			g2.setStroke(new BasicStroke(RoadType.getStroke(roadType)));
			g2.draw(line); 			
		}		
		Edge edgeToDraw;
		if(pathTo != null) 
		{
			Stack<Edge> drawPath = (Stack<Edge>) pathTo.clone();
			while(!drawPath.empty()) 
			{
				edgeToDraw = drawPath.pop();
				g2.setColor(Color.ORANGE);
				line.setLine(edgeToDraw.getLine2DToDraw(coordConverter));
				g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2.draw(line); 
			}
		}

		if(fromEdgesToHighlight != null) {
			for(int i = 0; i<fromEdgesToHighlight.length; i++) 
			{
				edgeToDraw = fromEdgesToHighlight[i];
				
				g2.setColor(setHighLightFromColor());
				line.setLine(edgeToDraw.getLine2DToDraw(coordConverter));
				g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2.draw(line); 
			}
		}
		
		if(toEdgesToHighlight != null) {
			for(int i = 0; i<toEdgesToHighlight.length; i++) 
			{
				edgeToDraw = toEdgesToHighlight[i];
				g2.setColor(setHighLightToColor());
				line.setLine(edgeToDraw.getLine2DToDraw(coordConverter));
				g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
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
	
	public Color setHighLightFromColor(){
		Color color = new Color(204,0,102);
		if(!MainGui.coastlinesWanted || !MainGui.colorFollowTheme) 
			color = Color.GREEN;
		else if(ColorTheme.summerTheme || ColorTheme.springTheme || ColorTheme.autumnTheme)	
			color = Color.WHITE;
		else if(ColorTheme.winterTheme)
			color = Color.yellow;
		return color;
	}
	
	public Color setHighLightToColor(){
		Color color = new Color(204,0,102);
		if(!MainGui.coastlinesWanted || !MainGui.colorFollowTheme) 
			color = Color.CYAN;
		else if(ColorTheme.summerTheme || ColorTheme.springTheme || ColorTheme.autumnTheme)	
			color = Color.YELLOW;
		else if(ColorTheme.winterTheme) 
			color = Color.GRAY;
		return color;
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
		setBorder(new LineBorder(ColorTheme.DARK_COLOR));

	}

	public void setPathTo(Stack<Edge> pathTo) {
		this.pathTo = pathTo;
	}
	
	public void setFromEdgesToHighlight(Edge[] edges)
	{
		fromEdgesToHighlight = edges;
		repaintMap();
	}
	
	public void setToEdgesToHighlight(Edge[] edges) {
		toEdgesToHighlight = edges;
		repaintMap();
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
	
	public void setFromColor(Color fromColor) {
		this.fromColor = fromColor;
	}
	
	public void setToColor(Color toColor) {
		this.toColor = toColor;
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