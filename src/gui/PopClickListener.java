package gui;

import java.awt.event.*;

class PopClickListener extends MouseAdapter {
    public void mousePressed(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    private void doPop(MouseEvent e){
        MapPopUp menu = new MapPopUp();
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
