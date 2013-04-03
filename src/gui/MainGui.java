package gui;

import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import mapDrawer.AreaToDraw;
import mapDrawer.dataSupplying.FindRelevantEdges;
import mapDrawer.dataSupplying.QuadTree;
/**
 * This is our main gui class. It holds methods that is common for all
 * old and future windows.
 */
public class MainGui {

	private static MainGui instance;

	//Three static final fields to easily change the color of our program
	public static final Color DARK_COLOR = new Color(207, 181, 59);

	public static final Color BACKGROUND_COLOR = new Color(255, 215, 0);

	public static final Color BUTTON_CLICKED_COLOR = new Color(230, 190, 138);
	
	public static final Color TEXT_COLOR = new Color(153, 101, 21);

	public static boolean undecoratedBoolean = false;

	public static boolean coordinatesBoolean = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Making QuadTree"); 
		QuadTree.initializeEntireQuadTree();
		System.out.println("Making NodeMap"); 
		FindRelevantEdges.getNodeCoordinatesMap();
		MainGui.getInstance();
	}

	/**
	 * Uses a singleton API to get the instance of the program
	 * @return the instance of the program
	 */
	public static MainGui getInstance(){
		if(instance != null)
			return instance;
		else{
			instance = new MainGui();
			return instance;
		}
	}

	private MainGui(){
		new StartupWindow();
	}

	/**
	 * Makes the menu and adds shortcuts to it. This is the standard menu for all future windows.
	 */
	public static void makeMenu(final JFrame frameForMenu, final int windowID){
		final int SHORT_CUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		JMenuBar menuBar = new JMenuBar();
		frameForMenu.setJMenuBar(menuBar);
		menuBar.setBackground(BACKGROUND_COLOR);
		menuBar.setBorder(BorderFactory.createEtchedBorder());

		JMenu fileMenu = new JMenu("File");
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				frameForMenu.dispose();			
			}
		});
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORT_CUT_MASK));

		JMenuItem settingsItem = new JMenuItem("Settings");
		settingsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame settingsFrame = new JFrame("Settings");
				settingsFrame.setLocationRelativeTo(null);	

				Container container = settingsFrame.getContentPane();
				container.setLayout(new BorderLayout());

				ColoredJPanel settingsPanel = new ColoredJPanel();
				final ColoredJCheckBox undecorated = new ColoredJCheckBox("Undecorated");
				undecorated.setSelected(undecoratedBoolean);

				undecorated.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.DESELECTED){
							undecoratedBoolean = false;
							frameForMenu.dispose();
							frameForMenu.setUndecorated(undecoratedBoolean);
							if(windowID == StartupWindow.getWindowId())
								new StartupWindow();
							if(windowID == MapWindow.getWindowId())
								new MapWindow();
							settingsFrame.dispose();
						}

						if(e.getStateChange() == ItemEvent.SELECTED){
							undecoratedBoolean = true;
							frameForMenu.dispose();
							frameForMenu.setUndecorated(undecoratedBoolean);							
							if(windowID == StartupWindow.getWindowId())
								new StartupWindow();
							if(windowID == MapWindow.getWindowId())
								new MapWindow();
							settingsFrame.dispose();
						}
					}});

				final ColoredJCheckBox coordinates = new ColoredJCheckBox("Coordinates");
				coordinates.setSelected(coordinatesBoolean);
				coordinates.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.DESELECTED)
							coordinatesBoolean = false;
						if(e.getStateChange() == ItemEvent.SELECTED)
							coordinatesBoolean = true;
					}
				});

				container.add(settingsPanel, BorderLayout.CENTER);

				settingsPanel.add(undecorated);
				settingsPanel.add(coordinates);
				settingsFrame.pack();
				settingsFrame.setVisible(true);
			}
		});
		settingsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORT_CUT_MASK));
		fileMenu.add(settingsItem);
		fileMenu.add(quitItem);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frameForMenu, "Welcome to T-A-M maps. Please enter an address" +
						" to get on your way, or simply click the globe to browse the map");		
			}
		});
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, SHORT_CUT_MASK));
		helpMenu.add(aboutItem);

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);		
	}

	/**
	 * Makes the footer for all future windows
	 * @return the footer in the form of a ColoredJpanel
	 */
	public static ColoredJPanel makeFooter(){
		ColoredJPanel footer = new ColoredJPanel();
		JLabel footerText = new JLabel("Team-Awesome-Maps ver 1.4");
		footer.add(footerText);
		return footer;
	}
}