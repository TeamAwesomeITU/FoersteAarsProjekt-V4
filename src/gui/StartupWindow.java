package gui;

import inputHandler.AdressParser;
import inputHandler.exceptions.MalformedAdressException;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class StartupWindow {
	
	private static final int WINDOW_ID = 1;
	private AdressParser adressParser;
	private JFrame frame;
	private JTextField searchQuery;
	private Container contentPane;
	
	public StartupWindow(){
		createStartupScreen();
	}
	
	
	public static final int getWindowId(){
		return WINDOW_ID;
	}
	
	/**
	 * Builds the frame and sets it up
	 */
	public void createStartupScreen(){
		frame = new JFrame();
		frame.setUndecorated(MainGui.undecoratedBoolean);
		Dimension frameSize = new Dimension(400, 400);
		frame.setBounds(0,0,frameSize.width, frameSize.height);
		frame.setPreferredSize(frameSize);
		frame.setLocationRelativeTo(null);

		MainGui.makeMenu(frame, MainGui.BACKGROUND_COLOR, WINDOW_ID);
		fillContentPane();		

		frame.pack();
		searchQuery.requestFocusInWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * Fills the contentpane, and calls the makeButton method to make the buttons.
	 */
	public void fillContentPane(){
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(MainGui.makeFooter(), BorderLayout.SOUTH);
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
		mapButton.setIcon(new ImageIcon("resources/Logo.png"));
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
	
	//---------------------------------Listeners from here-----------------------------//

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
