package gui.settingsAndPopUp;

import gui.MainGui;
import gui.MapWindow;
import gui.customJComponents.ColoredJMenuItem;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import mapCreationAndFunctions.MapMouseZoomAndPan;
import mapCreationAndFunctions.MapPanel;
/**
 * The pop up menu for our map
 */
@SuppressWarnings("serial")
public class MapPopUp extends JPopupMenu {
	private MapPanel mapPanel;
	private Point zoomInPoint;
	/**
	 * Creates the pop up for the mappanel
	 * @param mapPanel which has the pop up
	 * @param e the coordinates of the mouse. Used for the zoomin function
	 */
    public MapPopUp(MapPanel mapPanel, MouseEvent e){
    	addItems();
    	stylize();
    	this.mapPanel = mapPanel;
    	zoomInPoint = e.getPoint();
    }
    
    /**
     * Adds the menuitems to the popup menu 
     */
    private void addItems(){
    	ColoredJMenuItem locationCopyTo = new ColoredJMenuItem("Copy location to TO");
    	locationCopyTo.addActionListener(new CopyToActionListener());
    	
    	ColoredJMenuItem locationCopyFrom = new ColoredJMenuItem("Copy location to FROM");
    	locationCopyFrom.addActionListener(new CopyFromActionListener());
    	
    	ColoredJMenuItem coordsCopyItem = new ColoredJMenuItem("Copy coordinates to clipboad");
    	coordsCopyItem.addActionListener(new CopyCoordsActionListener());
    	coordsCopyItem.setEnabled(MainGui.coordinatesBoolean);
    	
    	ColoredJMenuItem zoomInItem = new ColoredJMenuItem("Zoom in");
    	zoomInItem.addActionListener(new ZoomInActionListener());
    	
    	ColoredJMenuItem zoomOutItem = new ColoredJMenuItem("Zoom out");
    	zoomOutItem.addActionListener(new ZoomOutActionListener());
    	
    	add(locationCopyTo);
    	add(locationCopyFrom);
        add(coordsCopyItem);
        add(new JSeparator());
        add(zoomInItem);
        add(zoomOutItem);
    }
    
    /**
     * Styles the menu
     */
    private void stylize(){
    	setBackground(ColorTheme.BACKGROUND_COLOR);
    	setForeground(ColorTheme.TEXT_COLOR);
    }
    
    /**
     * Copies the address to the to textfield
     */
    class CopyToActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			MapWindow.toSearchQuery.setText(MainGui.locationString);
		}
    	
    }

    /**
     * Copies the address to the from textfield
     */
    class CopyFromActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			MapWindow.fromSearchQuery.setText(MainGui.locationString);
		}
    	
    }
    /**
     * Copy the coordinates to the clipboard. The user can then paste them into any program.
     */
    class CopyCoordsActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CoordsClipboard textTransfer = new CoordsClipboard();
			textTransfer.setClipboardContents(MainGui.coordinatesString);
		}
    	
    }
    /**
     * Zooms in on the map depening on the mouse's position
     */
    class ZoomInActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			MapMouseZoomAndPan mapMouseZoomAndPan = mapPanel.getMapMouseZoomAndPan();
			mapMouseZoomAndPan.zoomInFromPopUpMenu(zoomInPoint);
		}
    	
    }
    
    /**
     * Zooms all the way out to the start.
     */
    class ZoomOutActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			MapMouseZoomAndPan mapMouseZoomAndPan = mapPanel.getMapMouseZoomAndPan();
			mapMouseZoomAndPan.zoomOut();
		}
    	
    }
}