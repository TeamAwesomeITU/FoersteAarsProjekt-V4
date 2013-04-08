package mapDrawer.drawing;


import gui.MapWindow;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * MapComponentAdapter is used for listening to our MapWindow if it is resized. If it is resized, the MapPanel is resized and therfor,
 * the map within it, is also resized.
 * 
 */
public class MapComponentAdapter extends ComponentAdapter {
	private Timer recalculateTimer = new Timer( 500, new MapActionListener());
	private boolean isResizing = false;
	private JFrame jf;
	private MapPanel mp;
	private MapWindow mw;

	/**
	 * Constructor for a MapComponentAdapter.
	 * @param mw is the MapWindow that is listened to.
	 */
	public MapComponentAdapter(MapWindow mw) {
		Component[] centerArray = mw.getCenterColoredJPanel().getComponents(); Component component = centerArray[0];
		if(component instanceof MapPanel)
			mp = (MapPanel)component;
		jf = mw.getJFrame();
		this.mw = mw;
		recalculateTimer.setRepeats(false);
	}
	
	/**
	 * If the MapWindow is being resized this is called. Makes sure that the map is not 
	 * resized constantly while you're dragging the MapWindow bigger/smaller.
	 */
	public void componentResized(ComponentEvent e) {
		if(jf.isVisible() == true) {
			isResizing = true;
			if (recalculateTimer.isRunning()){
				recalculateTimer.restart();
			} 	
			else {
				recalculateTimer.start();
			}
		}
	}
	
	/**
	 * Is used for handling the resize of the map within the MapPanel.
	 *
	 */
	private class MapActionListener implements ActionListener {
		
		public MapActionListener() {
			
		}

		/**
		 * In case of a resize event, the map is resized to fit the new width and height.
		 */
		public void actionPerformed(ActionEvent e) {
				if(isResizing == true) {
					double newWidth = mw.getWidthForMap();
					mp.setMapWidth(Math.round(newWidth)); 
					double newHeight = (newWidth*mp.getArea().getHeight())/mp.getArea().getWidth();
					mp.setMapHeight(Math.round(newHeight));
					mp.repaintMap();
					isResizing = false;
			}
		}
		
	}
}