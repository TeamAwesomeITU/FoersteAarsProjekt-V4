package gui;

import java.awt.Color;

public class ColorTheme {
	public static Color DARK_COLOR = new Color(153, 101, 21);

	public static  Color BACKGROUND_COLOR = new Color(212, 175, 55);

	public static  Color BUTTON_CLICKED_COLOR = new Color(230, 190, 138);
	
	public static  Color TEXT_COLOR = new Color(0, 0, 0);
	
	public static boolean summerTheme = true;

	public static boolean winterTheme = false;
	
	public static boolean springTheme = false;
	
	public static boolean autumnTheme = false;
	
	
	public ColorTheme(){
	}
	
	public static void setSummerTheme(){
		summerTheme = true;
				
		winterTheme = false;
		springTheme = false;
		autumnTheme = false;
		setTheme();
	}
	
	public static void setWinterTheme(){
		winterTheme = true;
		
		summerTheme = false;
		springTheme = false;
		autumnTheme = false;
		setTheme();
	}
	
	public static void setSpringTheme(){
		springTheme = true;
		
		summerTheme = false;
		winterTheme = false;
		autumnTheme = false;
		setTheme();
	}
	
	public static void setautumnTheme(){
		autumnTheme = true;
		
		summerTheme = false;
		winterTheme = false;
		springTheme = false;
		setTheme();
	}
	
	public static void setTheme(){
		if(summerTheme){
			Color darkRed = new Color(139, 0, 0);
			Color tomato = new Color(255, 99, 91);
			Color lightSalmon = new Color(255, 160, 122);
			Color maroon = new Color(128, 0, 0);
			setColors(darkRed, tomato, lightSalmon, maroon);
		}
		if(winterTheme){
			Color darkBlue = new Color(0, 0, 139);
			Color cornflowerBlue = new Color(100, 149, 237);
			Color lightBlue = new Color(173, 216, 230);
			Color skyBlue = new Color(135, 206, 235);
			setColors(darkBlue, cornflowerBlue, lightBlue, skyBlue);
		}
		if(springTheme){
			Color darkGreen = new Color(0, 100, 0);
			Color lawnGreen = new Color(124, 252, 0);
			Color lightGreen = new Color(144, 238, 144);
			Color white = new Color(255, 255, 255);
			setColors(darkGreen, lawnGreen, lightGreen, white);
		}
		if(autumnTheme){
			Color goldenBrown = new Color(153, 101, 21);
			Color metallicGold = new Color(212, 175, 55);
			Color paleGold = new Color(230, 190, 138);
			Color black = new Color(0, 0, 0);
			setColors(goldenBrown, metallicGold, paleGold, black);
		}
	}
	
	public static void setColors(Color darkColor, Color background, Color buttonClicked, Color textColor){
		DARK_COLOR = darkColor;
		BACKGROUND_COLOR = background;
		BUTTON_CLICKED_COLOR = buttonClicked;
		TEXT_COLOR = textColor;
	}

}
