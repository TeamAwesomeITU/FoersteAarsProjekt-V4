package gui;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;

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
		setStringPainted(true);
		setBorder(BorderFactory.createEmptyBorder());
	}
}
