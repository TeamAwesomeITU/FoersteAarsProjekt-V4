package mapDrawer.drawing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import mapDrawer.AreaToDraw;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.InvalidAreaProportionsException;
import mapDrawer.exceptions.NegativeAreaSizeException;




@SuppressWarnings("serial")
public class MapKeyBinding extends AbstractAction{

	private String action;
	private JTextField toField, fromField;
	private MapPanel mp;
	private AreaToDraw currentArea, newArea;
	private double smallX, bigX, smallY, bigY;
	private Timer recalculateTimer = new Timer(10, new MapPanActionListener());
	private double keyPressedRight = 0, keyPressedLeft = 0, keyPressedUp = 0, keyPressedDown = 0;
	    
	public MapKeyBinding(String actionName, JTextField toField, JTextField fromField, MapPanel mp){
		action = actionName;
	    this.toField = toField;
	    this.fromField = fromField;
	    this.mp = mp;
	    recalculateTimer.setRepeats(false);
	}
	    
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
    
    @SuppressWarnings("static-access")
	public static void addKeyBinding(MapPanel mp, JTextField toField, JTextField fromField) {

    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right");
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "right");
    	mp.getActionMap().put("right", new MapKeyBinding("right", toField, fromField, mp));
    	
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left");
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "left");
    	mp.getActionMap().put("left", new MapKeyBinding("left", toField, fromField, mp));
    	
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "up");
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "up");
    	mp.getActionMap().put("up", new MapKeyBinding("up", toField, fromField, mp));
    	
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "down");
    	mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "down");
    	mp.getActionMap().put("down", new MapKeyBinding("down", toField, fromField, mp));
    }
    
    private class MapPanActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			currentArea = mp.getArea();
			smallX = currentArea.getSmallestX();
			bigX = currentArea.getLargestX();
			smallY = currentArea.getSmallestY();
			bigY = currentArea.getLargestY();
	    	if(toField.hasFocus() == false && fromField.hasFocus() == false) {
	    		double NumberOfTimesToPan = 0;
	    		//GOING LEFT
	    		if (keyPressedLeft>keyPressedRight) {
					NumberOfTimesToPan = 1-((keyPressedLeft-keyPressedRight)/100);
					smallX = smallX*NumberOfTimesToPan;
					bigX = bigX*NumberOfTimesToPan;
				}
	    		//GOING RIGHT
	    		else if (keyPressedRight>keyPressedLeft) {
					NumberOfTimesToPan = 1+((keyPressedRight-keyPressedLeft)/100);
					smallX = smallX*NumberOfTimesToPan;
					bigX = bigX*NumberOfTimesToPan;
				}
	    		//GOING UP
	    		if (keyPressedDown>keyPressedUp) {
					NumberOfTimesToPan = 1-((keyPressedDown-keyPressedUp)/1000);
					smallY = smallY*NumberOfTimesToPan;
					bigY = bigY*NumberOfTimesToPan;
				}
	    		//GOING DOWN
	    		else if (keyPressedUp>keyPressedDown) {
					NumberOfTimesToPan = 1+((keyPressedUp-keyPressedDown)/1000);
					smallY = smallY*NumberOfTimesToPan;
					bigY = bigY*NumberOfTimesToPan;
				}
		    	keyPressedRight = 0; keyPressedLeft = 0; keyPressedUp = 0; keyPressedDown = 0;
		    	
		    	try {
		    		newArea = new AreaToDraw(smallX, bigX, smallY, bigY, true);
		  		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1) {
		  			e1.printStackTrace();
		  			newArea = currentArea;		  			
				}
				mp.repaintMap(newArea);
		   	}	
		}
    }
}
