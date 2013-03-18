package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;

public class ColoredJButton extends JButton {
	
	public ColoredJButton(){
		super();
		stylize();
	}
	
	public ColoredJButton(String text){
		super(text);
		stylize();		
	}
	
	private void stylize(){
		//The color of the button when pressed
		UIManager.put("Button.select", MainGui.VERY_LIGHT_COLOR);
		setFocusPainted(false);
		setPreferredSize(new Dimension(140, 20));
		setBackground(MainGui.DARK_COLOR);
		//Sets the color of the text
		setForeground(Color.white);
		
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBorder(BorderFactory.createEtchedBorder());
	}
}
