package gui;
import javax.swing.JPanel;

/**
 * Our implementation of our JPanel
 */
@SuppressWarnings("serial")
public class ColoredJPanel extends JPanel {
	/**
	 * Calls the super constructor and sets the color of the panel.
	 */
	public ColoredJPanel(){
		super();
		super.setBackground(MainGui.BACKGROUND_COLOR);
	}
	

}
