package gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class CustomJTextField extends JTextField {
	
	public CustomJTextField(){
		super();
		stylize();
	}
	
	private void stylize(){
		setBorder(BorderFactory.createRaisedSoftBevelBorder());
		addFocusListener(new HighlightFocusListener(this));

	}
	
	public void inFocusColor(){
		UIManager.put("TextField.highlight", Color.black);
	}
	
	public void outOfFocusColor(){
		UIManager.put("TextField.highlight", Color.white);
	}
	
	class HighlightFocusListener implements FocusListener{
		
		private CustomJTextField field;
		
		public HighlightFocusListener(CustomJTextField field){
			this.field = field;
		}

		@Override
		public void focusGained(FocusEvent e) {
			field.inFocusColor();
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			field.outOfFocusColor();
		}
		
	}
}
