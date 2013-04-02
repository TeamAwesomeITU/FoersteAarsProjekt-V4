package mapDrawer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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
public class FindRelevantNodes {

	//A HashMap of the coordinates of all nodes in the entire map - the node's ID is the key
	private static final HashMap<Integer, Double[]> nodeCoordinatesMap = makeNodeCoordinatesMap();

	private static final HashSet<Edge> allEdgesSet = makeEdgeSet();

	/*
	 * 
	 * @return HashSet<Edge> A HashSet of all Edges, which are connected to a node in the specified AreaToDraw
	 */
	public static HashSet<Edge> findNodesToDraw(AreaToDraw area)
	{
		long startTime = System.currentTimeMillis();
		HashSet<Integer> nodeSet = findNodes(area);
		long endTime = System.currentTimeMillis();
		System.out.println("Retrieving nodeIDs from area took " + (endTime - startTime) + " milliseconds");
		return findEdges(area, nodeSet);
	}

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
				int type = 48;
				apEdge.selectXPath("//roadSegmentCollection/roadSegment[TYP <= "+type+"]");							
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

	private static HashSet<Integer> findNodes(AreaToDraw area)
	{
		return QuadTree.searchAreaForNodeIDs(area);
	}

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

	public static HashMap<Integer, Double[]> getNodeCoordinatesMap()	{
		return nodeCoordinatesMap;
	}
}
