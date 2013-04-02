package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
	
	private static final int WINDOW_ID = 2;
	private JFrame frame;
	private Container contentPane;
	private JTextField toSearchQuery, fromSearchQuery;
	private ColoredJPanel centerColoredJPanel, westColoredJPanel = makeToolBar(), 
						  eastColoredJPanel = makeCoordinateJPanel(), southColoredJPanel = MainGui.makeFooter();
	private JLabel X_CORD, Y_CORD;
	
	public static void main(String[] args) {
		new MapWindow();
	}
	
	public MapWindow(){
		createMapScreen();
	}
	
	public MapWindow(String searchQuery){
		createMapScreen();
		toSearchQuery.setText(searchQuery);
	}
	
	public static final int getWindowId(){
		return WINDOW_ID;
	}
	
	public void createMapScreen(){
		frame = new JFrame("Team Awesome Maps");
		frame.setUndecorated(MainGui.undecoratedBoolean);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0,0,screenSize.width, screenSize.height);
		frame.setPreferredSize(screenSize);
		
		MainGui.makeMenu(frame, MainGui.BACKGROUND_COLOR, WINDOW_ID);
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
		flow.add(toolBar);
		
		return flow;
	}
	
	public ColoredJPanel makeCoordinateJPanel(){
		ColoredJPanel coordPanel = new ColoredJPanel();
		coordPanel.setLayout(new GridLayout(2, 2, 5, 3));
		
		JLabel xCordJLabel = new JLabel("X-CORD");
		xCordJLabel.setForeground(Color.BLUE);
		JLabel yCordJLabel = new JLabel("Y-CORD");
		yCordJLabel.setForeground(Color.BLUE);
		
		X_CORD = new JLabel();
		Y_CORD = new JLabel();
		
		coordPanel.add(xCordJLabel);
		coordPanel.add(yCordJLabel);
		coordPanel.add(X_CORD);
		coordPanel.add(Y_CORD);

		
		ColoredJPanel flow = new ColoredJPanel();
		flow.add(coordPanel);
		
		return flow;
	}
	
	private void createMapOfDenmark(double width, double height) {
		centerColoredJPanel = new ColoredJPanel();
		centerColoredJPanel.setLayout(new BoxLayout(centerColoredJPanel, BoxLayout.PAGE_AXIS));
		
		MapPanel mapPanel = new MapPanel(frame, centerColoredJPanel, (int)Math.round(width*0.98), (int)Math.round(height*0.98));
		mapPanel.setLayout(new BoxLayout(mapPanel, BoxLayout.PAGE_AXIS));
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
		double width = heightForMap()*areaToDraw.getWidthHeightRelation();
		if(width <= Math.round(frame.getWidth()*0.9 - (eastColoredJPanel.getWidth() + westColoredJPanel.getWidth())))
			return  width;
		else 
			return widthForMap(heightForMap()*0.9);
	}
	
	private double widthForMap(double height) {
		AreaToDraw areaToDraw = new AreaToDraw();
		double width = Math.round(height*areaToDraw.getWidthHeightRelation());
		if(width <Math.round(frame.getWidth()*0.9 - (eastColoredJPanel.getWidth() + westColoredJPanel.getWidth())))
			return width;
		else 
			return widthForMap(height*0.9);
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
				String[] fromArray = new String[6];
				for(int i = 0; adressParser.getAdressArray().length > i; i++){
					fromArray[i] = adressParser.getAdressArray()[i];
				}
				
				adressParser.parseAdress(toSearchQuery.getText());

				String[] toArray = adressParser.getAdressArray();
				for(int i = 0; adressParser.getAdressArray().length > i; i++){
					toArray[i] = adressParser.getAdressArray()[i];
				}
				
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
			if (MainGui.coordinatesBoolean) {
				mapAreaToDraw = mapPanel.getArea();
				coordConverter = new CoordinateConverter((int)Math.round(widthForMap()*0.98), (int)Math.round(heightForMap()*0.98), mapAreaToDraw);
				double xCord = coordConverter.pixelToUTMCoordX(e.getX());
				double yCord = coordConverter.pixelToUTMCoordY(e.getY());

				String xString = String.format("%.2f", xCord);
				String yString = String.format("%.2f", yCord);

				X_CORD.setText(xString);
				Y_CORD.setText(yString);
			} else {
				X_CORD.setText("");
				Y_CORD.setText("");
			}
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


