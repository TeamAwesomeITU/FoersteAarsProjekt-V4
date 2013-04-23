package mapDrawer.dataSupplying;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

import mapDrawer.drawing.Edge;


public class DataHolding {
	
	/*A HashMap of the coordinates of all nodes in the entire map - the node's ID is the key
	This is made at startup so the program can access it at will. */
	private static HashMap<Integer, Double[]> nodeCoordinatesMap;
	/*A set of all edges. When at 100% zoom all edges from this are drawn. When closer less are drawn.
	This is made at startup so the program can access it at will. */
	private static HashMap<Integer, Edge> allEdgesMap;
	
	//At some point, the nodeMap and QuadTree should be created in parallel, as they use the same resource-file in the same way - will decrease startup time
	private static QuadTree qTree;
	
	private static HashMap<Integer, Edge> makeEdgeMapFromTXT()
	{
		try {				
			HashMap<Integer, Edge> edgeMap = new HashMap<Integer, Edge>();
			
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

				edgeMap.put(edgeID, new Edge(fromNode, toNode, type, roadName, postalNumber, oneWay));
			}
			long endTime = System.currentTimeMillis();
			System.out.println(endTime-startTime);
			reader.close();			
			return edgeMap;
			
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
	
	private static HashMap<Integer, Double[]> makeNodeCoordinatesMapFromTXT()
	{
		try {				
			HashMap<Integer, Double[]> nodeMap = new HashMap<Integer, Double[]>();
			
			File file = new File("XML/kdv_node_unload.txt_modified.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			//To skip the first line
			reader.readLine();
			
			String line;
			
			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split("\\,");
				Integer KDV = Integer.parseInt(lineParts[0]);				
				Double[] coords = new Double[]{Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[2])};
				nodeMap.put(KDV, coords);
			}
				
			reader.close();			
			return nodeMap;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}

	/**
	 * Gets a HashMap of all of the entire map's Nodes and their coordinates - the Node ID is the key, the values are its coordinates
	 * @return A HashMap of all of the maps Nodes and their coordinates - the Node ID is the key, the values are x-/y- coordinates
	 */
	public static HashMap<Integer, Double[]> getNodeCoordinatesMap()
	{
		initializeNodeCoordinatesMap();
		return nodeCoordinatesMap;
	}
	
	/**
	 * Gets a HashSet of all of the entire map's Edges
	 * @return A HashSet of all of the maps Edges 
	 */
	public static HashMap<Integer, Edge> getEdgeMap()
	{
		initializeEdgeMap();
		return allEdgesMap;
	}
	
	private static void initializeNodeCoordinatesMap()
	{
		if(nodeCoordinatesMap == null)
			nodeCoordinatesMap = makeNodeCoordinatesMapFromTXT();
		else
			return;
	}
	
	public static void initializeNodeCoordinatesMap(HashMap<Integer, Double[]> nodeMap ) 
	{
		if(nodeCoordinatesMap == null)
			nodeCoordinatesMap = nodeMap;
		else
			return;
	}
		
	private static void initializeEdgeMap()
	{
		if(allEdgesMap == null)
			allEdgesMap = makeEdgeMapFromTXT();
		else
			return;
	}
	
	
	public static class EdgeMapCreation implements Runnable
	{
		@Override
		public void run()
		{
			initializeEdgeMap();
		}		
	}
	
	public static class NodeMapCreation implements Runnable
	{
		@Override
		public void run()
		{
			initializeNodeCoordinatesMap();
		}
	}

}
