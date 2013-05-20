package txtEditingAndConversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class creates a new data file from which to obtain our Nodes. The new data file includes all of the Edges, which each Node refers to, as ID's behind each decleration of a Node. 
 *
 */
public class RefinedNODEUNLOADTxtCreator {

	private static String nodeFileName = "resources/kdv_node_unload.txt";
	private static String edgeFileName = "resources/kdv_unload.txt";

	/**
	 * Gets the corresponding EdgeID's for each Node, and saves them in the same line as the Node.
	 */
	private static void createRefinedTXT()
	{
		//THE NUMBER OF NODES
		int numberOfNodes = 675902;

		ArrayList<ArrayList<Integer>> edgeRefsLists = readEdgeReferences(numberOfNodes);
		writeEdgeRefsToTXT(edgeRefsLists);
	}

	/**
	 * Obtains the ID's of all Edge's, that belongs to any Node, and returns them in an ArrayList.
	 * @param numberOfNodes The total number of Nodes in our data.
	 * @return an ArrayList of ArrayLists, which in turn contains all of the Node's Edge's.
	 */
	private static ArrayList<ArrayList<Integer>> readEdgeReferences(int numberOfNodes)
	{
		ArrayList<ArrayList<Integer>> edgeRefsLists = new ArrayList<ArrayList<Integer>>(numberOfNodes);

		for (int i = 0; i < numberOfNodes; i++) {
			edgeRefsLists.add(i, new ArrayList<Integer>());
		}

		try {				
			File file = new File(edgeFileName);
			BufferedReader reader = new BufferedReader(new FileReader(file));

			//To skip the first line
			reader.readLine();

			String line;

			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split("\\,");
				int nodeFrom =  Integer.parseInt(lineParts[0]);
				int nodeTo =  Integer.parseInt(lineParts[1]);
				int edgeID = Integer.parseInt(lineParts[4]);

				//-1 to compensate for the fact that an array's index starts at 0!
				edgeRefsLists.get(nodeFrom-1).add(edgeID);
				edgeRefsLists.get(nodeTo-1).add(edgeID);
			}

			System.out.println("Node 1 has this road: " + edgeRefsLists.get(0).iterator().next());

			reader.close();			
			return edgeRefsLists;

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}
	}

	/**
	 * Creates a new data file, where each of a Node's Edge's IDs are listed behind the Node.
	 * @param edgeRefsLists The collection of EdgeIDs
	 */
	private static void writeEdgeRefsToTXT(ArrayList<ArrayList<Integer>> edgeRefsLists)
	{
		try {
			File file = new File(nodeFileName);

			BufferedReader reader = new BufferedReader(new FileReader(file));
			BufferedWriter writer = new BufferedWriter(new FileWriter(nodeFileName + "_modified.txt"));

			//Skips the first line of the file
			reader.readLine();

			writer.write("MODIFIED FILE");
			writer.newLine();

			String line;

			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split("\\,");
				int nodeID = Integer.parseInt(lineParts[2]);
				//Leaves out the unnecessary information in the original file
				String stringToAdd = lineParts[2] + "," + lineParts[3] + "," + lineParts[4] + ",";

				Iterator<Integer> iterator = edgeRefsLists.get(nodeID-1).iterator();
				while(iterator.hasNext())
					stringToAdd += iterator.next() + " "; 

				writer.write(stringToAdd.trim());
				writer.newLine();
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
