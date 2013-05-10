package mapCreationAndFunctions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.Timer;

import mapCreationAndFunctions.data.CoordinateConverter;
import mapCreationAndFunctions.exceptions.AreaIsNotWithinDenmarkException;
import mapCreationAndFunctions.exceptions.InvalidAreaProportionsException;
import mapCreationAndFunctions.exceptions.NegativeAreaSizeException;
/**
 * This class is responsible for handling zooming with the mouse wheel, both in and out.
 *
 */
public class MapMouseWheelZoom implements MouseWheelListener {

	private Timer recalculateTimer = new Timer(50, new ZoomWheelActionListener());
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
			mouseLocation = e.getPoint();
		}
	}

	/**
	 * Handles the actual zoom in/out.
	 */
	private class ZoomWheelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if(zoomWay != 0) 
			{
				smallX = 0; bigX = mp.getMapWidth(); smallY = 0; bigY = mp.getMapHeight();
				double xCoord = mouseLocation.getX();
				double yCoord = mouseLocation.getY();
				currentArea = mp.getArea();		
				CoordinateConverter coordConverter = new CoordinateConverter(mp.getMapWidth(), mp.getMapHeight(), currentArea);
				double zoomX = ((coordConverter.pixelToUTMCoordX((int)bigX) - coordConverter.pixelToUTMCoordX((int)smallX))/100);
				double zoomY = ((coordConverter.pixelToUTMCoordY((int)bigY) - coordConverter.pixelToUTMCoordY((int)smallY))/100);						

				if(zoomWay > 0) 
					zoomingOut(zoomX, zoomY, coordConverter);
				else if(zoomWay < 0 && mp.getArea().getPercentageOfEntireMap() > 0.01)
					zoomingIn(coordConverter, xCoord, yCoord, zoomX, zoomY);
				else {
					smallX = mp.getArea().getSmallestX(); bigX = mp.getArea().getLargestX(); 
					smallY = mp.getArea().getSmallestY(); bigY = mp.getArea().getLargestY();
				}

				zoomWay = 0;
				try {		
					newArea = new AreaToDraw(smallX, bigX, smallY, bigY, true);
				} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1) {
					System.out.println(e1.getClass() + ": " + e1.getMessage());
					newArea = currentArea;
				}
				mp.repaintMap(newArea);
			}
		}
		/**
		 * Zooming out is done by adding 15% of the current map to all sides. If any exceed max coords, the remaining difference is added in the opposite side,
		 * until the map is almost full size, then the map is shown in full size instead.
		 * @param zoomX
		 * @param zoomY
		 * @param coordConverter
		 */
		@SuppressWarnings("static-access")
		private void zoomingOut(double zoomX, double zoomY, CoordinateConverter coordConverter) {
			//ZOOM OUT
			smallX = coordConverter.pixelToUTMCoordX((int)smallX) - (zoomX*20);
			bigX = coordConverter.pixelToUTMCoordX((int)bigX) + (zoomX*20);
			smallY = coordConverter.pixelToUTMCoordY((int)smallY) - (zoomY*20);
			bigY = coordConverter.pixelToUTMCoordY((int)bigY) + (zoomY*20);
			double temp = bigY; bigY = smallY; smallY = temp;
			if(bigX > currentArea.getLargestXOfEntireMap()) {
				bigX = currentArea.getLargestXOfEntireMap();
			}
			if(smallX < currentArea.getSmallestXOfEntireMap()) {
				smallX = currentArea.getSmallestXOfEntireMap();
			}
			if(bigY > currentArea.getLargestYOfEntireMap()) {
				bigY = currentArea.getLargestYOfEntireMap();
			}
			if(smallY < currentArea.getSmallestYOfEntireMap()) {
				smallY = currentArea.getSmallestYOfEntireMap();
			} 
		}

		/** 
		 * Zooming in is done by taking the mouse's position and adding 25% of the current maps size to either side, and thus there's a new areaToDraw.
		 * @param coordConverter
		 * @param xCoord
		 * @param yCoord
		 * @param zoomX
		 * @param zoomY
		 */
		@SuppressWarnings("static-access")
		private void zoomingIn(CoordinateConverter coordConverter, double xCoord, double yCoord, double zoomX, double zoomY) {				
			smallX = coordConverter.pixelToUTMCoordX((int)xCoord) - (zoomX*15);
			bigX = coordConverter.pixelToUTMCoordX((int)xCoord) + (zoomX*15);
			smallY = coordConverter.pixelToUTMCoordY((int)yCoord) + (zoomY*15);
			bigY = coordConverter.pixelToUTMCoordY((int)yCoord) - (zoomY*15);
			if(bigX > currentArea.getLargestXOfEntireMap()) {
				System.out.println("bigX");
				bigX = currentArea.getLargestXOfEntireMap();
			}
			if(smallX < currentArea.getSmallestXOfEntireMap()) {
				System.out.println("smallX");
				smallX = currentArea.getSmallestXOfEntireMap();
			}
			if(bigY > currentArea.getLargestYOfEntireMap()) {
				System.out.println("bigY");
				bigY = currentArea.getLargestYOfEntireMap();
			}
			if(smallY < currentArea.getSmallestYOfEntireMap()) {
				System.out.println("smallY");
				smallY = currentArea.getSmallestYOfEntireMap();
			}
		}	
	}
}
