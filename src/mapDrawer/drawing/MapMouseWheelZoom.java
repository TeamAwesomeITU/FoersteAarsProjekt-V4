package mapDrawer.drawing;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.Timer;
import mapDrawer.AreaToDraw;
import mapDrawer.dataSupplying.CoordinateConverter;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.NegativeAreaSizeException;

public class MapMouseWheelZoom implements MouseWheelListener {

	private Timer recalculateTimer = new Timer(300, new ZoomWheelActionListener());
	private double zoomWay = 0;
	private MapPanel mp;
	private double smallX = 0, bigX = 0, smallY = 0, bigY = 0;
	private Point mouseLocation;
	private AreaToDraw currentArea, newArea;
	
	public MapMouseWheelZoom(MapPanel mp) {
		recalculateTimer.setRepeats(false);
		this.mp = mp;
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (recalculateTimer.isRunning()) {
			recalculateTimer.restart();
			zoomWay += e.getWheelRotation();
		}
		else {
			recalculateTimer.start();
			zoomWay += e.getWheelRotation();
		}
		mouseLocation = e.getPoint();
		
		
	}
	
	private class ZoomWheelActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if(zoomWay != 0) {
				currentArea = mp.getArea();								
				if(zoomWay>0) {
					//ZOOM OUT
					System.out.println("over 0");
					smallX = smallX*(1-((2*zoomWay)/100));
					bigX = mp.getMapWidth()+(2*zoomWay);
					smallY -= 2*zoomWay;
					bigY = mp.getMapHeight()+(2*zoomWay);
					CoordinateConverter coordConverter = new CoordinateConverter(mp.getMapWidth(), mp.getMapHeight(), currentArea);
					smallX = coordConverter.pixelToUTMCoordX((int)smallX);
					bigX = coordConverter.pixelToUTMCoordX((int)bigX);
					smallY = coordConverter.pixelToUTMCoordY((int)smallY);
					bigY = coordConverter.pixelToUTMCoordY((int)bigY);
				}
			
				else {
					zoomWay = Math.abs(zoomWay);					
					double xCoord = mouseLocation.getX();
					double yCoord = mouseLocation.getY();
					double percentageOfMapX = zoomWay*(5*(mp.getMapWidth()/100));
					double percentageOfMapY = zoomWay*(5*(mp.getMapHeight()/100));
					System.out.println("under 0");
					System.out.println(mouseLocation.getX() +", " +mouseLocation.getY());
					smallX = xCoord-percentageOfMapX;
					bigX = xCoord+percentageOfMapX;
					smallY = yCoord-percentageOfMapY;
					bigY = yCoord+percentageOfMapY;
					/*if(bigX > mp.getWidth()) {
						double diffX = bigX-mp.getWidth(); smallX -= diffX; bigX = mp.getWidth();
					}
					else if(smallX < 0)
					if(bigY > mp.getHeight()) {
						double diffY = bigY-mp.getHeight(); smallY -= diffY; bigY = mp.getHeight();
					}*/
					
					System.out.println(smallX +", "+ bigX +", "+ smallY +", "+ bigY);
					CoordinateConverter coordConverter = new CoordinateConverter(mp.getMapWidth(), mp.getMapHeight(), currentArea);
					smallX = coordConverter.pixelToUTMCoordX((int)smallX);
					bigX = coordConverter.pixelToUTMCoordX((int)bigX);
					smallY = coordConverter.pixelToUTMCoordY((int)smallY);
					bigY = coordConverter.pixelToUTMCoordY((int)bigY);
					System.out.println(smallX +", "+ bigX +", "+ smallY +", "+ bigY);
				}
				zoomWay = 0;
				try {
		    		newArea = new AreaToDraw(smallX, bigX, bigY, smallY, true);
		  		} catch (NegativeAreaSizeException e1) {
		  			System.out.println("NASE");
		  			newArea = currentArea;
				} catch (AreaIsNotWithinDenmarkException e1) {
					System.out.println("ASIWDE");
					newArea = currentArea;
				}
				mp.repaintMap(newArea);
			}
		}
	}

}
