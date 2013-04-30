package gui;

import javax.swing.JMenu;

@SuppressWarnings("serial")
public class ColoredJMenu extends JMenu {

	public ColoredJMenu(String text){
		super(text);
		stylize();
	}
	
	private void stylize(){
		setBackground(ColorTheme.BACKGROUND_COLOR);
		setForeground(ColorTheme.TEXT_COLOR);
	}

	public void add(ColoredJMenuItem item) {
		super.add(item);
	}
}
