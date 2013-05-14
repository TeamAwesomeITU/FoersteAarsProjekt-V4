package gui.customJComponents;



import gui.settingsAndPopUp.ColorTheme;

import javax.swing.JMenuItem;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ColoredJMenuItem extends JMenuItem {
	public ColoredJMenuItem(String text){
		super(text);
		stylize();
	}
	
	private void stylize(){
		setBackground(ColorTheme.BACKGROUND_COLOR);
		setForeground(ColorTheme.TEXT_COLOR);
		UIManager.put("MenuItem.selectionBackground", ColorTheme.BUTTON_CLICKED_COLOR);
		UIManager.put("MenuItem.selectionForeground", ColorTheme.BACKGROUND_COLOR);
	}
}
