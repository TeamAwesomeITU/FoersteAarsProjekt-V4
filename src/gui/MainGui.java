package gui;

import gui.customJComponents.*;
import gui.settingsAndPopUp.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * This is our main gui class. It holds methods that is common for all
 * old and future windows.
 */
public class MainGui {

	private static MainGui instance;

	public static boolean coastlinesWanted = false;

	public static boolean undecoratedBoolean = false;

	public static boolean coordinatesBoolean = false;

	public static boolean menuBoolean = false;

	public static boolean isDefaultCursor;

	public static boolean dragonBoolean = false;

	public static boolean colorFollowTheme = false;

	public static String locationString, coordinatesString;

	public static JFrame frame;

	public static Container contentPane;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		readSettingsFile();
		ColorTheme.setTheme();
		ScreenSize.setScreenSize();
		MainGui.getInstance();
		long endTime = System.currentTimeMillis();
		System.out.println("Total startup time is " + (endTime - startTime) + " milliseconds");

	}

	/**
	 * Uses a singleton API to get the instance of the program
	 * @return the instance of the program
	 */
	public static MainGui getInstance(){
		if(instance != null)
			return instance;
		else{
			instance = new MainGui();
			return instance;
		}
	}
	/**
	 * the constructor for the main gui. It makes the frame and adds the footer.
	 */
	private MainGui(){
		setMacLookAndFeel();
		makeFrameAndContentPane();
		contentPane.add(makeFooter(), BorderLayout.SOUTH);
		new StartupWindow();
	}

	/**
	 * The frame is made and the contentpane is initialized
	 */
	public static void makeFrameAndContentPane(){
		frame = new JFrame("Team Awesome Maps");
		frame.setUndecorated(MainGui.undecoratedBoolean);

		changeScreenSize();
		frame.setPreferredSize(ScreenSize.screenSize);

		contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
	}
	

	/**
	 * Makes the menu and adds shortcuts to it. This is the standard menu for all future windows.
	 * It contains alot of anonymous inner classes. These are used mostly by the settings menu, 
	 * which can alter much of the appearance of the gui.
	 */
	public static void makeMenu(){
		final int SHORT_CUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.setBackground(ColorTheme.BUTTON_CLICKED_COLOR);
		menuBar.setBorder(BorderFactory.createEmptyBorder());

		ColoredJMenu fileMenu = new ColoredJMenu("File");
		ColoredJMenuItem quitItem = new ColoredJMenuItem("Quit");
		quitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORT_CUT_MASK));

		ColoredJMenuItem settingsItem = new ColoredJMenuItem("Settings");
		settingsItem.setEnabled(menuBoolean);
		settingsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readSettingsFile();				
				final JFrame settingsFrame = new JFrame("Settings");

				Container container = settingsFrame.getContentPane();
				container.setLayout(new BorderLayout());

				ColoredJPanel settingsPanel = new ColoredJPanel();
				final ColoredJCheckBox undecorated = new ColoredJCheckBox("Undecorated");
				undecorated.setSelected(undecoratedBoolean);

				undecorated.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.DESELECTED)	undecoratedBoolean = false;
						if(e.getStateChange() == ItemEvent.SELECTED)	undecoratedBoolean = true;
					}});

				final ColoredJCheckBox dragon = new ColoredJCheckBox("Dragon");
				dragon.setSelected(dragonBoolean);
				dragon.addItemListener(new ItemListener(){
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.DESELECTED)	dragonBoolean = false; 
						if(e.getStateChange() == ItemEvent.SELECTED)	dragonBoolean = true; 
					}
				});

				final ColoredJCheckBox coordinates = new ColoredJCheckBox("Coordinates");
				coordinates.setSelected(coordinatesBoolean);
				coordinates.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.DESELECTED)	coordinatesBoolean = false;
						if(e.getStateChange() == ItemEvent.SELECTED)	coordinatesBoolean = true;
					}
				});

				final ColoredJCheckBox colorFollow = new ColoredJCheckBox("Map Colors Follow Theme");
				colorFollow.setSelected(colorFollowTheme);
				colorFollow.setEnabled(coastlinesWanted);
				colorFollow.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.DESELECTED) 	colorFollowTheme = false;
						if(e.getStateChange() == ItemEvent.SELECTED) 	colorFollowTheme = true;
					}
				});

				final ColoredJCheckBox coastlines = new ColoredJCheckBox("Coastlines");
				coastlines.setSelected(coastlinesWanted);
				coastlines.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.DESELECTED){ 	
							coastlinesWanted = false;
							colorFollowTheme = false;
						}
						if(e.getStateChange() == ItemEvent.SELECTED){
							coastlinesWanted = true;
							colorFollowTheme = true;
						}
						colorFollow.setSelected(colorFollowTheme);
						colorFollow.setEnabled(coastlinesWanted);
					}
				});

				ColoredJComboBox colorThemesBox = new ColoredJComboBox();
				colorThemesBox.setPreferredSize(new Dimension(120, 30));
				colorThemesBox.setEditable(true);
				String[][] themes = {{"Summer", "resources/SummerLogo.png"},
						{"Winter", "resources/WinterLogo.png"},
						{"Spring", "resources/SpringLogo.png"},
						{"Autumn", "resources/AutumnLogo.png"}};
				colorThemesBox.addItems(themes);
				colorThemesBox.setUI(ColoredArrowUI.createUI(colorThemesBox));

				JLabel themesLabel = new JLabel("Themes:");
				if(ColorTheme.autumnTheme) colorThemesBox.setSelectedIndex(3);
				else if(ColorTheme.springTheme) colorThemesBox.setSelectedIndex(2);
				else if(ColorTheme.winterTheme) colorThemesBox.setSelectedIndex(1);
				else if(ColorTheme.summerTheme) colorThemesBox.setSelectedIndex(0);
				colorThemesBox.addActionListener(new ActionListener() {
					@SuppressWarnings({ "rawtypes" })
					public void actionPerformed(ActionEvent e) {
						try {
							JComboBox cb = (JComboBox)e.getSource();
							String selectedTheme = (String) cb.getSelectedItem();
							if(selectedTheme.equals("Summer")) ColorTheme.setSummerTheme();
							if(selectedTheme.equals("Winter")) ColorTheme.setWinterTheme();
							if(selectedTheme.equals("Spring")) ColorTheme.setSpringTheme();
							if(selectedTheme.equals("Autumn")) ColorTheme.setAutumnTheme();

						} catch (ClassCastException e2) {
							return;
						}

					}
				});
				ColoredJComboBox frameSizesBox = new ColoredJComboBox();
				frameSizesBox.setPreferredSize(new Dimension(120, 30));
				frameSizesBox.setEditable(true);
				String[][] frameSizes = {{"Fullscreen", ""}, {"Medium", ""},
						{"Small", ""}, {"Dualscreen LEFT", ""},
						{"Dualscreen RIGHT", ""}};
				frameSizesBox.addItems(frameSizes);
				frameSizesBox.setUI(ColoredArrowUI.createUI(frameSizesBox));
				JLabel fameSizesLabel = new JLabel("Frame Size:");

				if(ScreenSize.fullScreen) frameSizesBox.setSelectedIndex(0);
				else if(ScreenSize.mediumScreen) frameSizesBox.setSelectedIndex(1);
				else if(ScreenSize.smallScreen) frameSizesBox.setSelectedIndex(2);
				else if(ScreenSize.dualScreenLeft) frameSizesBox.setSelectedIndex(3);
				else if(ScreenSize.dualScreenRight) frameSizesBox.setSelectedIndex(4);
				frameSizesBox.addActionListener(new ActionListener() {
					@SuppressWarnings({ "rawtypes" })
					public void actionPerformed(ActionEvent e) {
						try {
							JComboBox cb = (JComboBox)e.getSource();
							String selectedSize = (String)cb.getSelectedItem();
							if(selectedSize.equals("Fullscreen")) ScreenSize.setFullScreen();
							if(selectedSize.equals("Medium")) ScreenSize.setMediumScreen();
							if(selectedSize.equals("Small")) ScreenSize.setSmallScreen();
							if(selectedSize.equals("Dualscreen LEFT")) ScreenSize.setDualScreenLeft();
							if(selectedSize.equals("Dualscreen RIGHT")) ScreenSize.setDualScreenRight();
						} catch (ClassCastException e2) {
							return;
						}
					}
				});				

				ColoredJButton applyButton = new ColoredJButton("Apply");
				applyButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						frame.dispose();
						frame.setUndecorated(undecoratedBoolean);
						changeScreenSize();
						new MainGui();
						setMainHand();
						settingsFrame.dispose();
						updateSettingsFile();
					}

				});
				ColoredJButton cancelButton = new ColoredJButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						settingsFrame.dispose();
					}
				});

				ColoredJPanel applySettingsPanel = new ColoredJPanel();
				applySettingsPanel.add(applyButton);
				applySettingsPanel.add(cancelButton);

				ColoredJPanel checkboxPanel = new ColoredJPanel();
				checkboxPanel.add(themesLabel);
				checkboxPanel.add(colorThemesBox);
				checkboxPanel.add(fameSizesLabel);
				checkboxPanel.add(frameSizesBox);

				container.add(settingsPanel, BorderLayout.NORTH);
				container.add(checkboxPanel, BorderLayout.CENTER);
				container.add(applySettingsPanel, BorderLayout.SOUTH);

				settingsPanel.add(undecorated);
				settingsPanel.add(coordinates);
				settingsPanel.add(dragon);
				settingsPanel.add(coastlines);
				settingsPanel.add(colorFollow);
				settingsFrame.pack();
				settingsFrame.setLocationRelativeTo(null);	
				settingsFrame.setVisible(true);
			}
		});

		settingsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORT_CUT_MASK));
		fileMenu.add(settingsItem);
		fileMenu.add(quitItem);

		ColoredJMenu helpMenu = new ColoredJMenu("Help");
		ColoredJMenuItem controlsItem = new ColoredJMenuItem("Contols");
		controlsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "Zooming: Use the scrollwheel or SHIFT + Left Mouse Button to use a rectangle zoomer. To go a zoomlevel out use Right Mouse button.\n"
						+ "Panning: Use the arrows buttons or W, S, A or D or drag the map with Left Mouse Button");
			}
		});
		ColoredJMenuItem aboutItem = new ColoredJMenuItem("About");
		aboutItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "Welcome to T-A-M maps. Please enter an address" +
						" to get on your way.");		
			}
		});

		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, SHORT_CUT_MASK));
		helpMenu.add(aboutItem);
		helpMenu.add(controlsItem);


		ColoredJMenu exitMenu = new ColoredJMenu("X");
		exitMenu.setForeground(Color.red);
		exitMenu.setVisible(undecoratedBoolean);
		exitMenu.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		}); 

		ColoredJMenu changeScreenSizeMenu = new ColoredJMenu("□");
		changeScreenSizeMenu.setVisible(undecoratedBoolean);
		changeScreenSizeMenu.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(ScreenSize.fullScreen)
					ScreenSize.setMediumScreen();
				else
					ScreenSize.setFullScreen();
				changeScreenSize();
			}
		});
		
		ColoredJMenu minimizeMenu = new ColoredJMenu("_");
		minimizeMenu.setVisible(undecoratedBoolean);
		minimizeMenu.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("static-access")
			public void mouseClicked(MouseEvent e) {
				if(frame.getState() == frame.ICONIFIED)
					frame.setState(frame.NORMAL);
				else if(frame.getState() == frame.NORMAL)
					frame.setState(frame.ICONIFIED);
			}
		});

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(minimizeMenu);
		menuBar.add(changeScreenSizeMenu);
		menuBar.add(exitMenu);
	}

	/**
	 * Makes the footer for all future windows
	 * @return the footer in the form of a ColoredJpanel
	 */
	public static ColoredJPanel makeFooter(){
		ColoredJPanel footer = new ColoredJPanel();
		footer.setBackground(ColorTheme.BUTTON_CLICKED_COLOR);
		JLabel footerText = new JLabel("Team-Awesome-Maps ver 1.4.1 - dey tuk our djerps!");
		footer.add(footerText);
		return footer;
	}
	/**
	 * Changes the screen size depending on the chosen setting.
	 */
	public static void changeScreenSize(){
		frame.setBounds(0, 0, ScreenSize.screenWidth, ScreenSize.screenHeight);
		if(ScreenSize.mediumScreen || ScreenSize.smallScreen)
			frame.setLocationRelativeTo(null);
		if(ScreenSize.dualScreenRight)
			frame.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), 0);
	}

	/**
	 * reads the config file to start the program with the last settings.
	 * 	 */
	public static void readSettingsFile(){
		try {
			BufferedReader fileStream = new BufferedReader(new FileReader(getConfigFile()));
			if(fileStream.ready()){

				if(fileStream.ready()){
					String undecoratedSetting = fileStream.readLine().trim();
					if(undecoratedSetting.equals("true"))
						undecoratedBoolean = true;
					else if (undecoratedSetting.equals("false")) 
						undecoratedBoolean = false;
				}else updateSettingsFile();

				if(fileStream.ready()){
					String coordinatesSetting = fileStream.readLine().trim();
					if(coordinatesSetting.equals("true"))
						coordinatesBoolean = true;
					else if(coordinatesSetting.equals("false"))
						coordinatesBoolean = false;
				}else updateSettingsFile();

				if(fileStream.ready()){
					String colorThemeSetting = fileStream.readLine().trim();
					if(colorThemeSetting.equals("Spring"))
						ColorTheme.setSpringTheme();
					else if(colorThemeSetting.equals("Summer"))
						ColorTheme.setSummerTheme();
					else if(colorThemeSetting.equals("Winter"))
						ColorTheme.setWinterTheme();
					else if(colorThemeSetting.equals("Autumn"))
						ColorTheme.setAutumnTheme();
				}else updateSettingsFile();

				if(fileStream.ready()){
					String screenSetting = fileStream.readLine().trim();
					if(screenSetting.equals("Fullscreen"))
						ScreenSize.setFullScreen();
					if(screenSetting.equals("Mediumscreen"))
						ScreenSize.setMediumScreen();
					if(screenSetting.equals("Smallscreen"))
						ScreenSize.setSmallScreen();
					if(screenSetting.equals("Dualscreenleft"))
						ScreenSize.setDualScreenLeft();
					if(screenSetting.equals("Dualscreenright"))
						ScreenSize.setDualScreenRight();
				}else updateSettingsFile();

				if(fileStream.ready()){
					String dragonSetting = fileStream.readLine().trim();
					if(dragonSetting.equals("true"))
						dragonBoolean = true;
					else if(dragonSetting.equals("false"))
						dragonBoolean = false;
				}else updateSettingsFile();

				if(fileStream.ready()){
					String coastlinesString = fileStream.readLine().trim();
					if(coastlinesString.equals("true"))
						coastlinesWanted = true;
					else if(coastlinesString.equals("false"))
						coastlinesWanted = false;
				}else updateSettingsFile();

				if(fileStream.ready()){
					String followString = fileStream.readLine().trim();
					if(followString.equals("Follow"))
						colorFollowTheme = true;
					else if(followString.equals("Unfollow"))
						colorFollowTheme = false;
				}else updateSettingsFile();

			}
			else
				updateSettingsFile();
			fileStream.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Could not find config file. Setting defualt settings");
		}
	}

	/**
	 * Updates the config file with the new settings
	 */
	public static void updateSettingsFile(){
		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(getConfigFile()));
			if(undecoratedBoolean)
				fileWriter.write("true\n");
			else
				fileWriter.write("false\n");

			if(coordinatesBoolean)
				fileWriter.write("true\n");
			else
				fileWriter.write("false\n");

			if(ColorTheme.summerTheme)
				fileWriter.write("Summer\n");
			if(ColorTheme.winterTheme)
				fileWriter.write("Winter\n");
			if(ColorTheme.autumnTheme)
				fileWriter.write("Autumn\n");
			if(ColorTheme.springTheme)
				fileWriter.write("Spring\n");

			if(ScreenSize.fullScreen)
				fileWriter.write("Fullscreen\n");
			if(ScreenSize.mediumScreen)
				fileWriter.write("Mediumscreen\n");
			if(ScreenSize.smallScreen)
				fileWriter.write("Smallscreen\n");
			if(ScreenSize.dualScreenLeft)
				fileWriter.write("Dualscreenleft\n");
			if(ScreenSize.dualScreenRight)
				fileWriter.write("Dualscreenright\n");

			if(dragonBoolean)
				fileWriter.write("true\n");
			else
				fileWriter.write("false\n");

			if(coastlinesWanted)
				fileWriter.write("true\n");
			else
				fileWriter.write("false\n");

			if(colorFollowTheme)
				fileWriter.write("Follow\n");
			else
				fileWriter.write("Unfollow\n");


			fileWriter.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Could not find config file. Setting defualt settings");
		}
	}

	/**
	 * Reads the config file. If none is found, one is made with the default settings
	 * @return the configfile for the settings
	 */
	public static File getConfigFile() {
		File configFile = new File("config.txt");
		if(!configFile.exists())
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, "Could not create config file");
			}
		return configFile;
	}

	/**
	 * Sets the look of the program on mac to be the same as on windows.
	 */
	private void setMacLookAndFeel(){
		String OS = System.getProperty("os.name").toLowerCase();
		if(OS.indexOf("mac") >= 0)
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e) {
				JOptionPane.showMessageDialog(frame, "Something went wrong setting up the program, please contact TeamAwesome.");
			}
	}

	/**
	 * Defines the cursor as the dragon cursor or as the default cursor.
	 */
	public static void setMainHand(){
		if(dragonBoolean == false)
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		else { Toolkit toolkit = Toolkit.getDefaultToolkit(); 
		Image image = toolkit.getImage("resources/dragon.png"); 
		Point hotSpot = new Point(0,0);
		Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "Dragon"); 
		frame.setCursor(cursor);
		}
	}
}
