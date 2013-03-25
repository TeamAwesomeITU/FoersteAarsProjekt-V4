package mapDrawer;

import java.util.HashMap;
import java.util.HashSet;

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

	/*
	 * 
	 * @return HashSet<Edge> A HashSet of all Edges, which are connected to a node in the specified AreaToDraw
	 */
	public static HashSet<Edge> findNodesToDraw(AreaToDraw area)
	{
		//Doesn't use a HashSet of Integer, since XPath uses Strings as "input"
		HashSet<Integer> nodeSet = findNodes(area);
		return findEdges(area, nodeSet);
	}

	private static HashSet<Integer> findNodes(AreaToDraw area)
	{
		HashSet<Integer> nodeSet = new HashSet<Integer>();
		long startTime = System.currentTimeMillis();
		try {
			VTDGen vgNode = new VTDGen();
			if(vgNode.parseFile("XML/kdv_node_unload.xml", false)) {
				VTDNav vnNode = vgNode.getNav();
				AutoPilot apNode = new AutoPilot(vnNode);

				double Xmax = area.getLargestX(); double Xmin = area.getSmallestX();
				double Ymax = area.getLargestY(); double Ymin = area.getSmallestY();
				double YCOORD = 0.0; double XCOORD = 0.0;
				apNode.selectXPath("//nodeCollection/node");

				int count = 0;
				while (apNode.evalXPath() != -1) {
					vnNode.toElement(VTDNav.FIRST_CHILD, "Y-COORD"); 
					YCOORD = vnNode.parseDouble(vnNode.getText());
					if(YCOORD < Ymax && YCOORD > Ymin) {
						vnNode.toElement(VTDNav.PREV_SIBLING, "X-COORD"); 
						XCOORD = vnNode.parseDouble(vnNode.getText());
						if(XCOORD < Xmax && XCOORD > Xmin) {					
							vnNode.toElement(VTDNav.PREV_SIBLING, "KDV");				
							nodeSet.add(vnNode.parseInt(vnNode.getText()));
							count++;
						}
					}
					vnNode.toElement(VTDNav.PARENT);
				}
				System.out.println("Total# of element "+count);
				System.out.println("Size of nodeSet " + nodeSet.size());
			}
		} catch (VTDException e){
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("nodeSet tager " + (endTime - startTime) + " milliseconds");
		return nodeSet;
	}

	private static HashSet<Edge> findEdges(AreaToDraw area, HashSet<Integer> nodeSet)	{
		long startTime = System.currentTimeMillis();
		HashSet<Edge> edgeSet = new HashSet<Edge>();
		try {	
			VTDGen vgEdge = new VTDGen();
			if(vgEdge.parseFile("XML/kdv_unload_new.xml", false)) {

				VTDNav vnEdge = vgEdge.getNav();
				AutoPilot apEdge = new AutoPilot(vnEdge);
				apEdge.selectXPath("//roadSegmentCollection/roadSegment" + ZoomLevel.getlevel(area.getPercentageOfEntireMap()));							
				int FNODE = 0; int TNODE = 0; 
				int TYP = 0;   String ROAD = ""; 
				int POST = 0;
				int count = 0;
				while((apEdge.evalXPath())!=-1)
				{
					vnEdge.toElement(VTDNav.FIRST_CHILD, "FNODE");
					FNODE = vnEdge.parseInt(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "TNODE");
					TNODE = vnEdge.parseInt(vnEdge.getText());
					if(nodeSet.contains(FNODE)||nodeSet.contains(TNODE)) {
						vnEdge.toElement(VTDNav.NEXT_SIBLING, "TYP");
						TYP = vnEdge.parseInt(vnEdge.getText());
						vnEdge.toElement(VTDNav.NEXT_SIBLING, "VEJNAVN");
						ROAD = vnEdge.toString(vnEdge.getText());
						vnEdge.toElement(VTDNav.NEXT_SIBLING, "H_POSTNR");
						POST = vnEdge.parseInt(vnEdge.getText());
						edgeSet.add(new Edge(FNODE,TNODE,TYP,ROAD,POST));
					}
					vnEdge.toElement(VTDNav.PARENT); 
				} 
				apEdge.resetXPath();
				System.out.println("Number of FNODES and TNODES Checked "+count);
			}
		}
		catch (VTDException e){
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("EdgeHalløj tager " + (endTime - startTime) + " milliseconds");
		System.out.println("Done");
		return edgeSet;
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
