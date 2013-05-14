package gui.customJComponents;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * This code is taken from codejava.net, we take no credit for it. We have customized the code a bit to fit our needs.
 * It is used to make a custom gui for our ComboBoxes.
 * @author wwww.codejava.net
 *
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class ColoredJComboBox extends JComboBox {
	private DefaultComboBoxModel model;

	@SuppressWarnings("unchecked")
	public ColoredJComboBox(){
		model = new DefaultComboBoxModel();
		setModel(model);
		setRenderer(new CustomItemRenderer());
		setEditor(new CustomItemEditor());
	}

	/**
	 * Add an array items to this combo box.
	 * Each item is an array of two String elements:
	 * - first element is the name.
	 * - second element is path of an image file.
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	public void addItems(String[][] items) {
		for (String[] anItem : items) {
			model.addElement(anItem);
		}
	}
}