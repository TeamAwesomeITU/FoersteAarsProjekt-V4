package mapDrawer.dataSupplying;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import routing.EdgeWeightedDigraph;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

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
	
	private static EdgeWeightedDigraph graph = new EdgeWeightedDigraph(675902);

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
			int fromNode, toNode, type, postalNumber = 0;
			Integer edgeID = 0;
			String roadName, oneWay = "";


			long startTime = System.currentTimeMillis();
			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split(("\\;"));
				fromNode = Integer.parseInt(lineParts[0]);
				toNode = Integer.parseInt(lineParts[1]);
				edgeID = Integer.parseInt(lineParts[3]);
				type = Integer.parseInt(lineParts[4]);
				roadName = lineParts[5];
				oneWay = lineParts[18];
				postalNumber = Integer.parseInt(lineParts[14]);

				edgeArray[edgeID-1] = new Edge(fromNode, toNode, type, roadName, postalNumber, oneWay);
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

	/**
	 * This method parses the XML-file kdv_node containing nodes and their coordinates. It uses an autoPilot
	 * to navigate nodes and manually retrieves child-elements. At the end it navigates to the parent again. 
	 * @return A HashMap of nodes where the ID is node-id and value is an Array[2] of doubles 
	 * containing x-/y-coordinates. 
	 */
	@Deprecated
	private static HashMap<Integer, Double[]> makeNodeCoordinatesMapFromXML()
	{
		HashMap<Integer, Double[]> map = new HashMap<Integer, Double[]>();

		try {
			long startTime = System.currentTimeMillis();
			VTDGen vg = new VTDGen();
			AutoPilot ap = new AutoPilot(); 
			ap.selectXPath("/nodeCollection/node");
			if (vg.parseFile("XML/kdv_node_unload.xml", false))
			{
				VTDNav vn = vg.getNav();
				ap.bind(vn);
				while((ap.evalXPath())!=-1)
				{ 
					vn.toElement(VTDNav.FIRST_CHILD, "KDV");
					Integer kdv = vn.parseInt(vn.getText());

					Double[] coords = new Double[2];

					vn.toElement(VTDNav.NEXT_SIBLING, "X-COORD"); 
					coords[0] = vn.parseDouble(vn.getText());

					vn.toElement(VTDNav.NEXT_SIBLING, "Y-COORD"); 
					coords[1] = vn.parseDouble(vn.getText());
					map.put(kdv, coords);
					vn.toElement(VTDNav.PARENT); // move the cursor back
				} 
				ap.resetXPath();
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Creating HashMap of all Nodes takes " + (endTime - startTime) + " milliseconds");
		} catch (NavException | XPathEvalException | XPathParseException e) {
			e.printStackTrace();
		}
		return map;
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
					edgeIDs[i] = Integer.parseInt(references[i]);
					/*
					if(allEdgesArray.get(references[i]).getOneWay().equals("ft") || allEdgesArray.get(references[i]).getOneWay().equals("")) {
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
