package gui;

import gui.customJComponents.ColoredJButton;
import gui.customJComponents.ColoredJMenu;
import gui.customJComponents.ColoredJMenuBar;
import gui.customJComponents.ColoredJMenuItem;
import gui.customJComponents.ColoredJPanel;
import gui.customJComponents.CustomJTextField;
import gui.settingsAndPopUp.ColorTheme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.java.swing.plaf.motif.MotifBorders.BevelBorder;

@SuppressWarnings("serial")
public class ExpandedSearch extends JFrame{
	
	private CustomJTextField fromRoadNameField, fromRoadNumberField, fromRoadLetterField, fromCityNameField, fromCityPostalNumberField;
	private CustomJTextField toRoadNameField, toRoadNumberField, toRoadLetterField, toCityNameField, toCityPostalNumberField;
	
	private Container contentPane;
	
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
		
		textFieldPanel.add(fromPanel);
		textFieldPanel.add(toPanel);
		
		return textFieldPanel;
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
