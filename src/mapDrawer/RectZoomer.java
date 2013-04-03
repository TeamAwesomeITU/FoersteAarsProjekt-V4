package mapDrawer;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.NegativeAreaSizeException;
import mapDrawer.exceptions.InvalidAreaProportionsException;

/**
 * The rectangle that is used to zoom. 
 */
public class RectZoomer extends MouseAdapter {
	private boolean drawing = false;
	private Point mousePress = null; 
	private double startX, startY, endX, endY;
	private Rectangle rect = null;
	private MapPanel mp = null;
	private ResizingArrayStack<AreaToDraw> ras; 

	/**
	 * Initializes the RectZoomer.
	 * @param mp The MapPanel for the RectZoomer.
	 */
	public RectZoomer(MapPanel mp) {
		this.mp = mp;
		ras = new ResizingArrayStack<AreaToDraw>();
	}

	/**
	 * Registers when mouse is pressed.
	 * @param e The cursors point when the mouse is pressed.
	 */
	public void mousePressed(MouseEvent e) {
		mousePress = e.getPoint();
	}

	/**
	 * Registers when the mouse is dragged and draws the rectangle. 
	 * @param e Which mousebutton is used.
	 */
	public void mouseDragged(MouseEvent e) {

		if(SwingUtilities.isLeftMouseButton(e)) {
			drawing = true;

			endX = Math.abs(mousePress.x - e.getPoint().x);
			endY = Math.abs(mousePress.y - e.getPoint().y);

			if(endX > mp.getWidth()) endX = mp.getWidth(); if(endY > mp.getHeight()) endY = mp.getHeight();

			double rectWidth = Math.abs(endX);
			double rectHeight = Math.abs((rectWidth*AreaToDraw.getHeightOfEntireMap())/AreaToDraw.getWidthOfEntireMap());    

			startX = Math.min(mousePress.x, e.getPoint().x);
			
			if(mousePress.y < e.getPoint().y)
				startY = mousePress.y;
			else
				startY = mousePress.y - rectHeight;			

			if(startX < 0) startX = 0;
			if(startY < 0) startY = 0;


			rect = new Rectangle((int) startX, (int) startY, (int) rectWidth, (int) rectHeight);
			endX = startX+rectWidth;
			endY = startY+rectHeight;
			mp.repaint();
		}}

	/**
	 * Registers if the mouse is released and then draws the new area.
	 * @param e When the mouse is released.
	 */
	public void mouseReleased(MouseEvent e) {
		//Zoom in on shizz.
		if(SwingUtilities.isRightMouseButton(e)) { 	
			zoomOut();
		}
		else if(SwingUtilities.isLeftMouseButton(e)) {
			AreaToDraw area = mp.getArea();
			ras.push(area);
			CoordinateConverter coordConverter = new CoordinateConverter((int)mp.getMapWidth(), (int)mp.getMapHeight(), area);
			if(startX < 0) startX = 0; if(startY < 0) startY = 0;
			if(endX > mp.getWidth()) endX = mp.getWidth(); if(endY > mp.getHeight()) endY = mp.getHeight();
			double startXCoord = coordConverter.pixelToUTMCoordX((int) startX);
			double startYCoord = coordConverter.pixelToUTMCoordY((int) endY);
			double endXCoord = coordConverter.pixelToUTMCoordX((int) endX);
			double endYCoord = coordConverter.pixelToUTMCoordY((int) startY);
			System.out.println("startX: " +startXCoord + " startY: " +startYCoord+ " endX: " +endXCoord+ "endY: " +endYCoord);
			try {
				rect = null;    		
				double newAreaProportions = Math.round((((endX-startX)/(endY-startY))*100.0))/100.0;
				double proportionsOfDenmarkMap = Math.round(((AreaToDraw.getWidthOfEntireMap()/AreaToDraw.getHeightOfEntireMap())*100.0))/100.0;
				System.out.println("newAreaProportions: " + newAreaProportions + ", proportionsOfDenmarkMap: " + proportionsOfDenmarkMap);
				if(newAreaProportions != proportionsOfDenmarkMap) {
					throw new InvalidAreaProportionsException("Area proportions do not equal those of the entire map of Denmark");
				}
				else {
					area = new AreaToDraw(startXCoord, endXCoord, startYCoord, endYCoord);
					mp.setArea(area);
					mp.setLinesForMap();
					mp.repaint();
				}
			} 
			catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1) {
				JOptionPane.showMessageDialog(mp, "The selected area is not within the map, please try again.");
				rect = null;
				mp.repaint();
				//mp.getParentFrame().dispose();
				//e1.printStackTrace();
			}
		}
	}

	/**
	 * Gets the rectangle.
	 * @return The current rectangle.
	 */
	public Rectangle getRect() {
		return rect;
	}

	/**
	 * Whether or not the user is drawing.
	 * @return Is it drawing or not.
	 */
	public boolean isDrawing() {
		return drawing;
	}

	/**
	 * Zooms out the picture to the last zoom.
	 */
	public void zoomOut()
	{
		AreaToDraw area = ras.pop();
		mp.setArea(area);
		mp.setLinesForMap();
		mp.repaint();
	}
}
