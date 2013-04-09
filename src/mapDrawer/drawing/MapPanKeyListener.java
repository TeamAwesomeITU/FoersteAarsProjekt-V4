package mapDrawer.drawing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import mapDrawer.AreaToDraw;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.NegativeAreaSizeException;

public class MapPanKeyListener implements KeyListener{
	MapPanel mp;
	AreaToDraw currentArea;
	AreaToDraw newArea;
	
	public MapPanKeyListener(MapPanel mp) {
		this.mp = mp;
		currentArea = mp.getArea();
	}
	public void keyPressed(KeyEvent e) {
		//LEFT
		System.out.println(e.getKeyCode());
		if(mp.hasFocus() && e.getKeyCode() == 10) {
			try {
				newArea = new AreaToDraw(currentArea.getSmallestX()*1.10, currentArea.getLargestX(), currentArea.getSmallestY(), currentArea.getLargestY(), true);
			} catch (NegativeAreaSizeException e1) {
				System.out.println("Mark, you are retarded, fix your code, something's wrong with the new X.");
				newArea = currentArea;
			} catch (AreaIsNotWithinDenmarkException e1) {
				newArea = currentArea;
			}
			mp.repaintMap(newArea);
		}
		
		//RIGHT
		if(mp.hasFocus() && e.getKeyCode() == 10) {
			try {
				newArea = new AreaToDraw(currentArea.getSmallestX(), currentArea.getLargestX()*1.10, currentArea.getSmallestY(), currentArea.getLargestY(), true);
			} catch (NegativeAreaSizeException e1) {
				System.out.println("Mark, you are retarded, fix your code, something's wrong with the new X.");
				newArea = currentArea;
			} catch (AreaIsNotWithinDenmarkException e1) {
				newArea = currentArea;
			}
			mp.repaintMap(newArea);
		}
		
		
		//UP
		if(mp.hasFocus() && e.getKeyCode() == 10) {
			try {
				newArea = new AreaToDraw(currentArea.getSmallestX(), currentArea.getLargestX(), currentArea.getSmallestY()*1.10, currentArea.getLargestY(), true);
			} catch (NegativeAreaSizeException e1) {
				System.out.println("Mark, you are retarded, fix your code, something's wrong with the new Y.");
				newArea = currentArea;
			} catch (AreaIsNotWithinDenmarkException e1) {
				newArea = currentArea;
			}
			mp.repaintMap(newArea);
		}
		
		//DOWN
		if(mp.hasFocus() && e.getKeyCode() == 10) {
			try {
				newArea = new AreaToDraw(currentArea.getSmallestX(), currentArea.getLargestX(), currentArea.getSmallestY(), currentArea.getLargestY()*1.10, true);
			} catch (NegativeAreaSizeException e1) {
				System.out.println("Mark, you are retarded, fix your code, something's wrong with the new Y.");
				newArea = currentArea;
			} catch (AreaIsNotWithinDenmarkException e1) {
				newArea = currentArea;
			}
			mp.repaintMap(newArea);
		}
		
	}

	public void keyReleased(KeyEvent e) {
		System.out.println(e.getKeyCode());
	}

	public void keyTyped(KeyEvent e) {
		System.out.println(e.getKeyCode());
	}

}
