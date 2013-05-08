package gui.customJUnits;


import gui.MapWindow;
import gui.settingsAndPopUp.ColorTheme;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

@SuppressWarnings("serial")
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
		setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorTheme.BUTTON_CLICKED_COLOR));
	}
	
	public void outOfFocusColor(){
		setBorder(BorderFactory.createRaisedSoftBevelBorder());
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
			if(MapWindow.listWindow != null)
				MapWindow.listWindow.dispose();
		}
		
	}
}
