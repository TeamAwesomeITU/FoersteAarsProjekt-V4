package mapDrawer.drawing.mutators;

import gui.MainGui;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import mapDrawer.AreaToDraw;
import mapDrawer.dataSupplying.CoordinateConverter;
import mapDrawer.drawing.MapPanel;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.InvalidAreaProportionsException;
import mapDrawer.exceptions.NegativeAreaSizeException;

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
	private double bigX, bigY,smallX,smallY;
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

	/**
	 * Registers when mouse is pressed.
	 * @param e The event for the mouse.
	 */
	public void mousePressed(MouseEvent e) {
		mousePressedAt = e.getPoint();
		mp.requestFocusInWindow();
		if(!e.isShiftDown())
			setHand();
	}

	/**
	 * Registers when the mouse is dragged and draws the rectangle. 
	 * If shift isn't pressed, the mouse panning is initialized.
	 * @param e The event for the mouse.
	 */
	public void mouseDragged(MouseEvent e) {

		if(SwingUtilities.isLeftMouseButton(e) && e.isShiftDown()) {
			drawing = true;

			endX = Math.abs(mousePressedAt.x - e.getPoint().x);
			endY = Math.abs(mousePressedAt.y - e.getPoint().y);

			if(endX > mp.getWidth()) endX = mp.getWidth(); if(endY > mp.getHeight()) endY = mp.getHeight();

			double rectWidth = Math.abs(endX);
			double rectHeight = Math.abs(endY);//Math.abs((rectWidth*AreaToDraw.getHeightOfEntireMap())/AreaToDraw.getWidthOfEntireMap());    

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
		}
		else { mousePan(e);}
			}
	

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
		MainGui.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
		smallX = 0;
		bigX = mp.getMapWidth();
		smallY = 0;
		bigY = mp.getMapHeight();
		CoordinateConverter coordConverter = new CoordinateConverter(mp.getMapWidth(), mp.getMapHeight(), currentArea);
		smallX = coordConverter.pixelToUTMCoordX((int)smallX);
		bigX = coordConverter.pixelToUTMCoordX((int)bigX);
		smallY = coordConverter.pixelToUTMCoordY((int)smallY);
		bigY = coordConverter.pixelToUTMCoordY((int)bigY);
		double panX = (bigX - smallX) /100;
		double panY = (bigY - smallY) /100;
		double temp = bigY; bigY = smallY; smallY = temp;

		if(mouseMovedTo.x > mousePressedAt.x)
		{
			double tempX = ((mousePressedAt.x - mouseMovedTo.x)/10)*panX;
			smallX += tempX;
			bigX += tempX;
			//bevæger os til højre
		}
		else if(mouseMovedTo.x < mousePressedAt.x)
		{
			double tempX = ((mouseMovedTo.x - mousePressedAt.x)/10)*panX;
			smallX -= tempX;
			bigX -= tempX;
			//bevæger os til venstre
		}

		if(mouseMovedTo.y > mousePressedAt.y)
		{
			double tempY = ((mousePressedAt.y - mouseMovedTo.y)/10)*panY;
			smallY += tempY;
			bigY += tempY;
			//bevæger os ned ad
		}
		else if(mouseMovedTo.y < mousePressedAt.y)
		{
			double tempY = ((mouseMovedTo.y - mousePressedAt.y)/10)*panY;
			smallY -= tempY;
			bigY -= tempY;
			//bevæger os ned ad
		}
		
		try {
			newArea = new AreaToDraw(smallX, bigX, smallY, bigY, true);
			mp.repaintMap(newArea);

		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException
				| InvalidAreaProportionsException e1) {
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
