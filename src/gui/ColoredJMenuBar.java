package gui;

import javax.swing.BorderFactory;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ColoredJMenuBar extends JMenuBar {
	
	public ColoredJMenuBar(){
		super();
		stylize();
	}
	
	private void stylize(){
		setBackground(ColorTheme.BUTTON_CLICKED_COLOR);
		setBorder(BorderFactory.createEmptyBorder());
		UIManager.put("Menu.selectionBackground", ColorTheme.BUTTON_CLICKED_COLOR);
		if(ColorTheme.summerTheme || ColorTheme.springTheme)			
			UIManager.put("Menu.selectionForeground", ColorTheme.DARK_COLOR);
		if(ColorTheme.winterTheme || ColorTheme.autumnTheme)
			UIManager.put("Menu.selectionForeground", ColorTheme.BACKGROUND_COLOR);		
	}

}
