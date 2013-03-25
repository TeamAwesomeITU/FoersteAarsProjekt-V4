package GUI;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class ColoredJCheckBox extends JCheckBox {
	
	ColoredJCheckBox(String text){
		super();
		super.setBackground(MainGui.BACKGROUND_COLOR);
		super.setText(text);
	}
	
	public ColoredJCheckBox(){
		super();
		super.setBackground(MainGui.BACKGROUND_COLOR);
	}

}