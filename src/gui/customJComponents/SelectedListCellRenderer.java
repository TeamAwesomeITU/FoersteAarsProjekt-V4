package gui.customJComponents;

import gui.settingsAndPopUp.ColorTheme;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

@SuppressWarnings("serial")
/**
 * This is used to change the color of the selected item in the list.
 * It's our own extension of the the defaultListCellRenderer
 */
public class SelectedListCellRenderer extends DefaultListCellRenderer {
	/**
	 * Overrides the normal getListCellRendererComponent.
	 * Then changes the background if it is selected to our wish.
	 */
    @SuppressWarnings("rawtypes")
	@Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (isSelected) {
            c.setBackground(ColorTheme.BUTTON_CLICKED_COLOR);
        }
        return c;
    }
}