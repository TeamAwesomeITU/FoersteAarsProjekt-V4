package gui;

import java.awt.Color;

public class ColorTheme {
	public static final Color DARK_COLOR = new Color(207, 181, 59);

	public static final Color BACKGROUND_COLOR = new Color(255, 215, 0);

	public static final Color BUTTON_CLICKED_COLOR = new Color(230, 190, 138);
	
	public static final Color TEXT_COLOR = new Color(153, 101, 21);
	
	public static boolean summerTheme = true;

	public static boolean winterTheme = false;
	
	public static boolean springTheme = false;
	
	public static boolean autumnTheme = false;
	
	
	public ColorTheme(){
	}
	
	public static void setSummerTheme(){
		summerTheme = true;
		setColors(summerTheme);
				
		winterTheme = false;
		springTheme = false;
		autumnTheme = false;
	}
	
	public static void setWinterTheme(){
		winterTheme = true;
		
		summerTheme = false;
		springTheme = false;
		autumnTheme = false;
	}
	
	public static void setSpringTheme(){
		springTheme = true;
		
		summerTheme = false;
		winterTheme = false;
		autumnTheme = false;
	}
	
	public static void setautumnTheme(){
		autumnTheme = true;
		
		summerTheme = false;
		winterTheme = false;
		springTheme = false;
	}
	
	public static void setColors(boolean colorTheme){
		if(summerTheme){
			//DARK_COLOR = new Color();
		}
		if(autumnTheme){
			
		}
	}

}
