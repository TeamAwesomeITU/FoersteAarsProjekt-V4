package gui.customJUnits;

import gui.settingsAndPopUp.ColorTheme;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
 
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxEditor;
 
/**
 * This code is taken from codejava.net, we take no credit for it. We have customized the code a bit to fit our needs.
 * It is used to make a custom gui for our ComboBoxes.
 * Editor for JComboBox
 * @author wwww.codejava.net
 *
 */
public class CustomItemEditor extends BasicComboBoxEditor {
    private JPanel panel = new JPanel();
    private JLabel labelItem = new JLabel();
    private String selectedValue;
     
    public CustomItemEditor() {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 5, 2, 2);
         
        labelItem.setOpaque(false);
        labelItem.setHorizontalAlignment(JLabel.LEFT);
        labelItem.setForeground(ColorTheme.TEXT_COLOR);
         
        panel.add(labelItem, constraints);
        panel.setBackground(ColorTheme.BACKGROUND_COLOR);        
    }
     
    public Component getEditorComponent() {
        return this.panel;
    }
     
    public Object getItem() {
        return this.selectedValue;
    }
     
    public void setItem(Object item) {
        if (item == null) {
            return;
        }
        try {
            String[] customList = (String[]) item;
            selectedValue = customList[0];
            labelItem.setText(selectedValue);
            labelItem.setIcon(new ImageIcon(customList[1]));  
		} catch (ClassCastException e) {
			return;
		}     
    }   
}
