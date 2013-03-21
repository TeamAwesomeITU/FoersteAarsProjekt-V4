package mapDrawer;

/*
 * Must be fed width the two corner coordinates of the wanted area to draw????
 */
public class CoordinateConverter {	
	
	//The width of the window to draw in
	private double canvasWidth;
	
	//The height of the window to draw in
	private double canvasHeight;
	
	//The Area to draw in
	private AreaToDraw area;
	
	/*
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
	
	public double KrakToDrawCoordX(double x)
	{
		return UTMToNormalizedCoord(x, area.getSmallestX(), area.getLargestX(), canvasWidth);
	}
	
	public double KrakToDrawCoordY(double y)
	{
		double coord = UTMToNormalizedCoord(y, area.getSmallestY(), area.getLargestY(), canvasHeight);
		coord = reflectCoordY(coord);
		return coord;
	}
	
	public double DrawToKrakCoordX(int x)
	{
		double coord = NormalizedToUTMCoord(x, area.getSmallestX(), area.getLargestX(), canvasWidth);
		return coord;
	}
	
	/*
	 * Input: The coordinate in Java-coordinate pixels
	 */
	public double DrawToKrakCoordY(int y)
	{		
		double coord = reflectCoordY(y);
		return NormalizedToUTMCoord(coord, area.getSmallestY(), area.getLargestY(), canvasHeight);
	}
	
	private double UTMToNormalizedCoord(double coord, double axisMinimum, double axisMaximum, double screenSizeAxis)
	{
		return ((coord-axisMinimum)*screenSizeAxis)/(axisMaximum-axisMinimum);		
	}
	
	private double NormalizedToUTMCoord(double coord, double axisMinimum, double axisMaximum, double screenSizeAxis)
	{
		return (((coord*(axisMaximum-axisMinimum))/screenSizeAxis)+axisMinimum);
	}
	
	private double reflectCoordY(double coord)
	{
		return canvasHeight - coord;
	}
	
	private double getWidthHeightRelation()
	{
		return canvasWidth/canvasHeight;
	}

}
