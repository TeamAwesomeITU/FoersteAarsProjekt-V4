package gui.customJComponents;

import gui.settingsAndPopUp.ColorTheme;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
 
/**
 * This code is taken from codejava.net, we take no credit for it. We have customized the code a bit to fit our needs.
 * It is used to make a custom gui for our ComboBoxes.
 * Customer renderer for JComboBox
 * @author www.codejava.net
 *
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class CustomItemRenderer extends JPanel implements ListCellRenderer {
    private JLabel labelItem = new JLabel();
     
    public CustomItemRenderer() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 2, 2, 2);
         
        labelItem.setOpaque(true);
        labelItem.setHorizontalAlignment(JLabel.LEFT);
         
        add(labelItem, constraints);
        setBackground(ColorTheme.BACKGROUND_COLOR);
    }
     
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String[] vehicleItem = (String[]) value;
        
        labelItem.setText(vehicleItem[0]);
        labelItem.setIcon(new ImageIcon(vehicleItem[1]));
         
        if (isSelected) {
            labelItem.setBackground(ColorTheme.BUTTON_CLICKED_COLOR);
            labelItem.setForeground(ColorTheme.TEXT_COLOR);
        } else {
            labelItem.setBackground(ColorTheme.BACKGROUND_COLOR);
            labelItem.setForeground(ColorTheme.TEXT_COLOR);
        }
         
        return this;
    }
 
}