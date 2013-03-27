package mapDrawer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

public class CoastLineMaker {

	private static HashSet<LinkedList<double[]>> polygonSet = createCoastLineCoordinateArray("resources/coasts_polygon.txt_convertedJCOORD.txt");

	private AreaToDraw area;

	public CoastLineMaker(AreaToDraw area)
	{
		this.area = area;
	}

	private int[] decideWhichPolygonsToDraw()
	{

	}

	private static HashSet<LinkedList<double[]>> createCoastLineCoordinateArray(String filenameAndLocation)
	{
		HashSet<LinkedList<double[]>> setOfPolygons = new HashSet<LinkedList<double[]>>();
		LinkedList<double[]> singlePolygonCoords = new LinkedList<double[]>();

		try {

			BufferedReader reader = new BufferedReader(new FileReader(filenameAndLocation));

			String line = "";
			while((line = reader.readLine()) != null)
			{
				line = line.trim();
				String[] lineParts = line.split("\\s+");
				System.out.println(lineParts.length);

				//If it is either the first line of the file, or it is time to make a new polygon
				if(lineParts.length == 1)
				{
					//If it is time to make a new polygon
					if(line.contains("<") && singlePolygonCoords.size() != 0)
					{
						setOfPolygons.add(singlePolygonCoords);
						singlePolygonCoords.clear();
					}
				}
				
				//If it is not anything else, it is coordinates
				else
				{				 
					singlePolygonCoords.add(new double[]{Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[0])});				
				}
			}

			reader.close();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(setOfPolygons.size());
		return setOfPolygons;
	}
	
	public static void main(String[] args)
	{
		System.out.println("polygonSetSize: " + polygonSet.size());
	}
	
	public class Polygon {
		
		//The "box" that marks the boundaries of the Polygon
		private AreaToDraw area;
		
		private LinkedList<double[]> nodeList;
		
		private Polygon(LinkedList<double[]> nodeList)
		{
			this.nodeList = nodeList;
			double xMin = AreaToDraw.getLargestXOfEntireMap(); double xMax; double yMin; double yMax;
			for(double[] coords : nodeList)
			{
					
			}				
				
			area = null;
		}
	}

}
