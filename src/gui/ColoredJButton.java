package gui;

import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;

/**
 * This is our own implementation of the JButton.
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
		
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBorder(BorderFactory.createEtchedBorder());
	}
}
