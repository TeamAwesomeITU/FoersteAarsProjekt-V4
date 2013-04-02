package mapDrawer;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.AreaNegativeSizeException;
import mapDrawer.exceptions.InvalidAreaProportionsException;

public class RectZoomer extends MouseAdapter {
	private boolean drawing = false;
	private Point mousePress = null; 
	private double startX, startY, endX, endY;
	private Rectangle rect = null;
	private MapPanel mp = null;
	private ResizingArrayStack<AreaToDraw> ras; 
	
	public RectZoomer(MapPanel mp) {
		this.mp = mp;
		ras = new ResizingArrayStack<AreaToDraw>();
		}
	
    public void mousePressed(MouseEvent e) {
    			mousePress = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {

    	if(SwingUtilities.isLeftMouseButton(e)) {
	       drawing = true;
	       startX = Math.min(mousePress.x, e.getPoint().x);
	       startY = Math.min(mousePress.y, e.getPoint().y);
	       endX = Math.abs(mousePress.x - e.getPoint().x);
	       endY = Math.abs(mousePress.y - e.getPoint().y);
	       if(startX < 0) startX = 0; if(startY < 0) startY = 0;
	       if(endX > mp.getWidth()) endX = mp.getWidth(); if(endY > mp.getHeight()) endY = mp.getHeight();
	       double rectWidth = Math.abs(endX);
	       double rectHeight = Math.abs((rectWidth*mp.getHeight())/mp.getWidth());
	       rect = new Rectangle((int) startX, (int) startY, (int) rectWidth, (int) rectHeight);
	       endX = startX+rectWidth;
	       endY = startY+rectHeight;
	       mp.repaint();
    }}

    public void mouseReleased(MouseEvent e) {
    	//Zoom in on shizz.
    	if(SwingUtilities.isRightMouseButton(e)) { 	
    		zoomOut();
    		}
    	else if(SwingUtilities.isLeftMouseButton(e)) {
	    	AreaToDraw area = mp.getArea();
	    	ras.push(area);
	    	CoordinateConverter coordConverter = new CoordinateConverter((int)mp.getPreferredSize().getWidth(), (int)mp.getPreferredSize().getHeight(), area);
	    	if(startX < 0) startX = 0; if(startY < 0) startY = 0;
	    	if(endX > mp.getWidth()) endX = mp.getWidth(); if(endY > mp.getHeight()) endY = mp.getHeight();
	    	double startXCoord = coordConverter.pixelToUTMCoordX((int) startX);
	    	double startYCoord = coordConverter.pixelToUTMCoordY((int) endY);
	    	double endXCoord = coordConverter.pixelToUTMCoordX((int) endX);
	    	double endYCoord = coordConverter.pixelToUTMCoordY((int) startY);
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
	    	catch (AreaNegativeSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1) {
	    		JOptionPane.showMessageDialog(mp, "The selected area is not within the map, please try again.");
	    		rect = null;
	    		mp.repaint();
	    		//mp.getParentFrame().dispose();
				//e1.printStackTrace();
			}
    	}
    }
    
    public Rectangle getRect() {
    	return rect;
    }
    public boolean isDrawing() {
    	return drawing;
    }
    
	public void zoomOut()
	{
		AreaToDraw area = ras.pop();
		mp.setArea(area);
		mp.setLinesForMap();
		mp.repaint();
	}
}
