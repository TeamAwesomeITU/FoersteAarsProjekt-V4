package gui.customJComponents;

import gui.settingsAndPopUp.ColorTheme;

import inputHandler.AddressSuggester;
import inputHandler.exceptions.MalformedAdressException;
import inputHandler.exceptions.NoAddressFoundException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
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
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;

@SuppressWarnings("serial")
public class ExpandedSearch extends JFrame{

	public static CustomJTextField fromRoadNameField, fromRoadNumberField, fromRoadLetterField, fromCityNameField, fromCityPostalNumberField;
	public static CustomJTextField toRoadNameField, toRoadNumberField, toRoadLetterField, toCityNameField, toCityPostalNumberField;

	public static SearchList<String> searchList;
	public static DefaultListModel<String> listModel;

	public static JWindow listWindow;

	public static AddressSuggester fromAddressSuggester = new AddressSuggester();
	public static AddressSuggester toAddressSuggester = new AddressSuggester();

	private Container contentPane;

	public ExpandedSearch(){
		makeWindow();
	}

	public void makeWindow(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		fillContentPane();
		makeMenu();
		pack();
		setVisible(true);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

		fromRoadNumberField.setEnabled(false);
		fromRoadLetterField.setEnabled(false);

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

		toRoadNumberField.setEnabled(false);
		toRoadLetterField.setEnabled(false);

		addActionListenersToTextFields();

		listWindow = new JWindow();
		listModel = new DefaultListModel();
		listModel.ensureCapacity(150000);
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

	
	public void makeListWindow() throws MalformedAdressException, NoAddressFoundException{
		Container listContentPane = listWindow.getContentPane();
		setLocationOfList();
		listWindow.setPreferredSize(new Dimension(285, 60));
		
		fillList();
		
		listContentPane.add(searchList.getScrollPane());
		listWindow.pack();
		
		if(listModel.getSize() == 0)
			listWindow.setVisible(false);
		else
			listWindow.setVisible(true);
	}

	@SuppressWarnings("static-access")
	private void createWarning(String message)
	{
		new JOptionPane().showMessageDialog(contentPane, message, "Error", JOptionPane.ERROR_MESSAGE);
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
	 * @throws NoAddressFoundException 
	 * @throws MalformedAdressException 
	 */
	public void fillList() throws MalformedAdressException, NoAddressFoundException{
		//first removes all the elements of the list
		listModel.removeAllElements();

		if(fromRoadNameField.hasFocus())
		{
			if(!fromRoadNameField.getText().trim().isEmpty())
			{
				fromAddressSuggester.enterRoadName(fromRoadNameField.getText().trim());
				for(String string : fromAddressSuggester.getPossibleRoadNames())
					listModel.addElement(string);
				//If the list model is not empty after this, roads must have been found - unlock the fromRoadNumberField
				if(!listModel.isEmpty())
					fromRoadNumberField.setEnabled(true);
				else
				{
					fromRoadNumberField.setText("");
					fromRoadNumberField.setEnabled(false);
				}

			}
		}
		else if(fromRoadNumberField.hasFocus())
		{
			if(!fromRoadNumberField.getText().trim().isEmpty())
			{
				fromAddressSuggester.enterRoadNumber(fromRoadNumberField.getText().trim());
				for(String number : fromAddressSuggester.getPossibleRoadNumbers())
					listModel.addElement(number);
				//If the list model is not empty after this, roads must have been found - unlock the fromRoadLetterField
				if(!listModel.isEmpty())
					fromRoadLetterField.setEnabled(true);
				else
				{
					fromRoadLetterField.setText("");
					fromRoadLetterField.setEnabled(false);
				}
			}
		}
		else if(fromRoadLetterField.hasFocus())
		{
			if(!fromRoadLetterField.getText().trim().isEmpty())
			{
				fromAddressSuggester.enterRoadLetter(fromRoadLetterField.getText().trim());
				for(String string : fromAddressSuggester.getPossibleRoadLetters())
					listModel.addElement(string);
			}
		}
		else if(fromCityNameField.hasFocus())
		{
			if(!fromCityNameField.getText().trim().isEmpty())
			{
				fromAddressSuggester.enterCityName(fromCityNameField.getText().trim());
				for(String string : fromAddressSuggester.getPossibleCityNames())
					listModel.addElement(string);
			}
		}
		else if(fromCityPostalNumberField.hasFocus())
		{
			if(!fromCityPostalNumberField.getText().trim().isEmpty())
			{
				fromAddressSuggester.enterPostalNumber(fromCityPostalNumberField.getText().trim());
				for(String string : fromAddressSuggester.getPossiblePostalNumbers())
					listModel.addElement(string);
			}
		}
		else if(toRoadNameField.hasFocus())
		{
			if(!toRoadNameField.getText().trim().isEmpty())
			{
				toAddressSuggester.enterRoadName(toRoadNameField.getText().trim());
				for(String string : toAddressSuggester.getPossibleRoadNames())
					listModel.addElement(string);
				//If the list model is not empty after this, roads must have been found - unlock the toRoadNumberField
				if(!listModel.isEmpty())
					toRoadNumberField.setEnabled(true);
				else
				{
					toRoadNumberField.setText("");
					toRoadNumberField.setEnabled(false);
				}

			}
		}
		else if(toRoadNumberField.hasFocus())
		{
			if(!toRoadNumberField.getText().trim().isEmpty())
			{
				toAddressSuggester.enterRoadNumber(toRoadNumberField.getText().trim());
				for(String number : toAddressSuggester.getPossibleRoadNumbers())
					listModel.addElement(number);
				//If the list model is not empty after this, roads must have been found - unlock the toRoadLetterField
				if(!listModel.isEmpty())
					toRoadLetterField.setEnabled(true);
				else
				{
					toRoadLetterField.setText("");
					toRoadLetterField.setEnabled(false);
				}
			}

		}
		else if(toRoadLetterField.hasFocus())
		{
			if(!toRoadLetterField.getText().trim().isEmpty())
			{
				toAddressSuggester.enterRoadLetter(toRoadLetterField.getText().trim());
				for(String string : toAddressSuggester.getPossibleRoadLetters())
					listModel.addElement(string);
			}
		}
		else if(toCityNameField.hasFocus())
		{
			if(!toCityNameField.getText().trim().isEmpty())
			{
				toAddressSuggester.enterCityName(toCityNameField.getText().trim());
				for(String string : toAddressSuggester.getPossibleCityNames())
					listModel.addElement(string);
			}
		}
		else if(toCityPostalNumberField.hasFocus())
		{
			if(!toCityPostalNumberField.getText().trim().isEmpty())
			{
				toAddressSuggester.enterPostalNumber(toCityPostalNumberField.getText().trim());
				for(String string : toAddressSuggester.getPossiblePostalNumbers())
					listModel.addElement(string);
			}
		}

		if(listModel.isEmpty())
			listModel.addElement("No search results");
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
			//			if(checkKeyEvent(e)){
			//				listModel.removeAllElements();
			//			}

			//			try {
			//				makeListWindow();
			//			} catch (MalformedAdressException | NoAddressFoundException e1) {
			//				createWarning(e1.getMessage());
			//			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			//			JTextField textField = (JTextField)e.getSource();
			//			String search = (String) textField.getText().trim();
			//			query = search;

			//			if(checkKeyEvent(e)){

			//				if(query.length() < 2 && listWindow != null)
			//					listWindow.dispose();
			//			}
			//			else
			//				return;

			//			if(e.getKeyCode() == 8 && listWindow != null)
			//				listWindow.dispose();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			//			if(!checkKeyEvent(e))
			try {
				makeListWindow();
				fillList();
			} catch (MalformedAdressException | NoAddressFoundException e1) {
				createWarning(e1.getMessage());
			}

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
