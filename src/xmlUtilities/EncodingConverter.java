package xmlUtilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.HashSet;

import mapCreationAndFunctions.data.Edge;

public class EncodingConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new EncodingConverter();
	}

	public EncodingConverter() {
		UTFsucks();
	}
	
	private static void UTFsucks()
	{
		try {				
			HashSet<Edge> edgeSet = new HashSet<Edge>();
			
			BufferedReader reader = new BufferedReader(new FileReader("road_names.txt"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream("roadNamesUTF.txt"), "UTF-8"));
			
			
			StringWriter line;
			line.
			while((line = reader.readLine()) != null)
			{
				line.
				writer.write(line);
				writer.newLine();
			}
		
			writer.close();
			reader.close();			

			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
