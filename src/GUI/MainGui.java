package GUI;

import java.awt.*;

import java.awt.event.*;

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

	public static final Color BACKGROUND_COLOR = new Color(149, 255, 149);

	public static final Color VERY_LIGHT_COLOR = new Color(200, 255, 200);

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
	}

	/**
	 * Builds the frame and sets it up
	 */
	public void startupScreen(){
		frame = new JFrame("Team Awesome Maps");
		frame.setUndecorated(true);
		Dimension frameSize = new Dimension(400, 400);
		frame.setBounds(0,0,frameSize.width, frameSize.height);
		frame.setPreferredSize(frameSize);
		frame.setLocationRelativeTo(null);

		makeMenu();
		fillContentPane();		

		frame.pack();
		searchQuery.requestFocusInWindow();
		frame.setVisible(true);
	}
	/**
	 * Makes the menu and adds shortcuts to it.
	 */
	public void makeMenu(){
		final int SHORT_CUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.setBackground(BACKGROUND_COLOR);
		menuBar.setBorder(BorderFactory.createEtchedBorder());

		JMenu fileMenu = new JMenu("File");
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(new QuitActionListener());
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORT_CUT_MASK));
		fileMenu.add(quitItem);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new AboutActionListener());
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

		ColoredJPanel footer = new ColoredJPanel();
		JLabel footerText = new JLabel("Team-Awesome-Maps ver 1.0");
		footer.add(footerText);

		contentPane.add(footer, BorderLayout.SOUTH);
		contentPane.add(makeButtons(), BorderLayout.CENTER);
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
	
	public void searchForAnAddress(){
		if(searchQuery.getText().trim().length() != 0){
			adressParser = new AdressParser();
			try {
				adressParser.parseAdress(searchQuery.getText());
			} catch (MalformedAdressException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			JOptionPane.showMessageDialog(frame, "You have to enter an address");
		}
	}

	class EnterKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == 10){
				searchForAnAddress();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	class FindAddressActionListener implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent e) {
			searchForAnAddress();
		}
	}
	
	class QuitActionListener implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();			
		}		
	}
	
	class AboutActionListener implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(frame, "Welcome to T-A-M maps. Please enter an address" +
					" to get on your way, or simply click the globe to browse the map");		
		}
	}
	
	class MapActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(frame, "it worked");		
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
