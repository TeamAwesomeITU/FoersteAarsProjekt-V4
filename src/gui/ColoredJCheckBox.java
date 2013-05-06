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
	 * @param text Sets the text of the checkbox.
	 */
	public ColoredJCheckBox(String text){
		super();
		super.setBackground(ColorTheme.BACKGROUND_COLOR);
		super.setText(text);
	}
	/**
	 * Calls the super constructor.
	 */
	public ColoredJCheckBox(){
		super();
		super.setBackground(ColorTheme.BACKGROUND_COLOR);
	}

}
