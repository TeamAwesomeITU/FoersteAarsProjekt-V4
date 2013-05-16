package gui.customJComponents;

import gui.settingsAndPopUp.ColorTheme;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * This is our own extension of the JToggleButton.
 * It allows us to easily change the look of all buttons.
 */
@SuppressWarnings("serial")
public class ColoredJToggleButton extends JToggleButton {

	/**
	 * Calls its superclass and makes a button without text.
	 */
	public ColoredJToggleButton(){
		super();
		stylize();
	}
	/**
	 * Calls its superclass and makes a button with text.
	 * @param text the text of the button
	 */
	public ColoredJToggleButton(String text){
		super(text);
		stylize();		
	}

	/**
	 * Makes our button the size and color of our choosing.
	 */
	private void stylize(){
		//The color of the button when pressed
		UIManager.put("ToggleButton.select", ColorTheme.BUTTON_CLICKED_COLOR);
		SwingUtilities.updateComponentTreeUI(this);
		setFocusPainted(false);
		setPreferredSize(new Dimension(140, 20));
		setBackground(ColorTheme.DARK_COLOR);
		//Sets the color of the text
		setForeground(ColorTheme.TEXT_COLOR);
		addFocusListener(new HighlightFocusListener(this));

		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBorder(BorderFactory.createRaisedSoftBevelBorder());
	}

	/**
	 * When the field has focus it highligths the field.
	 */
	public void inFocusColor(){
		UIManager.put("ToggleButton.highlight", Color.black);
	}
	/**
	 * When the field looses focus it returns to it original state.
	 */
	public void outOfFocusColor(){
		UIManager.put("ToggleButton.highlight", Color.white);
	}
	/**
	 * colors the button depending on if it has focus or not.
	 */
	class HighlightFocusListener implements FocusListener{

		private ColoredJToggleButton field;

		public HighlightFocusListener(ColoredJToggleButton coloredJToggleButton){
			this.field = coloredJToggleButton;
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
