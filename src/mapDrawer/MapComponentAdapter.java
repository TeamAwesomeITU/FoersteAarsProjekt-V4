package mapDrawer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

public class MapComponentAdapter extends ComponentAdapter {
	private Timer recalculateTimer = new Timer( 500, new MapActionListener());
	private boolean isResizing = false;
	private JFrame jf;

	public MapComponentAdapter(JFrame jf) {
		this.jf = jf;
		recalculateTimer.setRepeats(false);
	}
	
	public void componentResized(ComponentEvent e){
		if(jf.isVisible() == true) {
			isResizing = true;
			if ( recalculateTimer.isRunning() ){
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
			System.out.println("Jeg tror nok vi resizer nu.");
			isResizing = false;
			}
		}
		
	}
}