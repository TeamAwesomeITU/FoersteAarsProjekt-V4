package gui.customJComponents;

import javax.swing.JScrollBar;
/**
 * This is our own extension of the JScrollBar
 */
@SuppressWarnings("serial")
public class ColoredJScrollBar extends JScrollBar {
	/**
	 * Calls the constructor and sets up the ui.
	 */
	public ColoredJScrollBar(){
		super();
		setUI(new ColoredScrollBarUI());
	}
}
