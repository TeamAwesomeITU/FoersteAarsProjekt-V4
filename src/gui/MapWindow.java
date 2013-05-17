package gui;

import gui.customJComponents.*;
import gui.settingsAndPopUp.*;

import inputHandler.AddressSearch;
import inputHandler.exceptions.MalformedAdressException;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
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

	private Timer showAddressTimer = new Timer(400, new TimerListener());
	public static CustomJTextField toSearchQuery, fromSearchQuery;
	private ColoredJPanel centerColoredJPanel, westColoredJPanel = makeToolBar(), 
			eastColoredJPanel = makeEastJPanel(), southColoredJPanel = MainGui.makeFooter();
	private MapPanel mapPanel;
	private String VehicleType = "Bike", RouteType = "Fastest";
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

		JLabel fromHeader = new JLabel("From");
		fromHeader.setForeground(ColorTheme.TEXT_COLOR);
		fromSearchQuery = new CustomJTextField();
		fromSearchQuery.addKeyListener(new TextFieldListener());
		fromSearchQuery.addKeyListener(new EnterKeyListener());
		fromSearchQuery.addFocusListener(new UpdateFocusListener());
		fromSearchQuery.setPreferredSize(new Dimension(200, 20));

		JLabel toHeader = new JLabel("To");
		toHeader.setForeground(ColorTheme.TEXT_COLOR);
		toSearchQuery = new CustomJTextField();
		toSearchQuery.addKeyListener(new TextFieldListener());
		toSearchQuery.addKeyListener(new EnterKeyListener());
		toSearchQuery.addFocusListener(new UpdateFocusListener());

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

		ColoredJButton detailedDirectionsButton = new ColoredJButton("Detailed Directions");
		detailedDirectionsButton.addActionListener(new GetDirectionsListener(detailedDirectionsButton));

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
	 * @throws MalformedAdressException 
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

			int currentLength = 0;

			for(Edge edge : directionEdges)
			{
				currentLength += edge.getLength();
				if(!directions.get(directions.size()-1).contains(edge.getRoadName()))
				{
					if(!edge.getRoadName().contains("i krydset"))
					{
						if(edge.getRoadName().contains("Rundkørsel"))
							currentLength = 10;
						directions.add("Travel along " + edge.getRoadName() + " for " + currentLength + " meters");
						currentLength = 0;
					}
				}
			}	

			for(String string : directions)
				System.out.println(string);

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

	public void highLightEdges(){
		try {
			if(fromSearchQuery.hasFocus())
			{
				addressSearcherFrom.searchForAdress(fromSearchQuery.getText().trim());
				mapPanel.setFromEdgesToHighlight(addressSearcherFrom.getFoundEdges());
			}
			else if(toSearchQuery.hasFocus())
			{
				addressSearcherTo.searchForAdress(toSearchQuery.getText().trim());
				mapPanel.setToEdgesToHighlight(addressSearcherTo.getFoundEdges());
			}
		}catch (MalformedAdressException | NoAddressFoundException e1) {
			//				createWarning(e1.getMessage());
		}
	}



	//---------------------------------Listeners from here-----------------------------//


	private class TimerListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			highLightEdges();
		}

	}

	/**
	 * Resets the timer. If the user lingers it paints the edge inputted.
	 */
	class TextFieldListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg) {
			if (showAddressTimer.isRunning()){
				showAddressTimer.restart();
			} 	
			else {
				showAddressTimer.start();
			}

		}
		@Override
		public void keyReleased(KeyEvent e) {
			if (toSearchQuery.hasFocus() && toSearchQuery.getText().isEmpty() ||
					fromSearchQuery.hasFocus() && fromSearchQuery.getText().isEmpty()) 
			{
				if (mapPanel.getPathTo() != null) {
					try {
						mapPanel.setPathTo(null);
					} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e1) {
						createWarning(e1.getMessage());
					}
					mapPanel.repaintMap();
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
	class RouteTypeActionListener implements ActionListener{

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
	class VehicleTypeActionListener implements ActionListener{

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
	class CoordinatesMouseMotionListener extends MouseAdapter {

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
			if(edge != null)
				roadName = edge.getRoadName() + ", " + edge.getPostalNumberLeft() + " " + edge.getPostalNumberLeftCityName();
			if (MainGui.coordinatesBoolean) 				
				mapPanel.setToolTipText("X: " +  xString +" Y: " + yString + ", " + "Roadname: " + roadName);
			else 
				mapPanel.setToolTipText(roadName);
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
	class ReverseActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String tempFrom = fromSearchQuery.getText();
			fromSearchQuery.setText(toSearchQuery.getText());
			toSearchQuery.setText(tempFrom);			
		}
	}


	class EnterKeyListener implements KeyListener{

		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == 10){
				if(toSearchQuery.hasFocus()){
					try {
						findRoute();
					} catch (NoAddressFoundException | NoRoutePossibleException | NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e) {
						createWarning(e.getMessage());
					}
				}else if(fromSearchQuery.hasFocus())
					toSearchQuery.requestFocus();
			}
		}
		public void keyReleased(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}
	}
	/**
	 * Calls the findRoute() method.
	 */
	class FindRouteActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				findRoute();
			} catch (NoAddressFoundException | NoRoutePossibleException | NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e) {
				createWarning(e.getMessage());
			}			
		}

	}

	class GetDirectionsListener implements ActionListener{

		private JTextArea directionsArea;
		private JButton button;
		private JFrame frame;

		public GetDirectionsListener(JButton button){
			directionsArea = new JTextArea();
			directionsArea.setEditable(false);
			this.button = button;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ArrayList<String> testArrayList = new ArrayList<String>();
			testArrayList.add("KøgeMuffi");
			frame = makeDirectionsFrame();
			fillDirections(testArrayList);
		}

		public JFrame makeDirectionsFrame(){
			final JFrame directionsFrame = new JFrame();
			directionsFrame.setUndecorated(true);
			directionsFrame.setPreferredSize(new Dimension(300, 300));
			directionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			ColoredJMenuBar menuBar = new ColoredJMenuBar();
			directionsFrame.setJMenuBar(menuBar);

			ColoredJMenu exitMenu = new ColoredJMenu("x");
			exitMenu.setForeground(Color.red);
			exitMenu.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
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

			return directionsFrame;
		}

		public void fillDirections(ArrayList<String> directions){
			String outPut = "";
			for(String line : directions)
				outPut += line +"\n";
			directionsArea.setText(outPut);
		}

	}

	class UpdateFocusListener implements FocusListener{
		public void focusGained(FocusEvent e) {

		}
		public void focusLost(FocusEvent e) {
			try {
				if(fromSearchQuery.getText().trim().length() != 0){
					addressSearcherFrom.searchForAdress(fromSearchQuery.getText().trim());
					mapPanel.setFromEdgesToHighlight(addressSearcherFrom.getFoundEdges());
				}else if(toSearchQuery.getText().trim().length() != 0){
					addressSearcherTo.searchForAdress(toSearchQuery.getText().trim());
					mapPanel.setToEdgesToHighlight(addressSearcherTo.getFoundEdges());
				}
			} catch (MalformedAdressException | NoAddressFoundException e1) {
			}

		}

	}

}