package gui.customJComponents;


import gui.settingsAndPopUp.ColorTheme;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
/**
 * Used to make the arrow of the combobox the same theme as the rest of the combobox.
 */
public class ColoredArrowUI extends BasicComboBoxUI {
	/**
	 * Makes the ui for the arrow.
	 * @param component the component for the arrow.
	 * @return teh arrowUI
	 */
	public static ComboBoxUI createUI(JComponent component){
		return new ColoredArrowUI();
	}
	 
	/**
	 * Creates our custom arrow button theme.
	 */
	protected JButton createArrowButton(){
		return new BasicArrowButton(BasicArrowButton.SOUTH, ColorTheme.DARK_COLOR, 
									ColorTheme.BUTTON_CLICKED_COLOR, ColorTheme.TEXT_COLOR, 
									ColorTheme.BACKGROUND_COLOR);
	}
}
