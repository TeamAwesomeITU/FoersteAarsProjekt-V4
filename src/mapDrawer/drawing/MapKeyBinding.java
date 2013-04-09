package mapDrawer.drawing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class MapKeyBinding extends AbstractAction{

	    String action;
	    public MapKeyBinding(String actionName){
	    action = actionName;
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
