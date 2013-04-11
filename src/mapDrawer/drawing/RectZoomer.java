package mapDrawer.drawing;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import mapDrawer.AreaToDraw;
import mapDrawer.dataSupplying.CoordinateConverter;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.InvalidAreaProportionsException;
import mapDrawer.exceptions.NegativeAreaSizeException;

/**
 * The rectangle that is used to zoom. 
 */
public class RectZoomer extends MouseAdapter {
	private boolean drawing = false;
	private Point mousePress = null; 
	private double startX, startY, endX, endY;
	private Rectangle rect = null;
	private MapPanel mp = null;
	private ResizingArrayStack<AreaToDraw> stack; 

	/**
	 * Initializes the RectZoomer.
	 * @param mp The MapPanel for the RectZoomer.
	 */
	public RectZoomer(MapPanel mp) {
		this.mp = mp;
		stack = new ResizingArrayStack<AreaToDraw>();
	}

	/**
	 * Registers when mouse is pressed.
	 * @param e The event for the mouse.
	 */
	public void mousePressed(MouseEvent e ) {
		mousePress = e.getPoint();
		mp.requestFocusInWindow();
	}

	/**
	 * Registers when the mouse is dragged and draws the rectangle. 
	 * @param e The event for the mouse.
	 */
	public void mouseDragged(MouseEvent e) {

		if(SwingUtilities.isLeftMouseButton(e) && e.isShiftDown()) {
			drawing = true;

			endX = Math.abs(mousePress.x - e.getPoint().x);
			endY = Math.abs(mousePress.y - e.getPoint().y);

			if(endX > mp.getWidth()) endX = mp.getWidth(); if(endY > mp.getHeight()) endY = mp.getHeight();

			double rectWidth = Math.abs(endX);
			double rectHeight = Math.abs(endY);//Math.abs((rectWidth*AreaToDraw.getHeightOfEntireMap())/AreaToDraw.getWidthOfEntireMap());    

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
	 * @param e The event for the mouse.
	 */
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) { 	
			zoomOut();
		}
		else if(SwingUtilities.isLeftMouseButton(e) && e.isShiftDown()) {
			AreaToDraw area = mp.getArea();
			stack.push(area);
			CoordinateConverter coordConverter = new CoordinateConverter(mp.getMapWidth(), mp.getMapHeight(), area);
			
			//SWITCHING STARTY AND ENDY - WHAT DA FUCK
			double temp = startY;
			startY = endY;
			endY = temp;
			
			if(startX < 0)
				startX = 0;
			if(startY < 0)
				startY = 0;			
			if(endX > mp.getWidth())
				endX = mp.getWidth();
			if(endY > mp.getHeight())
				endY = mp.getHeight();
			
			double startXCoord = coordConverter.pixelToUTMCoordX((int) startX);
			double startYCoord = coordConverter.pixelToUTMCoordY((int) startY);

			double endXCoord = coordConverter.pixelToUTMCoordX((int) endX);
			double endYCoord = coordConverter.pixelToUTMCoordY((int) endY);

			try {
				System.out.println("Relation-calculation: startXCoord: " + startXCoord + ", endXCoord: " + endXCoord + ", startYCoord: " + startYCoord + ", endYCoord: " + endYCoord);	
				area = new AreaToDraw(startXCoord, endXCoord, startYCoord, endYCoord, true);	
				System.out.println("W/H of zoomed area: " + area.getWidth()/area.getHeight());
				rect = null;
				mp.repaintMap(area);
			}

			catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1) {
				JOptionPane.showMessageDialog(mp, "The selected area is not within the map, please try again.");
				System.out.println("ERROR: " + e1.getMessage());					
				rect = null;
				mp.repaint();
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
		AreaToDraw area = stack.pop();
		mp.repaintMap(area);
	}
}
