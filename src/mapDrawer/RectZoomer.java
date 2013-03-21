package mapDrawer;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import mapDrawer.exceptions.AreaNegativeSizeException;

import sun.awt.RepaintArea;

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
       endX += startX;
       endY += startY;
       mp.repaint();
    }

    public void mouseReleased(MouseEvent e) {
    	//Zoom in on shizz.
    	AreaToDraw area = mp.getArea();
    	CoordinateConverter coordConverter = new CoordinateConverter((int)mp.getPreferredSize().getWidth(), (int)mp.getPreferredSize().getHeight(), area);
    	System.out.println("start X: " + startX + " start Y: " + startY);
    	System.out.println("end X: "+endX+ " end Y: " +endY);
    	double startXCoord = coordConverter.DrawToKrakCoordX(startX);
    	double startYCoord = coordConverter.DrawToKrakCoordY(startY);
    	double endXCoord = coordConverter.DrawToKrakCoordX(endX);
    	double endYCoord = coordConverter.DrawToKrakCoordY(endY);
    	System.out.println("startX: " +startXCoord+ " endX: "+endXCoord+ " startY: " +startYCoord+ " endY: " +endYCoord);
    	try {
			area = new AreaToDraw(startXCoord, endXCoord, startYCoord, endYCoord);
			mp.setArea(area);
			mp.setLinesForMap();
			mp.repaint();
		} 
    	catch (AreaNegativeSizeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	/*double newScale = scale*scale;
       drawing = false;
       BufferedImage zoomImage = image.getSubimage(startZoomX, startZoomY, width, height);
       image = zoomImage;
       repaint();*/
    }
    
    public Rectangle getRect() {
    	return rect;
    }
    public boolean isDrawing() {
    	return drawing;
    }
}
