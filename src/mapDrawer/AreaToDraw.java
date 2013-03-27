package mapDrawer;

import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.AreaNegativeSizeException;

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

	public AreaToDraw(double smallestX, double largestX, double smallestY, double largestY) throws AreaNegativeSizeException, AreaIsNotWithinDenmarkException
	{
		if(smallestX > largestX || smallestY > largestY || smallestX < 0 || smallestY < 0)
			throw new AreaNegativeSizeException("Area size was invalid");
		
		if(smallestX < entireMapSmallestX || smallestY < entireMapSmallestY || largestX > entireMapLargestX || largestY > entireMapLargestY)
			throw new AreaIsNotWithinDenmarkException("Some part of the area is not within the map of Denmark");

		else {
			this.smallestX = smallestX;
			this.largestX = largestX;	
			this.smallestY = smallestY;
			this.largestY = largestY;
		}
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

	public double getWidthHeightRelation()
	{ return getWidth()/getHeight(); }

	/*
	 * Calculates how much of the entire map which consists of the AreaToDraw
	 * @return How much of the entire map which consists of the AreaToDraw in percent
	 */
	public double getPercentageOfEntireMap()
	{
		System.out.println((getWidth()*getHeight())/((getWidthOfEntireMap())*(getHeightOfEntireMap()))*100);
		return (getWidth()*getHeight())/((getWidthOfEntireMap())*(getHeightOfEntireMap()))*100;
	}

	public boolean isNodeInsideArea(Node node)
	{
		return ((this.getSmallestX() <= node.getXCoord() && node.getXCoord() <= this.getLargestX()) &&
				(this.getSmallestY() <= node.getYCoord() && node.getYCoord() <= this.getLargestY()));
	}

	public boolean isAreaIntersectingWithArea(AreaToDraw area)
	{
		//Doing a "separating axis test"
		double areaMidX = (area.getWidth()/2) + area.getSmallestX();
		double areaMidY = (area.getHeight()/2) + area.getSmallestY();
		
		double thisMidX = (this.getWidth()/2) + this.getSmallestX();
		double thisMidY = (this.getHeight()/2) + this.getSmallestY();
		
		return (Math.abs(areaMidX - thisMidX) < (Math.abs(area.getWidth() + this.getWidth()) / 2) 
				&& (Math.abs(areaMidY - thisMidY) < (Math.abs(area.getHeight() + this.getHeight()) / 2)));
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

