package gui.customJUnits;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ColoredJScrollPane extends JScrollPane {

	@SuppressWarnings("rawtypes")
	public ColoredJScrollPane(JList list){
		super(list);
		setVerticalScrollBar(new ColoredJScrollBar());
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	public ColoredJScrollPane(JPanel panel){
		super(panel);
		setVerticalScrollBar(new ColoredJScrollBar());
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
}
