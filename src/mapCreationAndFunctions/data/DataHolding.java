package mapCreationAndFunctions.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import navigation.EdgeWeightedDigraph;

/**
 * This class holds the primary data of the program. 
 *
 */
public class DataHolding {

	private static int numberOfNodes = 675902;
	private static int numberOfEdges = 812301;	

	private static EdgeWeightedDigraph graph = new EdgeWeightedDigraph(numberOfNodes);

	/*A HashMap of the coordinates of all nodes in the entire map - the node's ID is the key
	This is made at startup so the program can access it at will. */
	private static Node[] nodeArray = makeNodeArrayFromTXT();
	/*A set of all edges. When at 100% zoom all edges from this are drawn. When closer less are drawn.
	This is made at startup so the program can access it at will. */
	private static Edge[] allEdgesByIDArray = makeEdgeArrayFromTXT();

	/**
	 * Creates an array of Edge-objects. Edge ID's equals the index of the array. 
	 * @return An array of all Edges found in the .txt file
	 */
	private static Edge[] makeEdgeArrayFromTXT()
	{
		try {				
			Edge[] edgeArray = new Edge[numberOfEdges];
			File file = new File("resources/edge.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			//To skip the first line
			reader.readLine();

			String line;

			int fromNode, toNode, edgeID, roadType, fromLeftNumber, toLeftNumber, fromRightNumber, toRightNumber, 
			postalNumberLeft, postalNumberRight, highWayTurnoff, fromTurn, toTurn, tjekID;
			double length, driveTime;
			String roadName, oneWay, fromLeftLetter, toLeftLetter, fromRightLetter, toRightLetter;


			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split(("\\;"));

				fromNode = Integer.parseInt(lineParts[0]);
				toNode = Integer.parseInt(lineParts[1]);
				length = Double.parseDouble(lineParts[2]);
				edgeID = Integer.parseInt(lineParts[3]);
				roadType = Integer.parseInt(lineParts[4]);
				//If the roadName is 0, make it an empty string instead and ALL amounts of whitespaces between words will be replaces with a single whitespace
				roadName = (lineParts[5].equals("0") ? "" : lineParts[5].replaceAll("\\s+", " "));
				fromLeftNumber = Integer.parseInt(lineParts[6]);
				toLeftNumber = Integer.parseInt(lineParts[7]);
				fromRightNumber = Integer.parseInt(lineParts[8]);
				toRightNumber = Integer.parseInt(lineParts[9]);
				fromLeftLetter = lineParts[10];
				toLeftLetter = lineParts[11];
				fromRightLetter = lineParts[12];
				toRightLetter = lineParts[13];				

				highWayTurnoff = Integer.parseInt(lineParts[16]); 
				driveTime = Double.parseDouble(lineParts[17]);				
				oneWay = lineParts[18]; 

				//Because Integer.parseInt can't handle empty Strings, we check to see if they'r empty - if they are, set them as -1.
				postalNumberLeft = (!lineParts[14].isEmpty() ? Integer.parseInt(lineParts[14]) : -1);				
				postalNumberRight = (!lineParts[15].isEmpty() ? Integer.parseInt(lineParts[15]) : -1);				
				fromTurn = (!lineParts[19].isEmpty() ? Integer.parseInt(lineParts[19]) : -1);				
				toTurn = (!lineParts[20].isEmpty() ? Integer.parseInt(lineParts[20]) : -1);				
				tjekID = (!lineParts[21].isEmpty() ? Integer.parseInt(lineParts[21]) : -1);			

				//Adding edgeID's to the relevant cities IF it actually has a road name!
				if(!roadName.isEmpty())
				{
					City.addRoadToRelevantCity(postalNumberLeft, edgeID);
					City.addRoadToRelevantCity(postalNumberRight, edgeID);
				}

				edgeArray[edgeID-1] = new Edge(fromNode, toNode, length, edgeID, roadType, roadName, fromLeftNumber, toLeftNumber,
						fromRightNumber, toRightNumber, fromLeftLetter, toLeftLetter, fromRightLetter, toRightLetter,
						postalNumberLeft, postalNumberRight, highWayTurnoff, driveTime, oneWay, tjekID, fromTurn, toTurn);

			}

			reader.close();		
			
			return edgeArray;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}


	/**
	 * Creates an array of Node-objects. Node ID's equals the index of the array. 
	 * @return An array of all Node found in the .txt file
	 */
	private static Node[] makeNodeArrayFromTXT()
	{
		try {
			Node[] nodeArray = new Node[numberOfNodes];
			File file = new File("resources/kdv_node_unload.txt_modified.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			//To skip the first line
			reader.readLine();

			String line;
			String[] lineParts = null;
			Integer KDV = 0; 
			String[] references = null;
			int counter = 1;
			while((line = reader.readLine()) != null)
			{
				lineParts = line.split("\\,");
				KDV = Integer.parseInt(lineParts[0]);


				references = lineParts[3].split("\\s");
				int[] edgeIDs = new int[references.length];

				for(int i = 0; i < references.length; i++){	
					int currentEdgeID = Integer.parseInt(references[i]);
					edgeIDs[i] = currentEdgeID;
					graph.addEdge(counter, currentEdgeID);
				}
				counter++;

				nodeArray[KDV-1] = new Node(KDV, Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[2]), edgeIDs);
			}

			reader.close();		
			return nodeArray;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}

	/**
	 * Gets an array of all of the entire map's Nodes.
	 * @return An array of all existing Nodes.
	 */
	public static Node[] getNodeArray(){return nodeArray;}

	/**
	 * Gets an array of all of the entire map's Edges.
	 * @return An array of all existing Edges.
	 */
	public static Edge[] getEdgeArray(){return allEdgesByIDArray;}

	/**
	 * Gets the Node with the given ID
	 * @param nodeID The ID of the Node
	 * @return The Node with the input ID
	 */
	public static Node getNode(int nodeID){ return nodeArray[nodeID-1]; }

	/**
	 * Gets the Edge with the given ID
	 * @param edgeID The ID of the Edge
	 * @return The Edge with the input ID
	 */
	public static Edge getEdge(int edgeID){ return allEdgesByIDArray[edgeID-1]; }

	/**
	 * Gets the EdgeWeightedDigraph of the map
	 * @return the EdgeWeightedDigraph of the map
	 */
	public static EdgeWeightedDigraph getGraph() {
		return graph;
	}
	
	/**
	 * Gets the number of existing Edges
	 * @return the number of existing Edges
	 */
	public static int getNumberOfEdges() {
		return numberOfEdges;
	}

	/**
	 * Gets the number of existing Nodes
	 * @return the number of existing Nodes
	 */
	public static int getNumberOfNodes() {
		return numberOfNodes;
	}
}
