package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import mapCreationAndFunctions.MapMouseZoomAndPan;
import mapCreationAndFunctions.MapPanel;

@SuppressWarnings("serial")
class MapPopUp extends JPopupMenu {
	private MapPanel mapPanel;
    public MapPopUp(MapPanel mapPanel){
    	addItems();
    	stylize();
    	this.mapPanel = mapPanel;
    }
    
    public MapPopUp(){
    	addItems();
    	stylize();
    }
    
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
    
    private void stylize(){
    	setBackground(ColorTheme.BACKGROUND_COLOR);
    	setForeground(ColorTheme.TEXT_COLOR);
    }
    
    class CopyToActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			MapWindow.toSearchQuery.setText(MainGui.locationString);
		}
    	
    }
    
    class CopyFromActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			MapWindow.fromSearchQuery.setText(MainGui.locationString);
		}
    	
    }
    
    class CopyCoordsActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CoordsClipboard textTransfer = new CoordsClipboard();
			textTransfer.setClipboardContents(MainGui.coordinatesString);
		}
    	
    }
    
    class ZoomInActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			MapMouseZoomAndPan mapMouseZoomAndPan = mapPanel.getMapMouseZoomAndPan();
			mapMouseZoomAndPan.zoomIn();
		}
    	
    }
    
    class ZoomOutActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			MapMouseZoomAndPan mapMouseZoomAndPan = mapPanel.getMapMouseZoomAndPan();
			mapMouseZoomAndPan.zoomOut();
		}
    	
    }
}