package gui.customJComponents;

import gui.settingsAndPopUp.ColorTheme;
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
	
	private ColoredJScrollPane listScroller;
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
		setCellRenderer(new SelectedListCellRenderer());

		listScroller = new ColoredJScrollPane(this);
	}
	
	public ColoredJScrollPane getScrollPane(){
		return listScroller;
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
			else if(ExpandedSearch.fromRoadNumberField.hasFocus())
				ExpandedSearch.fromRoadNumberField.setText(adressSelected);
			else if(ExpandedSearch.fromRoadLetterField.hasFocus())
				ExpandedSearch.fromRoadLetterField.setText(adressSelected);
			else if(ExpandedSearch.fromCityNameField.hasFocus())
				ExpandedSearch.fromCityNameField.setText(adressSelected);
			else if(ExpandedSearch.fromCityPostalNumberField.hasFocus())
				ExpandedSearch.fromCityPostalNumberField.setText(adressSelected);
			else if(ExpandedSearch.toRoadNameField.hasFocus())
				ExpandedSearch.toRoadNameField.setText(adressSelected);
			else if(ExpandedSearch.toRoadNumberField.hasFocus())
				ExpandedSearch.toRoadNumberField.setText(adressSelected);
			else if(ExpandedSearch.toRoadLetterField.hasFocus())
				ExpandedSearch.toRoadLetterField.setText(adressSelected);
			else if(ExpandedSearch.toCityNameField.hasFocus())
				ExpandedSearch.toCityNameField.setText(adressSelected);
			else if(ExpandedSearch.toCityPostalNumberField.hasFocus())
				ExpandedSearch.toCityPostalNumberField.setText(adressSelected);
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
