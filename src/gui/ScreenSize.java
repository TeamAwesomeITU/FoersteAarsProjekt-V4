package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenSize {
	public static int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			
	public static Dimension screenSize;
	
	public static boolean fullScreen = true;
	public static boolean mediumScreen = false;
	public static boolean smallScreen = false;
	public static boolean dualScreen = false;
	
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
		if(dualScreen){
			screenWidth = (int)(screenWidth*0.50);
			screenSize = new Dimension(screenWidth, screenHeight);
		}
	}
	
	public static void setFullScreen(){
		fullScreen = true;
		
		mediumScreen = false;
		smallScreen = false;
		dualScreen = false;
		setScreenSize();
	}
	
	public static void setMediumScreen(){
		mediumScreen = true;
		
		fullScreen = false;
		smallScreen = false;
		dualScreen = false;
		setScreenSize();
	}
	
	public static void setSmallScreen(){
		smallScreen = true;
		
		fullScreen = false;
		mediumScreen = false;
		dualScreen = false;
		setScreenSize();
	}
	
	public static void setDualScreen(){
		dualScreen = true;
		
		fullScreen = false;
		smallScreen = false;
		mediumScreen = false;
		setScreenSize();
	}
}