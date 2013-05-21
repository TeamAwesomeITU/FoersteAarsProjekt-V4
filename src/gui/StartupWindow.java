package gui;


import gui.customJComponents.ColoredJPanel;
import gui.customJComponents.ColoredJProgressBar;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.QuadTree;

/**
 * This class makes the first window the user sees. It is
 * a simple loadingscreen window.
 */
public class StartupWindow{

	private ColoredJProgressBar loadingBar;
	/**
	 * The constructor for startupwindow.
	 * It makes the screen
	 */
	@SuppressWarnings("unused")
	public StartupWindow(){
		createStartupScreen();

		loadingBar.setString("Making QuadTree");
		QuadTree.getEntireQuadTree();
		
		loadingBar.setString("Making NodeArray");
		DataHolding.getNodeArray();

		loadingBar.setString("Making EdgeArray");
		DataHolding.getEdgeArray();
		MainGui.menuBoolean = true;		
		
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
		//ClassLoader classLoader = ImageIO.read(getClass().getResource("resources/Logo.png"));
		logo.setIcon(new ImageIcon("resources/Logo.png"));
		logo.setBorder(BorderFactory.createEmptyBorder());

		ColoredJPanel flow = new ColoredJPanel();
		flow.add(logo);
		loadingBar = new ColoredJProgressBar();
		loadingBar.setFont(loadingBar.getFont().deriveFont(40.0f));

		loadingPanel.add(flow);
		loadingPanel.add(loadingBar);
		return loadingPanel;
	}
}