package mapDrawer.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import mapDrawer.AreaToDraw;
import mapDrawer.RoadType;
import mapDrawer.dataSupplying.CoastLineMaker;
import mapDrawer.dataSupplying.CoordinateConverter;
import mapDrawer.dataSupplying.DataHolding;
import mapDrawer.dataSupplying.FindRelevantEdges;
import mapDrawer.drawing.mutators.MapMouseZoomAndPan;

@SuppressWarnings("serial")
/**
 * MapPanel is an extension of JPanel. It draws the map of Denmark on itself.
 *
 */
public class MapPanel extends JPanel {

	private MapMouseZoomAndPan mapMouseZoomAndPan;
	private AreaToDraw area;
	private HashSet<Edge> edgesToDraw; 
	private GeneralPath[] coastLineToDraw;
	private CoordinateConverter coordConverter;
	private double mapHeight, mapWidth;

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
		makeLinesForMap();
		makeCoastLineForMap();
		setBorderForPanel();
		addMouseListener(mapMouseZoomAndPan);
		addMouseMotionListener(mapMouseZoomAndPan);
		setFocusable(true);
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

		/*
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


				//If the points are not unreasonably far away from each other, and lie within the area, then make a new line
					if(distanceBetweenPoints < 4000)
						list.add(new EdgeLine(coordFromX, coordFromY, coordToX, coordToY, 100));

				line2 = line1;
				line1 = reader.readLine();
			}

			EdgeLine[] newLinesOfEdges = new EdgeLine[edgesToDraw.length + list.size()];
			for(int i = 0; i < newLinesOfEdges.length; i++)
			{
				if(i < edgesToDraw.length)
					newLinesOfEdges[i] = edgesToDraw[i];
				else
					newLinesOfEdges[i] = list.get(i-edgesToDraw.length);
			}

			edgesToDraw = newLinesOfEdges;

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
*/
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
		Line2D line = new Line2D.Double();
		for (Edge edge : edgesToDraw) {
			line.setLine(edge.getLine2DToDraw(coordConverter));
			int roadType = edge.getRoadType();
						
			g.setColor(RoadType.getColor(roadType));
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			((Graphics2D)g).setStroke(new BasicStroke(RoadType.getStroke(roadType)));
			((Graphics2D)g).draw(line); 
			setBounds(new Rectangle((int)mapWidth, (int) mapHeight));
		}
		
		//HER SKAL COASTLINE TEGNES

		Graphics2D g2 = (Graphics2D) g;
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
}