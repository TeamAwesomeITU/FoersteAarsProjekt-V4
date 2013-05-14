package gui.customJComponents;

import gui.MapWindow;
import gui.settingsAndPopUp.ColorTheme;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings({ "serial", "hiding" })
public class SearchList<Object> extends JList<String> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SearchList(ListModel model){
		super(model);
		stylize();
		addMouseListener(new SearchListSelectionListener());
	}

	private void stylize(){
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setLayoutOrientation(JList.VERTICAL);
		setBackground(ColorTheme.BACKGROUND_COLOR);
		setForeground(ColorTheme.TEXT_COLOR);

		ColoredJScrollPane listScroller = new ColoredJScrollPane(this);
		listScroller.setPreferredSize(new Dimension(250, 80));
	}

	class SearchListSelectionListener implements MouseListener{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			String adressSelected = (String) getSelectedValue();
			if(MapWindow.fromSearchQuery.hasFocus())
				MapWindow.fromSearchQuery.setText(adressSelected);
			if(MapWindow.toSearchQuery.hasFocus())
				MapWindow.toSearchQuery.setText(adressSelected);
			MapWindow.listWindow.dispose();
			
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}
