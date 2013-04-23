package mapDrawer.drawing.mutators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import mapDrawer.AreaToDraw;
import mapDrawer.dataSupplying.CoordinateConverter;
import mapDrawer.drawing.MapPanel;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.InvalidAreaProportionsException;
import mapDrawer.exceptions.NegativeAreaSizeException;




@SuppressWarnings("serial")
/**
 * This class is responsible for handling panning with the keyboard.
 *
 */
public class MapKeyPan extends AbstractAction{

	private String action;
	private JTextField toField, fromField;
	private MapPanel mp;
	private AreaToDraw currentArea, newArea;
	private double smallX, bigX, smallY, bigY;
	private Timer recalculateTimer = new Timer(0, new MapPanActionListener());
	private double keyPressedRight = 0, keyPressedLeft = 0, keyPressedUp = 0, keyPressedDown = 0;
	    
	public MapKeyPan(String actionName, JTextField toField, JTextField fromField, MapPanel mp){
		action = actionName;
	    this.toField = toField;
	    this.fromField = fromField;
	    this.mp = mp;
	    recalculateTimer.setRepeats(false);
	}
	    
	/**
	 * If none of the two textfields have focus, then we're supposed to move the map, in the direction according to the key pressed.
	 */
    public void actionPerformed(ActionEvent e) {
    	if (recalculateTimer.isRunning()) {
			recalculateTimer.restart();
			if(toField.hasFocus() == false && fromField.hasFocus() == false) {
		    	if(action.equals("right"))
		    		keyPressedRight++;
		    	if(action.equals("left"))
		    		keyPressedLeft++;
		    	if(action.equals("up"))
		    		keyPressedUp++;
		    	if(action.equals("down"))
		    		keyPressedDown++;
			}
    	}
		else {
			recalculateTimer.start();
			if(toField.hasFocus() == false && fromField.hasFocus() == false) {
		    	if(action.equals("right"))
		    		keyPressedRight++;
		    	if(action.equals("left"))
		    		keyPressedLeft++;
		    	if(action.equals("up"))
		    		keyPressedUp++;
		    	if(action.equals("down"))
		    		keyPressedDown++;
			}
		}
    }
    
    /**
     * This method adds the support for panning with wasd and the arrow keys.
     * @param mp
     * @param toField
     * @param fromField
     */
    @SuppressWarnings("static-access")
	public static void addKeyBinding(MapPanel mp, JTextField toField, JTextField fromField) {

    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right");
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "right");
    	mp.getActionMap().put("right", new MapKeyPan("right", toField, fromField, mp));
    	
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left");
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "left");
    	mp.getActionMap().put("left", new MapKeyPan("left", toField, fromField, mp));
    	
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "up");
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "up");
    	mp.getActionMap().put("up", new MapKeyPan("up", toField, fromField, mp));
    	
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "down");
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "down");
    	mp.getActionMap().put("down", new MapKeyPan("down", toField, fromField, mp));
    }
    
    /**
     * Handles the actual panning by adding 1% of the currently shown map to whatever direction the map is supposed to "move" towards.
     *
     */
    private class MapPanActionListener implements ActionListener {

		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) {
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
			double zoomX = (bigX - smallX) /100;
			double zoomY = (bigY - smallY) /100;
			double temp = bigY; bigY = smallY; smallY = temp;
			System.out.println(zoomX +", "+ smallX +","+ bigX +", "+ smallY +", "+ bigY);
	    	if(toField.hasFocus() == false && fromField.hasFocus() == false) {
	    		double pan = 0;
	    		//GOING LEFT
	    		if (keyPressedLeft>keyPressedRight) {
					pan = (keyPressedLeft-keyPressedRight)*zoomX;
					smallX -= pan;
					bigX -= pan;
					if (smallX < currentArea.getSmallestXOfEntireMap()) 
						smallX = currentArea.getSmallestXOfEntireMap();
				}
	    		//GOING RIGHT
	    		else if (keyPressedRight>keyPressedLeft) {
					pan = (keyPressedRight-keyPressedLeft)*zoomX;
					smallX += pan;
					bigX += pan;
					if (bigX > currentArea.getLargestXOfEntireMap()) 
						bigX = currentArea.getLargestXOfEntireMap();
				}
	    		//GOING DOWN
	    		if (keyPressedDown>keyPressedUp) {
					pan = (keyPressedDown-keyPressedUp)*zoomY;
					smallY += pan;
					bigY += pan;
					if (smallY < currentArea.getSmallestYOfEntireMap())
						smallY = currentArea.getSmallestYOfEntireMap();
				}
	    		//GOING UP
	    		else if (keyPressedUp>keyPressedDown) {
					pan = (keyPressedUp-keyPressedDown)*zoomY;
					smallY -= pan;
					bigY -= pan;
					if (bigY > currentArea.getLargestYOfEntireMap())
						bigY = currentArea.getLargestYOfEntireMap();
				}
	    		System.out.println(zoomX +", "+ smallX +","+ bigX +", "+ smallY +", "+ bigY);
		    	keyPressedRight = 0; keyPressedLeft = 0; keyPressedUp = 0; keyPressedDown = 0;
		    	
		    	try {
		    		newArea = new AreaToDraw(smallX, bigX, smallY, bigY, true);
		    		mp.repaintMap(newArea);
		  		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1)	{
		  			//Do nada because you've reached a side. Only  error that should be able to happen.
		  		}
		   	}	
		}
    }
}
