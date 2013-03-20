package mapDrawer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MapPanel extends JPanel {

	private Dimension preferredSize = setPreferredSize();
	private Rectangle2D[] rects = new Rectangle2D[50];
	private RectZoomer rectZoomer;

	public static void main(String[] args) {        
	    JFrame jf = createJFrame();
	}    

	public MapPanel() {
		rectZoomer = new RectZoomer(this);
	    // generate rectangles with pseudo-random coords
	    for (int i=0; i<rects.length; i++) {
	        rects[i] = new Rectangle2D.Double(
	                Math.random()*.8, Math.random()*.8, 
	                Math.random()*.2, Math.random()*.2);
	    }
	    // mouse listener to detect scrollwheel events
        addMouseListener(rectZoomer);
        addMouseMotionListener(rectZoomer);
	}

	private static JFrame createJFrame() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		JFrame jf = new JFrame("test");
	    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jf.setSize(400, 400);
	    jf.setExtendedState(Frame.MAXIMIZED_BOTH);
	    jf.add(new JScrollPane(new MapPanel()));
	    jf.setVisible(true);
	    return jf;
	}

	private void updatePreferredSize(int n, Point p) {
	    double d = (double) n * 1.08;
	    d = (n > 0) ? 1 / d : -d;
	
	    int w = (int) (getWidth() * d);
	    int h = (int) (getHeight() * d);
	    preferredSize.setSize(w, h);
	    
	    int offX = (int)(p.x * d) - p.x;
	    int offY = (int)(p.y * d) - p.y;
	    setLocation(getLocation().x-offX,getLocation().y-offY);
	    getParent().doLayout();
	}
	
	public Dimension getPreferredSize() {
	    return preferredSize;
	}
	
	private static Dimension setPreferredSize() {
		Dimension tmpSize = Toolkit.getDefaultToolkit().getScreenSize();
		double w = tmpSize.getWidth() * 0.90;
		double h = tmpSize.getHeight() * 0.90;
		tmpSize.setSize(w, h);
		return tmpSize;
	}
	
	/**
	 * Updates the size of the rectangles to match the zoom level.
	 */
	private Rectangle2D r = new Rectangle2D.Float();
	public void paint(Graphics g) {
	    super.paint(g);
	    g.setColor(Color.black);
	    int w = getWidth();
	    int h = getHeight();
	    for (Rectangle2D rect : rects) {
	        r.setRect(rect.getX() * w, rect.getY() * h, 
	                rect.getWidth() * w, rect.getHeight() * h);
	        ((Graphics2D)g).draw(r);
	    }
	    Graphics2D g2 = (Graphics2D) g;
	    if (rectZoomer.getRect() == null) {
	         return; 
	      } 
	      else if (rectZoomer.isDrawing() == true) {
	         g2.setColor(Color.red);
	         g2.draw(rectZoomer.getRect());
	      } 
	}
}
