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
import mapDrawer.exceptions.InvalidAreaProportionsException;
import mapDrawer.exceptions.NegativeAreaSizeException;

public class MapMouseWheelZoom implements MouseWheelListener {

	private Timer recalculateTimer = new Timer(300, new ZoomWheelActionListener());
	private double zoomWay = 0;
	private MapPanel mp;
	private double smallX, bigX, smallY, bigY;
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
		
		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) {
			if(zoomWay != 0) {
				smallX = 0; bigX = mp.getMapWidth(); smallY = 0; bigY = mp.getMapHeight();
				double xCoord = mouseLocation.getX();
				double yCoord = mouseLocation.getY();
				currentArea = mp.getArea();		
				CoordinateConverter coordConverter = new CoordinateConverter(mp.getMapWidth(), mp.getMapHeight(), currentArea);
				double zoomX = ((coordConverter.pixelToUTMCoordX((int)bigX) - coordConverter.pixelToUTMCoordX((int)smallX))/100);
				double zoomY = ((coordConverter.pixelToUTMCoordY((int)bigY) - coordConverter.pixelToUTMCoordY((int)smallY))/100);						
				if(zoomWay > 0) {
					//ZOOM OUT
					smallX = coordConverter.pixelToUTMCoordX((int)smallX) - (zoomX*10)*Math.abs(zoomWay);
					bigX = coordConverter.pixelToUTMCoordX((int)bigX) + (zoomX*10)*Math.abs(zoomWay);
					smallY = coordConverter.pixelToUTMCoordY((int)smallY) - (zoomY*10)*Math.abs(zoomWay);
					bigY = coordConverter.pixelToUTMCoordY((int)bigY) + (zoomY*10)*Math.abs(zoomWay);
					double temp = bigY; bigY = smallY; smallY = temp;
					System.out.println(smallX +", "+ bigX +", "+ smallY +", "+ bigY);
					if(bigX > currentArea.getLargestXOfEntireMap() || smallX < currentArea.getSmallestXOfEntireMap() || 
							bigY > currentArea.getLargestYOfEntireMap() || smallY < currentArea.getSmallestYOfEntireMap()) {
						smallX = currentArea.getSmallestXOfEntireMap(); bigX = currentArea.getLargestXOfEntireMap();
						smallY = currentArea.getSmallestYOfEntireMap(); bigY = currentArea.getLargestYOfEntireMap();
						System.out.println(smallX +", "+ bigX +", "+ smallY +", "+ bigY);
					}
				}
			
				else {
					//ZOOM IN				
					smallX = coordConverter.pixelToUTMCoordX((int)xCoord) - (zoomX*20)*Math.abs(zoomWay);
					bigX = coordConverter.pixelToUTMCoordX((int)xCoord) + (zoomX*20)*Math.abs(zoomWay);
					smallY = coordConverter.pixelToUTMCoordY((int)yCoord) + (zoomY*20)*Math.abs(zoomWay);
					bigY = coordConverter.pixelToUTMCoordY((int)yCoord) - (zoomY*20)*Math.abs(zoomWay);
					System.out.println(smallX +", "+ bigX +", "+ smallY +", "+ bigY);
					System.out.println(currentArea.getSmallestX() +", "+ currentArea.getLargestX() +", "+ currentArea.getSmallestY() +", "+ currentArea.getLargestY());
					if(bigX > currentArea.getLargestX()) {
						System.out.println("bigX");
						double diffX = bigX-currentArea.getLargestX(); smallX -= diffX; bigX = currentArea.getLargestX();
					}
					else if(smallX < currentArea.getSmallestX()) {
						System.out.println("smallX");
						double diffX = currentArea.getSmallestX()-smallX; bigX += +diffX; smallX = currentArea.getSmallestX();
					}
					if(bigY > currentArea.getLargestY()) {
						System.out.println("bigY");
						double diffY = bigY-currentArea.getLargestY(); smallY -= diffY; bigY = currentArea.getLargestY();
					}
					else if(smallY < currentArea.getSmallestY()) {
						System.out.println("smallY");
						double diffY = currentArea.getSmallestY()-smallY; bigY += diffY; smallY = currentArea.getSmallestY();
					}
				}
				zoomWay = 0;
				try {
					if(smallY < bigY) {
						System.out.println("bingo1");
						newArea = new AreaToDraw(smallX, bigX, smallY, bigY, true);
					}
					else {
						System.out.println("bingo2");
						newArea = new AreaToDraw(smallX, bigX, bigY, smallY, true);
					}
		  		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1) {
		  			System.out.println(e1.getClass() + ": " + e1.getMessage());
		  			newArea = currentArea;
				}
				mp.repaintMap(newArea);
			}
		}
	}

}
