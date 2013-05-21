package preRunConversion.jcoord;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * This class converts our coast line data from LonLat coordinates to UTM Zone 32, and saves the result in a new .txt file.
 *
 */
public class RunConverter {

	/**
	 * This method converts coast line data files with space as the only delimiter.
	 * @param filenameAndLocation The location of the file
	 * @return An ArrayList<String>, containing each line of the file converted into UTM Zone 32 coordinates.
	 * @throws NumberFormatException Is thrown, if a coordinate could not be recognized as a double value
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private static ArrayList<String> convertFileWithSpaceAsTheOnlyDelimiter(String filenameAndLocation) throws NumberFormatException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filenameAndLocation));

		String line = "";
		ArrayList<String> list = new ArrayList<String>();

		while((line = reader.readLine()) != null)
		{
			line = line.trim();			
			String[] lineParts = line.split("\\s+");
			LatLng latLngCoords = new LatLng(Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[0]));

			UTMRef UTMCoords = latLngCoords.toUTMRef();

			//If the coords are within the wanted map of Denmark
			//if(UTMCoords.getEasting() > AreaToDraw.getSmallestXOfEntireMap() && UTMCoords.getEasting() < AreaToDraw.getLargestXOfEntireMap() && 
			//		UTMCoords.getNorthing() > AreaToDraw.getSmallestYOfEntireMap() && UTMCoords.getNorthing() < AreaToDraw.getLargestYOfEntireMap())
				list.add(UTMCoords.getEasting() + " " + UTMCoords.getNorthing()); 
		}

		reader.close();

		return list;
	}

	/**
	 * This method converts coast line data files with ">" delimiters, which marks the start and end of each polygon in the file.
	 * @param filenameAndLocation The location of the file
	 * @return An ArrayList<String>, containing each line of the file converted into UTM Zone 32 coordinates.
	 * @throws NumberFormatException Is thrown, if a coordinate could not be recognized as a double value
	 * @throws IOException
	 */
	private static ArrayList<String> convertFileWithBiggerThanDelimiters(String filenameAndLocation) throws NumberFormatException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filenameAndLocation));

		String line = "";
		ArrayList<String> list = new ArrayList<String>();

		String previousLineAdded = "";

		while((line = reader.readLine()) != null)
		{
			line = line.trim();
			String[] lineParts = line.split("\\s+");
			System.out.println(line);			


			//If there is more than one substring in the line
			if(!line.contains(">"))
			{
				LatLng latLngCoords = new LatLng(Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[0]));

				UTMRef UTMCoords = latLngCoords.toUTMRef();

				//If the coords are within the wanted map of Denmark
				//if(UTMCoords.getEasting() > AreaToDraw.getSmallestXOfEntireMap() && UTMCoords.getEasting() < AreaToDraw.getLargestXOfEntireMap() && 
				//		UTMCoords.getNorthing() > AreaToDraw.getSmallestYOfEntireMap() && UTMCoords.getNorthing() < AreaToDraw.getLargestYOfEntireMap())
				{
					list.add(UTMCoords.getEasting() + " " + UTMCoords.getNorthing()); //arr[lineNumber++] = lineX + " " + lineY;
					previousLineAdded = line;
				}
			}
			else {
				if(!previousLineAdded.contains(">"))
					{
						list.add(line);
						previousLineAdded = line;
					}
			}
		}

		reader.close();

		return list;
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		//String file = "resources/denmark_coastline_fullres_shore_waaaaay_to_largeOfAnArea_shore.xyz";
		//String file = "resources/denmark_coastline_fullres_shore.xyz";
		//String file = "resources/osm_modified.txt";
		//ArrayList<String> list = convertFileWithSpaceAsTheOnlyDelimiter(file);		

		//String file = "resources/coasts_polygon.txt";
		String file = "resources/coast_polygon_orig.dat";
		ArrayList<String> list = convertFileWithBiggerThanDelimiters(file);


		for(String string : list)
			System.out.println(string);

		FileOutputStream fos = new FileOutputStream(file + "_convertedJCOORDWAAAAYToBig.txt");
		Writer out = new OutputStreamWriter(fos, "UTF-8");
		//To get the new line, i.e. "enter", so that a new line will be created with each String
		String newLineSeperator = System.getProperty("line.separator");

		for(String string : list)
		{
			string += newLineSeperator;
			out.write(string);
		}

		out.close();	

	}
}
