package mapDrawer.dataSupplying;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import routing.EdgeWeightedDigraph;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

import mapDrawer.RoadType;
import mapDrawer.drawing.Edge;


public class DataHolding {

	private static int numberOfNodes = 675902;
	private static int numberOfEdges = 812301;

	/*A HashMap of the coordinates of all nodes in the entire map - the node's ID is the key
	This is made at startup so the program can access it at will. */
	private static Node[] nodeArray;
	/*A set of all edges. When at 100% zoom all edges from this are drawn. When closer less are drawn.
	This is made at startup so the program can access it at will. */
	private static Edge[] allEdgesArray;

	private static EdgeWeightedDigraph graph = new EdgeWeightedDigraph(numberOfNodes);

	//At some point, the nodeArray and QuadTree should be created in parallel, as they use the same resource-file in the same way - will decrease startup time
	private static QuadTree qTree;

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
				roadName = lineParts[5]; 
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
			System.out.println(endTime-startTime);
			reader.close();			
			return edgeArray;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}


	private static Node[] makeNodeArrayFromTXT()
	{
		try {				
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
					int referencesConverted = Integer.parseInt(references[i]);
					edgeIDs[i] = referencesConverted;

					/* KAN IKKE LADE SIG G�RE, DA EDGEARRAYET ENDNU IKKE ER OPRETTET HER
					if(getEdge(referencesConverted).getOneWay().equals("ft") || getEdge(referencesConverted).getOneWay().equals("")) {
						graph.addEdge(Integer.parseInt(lineParts[0]), Integer.parseInt(references[i]));
						}			
					 */		

				}

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
		return allEdgesArray;
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
	{ return allEdgesArray[edgeID-1]; }

	private static void initializeNodeArray()
	{
		if(nodeArray == null)
			nodeArray = makeNodeArrayFromTXT();
		else
			return;
	}

	public static void initializeNodeArray(HashMap<Integer, Double[]> nodeArray ) 
	{
		if(nodeArray == null)
			nodeArray = nodeArray;
		else
			return;
	}

	private static void initializeEdgeArray()
	{
		if(allEdgesArray == null)
			allEdgesArray = makeEdgeArrayFromTXT();
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