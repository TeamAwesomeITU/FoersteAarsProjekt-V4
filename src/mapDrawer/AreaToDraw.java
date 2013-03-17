package mapDrawer;

/*
 * The area of the map to draw
 */
public class AreaToDraw {
	
	//The corner coordinates of the entire map
	private final static double entireMapSmallestX = 442254.35659;
	private final static double entireMapLargestX = 892658.21706;
	private final static double entireMapSmallestY = 6049914.43018;
	private final static double entireMapLargestY = 6402050.98297;
	
	//The x-coordinate of the most western coordinate
	private final double smallestX;
	
	//The x-coordinate of the most eastern coordinate
	private final double largestX;
	
	//The y-coordinate of the most southern coordinate
	private final double smallestY;	
	
	//The y-coordinate of the most northern coordinate
	private final double largestY;
	
	public AreaToDraw(double smallestX, double largestX, double smallestY, double largestY)
	{
		this.smallestX = smallestX;
		this.largestX = largestX;	
		this.smallestY = smallestY;
		this.largestY = largestY;
	}
	
	
	public AreaToDraw()
	{
		smallestX = entireMapSmallestX;
		largestX = entireMapLargestX;
		smallestY = entireMapSmallestY;
		largestY = entireMapLargestY;
	}
	
	public double getWidth()
	{ return largestX-smallestX; }
	
	public double getHeight()
	{ return largestY-smallestY; }
	
	public double getSmallestX()
	{ return smallestX; }
	
	public double getLargestX()
	{ return largestX; }
	
	public double getSmallestY()
	{ return smallestY;	}
	
	public double getLargestY()
	{ return largestY; }
	
	/*
	 * Calculates how much of the entire map which consists of the AreaToDraw
	 * @return How much of the entire map which consists of the AreaToDraw in percent
	 */
	public double getPercentageOfEntireMap()
	{
		return (getWidth()*getHeight())/((getWidthOfEntireMap())*(getHeightOfEntireMap()))*100;
	}
	
	public static double getSmallestXOfEntireMap()
	{ return entireMapSmallestX; }
	
	public static double getLargestXOfEntireMap()
	{ return entireMapLargestX; }
	
	public static double getSmallestYOfEntireMap()
	{ return entireMapSmallestY; }
	
	public static double getLargestYOfEntireMap()
	{ return entireMapLargestY; }	
	
	public static double getWidthOfEntireMap()
	{ return entireMapLargestX-entireMapSmallestX; }	
	
	public static double getHeightOfEntireMap()
	{ return entireMapLargestY-entireMapSmallestY; }	
	
}

