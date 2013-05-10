package gui.customJUnits;

import gui.settingsAndPopUp.ColorTheme;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;
public class ColoredScrollBarUI extends BasicScrollBarUI{
	
	/**
	 * Sets the color of our scrollbar to match the theme
	 */
	@Override
	protected void configureScrollBarColors() 
	{
	        LookAndFeel.installColors(scrollbar, "ScrollBar.background",
	                                  "ScrollBar.foreground");
	        thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
	        thumbLightShadowColor = UIManager.getColor("ScrollBar.thumbShadow");
	        thumbDarkShadowColor = UIManager.getColor("ScrollBar.thumbDarkShadow");
	        thumbColor = ColorTheme.DARK_COLOR;
	        trackColor = ColorTheme.BACKGROUND_COLOR;
	        trackHighlightColor = ColorTheme.DARK_COLOR;
	}
	
	/**
	 * Paints the thumb if it's there, and the scroll bar is enabled
	 * @param g Graphics component
	 * @param c JComponent
	 * @param thumbBounds Rectangle
	 */
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		//We've copied the source of BasicScrollBarUI and edited this method to fit our theme. This includes removing any 3d effects when pressed and such
		if(thumbBounds.isEmpty() || !scrollbar.isEnabled())     {
            return;
        }
        int w = thumbBounds.width;
        int h = thumbBounds.height;
        g.translate(thumbBounds.x, thumbBounds.y);
        g.setColor(thumbColor);
        g.fillRect(0, 0, w, h);
        g.translate(-thumbBounds.x, -thumbBounds.y);
	}
	
	/**
	 * Sets the color of the decrease arrow.
	 * @param orientation
	 */
	@Override
	protected JButton createDecreaseButton(int orientation) {
		BasicArrowButton button = new BasicArrowButton(orientation, ColorTheme.DARK_COLOR, ColorTheme.DARK_COLOR, ColorTheme.TEXT_COLOR, ColorTheme.DARK_COLOR);
	    button.setBackground(ColorTheme.DARK_COLOR);
	    button.setForeground(ColorTheme.TEXT_COLOR);
	    button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, (ColorTheme.DARK_COLOR)));
	    return button;
	}
	
	/**
	 * Sets the color of the increase arrow.
	 * The same as the decrease arrow exactly
	 * @param orientation
	 */
	protected JButton createIncreaseButton(int orientation) {
		BasicArrowButton button = new BasicArrowButton(orientation, ColorTheme.DARK_COLOR, ColorTheme.DARK_COLOR, ColorTheme.TEXT_COLOR, ColorTheme.DARK_COLOR);
	    button.setBackground(ColorTheme.DARK_COLOR);
	    button.setForeground(ColorTheme.TEXT_COLOR);
	    button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, (ColorTheme.DARK_COLOR)));
	    return button;
	}
}
