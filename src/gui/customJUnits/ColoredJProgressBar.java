package gui.customJUnits;


import gui.settingsAndPopUp.ColorTheme;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

@SuppressWarnings("serial")
public class ColoredJProgressBar extends JProgressBar {

	public ColoredJProgressBar(){
		super();
		stylize();
	}
	
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
