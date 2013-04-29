package gui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class MapPopUp extends JPopupMenu {
    JMenuItem anItem;
    public MapPopUp(){
        anItem = new JMenuItem("Click Me!");
        add(anItem);
    }
}