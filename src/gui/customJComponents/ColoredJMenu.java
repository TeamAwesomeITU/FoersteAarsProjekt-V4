package gui.customJComponents;


import gui.settingsAndPopUp.ColorTheme;

import javax.swing.JMenu;
import javax.swing.UIManager;
/**
 * Our custom extension of the JMenu component
 */
@SuppressWarnings("serial")
public class ColoredJMenu extends JMenu {

	/**
	 * calls the super constructor and the style method.
	 * @param text the text for the menu
	 */
	public ColoredJMenu(String text){
		super(text);
		stylize();
	}
	/**
	 * Styles the menu in our desired way.
	 */
	private void stylize(){
		setBackground(ColorTheme.BACKGROUND_COLOR);
		setForeground(ColorTheme.TEXT_COLOR);
		UIManager.put("Menu.selectionBackground", ColorTheme.BUTTON_CLICKED_COLOR);
		UIManager.put("Menu.selectionForeground", ColorTheme.BACKGROUND_COLOR);
	}

	/**
	 * Adds a menu ColoredJMenuItem to the Menu.
	 * @param item The item to add
	 */
	public void add(ColoredJMenuItem item) {
		super.add(item);
	}
}
