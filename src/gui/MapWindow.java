package gui;

import inputHandler.AdressParser;
import inputHandler.exceptions.MalformedAdressException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


import mapDrawer.AreaToDraw;
import mapDrawer.dataSupplying.CoordinateConverter;
import mapDrawer.drawing.MapComponentAdapter;
import mapDrawer.drawing.MapKeyBinding;
import mapDrawer.drawing.MapMouseWheelZoom;
import mapDrawer.drawing.MapPanel;
/**
 * This class holds the window with the map of denmark.
 */
public class MapWindow {

	private JTextField toSearchQuery, fromSearchQuery;
	private ColoredJPanel centerColoredJPanel, westColoredJPanel = makeToolBar(), 
						  eastColoredJPanel = makeCoordinateJPanel(), southColoredJPanel = MainGui.makeFooter();
	private JLabel X_CORD, Y_CORD;

	/**
	 * A constructor for making the window with an empty search query
	 */
	public MapWindow(){
		createMapScreen();
	}

	/**
	 * Makes the frame and fills it.
	 */
	public void createMapScreen(){
		fillContentPane();

		MainGui.frame.pack();
		fromSearchQuery.requestFocusInWindow();
		double widthOfFrame = widthForMap();
		double heightOfFrame = heightForMap();
		createMapOfDenmark(Math.round(widthOfFrame), Math.round(heightOfFrame));
		MainGui.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainGui.frame.pack();
		MapComponentAdapter mcp = new MapComponentAdapter(this);
		MainGui.frame.addComponentListener(mcp);
	}

	/**
	 * Fills the contentpane with the panels
	 */
	public void fillContentPane(){
		MainGui.contentPane.removeAll();
		MainGui.makeMenu();
		MainGui.contentPane.add(westColoredJPanel, BorderLayout.WEST);
		MainGui.contentPane.add(eastColoredJPanel, BorderLayout.EAST);
		MainGui.contentPane.add(MainGui.makeFooter(), BorderLayout.SOUTH);
	}

	/**
	 * Makes the toolbar for the search input
	 * @return the toolbar to be inserted later.
	 */
	public ColoredJPanel makeToolBar(){
		ColoredJPanel toolBar = new ColoredJPanel();
		toolBar.setLayout(new GridLayout(0, 1, 0, 3));

		JLabel fromHeader = new JLabel("From");
		fromHeader.setForeground(ColorTheme.TEXT_COLOR);
		fromSearchQuery = new JTextField();
		fromSearchQuery.addKeyListener(new EnterKeyListener());
		fromSearchQuery.setPreferredSize(new Dimension(240, 20));

		JLabel toHeader = new JLabel("To");
		toHeader.setForeground(ColorTheme.TEXT_COLOR);
		toSearchQuery = new JTextField();
		toSearchQuery.addKeyListener(new EnterKeyListener());

		ColoredJButton findRouteButton = new ColoredJButton("Find Route");
		findRouteButton.addActionListener((new FindRouteActionListener()));
		
		ColoredJPanel buttonPanel = new ColoredJPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(findRouteButton);

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
		toolBar.add(buttonPanel);

		ColoredJPanel flow = new ColoredJPanel();
		flow.add(toolBar);

		return flow;
	}
	/**
	 * makes the coordinates panel to display the coordinates
	 * @return the coordinates panel to be inserted later.
	 */
	public ColoredJPanel makeCoordinateJPanel(){
		ColoredJPanel coordPanel = new ColoredJPanel();
		coordPanel.setLayout(new GridLayout(2, 2, 5, 3));

		JLabel xCordJLabel = new JLabel("X-CORD");
		xCordJLabel.setForeground(ColorTheme.TEXT_COLOR);
		JLabel yCordJLabel = new JLabel("Y-CORD");
		yCordJLabel.setForeground(ColorTheme.TEXT_COLOR);

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

	/**
	 * Makes the map of denmark. It makes an instance of MapPanel
	 * @param width the width of the map
	 * @param height the height of the map
	 */
	private void createMapOfDenmark(double width, double height) {
		centerColoredJPanel = new ColoredJPanel();
		centerColoredJPanel.setLayout(new BoxLayout(centerColoredJPanel, BoxLayout.PAGE_AXIS));

		MapPanel mapPanel = new MapPanel((int)Math.round(width), (int)Math.round(height));
		mapPanel.setMinimumSize(new Dimension((int)width, (int)height));
		mapPanel.setMaximumSize(new Dimension((int)width, (int)height));
		mapPanel.addMouseMotionListener(new CoordinatesMouseMotionListener(mapPanel));
		mapPanel.addMouseWheelListener(new MapMouseWheelZoom(mapPanel));
		MapKeyBinding.addKeyBinding(mapPanel, toSearchQuery, fromSearchQuery);

		//centerColoredJPanel.add(mapPanel);
		MainGui.contentPane.add(mapPanel, BorderLayout.CENTER);
	}
	/**
	 * calculates the height for the map
	 * @return the calculated height
	 */
	private double heightForMap() {
		double height = MainGui.frame.getHeight()*0.99 - (southColoredJPanel.getHeight()+MainGui.frame.getJMenuBar().getHeight());
		if(height <= Math.round(MainGui.frame.getHeight()*0.99 - (southColoredJPanel.getHeight()+MainGui.frame.getJMenuBar().getHeight())))
			return  height;
		else 
			return heightForMap(height);
	}

	private double heightForMap(double temporaryHeight) {
		double height = temporaryHeight*0.99 - (southColoredJPanel.getHeight()+MainGui.frame.getJMenuBar().getHeight());
		if(height <= Math.round(MainGui.frame.getHeight()*0.99 - (southColoredJPanel.getHeight()+MainGui.frame.getJMenuBar().getHeight())))
			return  height;
		else 
			return heightForMap(height);
	}
	/**
	 * @return the height of the map
	 */
	public double getHeightForMap(){
		return heightForMap();
	}
	/**
	 * Calculates the width for the map by making sure the relation between width and height of the map is the same as that of
	 * the "real" map of Denmark.
	 * @return the calculated width
	 */
	private double widthForMap() {
		AreaToDraw areaToDraw = new AreaToDraw();
		double width = heightForMap()*areaToDraw.getWidthHeightRelation();
		if(width <= Math.round(MainGui.frame.getWidth()*0.99 - (eastColoredJPanel.getWidth() + westColoredJPanel.getWidth())))
			return  width;
		else 
			return widthForMap(heightForMap()*0.99);
	}
	/**
	 * is the recursive method called by the widthForMap without input parameters. If the new width is too big,
	 * the method is called where the height is 10% smaller.
	 * @param height
	 * @return
	 */
	private double widthForMap(double height) {
		AreaToDraw areaToDraw = new AreaToDraw();
		double width = Math.round(height*areaToDraw.getWidthHeightRelation());
		if(width <= Math.round(MainGui.frame.getWidth()*0.99 - (eastColoredJPanel.getWidth() + westColoredJPanel.getWidth())))
			return width;
		else 
			return widthForMap(height*0.99);
	}
	/**
	 * @return the width for the map
	 */
	public double getWidthForMap() {
		return widthForMap();
	}

	/**
	 * NOT DONE
	 */
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

				JOptionPane.showMessageDialog(MainGui.frame, "From: " + fromArray[0] 
												+ "\nTo: " + toArray[0]);
			} catch (MalformedAdressException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			JOptionPane.showMessageDialog(MainGui.frame, "You have to enter an address");
		}
	}
	/**
	 * @return the center panel witch hold the map
	 */
	public ColoredJPanel getCenterColoredJPanel() {
		return centerColoredJPanel;
	}
	/**
	 * @return the frame
	 */
	public JFrame getJFrame() {
		return MainGui.frame;
	}

	//---------------------------------Listeners from here-----------------------------//

	/**
	 * The listener for the coordinates
	 */
	class CoordinatesMouseMotionListener extends MouseAdapter{

		private MapPanel mapPanel;
		private AreaToDraw mapAreaToDraw;
		private CoordinateConverter coordConverter;

		public CoordinatesMouseMotionListener(MapPanel mapPanel){
			this.mapPanel = mapPanel;
		}

		/**
		 * gets the mouse moved coodinates and converts them
		 * @Override the mouseMoved method
		 */
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
	/**
	 * Flips the from and to address
	 */
	class ReverseActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String tempFrom = fromSearchQuery.getText();
			fromSearchQuery.setText(toSearchQuery.getText());
			toSearchQuery.setText(tempFrom);			
		}
	}
	/**
	 * If the user is in the search text field, then by pressing enter
	 * is the same as clicking the find route button
	 */
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
	/**
	 * This listeren isn't done yet.
	 */
	class FindRouteActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			findRoute();			
		}

	}



}