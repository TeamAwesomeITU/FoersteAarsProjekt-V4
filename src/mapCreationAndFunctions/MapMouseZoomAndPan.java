package mapCreationAndFunctions;

import gui.MainGui;
import gui.settingsAndPopUp.MapPopUp;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import mapCreationAndFunctions.data.CoordinateConverter;
import mapCreationAndFunctions.data.MapStack;
import mapCreationAndFunctions.exceptions.AreaIsNotWithinDenmarkException;
import mapCreationAndFunctions.exceptions.InvalidAreaProportionsException;
import mapCreationAndFunctions.exceptions.NegativeAreaSizeException;

/**
 * The rectangle that is used to zoom. 
 */
public class MapMouseZoomAndPan extends MouseAdapter {
	private boolean drawing = false;
	private Point mousePressedAt = null; 
	private double startX, startY, endX, endY;
	private Rectangle rect = null;
	private MapPanel mp = null;
	private MapStack<AreaToDraw> stack; 
	private AreaToDraw currentArea;
	private AreaToDraw newArea;

	/**
	 * Initializes the RectZoomer.
	 * @param mp The MapPanel for the RectZoomer.
	 */
	public MapMouseZoomAndPan(MapPanel mp) {
		this.mp = mp;
		stack = new MapStack<AreaToDraw>();
	}
	
	public void setSetRect(int x, int y){
		
	}

	/**
	 * Registers when mouse is pressed.
	 * @param e The event for the mouse.
	 */
	public void mousePressed(MouseEvent e) {
		mousePressedAt = e.getPoint();
		mp.requestFocusInWindow();
	}

	/**
	 * Registers when the mouse is dragged and draws the rectangle. 
	 * If shift isn't pressed, the mouse panning is initialized.
	 * @param e The event for the mouse.
	 */
	public void mouseDragged(MouseEvent e) {

		if(SwingUtilities.isLeftMouseButton(e) && e.isShiftDown()) {
			AreaToDraw area = mp.getArea();
			if(area.getPercentageOfEntireMap() > 0.01){
			drawing = true;

			endX = Math.abs(mousePressedAt.x - e.getPoint().x);
			endY = Math.abs(mousePressedAt.y - e.getPoint().y);

			if(endX > mp.getWidth()) endX = mp.getWidth(); if(endY > mp.getHeight()) endY = mp.getHeight();

			double rectWidth = Math.abs(endX);
			double rectHeight = Math.abs(endY); 

			startX = Math.min(mousePressedAt.x, e.getPoint().x);

			if(mousePressedAt.y < e.getPoint().y)
				startY = mousePressedAt.y;
			else
				startY = mousePressedAt.y - rectHeight;			

			if(startX < 0) startX = 0;
			if(startY < 0) startY = 0;


			rect = new Rectangle((int) startX, (int) startY, (int) rectWidth, (int) rectHeight);
			endX = startX+rectWidth;
			endY = startY+rectHeight;
			mp.repaint();
		}}
		else { mousePan(e); }
			}
	

	/**
	 * Registers if the mouse is released and then draws the new area.
	 * @param e The event for the mouse.
	 */
	public void mouseReleased(MouseEvent e) {
		MainGui.setMainHand();
		if(SwingUtilities.isRightMouseButton(e) && e.isShiftDown()) { 	
			zoomOut();
		}
		else if(SwingUtilities.isRightMouseButton(e) && !e.isShiftDown()) {
			MapPopUp mpu = new MapPopUp(mp, e);
			mpu.show(e.getComponent(), e.getX(), e.getY());
		}
		else if(SwingUtilities.isLeftMouseButton(e) && e.isShiftDown()) {
			AreaToDraw area = mp.getArea();
			if(area.getPercentageOfEntireMap() > 0.01)
				zoomIn();
		}
	}
	
	/**
	 * Pans in the map by dragging the mouse.
	 * @param e MouseEvent
	 */
	public void mousePan(MouseEvent e)
	{	
		
		setHand();
		Point mouseMovedTo = e.getPoint();
		currentArea = mp.getArea();
		startX = 0;
		endX = mp.getMapWidth();
		startY = 0;
		endY = mp.getMapHeight();
		CoordinateConverter coordConverter = new CoordinateConverter(mp.getMapWidth(), mp.getMapHeight(), currentArea);
		startX = coordConverter.pixelToUTMCoordX((int)startX);
		endX = coordConverter.pixelToUTMCoordX((int)endX);
		startY = coordConverter.pixelToUTMCoordY((int)startY);
		endY = coordConverter.pixelToUTMCoordY((int)endY);
		double temp = endY; endY = startY; startY = temp;

		if(mouseMovedTo.x > mousePressedAt.x)
		{
			double tempX = coordConverter.pixelToUTMCoordX(mouseMovedTo.x) - coordConverter.pixelToUTMCoordX(mousePressedAt.x);
			startX -= tempX;
			endX -= tempX;
			//bevæger os til højre
		}
		else if(mouseMovedTo.x < mousePressedAt.x)
		{
			double tempX = coordConverter.pixelToUTMCoordX(mousePressedAt.x) - coordConverter.pixelToUTMCoordX(mouseMovedTo.x);
			startX += tempX;
			endX += tempX;
			//bevæger os til venstre
		}

		if(mouseMovedTo.y < mousePressedAt.y)
		{	
			double tempY = coordConverter.pixelToUTMCoordY(mouseMovedTo.y) - coordConverter.pixelToUTMCoordY(mousePressedAt.y);
			startY -= tempY;
			endY -= tempY;
			//bevæger os ned ad
		}
		else if(mouseMovedTo.y > mousePressedAt.y)
		{
			double tempY = coordConverter.pixelToUTMCoordY(mousePressedAt.y) - coordConverter.pixelToUTMCoordY(mouseMovedTo.y);
			startY += tempY;
			endY += tempY;
			//bevæger os op ad	
		}
		
		try {
			newArea = new AreaToDraw(startX, endX, startY, endY, true);
			mp.repaintMap(newArea);
			mousePressed(e);

		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException
				| InvalidAreaProportionsException e1) {
			e1.printStackTrace();
			newArea = currentArea;
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
	
	public void zoomIn() 
	{
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
			area = new AreaToDraw(startXCoord, endXCoord, startYCoord, endYCoord, true);	
			rect = null;
			mp.repaintMap(area);
		}

		catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1) {
			JOptionPane.showMessageDialog(mp, "The selected area is not within the map, please try again.");
			rect = null;
			mp.repaint();
		}
	}
	
	public void zoomInFromPopUpMenu(Point p) {				
			CoordinateConverter coordConverter = new CoordinateConverter(mp.getMapWidth(), mp.getMapHeight(), mp.getArea());
			startX = 0; endX = mp.getMapWidth(); startY = 0; endY = mp.getMapHeight();
			double zoomX = ((coordConverter.pixelToUTMCoordX((int)endX) - coordConverter.pixelToUTMCoordX((int)startX))/100);
			double zoomY = ((coordConverter.pixelToUTMCoordY((int)endY) - coordConverter.pixelToUTMCoordY((int)startY))/100);
			startX = coordConverter.pixelToUTMCoordX((int)p.getX()) - (zoomX*25);
			endX = coordConverter.pixelToUTMCoordX((int)p.getX()) + (zoomX*25);
			startY = coordConverter.pixelToUTMCoordY((int)p.getY()) + (zoomY*25);
			endY = coordConverter.pixelToUTMCoordY((int)p.getY()) - (zoomY*25);
			if(endX > AreaToDraw.getLargestXOfEntireMap()) {
				endX = AreaToDraw.getLargestXOfEntireMap();
			}
			if(startX < AreaToDraw.getSmallestXOfEntireMap()) {
				startX = AreaToDraw.getSmallestXOfEntireMap();
			}
			if(endY > AreaToDraw.getLargestYOfEntireMap()) {
				endY = AreaToDraw.getLargestYOfEntireMap();
			}
			if(startY < AreaToDraw.getSmallestYOfEntireMap()) {
				startY = AreaToDraw.getSmallestYOfEntireMap();
			}
			
			try {
				newArea = new AreaToDraw(startX, endX, startY, endY, true);
  		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1) {
  			newArea = currentArea;
		}
		mp.repaintMap(newArea);
		}
	
	/**
	 * Sets the cursor as a dragging hand.
	 */
	public void setHand(){
		Toolkit toolkit = Toolkit.getDefaultToolkit(); 
		Image image = toolkit.getImage("resources/draggingHand.png"); 
		Point hotSpot = new Point(0,0);
		Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "Dragging Hand"); 
		MainGui.frame.setCursor(cursor);
	}
}
