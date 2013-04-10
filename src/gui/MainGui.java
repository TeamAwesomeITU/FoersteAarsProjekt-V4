package gui;

import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import org.hamcrest.core.Is;
/**
 * This is our main gui class. It holds methods that is common for all
 * old and future windows.
 */
public class MainGui {

	private static MainGui instance;

	public static boolean undecoratedBoolean = true;

	public static boolean coordinatesBoolean = false;
	
	public static JFrame frame;
	
	public static Container contentPane;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ColorTheme.setTheme();
		MainGui.getInstance();
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
	
	private void setMacLookAndFeel(){
		String OS = System.getProperty("os.name").toLowerCase();
		if(OS.indexOf("mac") >= 0)
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			
	}

	/**
	 * The frame is maded and the contentpane is initialized
	 */
	public static void makeFrameAndContentPane(){
		frame = new JFrame("Team Awesome Maps");
		frame.setUndecorated(MainGui.undecoratedBoolean);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0,0,screenSize.width, screenSize.height);
		frame.setPreferredSize(screenSize);
		
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
	}

	/**
	 * Makes the menu and adds shortcuts to it. This is the standard menu for all future windows.
	 */
	public static void makeMenu(){
		final int SHORT_CUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.setBackground(ColorTheme.BUTTON_CLICKED_COLOR);
		menuBar.setBorder(BorderFactory.createEtchedBorder());

		JMenu fileMenu = new JMenu("File");
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.setBackground(ColorTheme.BUTTON_CLICKED_COLOR);
		quitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORT_CUT_MASK));

		JMenuItem settingsItem = new JMenuItem("Settings");
		settingsItem.setBackground(ColorTheme.BUTTON_CLICKED_COLOR);
		settingsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame settingsFrame = new JFrame("Settings");
				settingsFrame.setLocationRelativeTo(null);	

				Container container = settingsFrame.getContentPane();
				container.setLayout(new BorderLayout());

				ColoredJPanel settingsPanel = new ColoredJPanel();
				final ColoredJCheckBox undecorated = new ColoredJCheckBox("Undecorated");
				undecorated.setSelected(undecoratedBoolean);

				undecorated.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.DESELECTED){
							undecoratedBoolean = false;
							frame.dispose();
							frame.setUndecorated(undecoratedBoolean);
							new MainGui();
							settingsFrame.dispose();
						}

						if(e.getStateChange() == ItemEvent.SELECTED){
							undecoratedBoolean = true;
							frame.dispose();
							frame.setUndecorated(undecoratedBoolean);
							new MainGui();
							settingsFrame.dispose();
						}
					}});

				final ColoredJCheckBox coordinates = new ColoredJCheckBox("Coordinates");
				coordinates.setSelected(coordinatesBoolean);
				coordinates.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.DESELECTED)
							coordinatesBoolean = false;
						if(e.getStateChange() == ItemEvent.SELECTED)
							coordinatesBoolean = true;
					}
				});

				JLabel themesLabel = new JLabel("Themes:");

				String[] themes = {"Summer", "Winter", "Spring", "Autumn"};

				JComboBox<String> colorThemesBox = new JComboBox<>(themes);
				if(ColorTheme.autumnTheme) colorThemesBox.setSelectedIndex(3);
				else if(ColorTheme.springTheme) colorThemesBox.setSelectedIndex(2);
				else if(ColorTheme.winterTheme) colorThemesBox.setSelectedIndex(1);
				else if(ColorTheme.summerTheme) colorThemesBox.setSelectedIndex(0);
				colorThemesBox.addActionListener(new ActionListener() {
					@SuppressWarnings({ "rawtypes" })
					public void actionPerformed(ActionEvent e) {
						JComboBox cb = (JComboBox)e.getSource();
						String selectedTheme = (String)cb.getSelectedItem();
						if(selectedTheme.equals("Summer")) ColorTheme.setSummerTheme();
						if(selectedTheme.equals("Winter")) ColorTheme.setWinterTheme();
						if(selectedTheme.equals("Spring")) ColorTheme.setSpringTheme();
						if(selectedTheme.equals("Autumn")) ColorTheme.setAutumnTheme();
						frame.dispose();
						new MainGui();
						settingsFrame.dispose();
					}
				});

				ColoredJPanel themesPanel = new ColoredJPanel();
				themesPanel.add(themesLabel);
				themesPanel.add(colorThemesBox);				

				container.add(settingsPanel, BorderLayout.CENTER);
				container.add(themesPanel, BorderLayout.SOUTH);

				settingsPanel.add(undecorated);
				settingsPanel.add(coordinates);
				settingsFrame.pack();
				settingsFrame.setVisible(true);
			}
		});
		settingsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORT_CUT_MASK));
		fileMenu.add(settingsItem);
		fileMenu.add(quitItem);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.setBackground(ColorTheme.BUTTON_CLICKED_COLOR);;
		aboutItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "Welcome to T-A-M maps. Please enter an address" +
						" to get on your way, or simply click the globe to browse the map");		
			}
		});
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, SHORT_CUT_MASK));
		helpMenu.add(aboutItem);

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);		
	}

	/**
	 * Makes the footer for all future windows
	 * @return the footer in the form of a ColoredJpanel
	 */
	public static ColoredJPanel makeFooter(){
		ColoredJPanel footer = new ColoredJPanel();
		footer.setBackground(ColorTheme.BUTTON_CLICKED_COLOR);
		JLabel footerText = new JLabel("Team-Awesome-Maps ver 1.4");
		footer.add(footerText);
		return footer;
	}
}