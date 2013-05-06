package gui;


import javax.swing.JMenu;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ColoredJMenu extends JMenu {

	public ColoredJMenu(String text){
		super(text);
		stylize();
	}
	
	private void stylize(){
		setBackground(ColorTheme.BACKGROUND_COLOR);
		setForeground(ColorTheme.TEXT_COLOR);
		UIManager.put("Menu.selectionBackground", ColorTheme.BUTTON_CLICKED_COLOR);
		UIManager.put("Menu.selectionForeground", ColorTheme.BACKGROUND_COLOR);
	}

	public void add(ColoredJMenuItem item) {
		super.add(item);
	}
}
