package preRunConversion.jcoord;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import mapDrawer.AreaToDraw;
import preRunConversion.LonLatToUTMCoordinateConversion;

public class RunConverter {
	
	public static void main(String[] args)
	{
		 try {
		    	//String file = "resources/denmark_coastline_fullres_shore.xyz";
			 	String file = "resources/osm_view.txt";
				BufferedReader reader = new BufferedReader(new FileReader(file));
				
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
				reader.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		      
		      
	}

}
