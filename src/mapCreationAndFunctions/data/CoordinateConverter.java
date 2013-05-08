package mapCreationAndFunctions.data;

import mapCreationAndFunctions.AreaToDraw;

/**
 * This class can convert coordinates to/from UTM and Java pixels.
 */
public class CoordinateConverter {	
	
	//The width of the window to draw in
	private double canvasWidth;
	
	//The height of the window to draw in
	private double canvasHeight;
	
	//The Area to draw in
	private AreaToDraw area;
	
	/**
	 * Create a CoordinateConverter with the specified screen size and AreaToDraw.
	 * 
	 * @param canvasWidth The width of the drawing area in pixels
	 * @param canvasHeight The height of the drawing area in pixels
	 * @param area The part of the map to draw
	 */
	public CoordinateConverter(double canvasWidth, double canvasHeight, AreaToDraw area)
	{
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;		
		this.area = area;
			
	}
	
	/**
	 * Converts an X-coordinate from UTM to Java-coordinate pixels
	 * @param x The coordinate in UTM
	 * @return The converted coordinate in Java-coordinate pixels
	 */
	public double UTMToPixelCoordX(double x)
	{
		return UTMToNormalizedCoord(x, area.getSmallestX(), area.getLargestX(), canvasWidth);
	}
	
	/**
	 * Converts an Y-coordinate from UTM to Java-coordinate pixels
	 * @param y The coordinate in UTM
	 * @return The converted coordinate in Java-coordinate pixels
	 */
	public double UTMToPixelCoordY(double y)
	{
		double coord = UTMToNormalizedCoord(y, area.getSmallestY(), area.getLargestY(), canvasHeight);
		coord = reflectCoordY(coord);
		return coord;
	}
	
	/**
	 * Converts an X-coordinate from Java-coordinate pixels to UTM
	 * @param x The coordinate in Java-coordinate pixels
	 * @return The converted coordinate in UTM
	 */
	public double pixelToUTMCoordX(int x)
	{
		double coord = NormalizedToUTMCoord(x, area.getSmallestX(), area.getLargestX(), canvasWidth);
		return coord;
	}
	
	/**
	 * Converts a Y-coordinate from Java-coordinate pixels to UTM
	 * @param y The coordinate in Java-coordinate pixels
	 * @return The converted coordinate in UTM
	 */
	public double pixelToUTMCoordY(int y)
	{		
		double coord = reflectCoordY(y);
		return NormalizedToUTMCoord(coord, area.getSmallestY(), area.getLargestY(), canvasHeight);
	}
	
	/**
	 * Converts from UTM to pixel coordinates - does not take Java's reversed y-axis into account.
	 * @param coord The coordinate to convert
	 * @param axisMinimum The coordinate's axis' minimum value.
	 * @param axisMaximum The coordinate's axis' maximum value.
	 * @param screenSizeAxis The canvas' size in pixels
	 * @return The converted coordinate
	 */
	private double UTMToNormalizedCoord(double coord, double axisMinimum, double axisMaximum, double screenSizeAxis)
	{
		return ((coord-axisMinimum)*screenSizeAxis)/(axisMaximum-axisMinimum);		
	}
	
	/**
	 * Converts from pixel coordinates to UTM - does not take Java's reversed y-axis into account.
	 * @param coord The coordinate to convert
	 * @param axisMinimum The coordinate's axis' minimum value.
	 * @param axisMaximum The coordinate's axis' maximum value.
	 * @param screenSizeAxis The canvas' size in pixels
	 * @return The converted coordinate
	 */
	private double NormalizedToUTMCoord(double coord, double axisMinimum, double axisMaximum, double screenSizeAxis)
	{
		return (((coord*(axisMaximum-axisMinimum))/screenSizeAxis)+axisMinimum);
	}
	
	/**
	 * Mirrors the y-coordinates in the y-axis.  
	 * @param coord The y-coordinate to reflect
	 * @return The reflect y-coordinate
	 */
	private double reflectCoordY(double coord)
	{
		return canvasHeight - coord;
	}

}
