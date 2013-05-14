package gui.customJComponents;


import gui.MapWindow;
import gui.settingsAndPopUp.ColorTheme;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

/**
 * Our own extension of the JTextField
 */
@SuppressWarnings("serial")
public class CustomJTextField extends JTextField {
	/**
	 * Calls the super construtor and the stylize method
	 */
	public CustomJTextField(){
		super();
		stylize();
	}
	/**
	 * Styles the button in our own way
	 */
	private void stylize(){
		setBorder(BorderFactory.createRaisedSoftBevelBorder());
		addFocusListener(new HighlightFocusListener(this));

	}
	/**
	 * If the field has focus it is highlighted
	 */
	public void inFocusColor(){
		setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorTheme.BUTTON_CLICKED_COLOR));
	}
	/**
	 * When the field doesn't have a focus it is reverted back
	 */
	public void outOfFocusColor(){
		setBorder(BorderFactory.createRaisedSoftBevelBorder());
	}
	/**
	 * Keeps track of whether or not the textfield has focus
	 */
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
