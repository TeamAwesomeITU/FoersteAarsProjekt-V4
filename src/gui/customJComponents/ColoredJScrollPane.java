package gui.customJComponents;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Our own extension of the JScrollPane
 */
@SuppressWarnings("serial")
public class ColoredJScrollPane extends JScrollPane {
	/**
	 * Calls the super constructor and makes a scrollpane for the list
	 * @param list to create a scrollpane for
	 */
	@SuppressWarnings("rawtypes")
	public ColoredJScrollPane(JList list){
		super(list);
		setVerticalScrollBar(new ColoredJScrollBar());
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	/**
	 * calls the super constructor and makes a scrollpane for the panel
	 * @param panel to create a scrollpane for
	 */
	public ColoredJScrollPane(JPanel panel){
		super(panel);
		setVerticalScrollBar(new ColoredJScrollBar());
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
}
