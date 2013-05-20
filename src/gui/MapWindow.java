package gui;

import gui.customJComponents.*;
import gui.settingsAndPopUp.*;

import inputHandler.AddressSearch;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;


import navigation.DijkstraSP;
import navigation.exceptions.NoRoutePossibleException;

import mapCreationAndFunctions.AreaToDraw;
import mapCreationAndFunctions.MapMouseWheelZoom;
import mapCreationAndFunctions.MapPanel;
import mapCreationAndFunctions.MapPanelResize;
import mapCreationAndFunctions.data.CoordinateConverter;
import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.exceptions.AreaIsNotWithinDenmarkException;
import mapCreationAndFunctions.exceptions.InvalidAreaProportionsException;
import mapCreationAndFunctions.exceptions.NegativeAreaSizeException;
/**
 * This class holds the window with the map of denmark.
 */
public class MapWindow {

	private Timer showAddressTimer = new Timer(800, new TimerListener());
	public static CustomJTextField toSearchQuery, fromSearchQuery;
	private ColoredJPanel centerColoredJPanel, westColoredJPanel = makeToolBar(), 
			eastColoredJPanel = makeEastJPanel(), southColoredJPanel = MainGui.makeFooter();
	private MapPanel mapPanel;
	private ColoredJButton detailedDirectionsButton;

	private String VehicleType = "Car", RouteType = "Fastest";
	private static AddressSearch addressSearcherFrom = new AddressSearch();
	private static AddressSearch addressSearcherTo = new AddressSearch();
	private static ArrayList<Edge> directionEdges = new ArrayList<>();

	/**
	 * The constructor makes the frame
	 */
	public MapWindow(){
		showAddressTimer.setRepeats(false);
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
		MapPanelResize mcr = new MapPanelResize(this);
		MainGui.frame.addComponentListener(mcr);
		MainGui.changeScreenSize();
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
		toolBar.setLayout(new GridLayout(9, 1, 0, 3));

		TextFieldFocusListener textFieldFocusListener = new TextFieldFocusListener();
		JLabel fromHeader = new JLabel("From");
		fromHeader.setForeground(ColorTheme.TEXT_COLOR);
		fromSearchQuery = new CustomJTextField();
		fromSearchQuery.addKeyListener(new MapKeyListener());
		fromSearchQuery.addFocusListener(textFieldFocusListener);
		fromSearchQuery.setPreferredSize(new Dimension(200, 20));

		JLabel toHeader = new JLabel("To");
		toHeader.setForeground(ColorTheme.TEXT_COLOR);
		toSearchQuery = new CustomJTextField();
		toSearchQuery.addKeyListener(new MapKeyListener());
		toSearchQuery.addFocusListener(textFieldFocusListener);

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
		String[][] vehicleList = {{"Car", "resources/car.png"},
				{"Bike", "resources/bicycle.png"},
				{"Walk", "resources/walk2.png"}};
		vehicleBox.addItems(vehicleList);
		vehicleBox.setUI(ColoredArrowUI.createUI(vehicleBox));
		vehicleBox.addActionListener(new VehicleTypeActionListener());

		ColoredJComboBox routeBox = new ColoredJComboBox();
		routeBox.setPreferredSize(new Dimension(120, 30));
		routeBox.setEditable(true);
		String[][] routeList = {{"Fastest", ""},
				{"Shortest", ""}};
		routeBox.addItems(routeList);
		routeBox.setUI(ColoredArrowUI.createUI(routeBox));
		routeBox.addActionListener(new RouteTypeActionListener());

		detailedDirectionsButton = new ColoredJButton("Detailed Directions");
		detailedDirectionsButton.addActionListener(new GetDirectionsListener());
		setDirectionsEnabled();

		ColoredJPanel directionsPanel = new ColoredJPanel();
		directionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		directionsPanel.add(detailedDirectionsButton);

		toolBar.add(reverseButton);
		toolBar.add(fromHeader);
		toolBar.add(fromSearchQuery);
		toolBar.add(toHeader);
		toolBar.add(toSearchQuery);
		toolBar.add(buttonPanel);
		toolBar.add(vehicleBox);
		toolBar.add(routeBox);
		toolBar.add(directionsPanel);

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

		mapPanel = new MapPanel((int)Math.round(width), (int)Math.round(height));
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
	 * @throws MalformedAddressException 
	 * @throws NoAddressFoundException 
	 * @throws NoRoutePossibleException 
	 * @throws InvalidAreaProportionsException 
	 * @throws AreaIsNotWithinDenmarkException 
	 * @throws NegativeAreaSizeException 
	 */
	public void findRoute() throws NoAddressFoundException, NoRoutePossibleException, NegativeAreaSizeException, AreaIsNotWithinDenmarkException, InvalidAreaProportionsException{
		DijkstraSP dip = new DijkstraSP(DataHolding.getGraph(), addressSearcherFrom.getEdgeToNavigate(), DataHolding.getEdgeArray(), VehicleType, RouteType);
		setDirections((Stack<Edge>) dip.pathTo(addressSearcherTo.getEdgeToNavigate()));
		mapPanel.setPathTo((Stack<Edge>) dip.pathTo(addressSearcherTo.getEdgeToNavigate()));
		mapPanel.repaintMap();

		getDirections();
		setDirectionsEnabled();
	}

	public void setDirectionsEnabled(){
		if(directionEdges.size() == 0)
			detailedDirectionsButton.setEnabled(false);
		else 
			detailedDirectionsButton.setEnabled(true);
	}

	private void setDirections(Stack<Edge> edges)
	{
		directionEdges.clear();
		while(!edges.isEmpty())
			directionEdges.add(edges.pop());
	}

	public String[] getDirections() throws NoAddressFoundException
	{
		if(directionEdges.isEmpty())
			return new String[0];
		else {
			ArrayList<String> directions = new ArrayList<>();

			directions.add("Going from: " + addressSearcherFrom.getEdgeToNavigate().getRoadName() +  ", " + addressSearcherFrom.getEdgeToNavigate().getPostalNumberLeftCityName());
			directions.add("Going to: " + addressSearcherTo.getEdgeToNavigate().getRoadName() + ", " + addressSearcherTo.getEdgeToNavigate().getPostalNumberLeftCityName());
			directions.add("");

			double currentLength = 0;
			double totalTravelLength = 0;
			double totalTravelTime = 0;
			String currentRoadName = "";

			for(int i = 0; i < directionEdges.size(); i++)
			{
				Edge edge = directionEdges.get(i);
				totalTravelTime += edge.getDriveTime();
				currentLength += edge.getLength();	
				totalTravelLength += edge.getLength();
				//If the road does not contain a roadname, it is "a pathway"
				currentRoadName = (edge.getRoadName().isEmpty()) ? "a pathway" : edge.getRoadName();

				//
				if(i+1 < directionEdges.size())
				{
					if(!currentRoadName.equals(directionEdges.get(i+1).getRoadName())) //If the next Edge has the same roadname as the current one							
					{
						if(!edge.getRoadName().contains("i krydset"))
						{
							if(edge.getRoadName().contains("Rundkørsel"))
								currentLength = 10;
							directions.add("Travel along " + currentRoadName + " for " + String.format("%.1f", currentLength) + " meters");
							currentLength = 0;
						}						
					}
				}
				//If it is the last Edge of the route
				else {
					directions.add("Travel along " + currentRoadName + " for " + String.format("%.1f", currentLength) + " meters");

				}
			}

			double bikeSpeedMetersPerSec = 15.0/3.6;
			double walkSpeedMetersPerSec = 5.0/3.6;

			if(VehicleType.equals("Bike"))
				totalTravelTime = (totalTravelLength/bikeSpeedMetersPerSec)/60;
			else if(VehicleType.equals("Walk")) 
				totalTravelTime = (totalTravelLength/walkSpeedMetersPerSec)/60	;

			directions.add(2, "Total travel distance: " + String.format("%.1f", totalTravelLength/1000) + " kilometers");
			directions.add(3, "Total travel time: " + String.format("%.1f", totalTravelTime) + " minutes");
			return directions.toArray(new String[directions.size()]);

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

	@SuppressWarnings("static-access")
	private void createWarning(String message)
	{
		new JOptionPane().showMessageDialog(getJFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
		fromSearchQuery.requestFocus();
	}

	private void whichTextField() {
		if(fromSearchQuery.hasFocus() && fromSearchQuery.getText().length() > 0)
			highLightEdges(fromSearchQuery.getText().trim(), "from");
		
		if(toSearchQuery.hasFocus() && toSearchQuery.getText().length() > 0)
			highLightEdges(toSearchQuery.getText().trim(), "to");
	}
	private void highLightEdges(String input, String field){
		try {
			if(field.equals("from"))
			{
				addressSearcherFrom.searchForAdress(input);
				mapPanel.setFromEdgesToHighlight(addressSearcherFrom.getFoundEdges());
			}
			if(field.equals("to"))
			{
				addressSearcherTo.searchForAdress(input);
				mapPanel.setToEdgesToHighlight(addressSearcherTo.getFoundEdges());
			}
		}catch (MalformedAddressException | NoAddressFoundException e1) {
			createWarning(e1.getMessage());
		}
	}



	//---------------------------------Listeners from here-----------------------------//


	private class TimerListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			whichTextField();
		}

	}


	/**
	 * Resets the timer. If the user lingers it paints the edge inputted.
	 */
	private class MapKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg) {
			if(arg.getKeyCode() == 10) {
				enterKeyPress();
			}

			else {
				if (showAddressTimer.isRunning())
					showAddressTimer.restart(); 

				else 
					showAddressTimer.start();
			}

		}
		@Override
		public void keyReleased(KeyEvent e) {
			if(fromSearchQuery.hasFocus() && fromSearchQuery.getText().trim().isEmpty()) 
			{
				mapPanel.setFromEdgesToHighlight(null);
				addressSearcherFrom.clearResults();
				clearInputOnMap();
			}

			else if(toSearchQuery.hasFocus() && toSearchQuery.getText().trim().isEmpty())
			{
				mapPanel.setToEdgesToHighlight(null);
				addressSearcherTo.clearResults();
				clearInputOnMap();

			}

		}

		private void clearInputOnMap() {
			if (mapPanel.getPathTo() != null) {
				try {
					mapPanel.setPathTo(null);
					directionEdges = new ArrayList<>();
				} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1) {
					createWarning(e1.getMessage());
				}
			}
		}
		/**
		 * If something is written in both fields, it'll try to make a path, otherwise focus shifts to the other field.
		 */
		public void enterKeyPress() {
			if(toSearchQuery.hasFocus())
			{
				try {
					showAddressTimer.stop();
					addressSearcherTo.searchForAdress(toSearchQuery.getText().trim());
					mapPanel.setToEdgesToHighlight(addressSearcherTo.getFoundEdges());
					if(addressSearcherFrom.getFoundEdges().length > 0 && !toSearchQuery.getText().trim().isEmpty())
						findRoute();
					else { 
						fromSearchQuery.requestFocus();
					}
				} catch (MalformedAddressException | NoRoutePossibleException | NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException | NoAddressFoundException e) {
					createWarning(e.getMessage());
				}
			}
			else 
			{
				try {
					showAddressTimer.stop();
					addressSearcherFrom.searchForAdress(fromSearchQuery.getText().trim());
					mapPanel.setFromEdgesToHighlight(addressSearcherFrom.getFoundEdges());
					if(addressSearcherTo.getFoundEdges().length > 0 && !fromSearchQuery.getText().trim().isEmpty())
						findRoute();
					else  
						toSearchQuery.requestFocus();
				} catch (MalformedAddressException | NoAddressFoundException | NoRoutePossibleException | NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e) {
					createWarning(e.getMessage());
				}
			}
		}
		@Override
		public void keyTyped(KeyEvent e) {
		}
	}

	/**
	 * Changes the route type in the routing. 
	 */
	private class RouteTypeActionListener implements ActionListener{

		@SuppressWarnings("rawtypes")
		public void actionPerformed(ActionEvent e) {
			try {
				JComboBox cb = (JComboBox)e.getSource();
				String type = (String) cb.getSelectedItem();
				RouteType = type.trim();
			} catch (ClassCastException e2) {
				return;
			}

		}
	}

	/**
	 * Changes the vehicle type for the routing
	 */
	private class VehicleTypeActionListener implements ActionListener{

		@SuppressWarnings("rawtypes")
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				JComboBox cb = (JComboBox)e.getSource();
				String type = (String) cb.getSelectedItem();
				VehicleType = type.trim();
			} catch (ClassCastException e2) {
				return;
			}
		}
	}


	/**
	 * The listener for the coordinates
	 */
	private class CoordinatesMouseMotionListener extends MouseAdapter {

		private MapPanel mapPanel;
		private AreaToDraw mapAreaToDraw;
		private CoordinateConverter coordConverter;
		/**
		 * Makes the listener and initializes the fields
		 * @param mapPanel the panel for which to display the coordinates.
		 */
		public CoordinatesMouseMotionListener(MapPanel mapPanel){
			this.mapPanel = mapPanel;
			makeCoordinatesConverter();
		}
		/**
		 * calculate the correct x-coordinate
		 * @param e the mouse position on the map
		 * @return the x-coordinate
		 */
		public double getXCoord(MouseEvent e){
			makeCoordinatesConverter();
			double xCord = coordConverter.pixelToUTMCoordX(e.getX());				
			return xCord;
		}
		/**
		 * calculate the correct y-coordinate
		 * @param e the mouse position on the map
		 * @return the y-coordinate
		 */
		public double getYCoord(MouseEvent e){
			makeCoordinatesConverter();
			double yCord = coordConverter.pixelToUTMCoordY(e.getY());			
			return yCord;
		}
		/**
		 * A converter to convert the mouse coordinates to the coordinates of the map.
		 */
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

			UIManager.put("ToolTip.background", new ColorUIResource(ColorTheme.BACKGROUND_COLOR));
			Border border = BorderFactory.createLineBorder(ColorTheme.BUTTON_CLICKED_COLOR);
			UIManager.put("ToolTip.border", border);
			ToolTipManager.sharedInstance().setDismissDelay(15000);  

			String roadName = "";
			String toolTipText = "";
			if(edge != null)
			{
				roadName = (edge.getRoadName().trim().isEmpty()) ? "A pathway" : edge.getRoadName();
				toolTipText = roadName + ", " + edge.getPostalNumberLeft() + " " + edge.getPostalNumberLeftCityName();
			}
			if (MainGui.coordinatesBoolean) 				
				mapPanel.setToolTipText("X: " +  xString +" Y: " + yString + ", " + "Roadname: " + toolTipText);
			else 
				mapPanel.setToolTipText(toolTipText);
		}
		/**
		 * Saves the coordinates for the mouse when pressed. Is used to copy them to the clipboard.
		 */
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
	private class ReverseActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String tempFrom = fromSearchQuery.getText().trim();
			fromSearchQuery.setText(toSearchQuery.getText().trim());
			toSearchQuery.setText(tempFrom);	

			if(fromSearchQuery.getText().trim().length() > 0)
			{
				try {
					addressSearcherFrom.searchForAdress(fromSearchQuery.getText().trim());
					mapPanel.setFromEdgesToHighlight(addressSearcherFrom.getFoundEdges());
				} catch (MalformedAddressException | NoAddressFoundException e1) {
				}

			}
			else 
				addressSearcherFrom.clearResults();

			if(toSearchQuery.getText().trim().length() > 0)
			{
				try {
					addressSearcherTo.searchForAdress(toSearchQuery.getText().trim());
					mapPanel.setToEdgesToHighlight(addressSearcherTo.getFoundEdges());
				} catch (MalformedAddressException | NoAddressFoundException e1) {
				}
			}
			else
				addressSearcherTo.clearResults();

			if (addressSearcherFrom.getFoundEdges().length != 0 &&  addressSearcherTo.getFoundEdges().length != 0) {
				try {
					findRoute();
				} catch (NoAddressFoundException | NoRoutePossibleException
						| NegativeAreaSizeException
						| AreaIsNotWithinDenmarkException
						| InvalidAreaProportionsException e1) {
				}
			}
		}
	}

	/**
	 * Calls the findRoute() method.
	 */
	private class FindRouteActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				findRoute();
			} catch (NoAddressFoundException | NoRoutePossibleException | NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e) {
				createWarning(e.getMessage());
			}			
		}

	}

	private class GetDirectionsListener implements ActionListener{

		private JTextArea directionsArea;

		public GetDirectionsListener(){
			directionsArea = new JTextArea();
			directionsArea.setEditable(false);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			makeDirectionsFrame();
			try {
				fillDirections(getDirections());
			} catch (NoAddressFoundException e) {
				createWarning(e.getMessage());
			}
		}

		public void makeDirectionsFrame(){
			final JFrame directionsFrame = new JFrame();
			directionsFrame.setUndecorated(true);
			directionsFrame.setPreferredSize(new Dimension(300, 300));
			directionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			ColoredJMenuBar menuBar = new ColoredJMenuBar();
			directionsFrame.setJMenuBar(menuBar);

			ColoredJMenu exitMenu = new ColoredJMenu("x");
			exitMenu.setForeground(Color.red);
			exitMenu.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					directionsFrame.dispose();
				}
			});
			menuBar.add(Box.createHorizontalGlue());
			menuBar.add(exitMenu);

			Container contentPane = directionsFrame.getContentPane();
			contentPane.setLayout(new BorderLayout());

			ColoredJPanel panel = new ColoredJPanel();
			panel.setLayout(new BorderLayout());
			ColoredJScrollPane scrollPane = new ColoredJScrollPane(panel);


			JLabel label = new JLabel("Directions:");
			label.setForeground(ColorTheme.TEXT_COLOR);

			ColoredJPanel directionsPanel = new ColoredJPanel();
			directionsPanel.add(directionsArea);

			panel.add(directionsPanel, BorderLayout.CENTER);
			panel.add(label, BorderLayout.NORTH);

			directionsFrame.add(scrollPane, BorderLayout.CENTER);
			directionsFrame.pack();
			directionsFrame.setVisible(true);
			directionsFrame.setLocationRelativeTo(null);
		}

		public void fillDirections(String[] directions){
			String outPut = "";
			for(String line : directions)
				outPut += line +"\n";
			directionsArea.setText(outPut);
			directionsArea.setCaretPosition(0);
		}

	}

	private class TextFieldFocusListener implements FocusListener {
		private boolean fromFocusHolder = false;
		private boolean toFocusHolder = false;
		public void focusGained(FocusEvent e) {
			if (fromFocusHolder == false && e.getComponent() == fromSearchQuery) {
				toField();
				toFocusHolder = false;
			}
			if(toFocusHolder == false && e.getComponent() == toSearchQuery) {
				fromField();
				fromFocusHolder = false;
			}
		}

		public void focusLost(FocusEvent e) {
			
		}
		
		private void fromField() {
			if(fromFocusHolder  == false) 
			{
				if(fromSearchQuery.getText().trim().length() > 0 && addressSearcherFrom.getFoundEdges().length == 0)
				{
					highLightEdges(fromSearchQuery.getText().trim(), "from");
					showAddressTimer.stop();
					toFocusHolder = true;
				}
			}
		}
		
		private void toField() {
			if (toFocusHolder == false) 
			{
				if(toSearchQuery.getText().trim().length() > 0 && addressSearcherTo.getFoundEdges().length == 0)
				{
					highLightEdges(toSearchQuery.getText().trim(), "to");
					showAddressTimer.stop();
					fromFocusHolder = true;	
				}
			}
		}
	}

}