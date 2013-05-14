package inputHandler;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.MapWindow;

public class Zoidberg {


	public static void badInputMessages(){
		final JFrame zoidbergFrame = new JFrame("Malformed Address");

		Container contentPane = zoidbergFrame.getContentPane();
		contentPane.setLayout(new GridLayout(0, 1, 0, 5));

		JPanel zoidbergPanel = new JPanel();
		zoidbergPanel.setLayout(new FlowLayout());

		JLabel zoidbergLabel = new JLabel(new ImageIcon("resources/WhyNotZoidberg.png"));
		JButton okayButton = new JButton(new ImageIcon("resources/okay.png"));
		okayButton.setToolTipText("Click me!");
		okayButton.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == 10){
					zoidbergFrame.dispose();
					MapWindow.toRoadName.setText("");
					MapWindow.fromRoadName.setText("");							
				}
			}
			public void keyTyped(KeyEvent arg0) {}	public void keyReleased(KeyEvent arg0) {}
		});
		okayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoidbergFrame.dispose();
				MapWindow.toRoadName.setText("");
				MapWindow.fromRoadName.setText("");
			}
		});
		okayButton.setBorder(BorderFactory.createEmptyBorder());

		zoidbergPanel.add(zoidbergLabel);
		zoidbergPanel.add(okayButton);

		contentPane.add(zoidbergPanel);

		zoidbergFrame.pack();
		zoidbergFrame.setLocationRelativeTo(null);
		zoidbergFrame.setVisible(true);
	}
}
