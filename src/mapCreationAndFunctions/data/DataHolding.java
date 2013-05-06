package mapCreationAndFunctions.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import navigation.EdgeWeightedDigraph;




public class DataHolding {

	private static int numberOfNodes = 675902;
	private static int numberOfEdges = 812301;

	/*A HashMap of the coordinates of all nodes in the entire map - the node's ID is the key
	This is made at startup so the program can access it at will. */
	private static Node[] nodeArray;
	/*A set of all edges. When at 100% zoom all edges from this are drawn. When closer less are drawn.
	This is made at startup so the program can access it at will. */
	private static Edge[] allEdgesByIDArray;
	
	private static Edge[] edgesByName;

	private static EdgeWeightedDigraph graph = new EdgeWeightedDigraph(numberOfNodes);

	//At some point, the nodeArray and QuadTree should be created in parallel, as they use the same resource-file in the same way - will decrease startup time
	private static QuadTree qTree;

	/**
	 * This method creates an array of Edge-objects. Edge ID's equals the index of the array. 
	 * @return
	 */
	private static Edge[] makeEdgeArrayFromTXT()
	{
		try {				
			Edge[] edgeArray = new Edge[numberOfEdges];
			File file = new File("XML/edge.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));

			//To skip the first line
			reader.readLine();

			String line;
			
			int fromNode, toNode, edgeID, roadType, fromLeftNumber, toLeftNumber, fromRightNumber, toRightNumber, 
			postalNumberLeft, postalNumberRight, highWayTurnoff, fromTurn, toTurn, tjekID;
			double length, driveTime;
			String roadName, oneWay, fromLeftLetter, toLeftLetter, fromRightLetter, toRightLetter;


			long startTime = System.currentTimeMillis();
			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split(("\\;"));
				
				fromNode = Integer.parseInt(lineParts[0]);
				toNode = Integer.parseInt(lineParts[1]);
				length = Double.parseDouble(lineParts[2]);
				edgeID = Integer.parseInt(lineParts[3]);
				roadType = Integer.parseInt(lineParts[4]);
				//If the roadName is 0, make it an empty string instead
				roadName = (lineParts[5].equals("0") ? "" : lineParts[5]);
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
				
				edgeArray[edgeID-1] = new Edge(fromNode, toNode, length, edgeID, roadType, roadName, fromLeftNumber, toLeftNumber,
						fromRightNumber, toRightNumber, fromLeftLetter, toLeftLetter, fromRightLetter, toRightLetter,
						postalNumberLeft, postalNumberRight, highWayTurnoff, driveTime, oneWay, tjekID, fromTurn, toTurn);
				
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Creation of EdgeArray took " + (endTime-startTime));
	
			reader.close();		
			System.out.println(edgeArray.length);
			return edgeArray;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}


	private static Node[] makeNodeArrayFromTXT()
	{
		try {
			long startTime = System.currentTimeMillis();
			Node[] nodeArray = new Node[numberOfNodes];
			File file = new File("XML/kdv_node_unload.txt_modified.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));

			//To skip the first line
			reader.readLine();
			
			String line;
			String[] lineParts = null;
			Integer KDV = 0; 
			String[] references = null;
			
			while((line = reader.readLine()) != null)
			{
				lineParts = line.split("\\,");
				KDV = Integer.parseInt(lineParts[0]);


				references = lineParts[3].split("\\s");
				int[] edgeIDs = new int[references.length];

				for(int i = 0; i < references.length; i++){				
					edgeIDs[i] = Integer.parseInt(references[i]);
				}

				nodeArray[KDV-1] = new Node(KDV, Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[2]), edgeIDs);
			}
	
			reader.close();		
			long endTime = System.currentTimeMillis();
			System.out.println("Creation of nodeArray took " +(endTime-startTime));
			return nodeArray;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	/**
	 * Gets a HashMap of all of the entire map's Nodes - the Node ID is the key, the value is the Node
	 * @return A HashMap of all of the maps Nodes and their coordinates - the Node ID is the key, the values are x-/y- coordinates
	 */
	public static Node[] getNodeArray()
	{
		initializeNodeArray();
		return nodeArray;
	}

	/**
	 * Gets a HashSet of all of the entire map's Edges
	 * @return A HashSet of all of the maps Edges 
	 */
	public static Edge[] getEdgeArray()
	{
		initializeEdgeArray();
		return allEdgesByIDArray;
	}

	/**
	 * 
	 * @param nodeID The ID of the Node
	 * @return The Node with the input ID
	 */
	public static Node getNode(int nodeID)
	{ return nodeArray[nodeID-1]; }

	/**
	 * 
	 * @param edgeID The ID of the Edge
	 * @return The Edge with the input ID
	 */
	public static Edge getEdge(int edgeID)
	{ return allEdgesByIDArray[edgeID-1]; }

	private static void initializeNodeArray()
	{
		if(nodeArray == null)
			nodeArray = makeNodeArrayFromTXT();
		else
			return;
	}

	private static void initializeEdgeArray()
	{
		if(allEdgesByIDArray == null)
			allEdgesByIDArray = makeEdgeArrayFromTXT();
		else
			return;
	}


	public static class EdgeMapCreation implements Runnable
	{
		@Override
		public void run()
		{
			initializeEdgeArray();
		}		
	}

	public static class NodeMapCreation implements Runnable
	{
		@Override
		public void run()
		{
			initializeNodeArray();
		}
	}

}
