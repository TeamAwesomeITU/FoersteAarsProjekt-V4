package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;

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
		
		
		long startTime = System.currentTimeMillis();
		System.out.println("Making QuadTree");
		loadingBar.setString("Making QuadTree");
		QuadTree.getEntireQuadTree();
		long endTime = System.currentTimeMillis();
		System.out.println("QuadTree creation takes " + (endTime - startTime) + " milliseconds");

		long startTime2 = System.currentTimeMillis();
		System.out.println("Making NodeMap");
		loadingBar.setString("Making NodeMap");
		FindRelevantEdges.getNodeCoordinatesMap();
		long endTime2 = System.currentTimeMillis();
		System.out.println("NodeMap creation takes " + (endTime2 - startTime2) + " milliseconds");
		
		long startTime3 = System.currentTimeMillis();
		System.out.println("Making EdgeSet");
		loadingBar.setString("Making EdgeSet");
		FindRelevantEdges.getEdgeSet();
		long endTime3 = System.currentTimeMillis();
		System.out.println("EdgeSet creation takes " + (endTime3 - startTime3) + " milliseconds");
		
		MainGui.menuBoolean = true;
		
		/*
        try {    
    		System.out.println("Making static fields");
    		loadingBar.setString("Making static fields");
    		Thread t1 = new Thread(new QuadTree.QuadTreeCreation(), "Thread1");
    		Thread t2 = new Thread(new FindRelevantEdges.EdgeSetCreation(),"Thread2");
    		Thread t3 = new Thread(new FindRelevantEdges.NodeMapCreation(),"Thread3");

            t1.start();
            t1.join();
            t2.start();
            t2.join();
			t3.start();		
			t3.join();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		
		
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

		ColoredJPanel flow = new ColoredJPanel();
		flow.add(logo);
		loadingBar = new ColoredJProgressBar();
		loadingBar.setFont(loadingBar.getFont().deriveFont(40.0f));

		loadingPanel.add(flow);
		loadingPanel.add(loadingBar);
		return loadingPanel;
	}
}