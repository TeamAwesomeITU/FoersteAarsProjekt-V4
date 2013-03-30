package mapDrawer;

import java.awt.Dimension;
import java.awt.geom.GeneralPath;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.AreaNegativeSizeException;

public class CoastLineMaker {

	private static HashSet<Polygon> polygonSet = createPolygonSet("resources/coasts_polygon.txt_convertedJCOORD.txt");

	/*
	 * If a relevant polygon's area is found to intersect the area to be drawn, the ENTIRE polygon will be drawn
	 */
	public static GeneralPath[] getCoastLineToDraw(double canvasWidth, double canvasHeight, AreaToDraw area)
	{
		HashSet<Polygon> relevantPolygonSet = findWhichPolygonsToDraw(area);
		return createGeneralPaths(canvasWidth, canvasHeight, area, relevantPolygonSet);
	}	

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
			
			polygonsArray[indexToInsertPolygon++] = path;
		}
		
		return polygonsArray;
	}


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

	/*
	 * Helper class
	 */
	private static class Polygon {

		//The "box" that marks the boundaries of the Polygon
		private AreaToDraw area;
		
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
			} catch (AreaNegativeSizeException | AreaIsNotWithinDenmarkException e) {
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
		System.out.println("something before something: " + polygonSet.iterator().next().getCoordsList().size());
		
		long startTime = System.currentTimeMillis();
		getCoastLineToDraw(200, 200, new AreaToDraw())[0].getBounds();
		long endTime = System.currentTimeMillis();
		System.out.println("At retrieve polygoner til tegning af hele Danmark tager " + (endTime - startTime) + " milliseconds");
		
		long startTime1 = System.currentTimeMillis();
		getCoastLineToDraw(200, 200, new AreaToDraw())[0].getBounds();
		long endTime1 = System.currentTimeMillis();
		System.out.println("At retrieve polygoner til tegning af hele Danmark tager " + (endTime1 - startTime1) + " milliseconds");		
	}
}
