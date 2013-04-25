package gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class ColoredArrowUI extends BasicComboBoxUI {
	public static ComboBoxUI createUI(JComponent component){
		return new ColoredArrowUI();
	}
	 
	protected JButton createArrowButton(){
		return new BasicArrowButton(BasicArrowButton.SOUTH, ColorTheme.DARK_COLOR, 
									ColorTheme.BUTTON_CLICKED_COLOR, ColorTheme.TEXT_COLOR, 
									ColorTheme.BACKGROUND_COLOR);
	}
}