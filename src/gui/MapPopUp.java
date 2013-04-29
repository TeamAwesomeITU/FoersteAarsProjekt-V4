package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
class MapPopUp extends JPopupMenu {
    public MapPopUp(){
    	addItems();
    	stylize();
    }
    
    private void addItems(){
    	ColoredJMenuItem coordsCopyItem = new ColoredJMenuItem("Copy coordinates to clipboad");
    	coordsCopyItem.addActionListener(new CopyCoordsActionListener());
    	coordsCopyItem.setEnabled(MainGui.coordinatesBoolean);
    	
    	ColoredJMenuItem zoomInItem = new ColoredJMenuItem("Zoom in");
    	zoomInItem.addActionListener(new ZoomInActionListener());
    	
    	ColoredJMenuItem zoomOutItem = new ColoredJMenuItem("Zoom out");
    	zoomOutItem.addActionListener(new ZoomOutActionListener());
    	
        add(coordsCopyItem);
        add(zoomInItem);
        add(zoomOutItem);
    }
    
    private void stylize(){
    	setBackground(ColorTheme.BACKGROUND_COLOR);
    	setForeground(ColorTheme.TEXT_COLOR);
    }
    
    class CopyCoordsActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CoordsClipboard textTransfer = new CoordsClipboard();
			textTransfer.setClipboardContents(MapWindow.getCoordinatesString());
		}
    	
    }
    
    class ZoomInActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		}
    	
    }
    
    class ZoomOutActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		}
    	
    }
}