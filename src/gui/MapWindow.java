package gui;

import gui.customJComponents.*;
import gui.settingsAndPopUp.*;

import inputHandler.exceptions.MalformedAdressException;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;

import navigation.DijkstraSP;

import mapCreationAndFunctions.AreaToDraw;
import mapCreationAndFunctions.MapMouseWheelZoom;
import mapCreationAndFunctions.MapPanel;
import mapCreationAndFunctions.MapPanelResize;
import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.CoordinateConverter;
import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.CitySearch;
import mapCreationAndFunctions.search.EdgeSearch;
/**
 * This class holds the window with the map of denmark.
 */
public class MapWindow {

	public static JTextField toSearchQuery, fromSearchQuery;
	public static SearchList<String> searchList;
	public static DefaultListModel<String> listModel;
	private ColoredJPanel centerColoredJPanel, westColoredJPanel = makeToolBar(), 
			eastColoredJPanel = makeEastJPanel(), southColoredJPanel = MainGui.makeFooter();
	private MapPanel mapPanel;
	private String VehicleType = "Bike", RouteType = "Fastest";
	public static JWindow listWindow;
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
		MapPanelResize mcr = new MapPanelResize(this);
		MainGui.frame.addComponentListener(mcr);
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
				{"Car", "resources/car.png"},
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

		listModel = new DefaultListModel();
		searchList = new SearchList(listModel);

		toolBar.add(reverseButton);
		toolBar.add(fromHeader);
		toolBar.add(fromSearchQuery);
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
	 */
	//TODO fix med jespers halløj
	public void findRoute(){
		/*if(fromSearchQuery.getText().trim().length() != 0 || 
				toSearchQuery.getText().trim().length() != 0){
			DijkstraSP dip = new DijkstraSP(DataHolding.getGraph(), fromSearchQuery.getText(), VehicleType, RouteType);
			mapPanel.setPathTo((Stack<Edge>) dip.pathTo(toSearchQuery.getText()));
			mapPanel.repaintMap();
		}*/
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
	 * If the user is in the search text field, then by pressing enter
	 * is the same as clicking the find route button
	 */
	class EnterKeyListener implements KeyListener{

		String query;

		@Override
		public void keyPressed(KeyEvent e) {
			if(checkKeyEvent(e)){
				if(e.getKeyCode() == 10){
					findRoute();
				}
				listModel.removeAllElements();
			}
		}

		public void keyReleased(KeyEvent e) {
			JTextField textField = (JTextField)e.getSource();
			String search = (String) textField.getText().trim();
			query = search;


			if(checkKeyEvent(e)){
				if(query.length() >= 2){
					if(listWindow != null)
						listWindow.dispose();
					listWindow = new JWindow();
					Container contentPane = listWindow.getContentPane();
					if(fromSearchQuery.hasFocus()){
						Point location = fromSearchQuery.getLocationOnScreen();
						listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+32)));
					}
					if(toSearchQuery.hasFocus()){
						Point location = toSearchQuery.getLocationOnScreen();
						listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+32)));
					}
					listWindow.setPreferredSize(new Dimension(200, 80));

					ColoredJPanel listPanel = new ColoredJPanel();
					listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
					ColoredJScrollPane scrollPane = new ColoredJScrollPane(listPanel);
					makeMatchingResult();
					listPanel.add(searchList);

					contentPane.add(scrollPane);

					listWindow.pack();
					if(listModel.getSize() == 0)
						listWindow.setVisible(false);
					else
						listWindow.setVisible(true);
				}
				if(query.length() < 2 && listWindow != null)
					listWindow.dispose();
			}
			else return;
			if(e.getKeyCode() == 8 && listWindow != null)
				listWindow.dispose();
		}

		public void keyTyped(KeyEvent e) {
			if(!checkKeyEvent(e)){
				makeMatchingResult();
			}
		}

		public void makeMatchingResult(){
			HashSet<String> listSet = new HashSet<>();
			Edge[] edgesList;
			try {
				edgesList = EdgeSearch.searchForRoadSuggestions(query, -1, "");
				for(Edge edge : edgesList){
					String hit = edge.getRoadName() + " " + edge.getPostalNumberLeft() + " " + edge.getPostalNumberLeftCityName();
					listSet.add(hit);
				}
				
				if(listSet.size() == 0)
					listModel.addElement("Could not find any matches");
				
				else {
					for(String searchResult : listSet){
						listModel.addElement(searchResult);
				}

				}
				
			} catch (MalformedAdressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			/*City[] citiesList = CitySearch.getCityNameSuggestions(query);
			for(City city : citiesList){
				listModel.addElement(city.getCityName());
				System.out.println(city.getCityName());
			}*/

		}

		private boolean checkKeyEvent(KeyEvent e){
			boolean check = true;

			int keyEvent = e.getKeyCode();
			switch (keyEvent) {
			case 8:	check = false;
			case 16: check = false;
			case 17: check = false;
			case 18: check = false;
			case 37: check = false;
			case 38: check = false;
			case 39: check = false;
			case 40: check = false;
			case 112: check = false;
			case 113: check = false;
			case 114: check = false;
			case 115: check = false;
			case 116: check = false;
			case 117: check = false;
			case 118: check = false;
			case 119: check = false;
			case 120: check = false;
			case 121: check = false;
			case 122: check = false;
			case 123: check = false;
			case 124: check = false;
			break;
			default:
				break;
			}
			return check;
		}
	}

	/**
	 * Not yet implemented!
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
	 * Not yet implemented!
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

			UIManager.put("ToolTip.background", new ColorUIResource(ColorTheme.BACKGROUND_COLOR));
			Border border = BorderFactory.createLineBorder(ColorTheme.BUTTON_CLICKED_COLOR);
			UIManager.put("ToolTip.border", border);
			ToolTipManager.sharedInstance().setDismissDelay(15000);  

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
	 * This listeren isn't done yet.
	 */
	class FindRouteActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			findRoute();			
		}

	}



}