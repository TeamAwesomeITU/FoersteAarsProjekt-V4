package mapDrawer;

import mapDrawer.dataSupplying.Node;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.InvalidAreaProportionsException;
import mapDrawer.exceptions.NegativeAreaSizeException;

/**
 * The area of the map to draw
 */
public class AreaToDraw {

	//The corner coordinates of the entire map - has been artificially been expanded in order to have some empty space around the map
	private final static double entireMapSmallestX = 438000; // 442254.35659;
	private final static double entireMapLargestX = 905000; // 892658.21706;
	private final static double entireMapSmallestY = 6047000; // 6049914.43018;
	private final static double entireMapLargestY = 6408000; // 6402050.98297;
	private final static double entireMapWidthHeightRelation = (entireMapLargestX-entireMapSmallestX)/(entireMapLargestY-entireMapSmallestY);

	//The x-coordinate of the most western coordinate
	private final double smallestX;

	//The x-coordinate of the most eastern coordinate
	private final double largestX;	

	//The y-coordinate of the most southern coordinate
	private final double smallestY;	

	//The y-coordinate of the most northern coordinate
	private final double largestY;

	/**
	 * Creates an AreaToDraw from the input parameters.
	 * @param smallestX The smallest x-coordinate of the area
	 * @param largestX The largest x-coordinate of the area
	 * @param smallestY The smallest y-coordinate of the area
	 * @param largestY The largest y-coordinate of the area
	 * @param preventDistortion Whether the area should be padded to fit the proportions of the entire map of Denmark or not - if true, the shortest side will be padded to fit the proportions.
	 * @throws NegativeAreaSizeException
	 * @throws AreaIsNotWithinDenmarkException
	 * @throws InvalidAreaProportionsException 
	 */
	public AreaToDraw(double smallestX, double largestX, double smallestY, double largestY, boolean preventDistortion) throws NegativeAreaSizeException, AreaIsNotWithinDenmarkException, InvalidAreaProportionsException
	{
		if(smallestX > largestX || smallestY > largestY || smallestX < 0 || smallestY < 0)
			throw new NegativeAreaSizeException("Area size was invalid");
		
		if(smallestX < entireMapSmallestX || smallestY < entireMapSmallestY || largestX > entireMapLargestX || largestY > entireMapLargestY)
			throw new AreaIsNotWithinDenmarkException("Some part of the area is not within the map of Denmark");

		double widthHeightRelation = (largestX-smallestX)/(largestY-smallestY);	
		double roundedwidthHeightRelation = Math.round((widthHeightRelation*100.0))/100.0;
		double roundedwidthHeightRelationEntireMap = Math.round(((getWidthHeightRelationOfEntireMap())*100.0))/100.0;

		//If it should be padded and the proportions are not correct, pad the area
		if(preventDistortion && roundedwidthHeightRelation != roundedwidthHeightRelationEntireMap)
			//if(preventDistortion)
		{
			double[] paddedCoords = padArea(smallestX, largestX, smallestY, largestY, widthHeightRelation);
			this.smallestX = paddedCoords[0];
			this.largestX = paddedCoords[1];
			this.smallestY = paddedCoords[2];
			this.largestY = paddedCoords[3];	

			System.out.println("Attempting to created padded AreaToDraw: smallestX: " + this.getSmallestX() + ", largestX: " + this.getLargestX() + ", smallestY: " + this.getSmallestY() + ", largestY: " + this.getLargestY());

			if(this.smallestX > largestX || this.smallestY > largestY || this.smallestX < 0 || this.smallestY < 0)
				throw new NegativeAreaSizeException("Area size was padded to an invalid size");			

			double newWidthHeightRelation = (this.largestX-this.smallestX)/(this.largestY-this.smallestY);	
			double newRoundedwidthHeightRelation = Math.round((newWidthHeightRelation*100.0))/100.0;
			if(newRoundedwidthHeightRelation != roundedwidthHeightRelationEntireMap)
				throw new InvalidAreaProportionsException("Area size was padded to an an area with invalid proportions: " + newRoundedwidthHeightRelation);	
		}

		//If no padding is wanted and the proportions are correct
		else {
			this.smallestX = smallestX;
			this.largestX = largestX;	
			this.smallestY = smallestY;
			this.largestY = largestY;				
		}
	}


	/**
	 * Creates an AreaToDraw consisting of the area of the entire map of Denmark.
	 */
	public AreaToDraw()
	{
		smallestX = entireMapSmallestX;
		largestX = entireMapLargestX;
		smallestY = entireMapSmallestY;
		largestY = entireMapLargestY;
	}

	/**
	 * Pads the area so it will have the same proportions, as the entire map of Denmark. Is used in order to prevent distortion, when the area is drawn.
	 * @param smallestX The smallest x-coordinate of the area
	 * @param largestX The largest x-coordinate of the area
	 * @param smallestY The smallest y-coordinate of the area
	 * @param largestY The largest y-coordinate of the area
	 * @param widthHeightRelation The relation between the width and the height of the area.
	 * @return A double[], where index 0: smallestX, index 1: largestX, index 2: smallestY and index 3: largestY
	 */
	private double[] padArea(double smallestX, double largestX, double smallestY, double largestY, double widthHeightRelation)
	{		
		
		/*
		  
		// IS THIS CORRECT?
		if(smallestX < entireMapSmallestX )
			smallestX = entireMapSmallestX;
		if(smallestY < entireMapSmallestY)
			smallestY = entireMapSmallestY;
		if(largestX > entireMapLargestX)
			largestX = entireMapLargestX;
		if(largestY > entireMapLargestY)
			largestY = entireMapLargestY;
		// IS THIS CORRECT?
		 
		 */
		
		System.out.println("Padding area!");
		double foundSmallestX = 0.0;
		double foundLargestX = 0.0;
		double foundSmallestY = 0.0;
		double foundLargestY = 0.0;

		//If the width is greater than the height
		if(widthHeightRelation > getWidthHeightRelationOfEntireMap())
		{
			System.out.println("Width is greater than height!");
			System.out.println("thisW/H-relation: " + widthHeightRelation + ", entireMapW/R: " + getWidthHeightRelationOfEntireMap());

			foundSmallestX = smallestX;
			foundLargestX = largestX;	
			//Calculates the correct height of the area, then subtracts the length, that the height already has
			double newHeight = (largestX-smallestX)/getWidthHeightRelationOfEntireMap();
			double heightToAdd = newHeight-(largestY-smallestY);

			System.out.println("newHeight is calculated to be: " + newHeight);
			System.out.println("heightToAdd is calculated to be: " + heightToAdd);

			boolean newSmallestYisInsideMap = (smallestY - heightToAdd/2) > entireMapSmallestY;
			boolean newLargestYisInsideMap = (largestY + heightToAdd/2) < entireMapLargestY;

			//If the new height does not exceed the top or bottom of the map
			if(newSmallestYisInsideMap && newLargestYisInsideMap)
			{
				System.out.println("New coordinates do NOT exceed the map!");
				foundSmallestY = smallestY - (heightToAdd/2);
				foundLargestY = largestY + (heightToAdd/2);
			}

			//If the new height DOES exceed the top or bottom of the map
			else
			{
				System.out.println("New coordinates DO exceed the map!");
				if(!newLargestYisInsideMap)
				{
					System.out.println("Largest coordinate exceed the map!");
					foundLargestY = entireMapLargestY;
					foundSmallestY = foundLargestY - newHeight;
				}

				else
				{
					System.out.println("Smallest coordinate exceed the map!");
					foundSmallestY = entireMapSmallestY;
					foundLargestY = foundSmallestY + newHeight;
				}							
			}
		}

		//If the height is greater than the width
		else
		{
			System.out.println("Height is greater than width!");
			System.out.println("thisW/H-relation: " + widthHeightRelation + ", entireMapW/R: " + getWidthHeightRelationOfEntireMap());
			foundSmallestY = smallestY;
			foundLargestY = largestY;	
			double newWidth = (largestY-smallestY)*getWidthHeightRelationOfEntireMap();
			double widthToAdd = newWidth-(largestX-smallestX);

			System.out.println("newWidth is calculated to be: " + newWidth);
			System.out.println("widthToAdd is calculated to be: " + widthToAdd);

			boolean newSmallestXisInsideMap = (smallestX - widthToAdd/2) > entireMapSmallestX;
			boolean newLargestXisInsideMap = (largestX + widthToAdd/2) < entireMapLargestX;

			//If the new width does not exceed the left side or right side of the map
			if(newSmallestXisInsideMap && newLargestXisInsideMap)
			{
				System.out.println("New coordinates do NOT exceed the map!");
				foundSmallestX = smallestX - (widthToAdd/2);
				foundLargestX = largestX + (widthToAdd/2);
			}

			//If the new width DOES exceed the top or bottom of the map
			else
			{
				System.out.println("New coordinates DO exceed the map!");
				if(!newLargestXisInsideMap)
				{
					System.out.println("Largest coordinate exceed the map!");
					foundLargestX = entireMapLargestX;
					foundSmallestX = foundLargestX - newWidth;
				}

				else
				{
					System.out.println("Smallest coordinate exceed the map!");
					foundSmallestX = entireMapSmallestX;
					foundLargestX = foundSmallestX + newWidth;
				}							

			}
		}

		//Index 0: smallestX, index 1: largestX, index 2: smallestY, index 3: largestY
		return new double[]{foundSmallestX, foundLargestX, foundSmallestY, foundLargestY};
	}

	/**
	 * Gets the width of the area in meters
	 * @return The width of the area in meters
	 */
	public double getWidth()
	{ return largestX-smallestX; }

	/**
	 * Gets the height of the area in meters
	 * @return The height of the area in meters
	 */
	public double getHeight()
	{ return largestY-smallestY; }

	/**
	 * Gets the smallest UTM coordinate on the x axis of the area.
	 * @return The smallest UTM coordinate on the x axis of the area.
	 */
	public double getSmallestX()
	{ return smallestX; }

	/**
	 * Gets the largest UTM coordinate on the x axis of the area.
	 * @return The largest UTM coordinate on the x axis of the area.
	 */
	public double getLargestX()
	{ return largestX; }

	/**
	 * Gets the smallest UTM coordinate on the y axis of the area.
	 * @return The smallest UTM coordinate on the y axis of the area.
	 */
	public double getSmallestY()
	{ return smallestY;	}

	/**
	 * Gets the largest UTM coordinate on the y axis of the area.
	 * @return The largest UTM coordinate on the y axis of the area.
	 */
	public double getLargestY()
	{ return largestY; }

	/**
	 * Gets the relation between the width and the height of the area. Calculated by (width/height).
	 * @return The relation between the width and the height of the area. Calculated by (width/height).
	 */
	public double getWidthHeightRelation()
	{ return getWidth()/getHeight(); }

	/**
	 * Calculates how much of the entire map which consists of the AreaToDraw
	 * @return How much of the entire map which consists of the AreaToDraw in percent
	 */
	public double getPercentageOfEntireMap()
	{ return (getWidth()*getHeight())/((getWidthOfEntireMap())*(getHeightOfEntireMap()))*100; }

	/**
	 * Checks whether the input Node lies within the area or not
	 * @param node The Node to check
	 * @return True if the Node lies inside the area
	 */
	public boolean isNodeInsideArea(Node node)
	{
		return ((this.getSmallestX() <= node.getXCoord() && node.getXCoord() <= this.getLargestX()) &&
				(this.getSmallestY() <= node.getYCoord() && node.getYCoord() <= this.getLargestY()));
	}

	/**
	 * Checks whether this area intersects with the input area.
	 * @param area The area to check
	 * @return True if this area intersects with the input area.
	 */
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

	/**
	 * Gets the smallest UTM coordinate on the x axis of the entire map of Denmark.
	 * @return The smallest UTM coordinate on the x axis of the entire map of Denmark.
	 */
	public static double getSmallestXOfEntireMap()
	{ return entireMapSmallestX; }

	/**
	 * Gets the largest UTM coordinate on the x axis of the entire map of Denmark.
	 * @return The largest UTM coordinate on the x axis of the entire map of Denmark.
	 */
	public static double getLargestXOfEntireMap()
	{ return entireMapLargestX; }

	/**
	 * Gets the smallest UTM coordinate on the y axis of the entire map of Denmark.
	 * @return The smallest UTM coordinate on the y axis of the entire map of Denmark.
	 */
	public static double getSmallestYOfEntireMap()
	{ return entireMapSmallestY; }

	/**
	 * Gets the largest UTM coordinate on the y axis of the entire map of Denmark.
	 * @return The largest UTM coordinate on the x axis of the entire map of Denmark.
	 */
	public static double getLargestYOfEntireMap()
	{ return entireMapLargestY; }	

	/**
	 * Gets the width of the entire map of Denmark in meters
	 * @return The width of entire map of Denmark in meters
	 */
	public static double getWidthOfEntireMap()
	{ return entireMapLargestX-entireMapSmallestX; }	

	/**
	 * Gets the height of the entire map of Denmark in meters
	 * @return The height of entire map of Denmark in meters
	 */
	public static double getHeightOfEntireMap()
	{ return entireMapLargestY-entireMapSmallestY; }	


	/**
	 * Gets the width-height relation of the entire map of Denmark
	 * @return The width-height relation of the entire map of Denmark
	 */
	public static double getWidthHeightRelationOfEntireMap()
	{ return entireMapWidthHeightRelation; }		
}

