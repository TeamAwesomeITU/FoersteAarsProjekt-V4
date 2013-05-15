package gui.customJComponents;


import gui.settingsAndPopUp.ColorTheme;

import javax.swing.BorderFactory;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
/**
 * This is our own extension of the JMenubar
 */
@SuppressWarnings("serial")
public class ColoredJMenuBar extends JMenuBar {
	
	/**
	 * Calls the super constructor and the stylize method.
	 */
	public ColoredJMenuBar(){
		super();
		stylize();
	}
	/**
	 * Styles the menubar according to our desire.
	 */
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
