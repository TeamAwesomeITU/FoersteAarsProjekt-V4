package gui.customJComponents;


import gui.settingsAndPopUp.ColorTheme;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;

/**
 * This is our own extension of the JButton.
 * It allows us to easily change the look of all buttons.
 */
@SuppressWarnings("serial")
public class ColoredJButton extends JButton {
	/**
	 * Calls its superclass and makes a button without text.
	 */
	public ColoredJButton(){
		super();
		stylize();
	}
	/**
	 * Calls its superclass and makes a button with text.
	 * @param text the text of the button
	 */
	public ColoredJButton(String text){
		super(text);
		stylize();		
	}
	
	/**
	 * Makes our button the size and color of our choosing.
	 */
	private void stylize(){
		//The color of the button when pressed
		UIManager.put("Button.select", ColorTheme.BUTTON_CLICKED_COLOR);
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
		UIManager.put("Button.highlight", Color.black);
	}
	/**
	 * When the field looses focus it returns to it original state.
	 */
	public void outOfFocusColor(){
		UIManager.put("Button.highlight", Color.white);
	}
	/**
	 * colors the button depending on if it has focus or not.
	 */
	class HighlightFocusListener implements FocusListener{
		
		private ColoredJButton field;
		
		public HighlightFocusListener(ColoredJButton field){
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
