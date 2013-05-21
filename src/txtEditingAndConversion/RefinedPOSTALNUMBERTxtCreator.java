package txtEditingAndConversion;

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

	private static String postalFileName = "resources/postalNumbersAndCityNames_uneditedWithSwedish.txt";

	/**
	 * Reads the original file, filters the irrelevant postal numbers, and writes the relevant ones to a new file.
	 */
	private static void createRefinedTXT()
	{
		HashSet<Integer> existingPostalNumbers = getExistingPostalNumbers();
		writeNewTXT(existingPostalNumbers);
	}

	/**
	 * Obtains postal numbers from every Edge in our data.
	 * @return all of the postal numbers, which the Edges refers to.
	 */
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

	/**
	 * Reads the original data file and writes a new file with only the existing postal numbers.
	 * @param existingPostalNumbers
	 */
	private static void writeNewTXT(HashSet<Integer> existingPostalNumbers)
	{
		try {
			File file = new File(postalFileName);

			BufferedReader reader = new BufferedReader(new FileReader(file));
			BufferedWriter writer = new BufferedWriter(new FileWriter(postalFileName.replaceAll(".txt", "") + "_refined.txt"));

			String line;

			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split("\\s");
				lineParts[lineParts.length-1] = lineParts[lineParts.length-1].split("\\s+")[0];
				int postalNumber =  Integer.parseInt(lineParts[0]);

				//If the postal number is one of the postal numbers found in our original data file, we want it
				if(existingPostalNumbers.contains(postalNumber))
				{
					int shouldBreakAt = -1;
					for (int i = 0; i < lineParts.length; i++) {
						if(lineParts[i].contains("Skåne")){
							shouldBreakAt = i;
							break;
						}
					}
					
					String lineToWrite = "";					
					if(shouldBreakAt != -1)
					{
						
						for (int i = 0; i < shouldBreakAt; i++) {
							lineToWrite += (lineParts[i] + " ");
						}
						writer.write(lineToWrite.trim());
					}
					
					else
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
