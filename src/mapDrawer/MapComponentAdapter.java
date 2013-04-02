package mapDrawer;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import GUI.ColoredJPanel;
import GUI.MapWindow;


public class MapComponentAdapter extends ComponentAdapter {
	private Timer recalculateTimer = new Timer( 500, new MapActionListener());
	private boolean isResizing = false;
	private JFrame jf;
	private MapPanel mp;
	private MapWindow mw;

	public MapComponentAdapter(MapWindow mw) {
		Component[] centerArray = mw.getCenterColoredJPanel().getComponents(); Component component = centerArray[0];
		if(component instanceof MapPanel)
			mp = (MapPanel)component;
		jf = mw.getJFrame();
		this.mw = mw;
		recalculateTimer.setRepeats(false);
	}
	
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
	
	private class MapActionListener implements ActionListener {
		
		public MapActionListener() {
			
		}

		public void actionPerformed(ActionEvent e) {
				if(isResizing == true) {
					double newWidth = mw.getWidthForMap();
					mp.setWidth(Math.round(newWidth)); 
					double newHeight = (newWidth*mp.getArea().getHeight())/mp.getArea().getWidth();
					mp.setHeight(Math.round(newHeight));
					mp.setLinesForMap();
					mp.repaint();
					isResizing = false;
			}
		}
		
	}
}