package gui;

import gui.customJUnits.*;
import gui.settingsAndPopUp.*;
import inputHandler.AdressParser;
import inputHandler.exceptions.MalformedAdressException;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import mapCreationAndFunctions.AreaToDraw;
import mapCreationAndFunctions.MapMouseWheelZoom;
import mapCreationAndFunctions.MapPanel;
import mapCreationAndFunctions.MapPanelResize;
import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.CoordinateConverter;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.data.search.CitySearch;
import mapCreationAndFunctions.data.search.EdgeSearch;
/**
 * This class holds the window with the map of denmark.
 */
public class MapWindow {

	public static JTextField toSearchQuery, fromSearchQuery;
	private ColoredJPanel centerColoredJPanel, westColoredJPanel = makeToolBar(), 
						  eastColoredJPanel = makeEastJPanel(), southColoredJPanel = MainGui.makeFooter();
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
		MapPanelResize mcp = new MapPanelResize(this);
		MainGui.frame.addComponentListener(mcp);
	}

	/**
	 * Fills the contentpane with the panels
	 */
	public void fillContentPane(){
		MainGui.contentPane.removeAll();
		MainGui.contentPane.revalidate();
		MainGui.contentPane.repaint();
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
		fromSearchQuery = new CustomJTextField();
		fromSearchQuery.addKeyListener(new EnterKeyListener());
		fromSearchQuery.setPreferredSize(new Dimension(200, 20));

		JLabel toHeader = new JLabel("To");
		toHeader.setForeground(ColorTheme.TEXT_COLOR);
		toSearchQuery = new CustomJTextField();
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
		
		ColoredJComboBox vehicleBox = new ColoredJComboBox();
		vehicleBox.setPreferredSize(new Dimension(120, 30));
		vehicleBox.setEditable(true);
        String[][] vehicleList = {{"Bike", "resources/bicycle.png"},
        						 {"Car", "resources/car.png"}};
        vehicleBox.addItems(vehicleList);
        vehicleBox.setUI(ColoredArrowUI.createUI(vehicleBox));
        
        ColoredJComboBox routeBox = new ColoredJComboBox();
        routeBox.setPreferredSize(new Dimension(120, 30));
        routeBox.setEditable(true);
        String[][] routeList = {{"Fastest", ""},
        						 {"Shortest", ""}};
        routeBox.addItems(routeList);
        routeBox.setUI(ColoredArrowUI.createUI(routeBox));
        
        String[] patterns = calculateResults("Nørregade");
		JList<String> testBox = new JList<String>(patterns);
		testBox.setPreferredSize(new Dimension(120, 30));
		testBox.setModel(new DefaultListModel<String>());
		
		//testBox.setEditable(false);
		testBox.addKeyListener(new SearchKeyListener());
		//Configurator.enableAutoCompletion(testBox);
		
		//JList list = new JList<>(patterns);
		//Configurator.enableAutoCompletion(list, fromSearchQuery);
		
        //fromSearchQuery.addKeyListener(new SearchKeyListener());

		toolBar.add(reverseButton);
		toolBar.add(fromHeader);
		toolBar.add(testBox);
		//toolBar.add(fromSearchQuery);
		toolBar.add(toHeader);
		toolBar.add(toSearchQuery);
		toolBar.add(buttonPanel);
		toolBar.add(vehicleBox);
		toolBar.add(routeBox);

		ColoredJPanel flow = new ColoredJPanel();
		flow.add(toolBar);

		return flow;
	}
	/**
	 * makes the coordinates panel to display the coordinates
	 * @return the coordinates panel to be inserted later.
	 */
	public ColoredJPanel makeEastJPanel(){
		ColoredJPanel JPanel = new ColoredJPanel();
		JPanel.setLayout(new GridLayout(2, 2, 5, 3));

		ColoredJPanel flow = new ColoredJPanel();
		flow.add(JPanel);

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
		mapPanel.addMouseListener(new CoordinatesMouseMotionListener(mapPanel));
		mapPanel.addMouseWheelListener(new MapMouseWheelZoom(mapPanel));

		centerColoredJPanel.add(mapPanel);
		MainGui.contentPane.add(centerColoredJPanel, BorderLayout.CENTER);
	}
	/**
	 * calculates the height for the map
	 * @return the calculated height
	 */
	private double heightForMap() {
		double height = MainGui.frame.getHeight()*0.97 - (southColoredJPanel.getHeight()+MainGui.frame.getJMenuBar().getHeight());
		if(height < Math.round(MainGui.frame.getHeight()*0.968 - (southColoredJPanel.getHeight()+MainGui.frame.getJMenuBar().getHeight())))
			return  height;
		else 
			return heightForMap(height);
	}

	private double heightForMap(double temporaryHeight) {
		double height = temporaryHeight*0.97 - (southColoredJPanel.getHeight()+MainGui.frame.getJMenuBar().getHeight());
		if(height < Math.round(MainGui.frame.getHeight()*0.968 - (southColoredJPanel.getHeight()+MainGui.frame.getJMenuBar().getHeight())))
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
		if(width < Math.round(MainGui.frame.getWidth()*0.98 - (eastColoredJPanel.getWidth() + westColoredJPanel.getWidth())))
			return  width;
		else 
			return widthForMap(heightForMap()*0.98);
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
		if(width < Math.round(MainGui.frame.getWidth()*0.978 - (eastColoredJPanel.getWidth() + westColoredJPanel.getWidth())))
			return width;
		else 
			return widthForMap(height*0.98);
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
		if(fromSearchQuery.getText().trim().length() != 0 || 
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
				final JFrame zoidbergFrame = new JFrame("Malformed Address");
				
				Container contentPane = zoidbergFrame.getContentPane();
				contentPane.setLayout(new GridLayout(0, 1, 0, 5));
				
				JPanel zoidbergPanel = new JPanel();
				zoidbergPanel.setLayout(new FlowLayout());
				
				JLabel zoidbergLabel = new JLabel(new ImageIcon("resources/WhyNotZoidberg.png"));
				JButton okayButton = new JButton(new ImageIcon("resources/okay.png"));
				okayButton.setToolTipText("Click me!");
				okayButton.addKeyListener(new KeyListener() {
					public void keyPressed(KeyEvent arg0) {
						if(arg0.getKeyCode() == 10){
							zoidbergFrame.dispose();
							toSearchQuery.setText("");
							fromSearchQuery.setText("");							
						}
					}
					public void keyTyped(KeyEvent arg0) {}	public void keyReleased(KeyEvent arg0) {}
				});
				okayButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						zoidbergFrame.dispose();
						toSearchQuery.setText("");
						fromSearchQuery.setText("");
					}
				});
				okayButton.setBorder(BorderFactory.createEmptyBorder());
				
				zoidbergPanel.add(zoidbergLabel);
				zoidbergPanel.add(okayButton);

				contentPane.add(zoidbergPanel);
				
				zoidbergFrame.pack();
				zoidbergFrame.setLocationRelativeTo(null);
				zoidbergFrame.setVisible(true);
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
	
	private String[] calculateResults(String stringToSearch)
	{
		Edge[] foundEdges = EdgeSearch.getRoadNameSuggestions(stringToSearch);
		String[] edgesAsStrings = new String[foundEdges.length];
		for(int i = 0; i < foundEdges.length; i++)
			edgesAsStrings[i] = foundEdges[i].getRoadName() + " - " + foundEdges[i].getPostalNumberLeft();		
		return edgesAsStrings;
	}
	

	//---------------------------------Listeners from here-----------------------------//

	class SearchKeyListener implements KeyListener{
    	String query = "";
    	
        public void keyReleased(KeyEvent e) {

          }

          public void keyTyped(KeyEvent e) {
          }

          public void keyPressed(KeyEvent e) {
        	  System.out.println(e.getKeyCode());
        	  JComboBox cb = (JComboBox)e.getSource();
        	  String search = (String) cb.getSelectedItem();
        	  if(search != null)
        		  query = search.trim();
        	  if(query.length() >= 2){
        		  City[] citiesList = CitySearch.getCityNameSuggestions(query);
        		  for(City city : citiesList)
        			  System.out.println(city.getCityName());
        	  }
          }

	}
	
	/**
	 * Not yet implemented!
	 */
	class RouteTypeActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * Not yet implemented!
	 */
	class VehicleTypeActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	
	
	/**
	 * The listener for the coordinates
	 */
	class CoordinatesMouseMotionListener extends MouseAdapter {

		private MapPanel mapPanel;
		private AreaToDraw mapAreaToDraw;
		private CoordinateConverter coordConverter;

		public CoordinatesMouseMotionListener(MapPanel mapPanel){
			this.mapPanel = mapPanel;
			makeCoordinatesConverter();
		}
		
		public double getXCoord(MouseEvent e){
			makeCoordinatesConverter();
			double xCord = coordConverter.pixelToUTMCoordX(e.getX());				
			return xCord;
		}
		
		public double getYCoord(MouseEvent e){
			makeCoordinatesConverter();
			double yCord = coordConverter.pixelToUTMCoordY(e.getY());			
			return yCord;
		}
		
		public void makeCoordinatesConverter(){
			mapAreaToDraw = mapPanel.getArea();
			coordConverter = new CoordinateConverter((int)Math.round(getWidthForMap()), (int)Math.round(getHeightForMap()), mapAreaToDraw);
		}

		/**
		 * gets the mouse moved coodinates and converts them
		 * @Override the mouseMoved method
		 */
		public void mouseMoved(MouseEvent e) {
			Edge edge = mapPanel.getHitEdge(getXCoord(e), getYCoord(e));
			String yString = String.format("%.2f", getYCoord(e));
			String xString = String.format("%.2f", getXCoord(e));
			
			String roadName = "";
			if(edge != null)
				roadName = edge.getRoadName() + ", " + edge.getPostalNumberLeft() + " " + edge.getPostalNumberLeftCityName() + " edgeID: "+ edge.getiD();
			if (MainGui.coordinatesBoolean) 				
				mapPanel.setToolTipText("X: " +  xString +" Y: " + yString + ", " + "Roadname: " + roadName);
			else 
				mapPanel.setToolTipText(roadName);
		}
		
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3){
				if(MainGui.coordinatesBoolean){
					String yString = String.format("%.2f", getYCoord(e));
					String xString = String.format("%.2f", getXCoord(e));

					MainGui.coordinatesString = "X: " + xString + " Y: " + yString;
				}
				Edge edge = mapPanel.getHitEdge(getXCoord(e), getYCoord(e));
				if(edge != null)
					MainGui.locationString = edge.getRoadName() + " " + edge.getPostalNumberLeft() + " " + edge.getPostalNumberLeftCityName();
			}
			
		}
		
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
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