package xmlUtilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class RefinedNODEUNLOADTxtCreator {

	private static String nodeFileName = "XML/kdv_node_unload.txt";
	private static String edgeFileName = "XML/kdv_unload.txt";

	private static void createRefinedTXT()
	{
		//THE NUMBER OF NODES
		int numberOfNodes = 675902;

		ArrayList<HashSet<Integer>> hashSetsList = readEdgeReferences(numberOfNodes);
		writeEdgeRefsToTXT(hashSetsList);
	}


	private static ArrayList<HashSet<Integer>> readEdgeReferences(int numberOfNodes)
	{
		ArrayList<HashSet<Integer>> hashSetsList = new ArrayList<HashSet<Integer>>(numberOfNodes);
		
		for (int i = 0; i < numberOfNodes; i++) {
			hashSetsList.add(i, new HashSet<Integer>());
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
				hashSetsList.get(nodeFrom-1).add(edgeID);
				hashSetsList.get(nodeTo-1).add(edgeID);
			}
			
			System.out.println("Node 1 has this road: " + hashSetsList.get(0).iterator().next());

			reader.close();			
			return hashSetsList;

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}
	}
	
	private static void writeEdgeRefsToTXT(ArrayList<HashSet<Integer>> hashSetsList)
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
				
				Iterator<Integer> iterator = hashSetsList.get(nodeID-1).iterator();
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
