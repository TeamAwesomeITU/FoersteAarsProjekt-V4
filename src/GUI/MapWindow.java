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
	private JTextField toSearchQuery;
	
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
		frame.setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0,0,screenSize.width, screenSize.height);
		frame.setPreferredSize(screenSize);
		
		MainGui.makeMenu(frame, MainGui.BACKGROUND_COLOR);
		fillContentPane();
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public ColoredJPanel makeToolBar(){
		ColoredJPanel toolBar = new ColoredJPanel();
		toolBar.setLayout(new GridLayout(0, 1, 0, 3));
		toolBar.setBackground(MainGui.BACKGROUND_COLOR);
		
		JLabel fromHeader = new JLabel("From");
		fromHeader.setForeground(Color.BLUE);
		JTextField fromSearchQuery = new JTextField();
		fromSearchQuery.addKeyListener(new EnterKeyListener());
		
		JLabel toHeader = new JLabel("To");
		toHeader.setForeground(Color.BLUE);
		toSearchQuery = new JTextField();
		toSearchQuery.addKeyListener(new EnterKeyListener());
		
		ColoredJButton findRouteButton = new ColoredJButton("Find Route");
		findRouteButton.addActionListener((new FindRouteActionListener(fromSearchQuery, toSearchQuery)));
		 
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
	
	class EnterKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == 10){
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
	
	class FindRouteActionListener implements ActionListener{

		private JTextField from, to;
		
		public FindRouteActionListener(JTextField from, JTextField to){
			this.from = from;
			this.to = to;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(from.getText().trim().length() != 0 && 
					to.getText().trim().length() != 0){
				AdressParser adressParser = new AdressParser();
				try {
					adressParser.parseAdress(from.getText());
					String[] fromArray = adressParser.getAdressArray();
					adressParser.parseAdress(to.getText());
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
		
	}
	
	

}


