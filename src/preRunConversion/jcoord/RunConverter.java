package preRunConversion.jcoord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import mapDrawer.AreaToDraw;

public class RunConverter {

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


			if(UTMCoords.getEasting() > AreaToDraw.getSmallestXOfEntireMap() && UTMCoords.getEasting() < AreaToDraw.getLargestXOfEntireMap() && 
					UTMCoords.getNorthing() > AreaToDraw.getSmallestYOfEntireMap() && UTMCoords.getNorthing() < AreaToDraw.getLargestYOfEntireMap())
				list.add(UTMCoords.getEasting() + " " + UTMCoords.getNorthing()); //arr[lineNumber++] = lineX + " " + lineY;
		}

		reader.close();

		return list;
	}

	private static ArrayList<String> convertFileWithBiggerThanDelimiters(String filenameAndLocation) throws NumberFormatException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filenameAndLocation));

		String line = "";
		ArrayList<String> list = new ArrayList<String>();

		while((line = reader.readLine()) != null)
		{
			line = line.trim();
			String[] lineParts = line.split("\\s+");
			
			//If there is more than one substring in the line
			if(lineParts.length > 1)
			{
				LatLng latLngCoords = new LatLng(Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[0]));

				UTMRef UTMCoords = latLngCoords.toUTMRef();

			if(UTMCoords.getEasting() > AreaToDraw.getSmallestXOfEntireMap() && UTMCoords.getEasting() < AreaToDraw.getLargestXOfEntireMap() && 
					UTMCoords.getNorthing() > AreaToDraw.getSmallestYOfEntireMap() && UTMCoords.getNorthing() < AreaToDraw.getLargestYOfEntireMap())
				list.add(UTMCoords.getEasting() + " " + UTMCoords.getNorthing()); //arr[lineNumber++] = lineX + " " + lineY;
			}
			else {
				list.add(line);
			}
		}

		reader.close();

		return list;
	}

	public static void main(String[] args) throws NumberFormatException, IOException
	{

		//String file = "resources/denmark_coastline_fullres_shore.xyz";
		//String file = "resources/osm_modified.txt";
		//ArrayList<String> list = convertFileWithSpaceAsTheOnlyDelimiter(file);

		String file = "resources/coasts_polygon.txt";
		ArrayList<String> list = convertFileWithBiggerThanDelimiters(file);


		for(String string : list)
			System.out.println(string);

		FileOutputStream m_fos = new FileOutputStream(file + "_convertedJCOORD.txt");
		Writer out = new OutputStreamWriter(m_fos, "UTF-8");
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
