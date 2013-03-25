package GUI;

import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;

import InputHandler.AdressParser;
import InputHandler.exceptions.MalformedAdressException;

public class MainGui {

	private AdressParser adressParser;
	private JFrame frame;
	private JTextField searchQuery;
	private Container contentPane;
	private static MainGui instance;

	//Three static final fields to easily change the color of our program
	public static final Color DARK_COLOR = new Color(8, 108, 8);

	public static final Color BACKGROUND_COLOR = new Color(140, 255, 140);

	public static final Color VERY_LIGHT_COLOR = new Color(200, 255, 200);
	
	public static boolean undecoratedBoolean;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainGui.getInstance();
	}

	/**
	 * Uses a singleton API to get the instance of the program
	 * @return
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
		startupScreen();
		readSettingsFile();
	}
	
	public static void readSettingsFile(){
		File file = new File("resources/SettingsFile.txt");
		try {
			Scanner settingsFileScanner = new Scanner(file);
			while(settingsFileScanner.hasNext()){
				if(settingsFileScanner.next().equals("undecorated")){
					undecoratedBoolean = settingsFileScanner.nextBoolean();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Builds the frame and sets it up
	 */
	public void startupScreen(){
		frame = new JFrame();
		frame.setUndecorated(undecoratedBoolean);
		Dimension frameSize = new Dimension(400, 400);
		frame.setBounds(0,0,frameSize.width, frameSize.height);
		frame.setPreferredSize(frameSize);
		frame.setLocationRelativeTo(null);

		makeMenu(frame, BACKGROUND_COLOR, undecoratedBoolean, 1);
		fillContentPane();		

		frame.pack();
		searchQuery.requestFocusInWindow();
		frame.setVisible(true);
	}
	/**
	 * Makes the menu and adds shortcuts to it.
	 */
	public static void makeMenu(final JFrame frameForMenu, final Color colorForMenu, final boolean undecorated, final int windowID){
		final int SHORT_CUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		JMenuBar menuBar = new JMenuBar();
		frameForMenu.setJMenuBar(menuBar);
		menuBar.setBackground(colorForMenu);
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
							if(windowID == 1)
								instance = new MainGui();
							if(windowID == 2)
								new MapWindow("");
							settingsFrame.dispose();
						}
						
						if(e.getStateChange() == ItemEvent.SELECTED){
							undecoratedBoolean = true;
							frameForMenu.dispose();
							frameForMenu.setUndecorated(undecoratedBoolean);							
							if(windowID == 1)
								instance = new MainGui();
							if(windowID == 2)
								new MapWindow("");
							settingsFrame.dispose();
						}
				}});
				
				container.add(settingsPanel, BorderLayout.CENTER);
				
				settingsPanel.add(undecorated);
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
	 * Fills the contentpane, and calls the makeButton method to make the buttons.
	 */
	public void fillContentPane(){
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(makeFooter(), BorderLayout.SOUTH);
		contentPane.add(makeButtons(), BorderLayout.CENTER);
	}
	
	public static ColoredJPanel makeFooter(){
		ColoredJPanel footer = new ColoredJPanel();
		JLabel footerText = new JLabel("Team-Awesome-Maps ver 1.4");
		footer.add(footerText);
		return footer;
	}

	/**
	 * makes the buttons
	 * @return a ColoredJPanel to be inserted into the contentpane 
	 */
	public ColoredJPanel makeButtons(){
		ColoredJPanel buttonPanel = new ColoredJPanel();
		buttonPanel.setLayout(new GridLayout(0, 1, 3, 1));

		JButton mapButton = new JButton();
		mapButton.setIcon(new ImageIcon("resources/globe.png"));
		mapButton.setBorder(BorderFactory.createEmptyBorder());
		mapButton.setContentAreaFilled(false);
		mapButton.setToolTipText("Press the globe to browse the map");
		mapButton.addActionListener(new MapActionListener());

		searchQuery = new JTextField();
		searchQuery.setPreferredSize(new Dimension(320, 20));
		searchQuery.addKeyListener(new EnterKeyListener());

		ColoredJButton findAddressButton = new ColoredJButton("Find Address");
		findAddressButton.addActionListener(new FindAddressActionListener());

		ColoredJButton clearButton = new ColoredJButton("Clear");
		clearButton.addActionListener(new ClearActionListener());

		ColoredJPanel flow = new ColoredJPanel();
		flow.add(mapButton);

		ColoredJPanel searchPanel = new ColoredJPanel();

		searchPanel.add(searchQuery);
		searchPanel.add(findAddressButton);
		searchPanel.add(clearButton);

		buttonPanel.add(flow);
		buttonPanel.add(searchPanel);

		return buttonPanel;
	}
	
	private void openMap() {
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));	
		new MapWindow(searchQuery.getText());
		frame.setCursor(Cursor.getDefaultCursor());
		frame.dispose();
	}
	
	public void searchForAnAddress(){
		if(searchQuery.getText().trim().length() != 0){
			adressParser = new AdressParser();
			try {
				adressParser.parseAdress(searchQuery.getText());
			} catch (MalformedAdressException e1) {
				e1.printStackTrace();
			}
			openMap();
		}
		else {
			JOptionPane.showMessageDialog(frame, "You have to enter an address");
		}
	}

	class EnterKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == 10 && searchQuery.hasFocus()){
				searchForAnAddress();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}

	class FindAddressActionListener implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent e) {
			searchForAnAddress();
		}
	}
	
	
	class MapActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			openMap();
		}
	}
	
	class ClearActionListener implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent e) {
			searchQuery.setText("");
			searchQuery.requestFocusInWindow();
		}	
	}
}
