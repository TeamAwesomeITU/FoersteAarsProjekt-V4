package gui.customJComponents;

import gui.settingsAndPopUp.ColorTheme;

import javax.swing.JPanel;

/**
 * Our own extension of our JPanel
 */
@SuppressWarnings("serial")
public class ColoredJPanel extends JPanel {
	/**
	 * Calls the super constructor and sets the color of the panel.
	 */
	public ColoredJPanel(){
		super();
		super.setBackground(ColorTheme.BACKGROUND_COLOR);
	}
	

}
