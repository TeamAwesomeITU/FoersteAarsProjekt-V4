package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import mapDrawer.dataSupplying.FindRelevantEdges;
import mapDrawer.dataSupplying.QuadTree;

/**
 * This class makes the first window the user sees. It is
 * a simple search window.
 */
public class StartupWindow{

	private ColoredJProgressBar loadingBar;
	/**
	 * The constructor for startupwindow.
	 * It makes the screen
	 */
	public StartupWindow(){
		createStartupScreen();

		System.out.println("Making QuadTree");
		loadingBar.setString("Making QuadTree");
		QuadTree.initializeEntireQuadTree();

		System.out.println("Making NodeMap");
		loadingBar.setString("Making NodeMap");
		FindRelevantEdges.getNodeCoordinatesMap();
		new MapWindow();
	}

	/**
	 * Builds the frame and sets it up
	 */
	public void createStartupScreen(){
		MainGui.contentPane.add(makeLoadingScreen(), BorderLayout.CENTER);
		MainGui.makeMenu();

		MainGui.frame.pack();
		MainGui.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainGui.frame.setVisible(true);
	}
	

	/**
	 * Makes the loading screen.
	 */
	public ColoredJPanel makeLoadingScreen(){
		ColoredJPanel loadingPanel = new ColoredJPanel();
		loadingPanel.setLayout(new GridLayout(0, 1, 3, 1));

		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon("resources/Logo.png"));
		logo.setBorder(BorderFactory.createEmptyBorder());
		logo.setToolTipText("Press the globe to browse the map");

		ColoredJPanel flow = new ColoredJPanel();
		flow.add(logo);
		loadingBar = new ColoredJProgressBar();
		flow.add(loadingBar);
		
		loadingPanel.add(flow);
		return loadingPanel;
	}
}