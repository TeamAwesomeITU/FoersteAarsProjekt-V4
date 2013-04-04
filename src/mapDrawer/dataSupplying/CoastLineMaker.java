package mapDrawer.dataSupplying;

import java.awt.geom.GeneralPath;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import mapDrawer.AreaToDraw;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.NegativeAreaSizeException;

/**
 * Creates GeneralPaths to draw the coast line of Denmark
 */
public class CoastLineMaker {

	//A HashSet of all of the coast line polygons in Denmark
	private static HashSet<Polygon> polygonSet = createPolygonSet("resources/coasts_polygon.txt_convertedJCOORD.txt");

	/**
	 * Generates an array of GeneralPaths which represents the coast line of Denmark within the specified AreaToDraw.
	 * 
	 * @param 	canvasWidth The width of the drawing area
	 * @param 	canvasHeight The height of the drawing area
	 * @param	area The area to draw
	 * @return	An array of the GeneralPaths that lies within the specified AreaToDraw
	 */
	public static GeneralPath[] getCoastLineToDraw(double canvasWidth, double canvasHeight, AreaToDraw area)
	{
		HashSet<Polygon> relevantPolygonSet = findWhichPolygonsToDraw(area);
		return createGeneralPaths(canvasWidth, canvasHeight, area, relevantPolygonSet);
	}	

	/**
	 * Locates the Polygons that intersects with the specified AreaToDraw
	 * @param	area The area to search for polygons to draw
	 * @return 	A HashSet of the found Polygons
	 */
	private static HashSet<Polygon> findWhichPolygonsToDraw(AreaToDraw area)
	{
		HashSet<Polygon> relevantPolygonSet = new HashSet<Polygon>();

		for(Polygon polygon : polygonSet)
		{
			//If the polygons area intersects with the parsed area
			if(polygon.getAreaToDraw().isAreaIntersectingWithArea(area))
				relevantPolygonSet.add(polygon);				
		}		
		return relevantPolygonSet;
	}	

	/**
	 * Creates GenerealPaths from the found Polygons
	 * @param 	canvasWidth The width of the drawing area
	 * @param 	canvasHeight The height of the drawing area
	 * @param	area The area to draw
	 * @param	relevantPolygonSet A HashSet of the Polygons to make GeneralPaths from
	 * @return	A GeneralPath[] of the found Polygons
	 */
	private static GeneralPath[] createGeneralPaths(double canvasWidth, double canvasHeight, AreaToDraw area, HashSet<Polygon> relevantPolygonSet)
	{
		GeneralPath[] polygonsArray = new GeneralPath[relevantPolygonSet.size()];
		CoordinateConverter coordConverter = new CoordinateConverter(canvasWidth, canvasHeight, area);

		int indexToInsertPolygon = 0;

		for(Polygon polygon : relevantPolygonSet)
		{
			GeneralPath path = new GeneralPath();
			LinkedList<double[]> polygonCoordList = polygon.getCoordsList();			

			for(double[] coords : polygonCoordList)	
			{
				double xCoord = coordConverter.UTMToPixelCoordX(coords[0]);				
				double yCoord = coordConverter.UTMToPixelCoordY(coords[1]);				
				path.moveTo(xCoord, yCoord);
			}
			
			path.closePath();
			polygonsArray[indexToInsertPolygon++] = path;
		}

		return polygonsArray;
	}


	/**
	 * Creates a HashSet of Polygons from the specified .txt-file
	 * @param 	filenameAndLocation The .txt-file from which to create the HashSet of Polygons from
	 * @return	A HashSet of every Polygon found in the specified .txt-file
	 */
	private static HashSet<Polygon> createPolygonSet(String filenameAndLocation)
	{
		HashSet<Polygon> setOfPolygons = new HashSet<Polygon>();
		LinkedList<double[]> singlePolygonCoords = new LinkedList<double[]>();


		try {
			long startTime = System.currentTimeMillis();
			BufferedReader reader = new BufferedReader(new FileReader(filenameAndLocation));

			String line = "";
			while((line = reader.readLine()) != null)
			{
				line = line.trim();
				String[] lineParts = line.split("\\s+");

				//If it is either the first line of the file, or it is time to make a new polygon
				if(lineParts.length == 1)
				{
					//If it is time to make a new polygon
					if(line.equals(">") && !singlePolygonCoords.isEmpty())
					{
						@SuppressWarnings("unchecked")
						LinkedList<double[]> polygonCoordsToSave = (LinkedList<double[]>) singlePolygonCoords.clone();
						setOfPolygons.add(new Polygon(polygonCoordsToSave));
						singlePolygonCoords.clear();
					}
				}

				//If it is not anything else, it is coordinates
				else
				{				 
					double xCoord = Double.parseDouble(lineParts[0]);
					double yCoord = Double.parseDouble(lineParts[1]);
					if(xCoord > AreaToDraw.getSmallestXOfEntireMap() && xCoord < AreaToDraw.getLargestXOfEntireMap() &&
							yCoord > AreaToDraw.getSmallestYOfEntireMap() && yCoord < AreaToDraw.getLargestYOfEntireMap())					
						singlePolygonCoords.add(new double[]{xCoord, yCoord});				

				}
			}

			reader.close();

			long endTime = System.currentTimeMillis();
			System.out.println("At create polygonSet tager " + (endTime - startTime) + " milliseconds");

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}

		return setOfPolygons;
	}

	/**
	 * Helper class - represents a Polygon.
	 */
	private static class Polygon {

		//The "box" that marks the boundaries of the Polygon
		private AreaToDraw area;

		//The list of Nodes, which represents the outline of the Polygon
		private LinkedList<double[]> nodeList;

		private Polygon(LinkedList<double[]> nodeList)
		{
			this.nodeList = nodeList;
			area = calculateAreaMinMax();
		}

		private AreaToDraw calculateAreaMinMax()
		{
			double xMin = AreaToDraw.getLargestXOfEntireMap(); double xMax = AreaToDraw.getSmallestXOfEntireMap();
			double yMin = AreaToDraw.getLargestYOfEntireMap(); double yMax = AreaToDraw.getSmallestYOfEntireMap();

			for(double[] coords : nodeList)
			{
				if(coords[0] < xMin)
					xMin = coords[0];
				if(coords[0] > xMax)
					xMax = coords[0];
				if(coords[1] < yMin)
					yMin = coords[1];
				if(coords[1] > yMax)
					yMax = coords[1];
			}				

			try {
				return new AreaToDraw(xMin, xMax, yMin, yMax);
			} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException e) {
				e.printStackTrace();		
				return null;
			}	
		}

		public AreaToDraw getAreaToDraw()
		{ return area; }		

		public LinkedList<double[]> getCoordsList()
		{ return nodeList; }
	}


	public static void main(String[] args)
	{
		System.out.println("polygonSetSize: " + polygonSet.size());

		long startTime = System.currentTimeMillis();
		getCoastLineToDraw(200, 200, new AreaToDraw())[0].getBounds();
		long endTime = System.currentTimeMillis();
		System.out.println("Retrieving polygons to draw for the entire map of Denmark takes " + (endTime - startTime) + " milliseconds");

		long startTime1 = System.currentTimeMillis();
		getCoastLineToDraw(200, 200, new AreaToDraw())[0].getBounds();
		long endTime1 = System.currentTimeMillis();
		System.out.println("Retrieving polygons to draw for the entire map of Denmark takes " + (endTime1 - startTime1) + " milliseconds");		
		
		
	}
}