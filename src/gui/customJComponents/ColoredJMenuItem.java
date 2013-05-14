package gui.customJComponents;



import gui.settingsAndPopUp.ColorTheme;

import javax.swing.JMenuItem;
import javax.swing.UIManager;
/**
 * Our own extension of the JMenuItem
 */
@SuppressWarnings("serial")
public class ColoredJMenuItem extends JMenuItem {
	/**
	 * Calls the super constructor and the stylize method
	 * @param text the text for the menu item
	 */
	public ColoredJMenuItem(String text){
		super(text);
		stylize();
	}
	/**
	 * Styles the menuitem according to our desire.
	 */
	private void stylize(){
		setBackground(ColorTheme.BACKGROUND_COLOR);
		setForeground(ColorTheme.TEXT_COLOR);
		UIManager.put("MenuItem.selectionBackground", ColorTheme.BUTTON_CLICKED_COLOR);
		UIManager.put("MenuItem.selectionForeground", ColorTheme.BACKGROUND_COLOR);
	}
}
