package mapDrawer;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.Timer;

import GUI.MapWindow;


public class MapComponentAdapter extends ComponentAdapter {
	private Timer recalculateTimer = new Timer( 500, new MapActionListener());
	private boolean isResizing = false;
	private JFrame jf;
	private MapPanel mp;
	private int counter = 0;

	public MapComponentAdapter(MapWindow mw) {
		Component[] centerArray = mw.getCenterColoredJPanel().getComponents();
		if(centerArray[0].getClass() == MapPanel)
		mp = centerArray[0];
		jf = mw.getJFrame();
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
			if(counter != 0) {
				if(isResizing == true) {
					
					System.out.println(centerArray[0]);
					jf.setSize((int)(jf.getWidth()*0.9), (int)(jf.getHeight()*0.9));
					isResizing = false;
					counter = 0;
				}
			}
			else
				counter = 1;
		}
		
	}
}