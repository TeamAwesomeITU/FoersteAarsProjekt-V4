package mapDrawer.dataSupplying;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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

/*
 * Locates the Edges that lies within the AreaToDraw
 */
public class FindRelevantEdges {


	/*A HashMap of the coordinates of all nodes in the entire map - the node's ID is the key
	This is made at startup so the program can access it at will. */
	private static final HashMap<Integer, Double[]> nodeCoordinatesMap = makeNodeCoordinatesMap();
	/*A set of all edges. When at 100% zoom all edges from this are drawn. When closer less are drawn.
	This is made at startup so the program can access it at will. */
	private static final HashSet<Edge> allEdgesSet = makeEdgeSet();

	/**
	 * This method is mainly used to retrieve nodes from the area you are currently viewing from the Quadtree. 
	 * Then findEdges is called with a set of these nodes as well as the area you are viewing.
	 * @return HashSet<Edge> A HashSet of all Edges, which are connected to a node in the specified AreaToDraw
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
	 * once at startup. See fields: allEdgesSet. 
	 * @return A HashSet containing all edges as edge-objects. 
	 */
	private static HashSet<Edge> makeEdgeSet()
	{
		long startTime = System.currentTimeMillis();
		HashSet<Edge> edgeSet = new HashSet<Edge>();
		int count = 0;
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
					count++;

					vnEdge.toElement(VTDNav.PARENT); 
				} 
				apEdge.resetXPath();
			}
		}
		catch (VTDException e){
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("EdgeHalløj tager " + (endTime - startTime) + " milliseconds");
		System.out.println("makeEdgeSet laver " + count + " Edges");
		return edgeSet;
	}
	/**
	 * This method calls the QuadTree class and retrieves the currently viewed area's nodes. 
	 * @param area Current area the user are viewing on the map.
	 * @return A HashSet of nodes. 
	 */
	private static HashSet<Integer> findNodes(AreaToDraw area)
	{
		return QuadTree.searchAreaForNodeIDs(area);
	}
	/**
	 * This method retrieves a HashSet from zoomlevel containing roadTypes. If roadTypes match it checks whether 
	 * nodes are either from-nodes or to-nodes in our allEdgesSet. If either are true it adds them to 
	 * foundEdgesSet. This is not even the final form. 
	 * @param area Current area the user are viewing on the map.
	 * @param nodeIDSet A HashSets of nodes. Supplied by findNodes which gets it from the QuadTree. 
	 * @return A HashSet of edges. Final amount of edges needed to be drawn. These are sent to drawing. 
	 */
	private static HashSet<Edge> findEdges(AreaToDraw area, HashSet<Integer> nodeIDSet)
	{
		Iterator<Edge> iterator = allEdgesSet.iterator();
		HashSet<Edge> foundEdgesSet = new HashSet<Edge>();

		HashSet<Integer> zoomLevel = ZoomLevel.getlevel(area.getPercentageOfEntireMap());

		System.out.println("Size of nodeset parsed to findEdges(): " + nodeIDSet.size());

		while(iterator.hasNext())
		{
			Edge edge = iterator.next();	
			if(zoomLevel.contains(edge.getRoadType()))
				if(nodeIDSet.contains(edge.getFromNode()) || nodeIDSet.contains(edge.getToNode()))
					foundEdgesSet.add(edge);
		}			

		System.out.println("Number of relevant Edges found: " + foundEdgesSet.size());

		return foundEdgesSet;
	}
	/**
	 * This method parses the XML-file kdv_node containing nodes and their coordinates. It uses an autoPilot
	 * to navigate nodes and manually retrieves child-elements. At the end it navigates to the parent again. 
	 * @return A HashMap of nodes where the ID is node-id and value is an Array[2] of doubles 
	 * containing x-/y-coordinates. 
	 */
	private static HashMap<Integer, Double[]> makeNodeCoordinatesMap()
	{
		HashMap<Integer, Double[]> map = new HashMap<Integer, Double[]>();

		try {
			long startTime = System.currentTimeMillis();
			VTDGen vg =new VTDGen();
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
			System.out.println("nodeMap tager " + (endTime - startTime) + " milliseconds");
		} catch (NavException | XPathEvalException | XPathParseException e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * Accessor-method 
	 * @return Returns the map containing node-id's and and x-/y- coordinates. 
	 */
	public static HashMap<Integer, Double[]> getNodeCoordinatesMap()	{
		return nodeCoordinatesMap;
	}
}