package gui.customJUnits;

import javax.swing.JScrollBar;

@SuppressWarnings("serial")
public class ColoredJScrollBar extends JScrollBar {

	public ColoredJScrollBar(){
		super();
		setUI(new ColoredScrollBarUI());
	}
}
