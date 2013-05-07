package xmlUtilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;

/**
 * Creates a new version of the postal number file. This new version only contains postal numbers, that can actually be found in the edges file.
 *
 */
public class RefinedPOSTALNUMBERTxtCreator {

	private static String postalFileName = "XML/postalNumbersAndCityNames_unedited.txt";

	private static void createRefinedTXT()
	{
		HashSet<Integer> existingPostalNumbers = getExistingPostalNumbers();
		writeNewTXT(existingPostalNumbers);
	}


	private static HashSet<Integer> getExistingPostalNumbers()
	{

		Edge[] allEdgesSet = DataHolding.getEdgeArray();
		HashSet<Integer> existingPostalNumbers = new HashSet<Integer>();

		for(Edge edge : allEdgesSet)
		{
			existingPostalNumbers.add(edge.getPostalNumberLeft());
			existingPostalNumbers.add(edge.getPostalNumberRight());
		}

		return existingPostalNumbers;
	}

	private static void writeNewTXT(HashSet<Integer> existingPostalNumbers)
	{
		try {
			File file = new File(postalFileName);

			BufferedReader reader = new BufferedReader(new FileReader(file));
			BufferedWriter writer = new BufferedWriter(new FileWriter(postalFileName + "_refined.txt"));

			//Skips the first line of the file
			reader.readLine();

			String line;

			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split("\\s+");
				int postalNumber =  Integer.parseInt(lineParts[0]);

				if(existingPostalNumbers.contains(postalNumber)){
					writer.write(line);
					writer.newLine();
				}
			}	    	

			reader.close();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main( String[] args ) {
		createRefinedTXT();
	}
}
