package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.naming.directory.SearchControls;
import javax.swing.*;

import GUI.MainGui.EnterKeyListener;
import InputHandler.AdressParser;
import InputHandler.exceptions.MalformedAdressException;

import mapDrawer.MapPanel;

public class MapWindow {
	
	private JFrame frame;
	private Container contentPane;
	private static MapWindow instance;
	private JTextField toSearchQuery, fromSearchQuery;
	
	public static void main(String[] args) {
		MapWindow.getInstance();	
	}
	
	private MapWindow(){
		createMapScreen();
	}
	
	public MapWindow(String searchQuery){
		createMapScreen();
		toSearchQuery.setText(searchQuery);
	}
	
	public static MapWindow getInstance(){
		if(instance != null)
			return instance;
		else{
			instance = new MapWindow();
			return instance;
		}
	}
	
	public void createMapScreen(){
		frame = new JFrame("Team Awesome Maps");
		frame.setUndecorated(MainGui.undecoratedBoolean);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0,0,screenSize.width, screenSize.height);
		frame.setPreferredSize(screenSize);
		
		MainGui.makeMenu(frame, MainGui.BACKGROUND_COLOR, MainGui.undecoratedBoolean, 2);
		fillContentPane();
		
		frame.pack();
		fromSearchQuery.requestFocusInWindow();
		frame.setVisible(true);
	}
	
	public ColoredJPanel makeToolBar(){
		ColoredJPanel toolBar = new ColoredJPanel();
		toolBar.setLayout(new GridLayout(0, 1, 0, 3));
		toolBar.setBackground(MainGui.BACKGROUND_COLOR);
		
		JLabel fromHeader = new JLabel("From");
		fromHeader.setForeground(Color.BLUE);
		fromSearchQuery = new JTextField();
		fromSearchQuery.addKeyListener(new EnterKeyListener());
		
		JLabel toHeader = new JLabel("To");
		toHeader.setForeground(Color.BLUE);
		toSearchQuery = new JTextField();
		toSearchQuery.addKeyListener(new EnterKeyListener());
		
		ColoredJButton findRouteButton = new ColoredJButton("Find Route");
		findRouteButton.addActionListener((new FindRouteActionListener()));
		
		ColoredJButton reverseButton = new ColoredJButton(); 
		reverseButton.setIcon(new ImageIcon("resources/reverse.png"));
		reverseButton.setBorder(BorderFactory.createEmptyBorder());
		reverseButton.setContentAreaFilled(false);
		reverseButton.setToolTipText("Click to reverse from and to");
		reverseButton.addActionListener(new ReverseActionListener());
		
		toolBar.add(reverseButton);
		toolBar.add(fromHeader);
		toolBar.add(fromSearchQuery);
		toolBar.add(toHeader);
		toolBar.add(toSearchQuery);
		toolBar.add(findRouteButton);
		
		ColoredJPanel flow = new ColoredJPanel();
		flow.setBackground(MainGui.BACKGROUND_COLOR);
		flow.add(toolBar);
		
		return flow;
	}
	
	public void fillContentPane(){
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(MainGui.makeFooter(), BorderLayout.SOUTH);
		contentPane.add(makeToolBar(), BorderLayout.WEST);
		contentPane.add(new MapPanel(frame), BorderLayout.CENTER);
		contentPane.add(new ColoredJPanel(), BorderLayout.EAST);
	}
	
	public void findRoute(){
		if(fromSearchQuery.getText().trim().length() != 0 && 
				toSearchQuery.getText().trim().length() != 0){
			AdressParser adressParser = new AdressParser();
			try {
				adressParser.parseAdress(fromSearchQuery.getText());
				String[] fromArray = adressParser.getAdressArray();
				adressParser.parseAdress(toSearchQuery.getText());
				String[] toArray = adressParser.getAdressArray();
				
				JOptionPane.showMessageDialog(frame, "From: " + fromArray[0] 
												+ "\nTo: " + toArray[0]);
			} catch (MalformedAdressException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			JOptionPane.showMessageDialog(frame, "You have to enter an address");
		}
	}
	
	class ReverseActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String tempFrom = fromSearchQuery.getText();
			fromSearchQuery.setText(toSearchQuery.getText());
			toSearchQuery.setText(tempFrom);			
		}
	}
	
	class EnterKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == 10){
				findRoute();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}
	
	class FindRouteActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			findRoute();			
		}
		
	}
	
	

}


