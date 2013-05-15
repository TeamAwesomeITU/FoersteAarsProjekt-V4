package gui.customJComponents;

import gui.ExpandedSearch;
import gui.MapWindow;
import gui.settingsAndPopUp.ColorTheme;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
/**
 * Our custom extension of JList. This is used to display the suggestions for addresses.
 *  @param <Object> the object which the list holds
 */
@SuppressWarnings({ "serial", "hiding" })
public class SearchList<Object> extends JList<Object> {
	/**
	 * Calls the super constructor and the stylize method.
	 * Also adds a ListSelectionListener to the list.
	 * @param model that holds the strings in the list.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SearchList(ListModel model){
		super(model);
		stylize();
		addMouseListener(new SearchListSelectionListener());
	}
	/**
	 * Styles the list to our desire
	 */
	private void stylize(){
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setLayoutOrientation(JList.VERTICAL);
		setBackground(ColorTheme.BACKGROUND_COLOR);
		setForeground(ColorTheme.TEXT_COLOR);

		ColoredJScrollPane listScroller = new ColoredJScrollPane(this);
		listScroller.setPreferredSize(new Dimension(250, 80));
	}

	/**
	 * Copies the selected address to the right field.
	 */
	class SearchListSelectionListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			String adressSelected = (String) getSelectedValue();
			if(ExpandedSearch.fromRoadNameField.hasFocus())
				ExpandedSearch.fromRoadNameField.setText(adressSelected);
			ExpandedSearch.listWindow.dispose();

				/*if(MapWindow.fromSearchQuery.hasFocus())
				MapWindow.fromSearchQuery.setText(adressSelected);
			if(MapWindow.toSearchQuery.hasFocus())
				MapWindow.toSearchQuery.setText(adressSelected);*/
				//MapWindow.listWindow.dispose();

			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		}
	}
