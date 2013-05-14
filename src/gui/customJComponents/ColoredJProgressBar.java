package gui.customJComponents;


import gui.settingsAndPopUp.ColorTheme;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * Our own extension of the JProgressBar
 */
@SuppressWarnings("serial")
public class ColoredJProgressBar extends JProgressBar {
	/**
	 * Calls the super constructor and styles the progressbar
	 */
	public ColoredJProgressBar(){
		super();
		stylize();
	}
	/**
	 * Styles the progressbar in our own way.
	 */
	private void stylize(){
		setIndeterminate(true);
		setBackground(ColorTheme.BACKGROUND_COLOR);
		setForeground(ColorTheme.BUTTON_CLICKED_COLOR);
		setUI(new BasicProgressBarUI(){
			protected Color getSelectionBackground(){ return ColorTheme.BACKGROUND_COLOR;}
			protected Color getSelectionForeground(){ return ColorTheme.BACKGROUND_COLOR;}
		});
		
		setStringPainted(true);
		setBorder(BorderFactory.createEmptyBorder());
	}
}
