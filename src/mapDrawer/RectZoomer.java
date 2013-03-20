package mapDrawer;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class RectZoomer extends MouseAdapter {
	private boolean drawing = false;
	private Point mousePress = null; 
	private int startX, startY, endX, endY;
	private Rectangle rect = null;
	private JPanel jp = null;

	public RectZoomer(JPanel jp) {
		this.jp = jp;
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
       jp.repaint();
    }

    public void mouseReleased(MouseEvent e) {
    	//Zoom in on shizz.
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
