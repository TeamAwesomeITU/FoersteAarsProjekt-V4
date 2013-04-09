package mapDrawer.drawing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class MapKeyBinding extends AbstractAction{

	String action;
	JTextField toField, fromField;
	    
	public static void addKeyBinding(String key, MapPanel mp, JTextField toField, JTextField fromField){

		mp.getInputMap(mp.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), key);
    	mp.getActionMap().put(key, new MapKeyBinding(key, toField, fromField));
	}
	
	public MapKeyBinding(String actionName, JTextField toField, JTextField fromField){
		action = actionName;
	    this.toField = toField;
	    this.fromField = fromField;
	}
	    
    public void actionPerformed(ActionEvent e){
	    if(action.equals("R")){
	        System.out.println("R");
	    }
	    if(action.equals("B")){
	        System.out.println("B");
	    }
	}
}
