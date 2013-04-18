package mapDrawer.dataSupplying;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import java_cup.internal_error;

import javax.sound.sampled.Line;

import mapDrawer.AreaToDraw;
import mapDrawer.ZoomLevel;
import mapDrawer.drawing.Edge;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

/**
 * Locates the Edges that has a least one point that lies within the AreaToDraw
 */
public class FindRelevantEdges {


	/*A HashMap of the coordinates of all nodes in the entire map - the node's ID is the key
	This is made at startup so the program can access it at will. */
	private static HashMap<Integer, Double[]> nodeCoordinatesMap;
	/*A set of all edges. When at 100% zoom all edges from this are drawn. When closer less are drawn.
	This is made at startup so the program can access it at will. */
	private static HashSet<Edge> allEdgesSet;

	/**
	 * Finds all Edges, which are connected to a node in the specified AreaToDraw
	 * @return A HashSet of all Edges, which are connected to a node in the specified AreaToDraw
	 */
	public static HashSet<Edge> findEdgesToDraw(AreaToDraw area)
	{
		long startTime = System.currentTimeMillis();
		HashSet<Integer> nodeSet = findNodes(area);
		long endTime = System.currentTimeMillis();
		System.out.println("Retrieving nodeIDs from area took " + (endTime - startTime) + " milliseconds");
		return findEdges(area, nodeSet);
	}

	/**
	 * This method parses the XML-file kdv_unload containing edge information. It uses an autoPilot when moving
	 * the cursor from "node" to "node" and then manually navigates through the child-elements in the while-loop
	 * After manual navigation it returns to its parent and the autoPilot moves to the next "node". It is only called
	 * once at startup. See field: allEdgesSet. 
	 * @return A HashSet containing all Edges as Edge-objects. 
	 */
	private static HashSet<Edge> makeEdgeSetFromXML()
	{		
		long startTime = System.currentTimeMillis();		
		
		HashSet<Edge> edgeSet = new HashSet<Edge>();
			
		try {	
			VTDGen vgEdge = new VTDGen();
			if(vgEdge.parseFile("XML/kdv_unload_new.xml", false)) {

				VTDNav vnEdge = vgEdge.getNav();
				AutoPilot apEdge = new AutoPilot(vnEdge);
				apEdge.selectXPath("//roadSegmentCollection/roadSegment");							
				int FNODE = 0; int TNODE = 0; 
				int TYP = 0;   String ROAD = ""; 
				int POST = 0;
				while((apEdge.evalXPath())!=-1)
				{
					vnEdge.toElement(VTDNav.FIRST_CHILD, "FNODE");
					FNODE = vnEdge.parseInt(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "TNODE");
					TNODE = vnEdge.parseInt(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "TYP");
					TYP = vnEdge.parseInt(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "VEJNAVN");
					ROAD = vnEdge.toString(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "H_POSTNR");
					POST = vnEdge.parseInt(vnEdge.getText());

					edgeSet.add(new Edge(FNODE,TNODE,TYP,ROAD,POST));

					vnEdge.toElement(VTDNav.PARENT); 
				} 
				apEdge.resetXPath();
			}
		}
		catch (VTDException e){
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Creating HashSet of all Edges takes " + (endTime - startTime) + " milliseconds");
		return edgeSet;
	}
	
	private static HashSet<Edge> makeEdgeSetFromTXT()
	{
		try {				
			HashSet<Edge> edgeSet = new HashSet<Edge>();
			
			File file = new File("XML/kdv_unload.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			//To skip the first line
			reader.readLine();
			
			String line;
			
			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split(("(?<=[']|\\d|\\*)[,](?=[']|\\d|\\*)"));
				int fromNode = Integer.parseInt(lineParts[0]);
				int toNode = Integer.parseInt(lineParts[1]);
				int type = Integer.parseInt(lineParts[5]);
				String roadName = lineParts[6];
				int postalNumber = Integer.parseInt(lineParts[17]);
				
				edgeSet.add(new Edge(fromNode, toNode, type, roadName, postalNumber));
			}
				
			reader.close();			
			return edgeSet;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}
		
	/**
	 * This method retrieves the currently viewed area's nodes. 
	 * @param area The current area the user are viewing on the map.
	 * @return A HashSet of all of the found Nodes ID's. 
	 */
	private static HashSet<Integer> findNodes(AreaToDraw area)
	{
		return QuadTree.searchAreaForNodeIDs(area);
	}
	/**
	 * This method finds the Edges, that is found inside the input area and belongs to a Node in the nodeIDSet.
	 * @param area Current area the user are viewing on the map.
	 * @param nodeIDSet A HashSets of nodes. Supplied by findNodes which gets it from the QuadTree. 
	 * @return A HashSet of the Edges, that is found inside the input area and belongs to a Node in the nodeIDSet.
	 */
	private static HashSet<Edge> findEdges(AreaToDraw area, HashSet<Integer> nodeIDSet)
	{
		Iterator<Edge> iterator = allEdgesSet.iterator();
		HashSet<Edge> foundEdgesSet = new HashSet<Edge>();

		HashSet<Integer> zoomLevel = ZoomLevel.getlevel(area.getPercentageOfEntireMap());

		while(iterator.hasNext())
		{
			Edge edge = iterator.next();	
			if(zoomLevel.contains(edge.getRoadType()))
				if(nodeIDSet.contains(edge.getFromNode()) || nodeIDSet.contains(edge.getToNode()))
					foundEdgesSet.add(edge);
		}			

		return foundEdgesSet;
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
			
			File file = new File("XML/kdv_node_unload.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			//To skip the first line
			reader.readLine();
			
			String line;
			
			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split("\\,");
				Integer KDV = Integer.parseInt(lineParts[2]);
				Double[] coords = new Double[]{Double.parseDouble(lineParts[3]), Double.parseDouble(lineParts[4])};
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
	public static HashSet<Edge> getEdgeSet()
	{
		initializeEdgeSet();
		return allEdgesSet;
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
		
	private static void initializeEdgeSet()
	{
		if(allEdgesSet == null)
			allEdgesSet = makeEdgeSetFromXML();
		else
			return;
	}
	
	public static class EdgeSetCreation implements Runnable
	{
		@Override
		public void run()
		{
			initializeEdgeSet();
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