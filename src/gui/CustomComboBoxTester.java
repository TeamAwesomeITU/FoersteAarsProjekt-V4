package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
 
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
 
public class CustomComboBoxTester extends JFrame {
     
    public CustomComboBoxTester() {
        super("Demo program for custom combobox");
        setLayout(new FlowLayout());
         
        ColoredJComboBox customCombobox = new ColoredJComboBox();
        customCombobox.setPreferredSize(new Dimension(120, 30));
        customCombobox.setEditable(true);
        customCombobox.addItems(vehicleList);
         
        add(customCombobox);
 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 100);
        setLocationRelativeTo(null);    // center on screen
    }
     
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CustomComboBoxTester().setVisible(true);
            }
        });
    }
 
    private String[][] vehicleList = {{"Bike", "resources/bicycle.png"},
                                      {"Car", "resources/car.png"}};
}
