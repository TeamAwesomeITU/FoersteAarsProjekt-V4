package gui;

import java.awt.Color;

public class ColorTheme {
	public static Color DARK_COLOR = new Color(210,180,140);

	public static  Color BACKGROUND_COLOR = new Color(240, 230, 140);

	public static  Color BUTTON_CLICKED_COLOR = new Color(189, 183, 107);
	
	public static  Color TEXT_COLOR = new Color(139, 69, 19);
	
	public static boolean summerTheme = false;

	public static boolean winterTheme = false;
	
	public static boolean springTheme = false;
	
	public static boolean autumnTheme = true;
	
	
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
	
	public static void setAutumnTheme(){
		autumnTheme = true;
		
		summerTheme = false;
		winterTheme = false;
		springTheme = false;
		setTheme();
	}
	
	public static void setTheme(){
		if(summerTheme){
			Color orangeRed = new Color(255,69,0);
			Color orange = new Color(255,208,36);
			Color darkOrange = new Color(255,140,0);
			Color white = new Color(255,255,255);
			setColors(orangeRed, orange, darkOrange, white);
		}
		else if(winterTheme){
			Color steelBlue = new Color(70,130,180);
			Color cornflowerBlue = new Color(100, 149, 237);
			Color royalBlue = new Color(65,105,225);
			Color white = new Color(255, 255, 255);
			setColors(steelBlue, cornflowerBlue, royalBlue, white);
		}
		else if(springTheme){
			Color forestGreen = new Color(34,139,34);
			Color chosenGreen = new Color(73, 216, 111);
			Color lightGreen = new Color(144, 238, 144);
			Color white = new Color(255, 255, 255);
			setColors(forestGreen, chosenGreen, lightGreen, white);
		}
		else if(autumnTheme){
			Color tan = new Color(210,180,140);
			Color khaki = new Color(240, 230, 140);
			Color paleGoldenRod = new Color(189, 183, 107);
			Color saddleBrown = new Color(139, 69, 19);
			setColors(tan, khaki, paleGoldenRod, saddleBrown);
		}
	}
	
	public static void setColors(Color darkColor, Color background, Color buttonClicked, Color textColor){
		DARK_COLOR = darkColor;
		BACKGROUND_COLOR = background;
		BUTTON_CLICKED_COLOR = buttonClicked;
		TEXT_COLOR = textColor;
	}

}
