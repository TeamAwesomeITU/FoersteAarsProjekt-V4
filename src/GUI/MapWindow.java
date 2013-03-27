package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import InputHandler.AdressParser;
import InputHandler.exceptions.MalformedAdressException;

import mapDrawer.AreaToDraw;
import mapDrawer.CoordinateConverter;
import mapDrawer.MapComponentAdapter;
import mapDrawer.MapPanel;

public class MapWindow {
	
	private JFrame frame;
	private Container contentPane;
	private static MapWindow instance;
	private JTextField toSearchQuery, fromSearchQuery;
	private ColoredJPanel centerColoredJPanel, westColoredJPanel = makeToolBar(), 
						  eastColoredJPanel = makeCoordinateJPanel(), southColoredJPanel = MainGui.makeFooter();
	
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
		double widthOfFrame = widthForMap();
		double heightOfFrame = heightForMap();
		frame.setVisible(false);
		createMapOfDenmark(Math.round(widthOfFrame), Math.round(heightOfFrame));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		MapComponentAdapter mcp = new MapComponentAdapter(this);
		frame.addComponentListener(mcp);
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
	
	public ColoredJPanel makeCoordinateJPanel(){
		ColoredJPanel coordPanel = new ColoredJPanel();
		coordPanel.setLayout(new GridLayout(2, 1));
		JTextField xCordTextField = new JTextField();
		JTextField yCordTextField = new JTextField();
		 
		return coordPanel;
	}
	
	private void createMapOfDenmark(double width, double height) {
		centerColoredJPanel = new ColoredJPanel();
		centerColoredJPanel.setLayout(new BoxLayout(centerColoredJPanel, BoxLayout.PAGE_AXIS));
		
		MapPanel mapPanel = new MapPanel(frame, (int)Math.round(width*0.98), (int)Math.round(height*0.98));
		mapPanel.setMinimumSize(new Dimension((int)width, (int)height));
		mapPanel.setMaximumSize(new Dimension((int)width, (int)height));
		mapPanel.addMouseMotionListener(new CoordinatesMouseMotionListener(mapPanel));
		
		centerColoredJPanel.add(mapPanel);		
		contentPane.add(centerColoredJPanel, BorderLayout.CENTER);
	}
	
	private double heightForMap() {
		return frame.getHeight()*0.9 - (southColoredJPanel.getHeight()+frame.getJMenuBar().getHeight());
	}
	
	public double getHeightForMap(){
		return heightForMap();
	}
	
	private double widthForMap() {
		AreaToDraw areaToDraw = new AreaToDraw();
		return  heightForMap()*areaToDraw.getWidthHeightRelation();//(frame.getWidth()*0.95 - (eastColoredJPanel.getWidth() + westColoredJPanel.getWidth()));
	}
	
	public double getWidthForMap() {
		return widthForMap();
	}
	
	public void fillContentPane(){
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		contentPane.add(southColoredJPanel, BorderLayout.SOUTH);
		contentPane.add(westColoredJPanel, BorderLayout.WEST);
		contentPane.add(eastColoredJPanel, BorderLayout.EAST);
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
	
	public ColoredJPanel getCenterColoredJPanel() {
		return centerColoredJPanel;
	}
	
	public JFrame getJFrame() {
		return frame;
	}
	
	//---------------------------------Listeners from here-----------------------------//
	
	class CoordinatesMouseMotionListener extends MouseAdapter{
		
		private MapPanel mapPanel;
		private AreaToDraw mapAreaToDraw;
		private CoordinateConverter coordConverter;
		
		public CoordinatesMouseMotionListener(MapPanel mapPanel){
			this.mapPanel = mapPanel;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mapAreaToDraw = mapPanel.getArea();
			coordConverter = new CoordinateConverter((int)Math.round(widthForMap()*0.98), (int)Math.round(heightForMap()*0.98), mapAreaToDraw);
			double xCord = coordConverter.DrawToKrakCoordX(e.getX());
			double yCord = coordConverter.DrawToKrakCoordY(e.getY());
			
			mapPanel.setToolTipText("x-cord: " + xCord + " y-cord: " + yCord);
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


