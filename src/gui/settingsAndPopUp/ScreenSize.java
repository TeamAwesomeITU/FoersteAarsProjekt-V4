package gui.settingsAndPopUp;

import java.awt.Dimension;
import java.awt.Toolkit;
/**
 * This class keeps track of our screen sizes.
 * Our Gui has five different screensizes to choose from. 
 */
public class ScreenSize {
	//This is our defualt settings for our screen size
	public static int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			
	public static Dimension screenSize;
	//5 different booleans to hold the settings
	public static boolean fullScreen = true;
	public static boolean mediumScreen = false;
	public static boolean smallScreen = false;
	public static boolean dualScreenLeft = false;
	public static boolean dualScreenRight = false;
	
	/**
	 * Sets the screensize selected from the settings. 
	 */
	public static void setScreenSize(){
		screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		if(fullScreen){
			screenSize = new Dimension(screenWidth, screenHeight);
		}
		if(mediumScreen){
			screenWidth = (int)(screenWidth*0.75);
			screenHeight = (int)(screenHeight*0.75);
			screenSize = new Dimension(screenWidth, screenHeight);
		}
		if(smallScreen){
			screenWidth = (int)(screenWidth*0.40);
			screenHeight = (int)(screenHeight*0.40);
			screenSize = new Dimension(screenWidth, screenWidth);
		}
		if(dualScreenLeft){
			screenWidth = (int)(screenWidth*0.50);
			screenSize = new Dimension(screenWidth, screenHeight);
		}
		if(dualScreenRight){
			screenWidth = (int) (screenWidth*0.50);
			screenSize = new Dimension(screenWidth, screenHeight);
		}
	}
	
	/**
	 * Sets the screen to fullsize
	 */
	public static void setFullScreen(){
		fullScreen = true;
		
		mediumScreen = false;
		smallScreen = false;
		dualScreenLeft = false;
		dualScreenRight = false;
		setScreenSize();
	}
	
	/**
	 * Sets the screen to a medium size
	 */
	public static void setMediumScreen(){
		mediumScreen = true;
		
		fullScreen = false;
		smallScreen = false;
		dualScreenLeft = false;
		dualScreenRight = false;
		setScreenSize();
	}
	
	/**
	 * Sets the screen to a small size
	 */
	public static void setSmallScreen(){
		smallScreen = true;
		
		fullScreen = false;
		mediumScreen = false;
		dualScreenLeft = false;
		dualScreenRight = false;
		setScreenSize();
	}
	
	/**
	 * sets the screen in dual mode left oriented. This allows for working in two different programs
	 */
	public static void setDualScreenLeft(){
		dualScreenLeft = true;
		
		fullScreen = false;
		smallScreen = false;
		mediumScreen = false;
		dualScreenRight = false;
		setScreenSize();
	}

	/**
	 * sets the screen in dual mode right oriented. This allows for working in two different programs
	 */
	public static void setDualScreenRight(){
		dualScreenRight = true;
		
		fullScreen = false;
		smallScreen = false;
		mediumScreen = false;
		dualScreenLeft = false;
		setScreenSize();
	}
}