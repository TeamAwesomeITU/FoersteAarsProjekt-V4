package preRunConversion.jcoord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RunConverter {

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

	@SuppressWarnings("unused")
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
}
