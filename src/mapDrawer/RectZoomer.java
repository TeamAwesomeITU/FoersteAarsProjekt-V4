package mapDrawer;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java_cup.internal_error;
import mapDrawer.exceptions.AreaNegativeSizeException;

public class RectZoomer extends MouseAdapter {
	private boolean drawing = false;
	private Point mousePress = null; 
	private int startX, startY, endX, endY;
	private Rectangle rect = null;
	private MapPanel mp = null;

	public RectZoomer(MapPanel mp) {
		this.mp = mp;
	}
    public void mousePressed(MouseEvent e) {
       mousePress = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {
       drawing = true;
       startX = Math.min(mousePress.x, e.getPoint().x);
       startY = Math.min(mousePress.y, e.getPoint().y);
       endX = Math.abs(mousePress.x - e.getPoint().x);
       endY = Math.abs(mousePress.y - e.getPoint().y);
       rect = new Rectangle(startX, startY, endX, endY);
       int rectWidth = (int) Math.round((mp.getWidth()/10))+endX;
       int rectHeight = (int) Math.round((mp.getHeight()/10))+endY;
       rect.setBounds(startX, startY, rectWidth, rectHeight);
       endX += startX+rectWidth;
       endY += startY+rectHeight;
       mp.repaint();
    }

    public void mouseReleased(MouseEvent e) {
    	//Zoom in on shizz.
    	AreaToDraw area = mp.getArea();
    	CoordinateConverter coordConverter = new CoordinateConverter((int)mp.getPreferredSize().getWidth(), (int)mp.getPreferredSize().getHeight(), area);
    	if(startX < 0) startX = 0; if(startY < 0) startY = 0;
    	if(endX > mp.getWidth()) endX = mp.getWidth(); if(endY > mp.getHeight()) endY = mp.getHeight();
    	double startXCoord = coordConverter.DrawToKrakCoordX(startX);
    	double startYCoord = coordConverter.DrawToKrakCoordY(endY);
    	double endXCoord = coordConverter.DrawToKrakCoordX(endX);
    	double endYCoord = coordConverter.DrawToKrakCoordY(startY);
    	try {
    		rect = null;
			area = new AreaToDraw(startXCoord, endXCoord, startYCoord, endYCoord);
			mp.setArea(area);
			mp.setLinesForMap();
			mp.repaint();
		} 
    	catch (AreaNegativeSizeException e1) {
			mp.getParentFrame().dispose();
			e1.printStackTrace();
		}
    }
    
    public Rectangle getRect() {
    	return rect;
    }
    public boolean isDrawing() {
    	return drawing;
    }
}
