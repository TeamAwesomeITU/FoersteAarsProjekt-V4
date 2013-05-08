package gui.customJUnits;

import gui.MapWindow;
import gui.settingsAndPopUp.ColorTheme;

import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class SearchList extends JList<String> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SearchList(ListModel model){
		super(model);
		stylize();
		addListSelectionListener(new SearchListSelectionListener());
	}

	private void stylize(){
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setLayoutOrientation(JList.VERTICAL);
		setBackground(ColorTheme.BACKGROUND_COLOR);
		setForeground(ColorTheme.TEXT_COLOR);

		JScrollPane listScroller = new ColoredJScrollPane(this);
		listScroller.setPreferredSize(new Dimension(250, 80));
	}

	class SearchListSelectionListener implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(e.getValueIsAdjusting())
				return;
			else {
				if(MapWindow.fromSearchQuery.hasFocus())
						MapWindow.fromSearchQuery.setText(getSelectedValue().toString());
				if(MapWindow.toSearchQuery.hasFocus())
						MapWindow.toSearchQuery.setText(getSelectedValue().toString());
			}
		}
	}
}
