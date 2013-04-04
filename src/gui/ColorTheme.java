package gui;

import java.awt.Color;
/**
 * This is class handles our gui's color theme. Only one
 * theme is enabled at any time.
 */
public class ColorTheme {
	//The four colors for our program, set by default.
	public static Color DARK_COLOR = new Color(210,180,140);

	public static  Color BACKGROUND_COLOR = new Color(240, 230, 140);

	public static  Color BUTTON_CLICKED_COLOR = new Color(189, 183, 107);
	
	public static  Color TEXT_COLOR = new Color(139, 69, 19);
	//What ever boolean is true, is the theme the progams starts in
	public static boolean summerTheme = false;

	public static boolean winterTheme = false;
	
	public static boolean springTheme = false;
	
	public static boolean autumnTheme = true;
	/**
	 * Sets the color theme to our SUMMERtheme
	 */
	public static void setSummerTheme(){
		summerTheme = true;
				
		winterTheme = false;
		springTheme = false;
		autumnTheme = false;
		setTheme();
	}
	/**
	 * Sets the color theme to our WINTERtheme
	 */
	public static void setWinterTheme(){
		winterTheme = true;
		
		summerTheme = false;
		springTheme = false;
		autumnTheme = false;
		setTheme();
	}
	/**
	 * Sets the color theme to our SPRINGtheme
	 */
	public static void setSpringTheme(){
		springTheme = true;
		
		summerTheme = false;
		winterTheme = false;
		autumnTheme = false;
		setTheme();
	}
	/**
	 * Sets the color theme to our AUTUMNtheme
	 */
	public static void setAutumnTheme(){
		autumnTheme = true;
		
		summerTheme = false;
		winterTheme = false;
		springTheme = false;
		setTheme();
	}
	/**
	 * Changes the theme of the gui to the theme boolean that is true
	 */
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
	/**
	 * Sets the colors of theme
	 * @param darkColor changes the DARK_COLOR field
	 * @param background changes the BACKGROUND_COLOR field
	 * @param buttonClicked changes the BUTTON_CLICKED_COLOR field
	 * @param textColor changes the TEXT_COLOR field
	 */
	public static void setColors(Color darkColor, Color background, Color buttonClicked, Color textColor){
		DARK_COLOR = darkColor;
		BACKGROUND_COLOR = background;
		BUTTON_CLICKED_COLOR = buttonClicked;
		TEXT_COLOR = textColor;
	}

}
