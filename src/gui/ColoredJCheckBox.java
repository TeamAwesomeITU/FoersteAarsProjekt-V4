package gui;

import javax.swing.JCheckBox;
/**
 * This is our own implementation of the JCheckBox.
 * It allows us to easily change the look of all checkboxes.
 */
@SuppressWarnings("serial")
public class ColoredJCheckBox extends JCheckBox {
	/**
	 * Calls the super constructor. 
	 * @param Sets the text of the checkbox.
	 */
	ColoredJCheckBox(String text){
		super();
		super.setBackground(MainGui.BACKGROUND_COLOR);
		super.setText(text);
	}
	/**
	 * Calls the super constructor.
	 */
	public ColoredJCheckBox(){
		super();
		super.setBackground(MainGui.BACKGROUND_COLOR);
	}

}
