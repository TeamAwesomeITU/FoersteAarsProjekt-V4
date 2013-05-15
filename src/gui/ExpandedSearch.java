package gui;

import gui.customJComponents.ColoredJButton;
import gui.customJComponents.ColoredJMenu;
import gui.customJComponents.ColoredJMenuBar;
import gui.customJComponents.ColoredJMenuItem;
import gui.customJComponents.ColoredJPanel;
import gui.customJComponents.ColoredJScrollPane;
import gui.customJComponents.CustomJTextField;
import gui.customJComponents.SearchList;
import gui.settingsAndPopUp.ColorTheme;

import inputHandler.AddressSuggester;
import inputHandler.AddressSearch;
import inputHandler.exceptions.MalformedAdressException;

import java.awt.BorderLayout;
import java.awt.Color;
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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JWindow;

@SuppressWarnings("serial")
public class ExpandedSearch extends JFrame{

	public static CustomJTextField fromRoadNameField, fromRoadNumberField, fromRoadLetterField, fromCityNameField, fromCityPostalNumberField;
	public static CustomJTextField toRoadNameField, toRoadNumberField, toRoadLetterField, toCityNameField, toCityPostalNumberField;

	public static SearchList<String> searchList;
	public static DefaultListModel<String> listModel;

	public static JWindow listWindow;

	public static AddressSearch fromAddressSearch = new AddressSearch();
	public static AddressSearch toAddressSearch = new AddressSearch();

	private Container contentPane;

	private static String query = "";

	private static ExpandedSearch instance;

	public static ExpandedSearch getInstance(){
		if(instance != null)
			return instance;
		else{
			instance = new ExpandedSearch();
			return instance;
		}
	}

	private ExpandedSearch(){
		makeWindow();
	}

	public static boolean isNull(){
		if(instance == null)
			return true;
		else
			return false;
	}

	public void makeWindow(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		fillContentPane();
		makeMenu();
		pack();
	}

	public void fillContentPane(){
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(makeTextFields(), BorderLayout.NORTH);
		contentPane.add(makeButtons(), BorderLayout.SOUTH);
	}

	public ColoredJPanel makeButtons(){
		ColoredJPanel panel = new ColoredJPanel();
		panel.setLayout(new GridLayout(1, 5, 5, 5));

		ColoredJButton searchButton = new ColoredJButton("Search");
		searchButton.addActionListener(new SearchActionListener());

		panel.add(Box.createGlue());
		panel.add(Box.createGlue());
		panel.add(Box.createGlue());
		panel.add(searchButton);

		return panel;
	}

	public void makeMenu(){
		ColoredJMenuBar menuBar = new ColoredJMenuBar();
		setJMenuBar(menuBar);

		ColoredJMenu exitMenu = new ColoredJMenu("x");
		exitMenu.setForeground(Color.red);
		exitMenu.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(exitMenu);
	}

	public ColoredJPanel makeTextFields(){
		ColoredJPanel textFieldPanel = new ColoredJPanel();
		textFieldPanel.setLayout(new GridLayout(1, 2));

		ColoredJPanel fromPanel = new ColoredJPanel();
		fromPanel.setLayout(new GridLayout(11, 1, 5, 5));
		fromPanel.setBorder(BorderFactory.createEtchedBorder());

		JLabel fromTitle = new JLabel("From:");
		fromTitle.setForeground(Color.blue);
		fromTitle.setBorder(BorderFactory.createEtchedBorder());

		JLabel roadNameLabel = new JLabel("Roadname:");
		roadNameLabel.setForeground(ColorTheme.TEXT_COLOR);
		JLabel roadNumberLabel = new JLabel("Roadnumber:");
		roadNumberLabel.setForeground(ColorTheme.TEXT_COLOR);
		JLabel roadLetterLabel = new JLabel("Roadletter:");
		roadLetterLabel.setForeground(ColorTheme.TEXT_COLOR);
		JLabel cityLabel = new JLabel("City:");
		cityLabel.setForeground(ColorTheme.TEXT_COLOR);
		JLabel cityPostalLabel = new JLabel("Postal:");
		cityPostalLabel.setForeground(ColorTheme.TEXT_COLOR);

		fromPanel.add(fromTitle);
		fromPanel.add(roadNameLabel);
		fromPanel.add(fromRoadNameField = new CustomJTextField());
		fromPanel.add(roadNumberLabel);
		fromPanel.add(fromRoadNumberField= new CustomJTextField());
		fromPanel.add(roadLetterLabel);
		fromPanel.add(fromRoadLetterField= new CustomJTextField());
		fromPanel.add(cityLabel);
		fromPanel.add(fromCityNameField= new CustomJTextField());
		fromPanel.add(cityPostalLabel);
		fromPanel.add(fromCityPostalNumberField = new CustomJTextField());

		ColoredJPanel toPanel = new ColoredJPanel();
		toPanel.setLayout(new GridLayout(11, 1, 5, 5));
		toPanel.setBorder(BorderFactory.createEtchedBorder());

		JLabel toTitle = new JLabel("To:");
		toTitle.setForeground(Color.blue);
		toTitle.setBorder(BorderFactory.createEtchedBorder());

		JLabel roadNameLabel1 = new JLabel("Roadname:");
		roadNameLabel1.setForeground(ColorTheme.TEXT_COLOR);
		JLabel roadNumberLabel1 = new JLabel("Roadnumber:");
		roadNumberLabel1.setForeground(ColorTheme.TEXT_COLOR);
		JLabel roadLetterLabel1 = new JLabel("Roadletter:");
		roadLetterLabel1.setForeground(ColorTheme.TEXT_COLOR);
		JLabel cityLabel1 = new JLabel("City:");
		cityLabel1.setForeground(ColorTheme.TEXT_COLOR);
		JLabel cityPostalLabel1 = new JLabel("Postal:");
		cityPostalLabel1.setForeground(ColorTheme.TEXT_COLOR);

		toPanel.add(toTitle);
		toPanel.add(roadNameLabel1);
		toPanel.add(toRoadNameField = new CustomJTextField());
		toPanel.add(roadNumberLabel1);
		toPanel.add(toRoadNumberField = new CustomJTextField());
		toPanel.add(roadLetterLabel1);
		toPanel.add(toRoadLetterField = new CustomJTextField());
		toPanel.add(cityLabel1);
		toPanel.add(toCityNameField = new CustomJTextField());
		toPanel.add(cityPostalLabel1);
		toPanel.add(toCityPostalNumberField = new CustomJTextField());
		toCityPostalNumberField.setPreferredSize(new Dimension(200, 20));

		addActionListenersToTextFields();

		listWindow = new JWindow();
		listModel = new DefaultListModel();
		searchList = new SearchList(listModel);

		textFieldPanel.add(fromPanel);
		textFieldPanel.add(toPanel);

		return textFieldPanel;
	}

	public void addActionListenersToTextFields(){
		fromRoadNameField.addKeyListener(new MatchingList());
		fromRoadNumberField.addKeyListener(new MatchingList());
		fromRoadLetterField.addKeyListener(new MatchingList());
		fromCityNameField.addKeyListener(new MatchingList());
		fromCityPostalNumberField.addKeyListener(new MatchingList());

		toRoadNameField.addKeyListener(new MatchingList());
		toRoadNumberField.addKeyListener(new MatchingList());
		toRoadLetterField.addKeyListener(new MatchingList());
		toCityNameField.addKeyListener(new MatchingList());
		toCityPostalNumberField.addKeyListener(new MatchingList());
	}

	@SuppressWarnings("static-access")
	public void makeListWindow(){
		Container listContentPane = listWindow.getContentPane();
		setLocationOfList();
		listWindow.setPreferredSize(new Dimension(285, 60));

		ColoredJPanel listPanel = new ColoredJPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		ColoredJScrollPane scrollPane = new ColoredJScrollPane(listPanel);

		fillList();

		listPanel.add(searchList);
		listContentPane.add(scrollPane);

		listWindow.pack();
		if(listModel.getSize() == 0)
			listWindow.setVisible(false);
		else
			listWindow.setVisible(true);

	}

	/**
	 * Checks if the entered key is a valid one.
	 * @param e the key entered
	 * @return a boolean whichs determines whether or not to update the list.
	 */
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

	/**
	 * Kig på den her jesper!!
	 */
	public void fillList(){
		//first removes all the elemnts of the list
		listModel.removeAllElements();
		if(fromRoadNameField.hasFocus())
		{
			//Do something. This is the first field from the left colon.
		}else if(fromRoadNumberField.hasFocus())
		{
			//Do something. This is the second field from the left colon.
		}else if(fromRoadLetterField.hasFocus())
		{
			//Do something. This is the third field from the left colon.
		}else if(fromCityNameField.hasFocus())
		{
			//Do something. This is the fourth field from the left colon.
		}else if(fromCityPostalNumberField.hasFocus())
		{
			//Do something. This is the fifth and final field from the left colon.
		}else if(toRoadNameField.hasFocus())
		{
			//Do something. This is the first field from the right colon.
		}else if(toRoadNumberField.hasFocus())
		{
			//Do something. This is the second field from the right colon.
		}else if(toRoadLetterField.hasFocus())
		{
			//Do something. This is the third field from the right colon.
		}else if(toCityNameField.hasFocus())
		{
			//Do something. This is the fourth field from the right colon.
		}else if(toCityPostalNumberField.hasFocus())
		{
			//Do something. This is the fifth and final field from the right colon.
		}
	}

	public void setLocationOfList(){
		if(fromCityNameField.hasFocus()){
			Point location = fromCityNameField.getLocationOnScreen();
			listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+22)));
		}else if(fromCityPostalNumberField.hasFocus()){
			Point location = fromCityPostalNumberField.getLocationOnScreen();
			listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+22)));
		}else if(fromRoadLetterField.hasFocus()){
			Point location = fromRoadLetterField.getLocationOnScreen();
			listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+22)));
		}else if(fromRoadNumberField.hasFocus()){
			Point location = fromRoadNumberField.getLocationOnScreen();
			listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+22)));
		}else if(fromRoadNameField.hasFocus()){
			Point location = fromRoadNameField.getLocationOnScreen();
			listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+22)));
		}else if(toRoadNameField.hasFocus()){
			Point location = toRoadNameField.getLocationOnScreen();
			listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+22)));
		}else if(toRoadNumberField.hasFocus()){
			Point location = toRoadNumberField.getLocationOnScreen();
			listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+22)));
		}else if(toRoadLetterField.hasFocus()){
			Point location = toRoadLetterField.getLocationOnScreen();
			listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+22)));
		}else if(toCityPostalNumberField.hasFocus()){
			Point location = toCityPostalNumberField.getLocationOnScreen();
			listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+22)));
		}else if(toCityNameField.hasFocus()){
			Point location = toCityNameField.getLocationOnScreen();
			listWindow.setLocation(new Point((int)location.getX(), ((int)location.getY()+22)));
		}
	}

	class MatchingList implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(checkKeyEvent(e)){
				listModel.removeAllElements();
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			JTextField textField = (JTextField)e.getSource();
			String search = (String) textField.getText().trim();
			query = search;

			if(checkKeyEvent(e)){
				makeListWindow();
				if(query.length() < 2 && listWindow != null)
					listWindow.dispose();
			}else return;
			if(e.getKeyCode() == 8 && listWindow != null)
				listWindow.dispose();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if(!checkKeyEvent(e))
				fillList();

		}

	}

	/**
	 * Jesper do your shit!
	 */
	class SearchActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
