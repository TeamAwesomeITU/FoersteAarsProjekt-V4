package gui;

import java.awt.event.*;

class PopClickListener extends MouseAdapter {
    public void mousePressed(MouseEvent e){
        if (e.isPopupTrigger())
        	makePopUpMenu(e);
    }

    public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger())
        	makePopUpMenu(e);
    }

    private void makePopUpMenu(MouseEvent e){
        MapPopUp menu = new MapPopUp();
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
