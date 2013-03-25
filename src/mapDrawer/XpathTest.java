package mapDrawer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
public class XpathTest {

	//A HashMap of the coordinates of all nodes in the entire map - the node's ID is the key
	private static final HashMap<Integer, Double[]> nodeCoordinatesMap = makeNodeCoordinatesMap();

	/*
	 * 
	 * @return HashSet<Edge> A HashSet of all Edges, which are connected to a node in the specified AreaToDraw
	 */
	public static HashSet<Edge> findNodesToDraw(AreaToDraw area)
	{
		//Doesn't use a HashSet of Integer, since XPath uses Strings as "input"
		HashSet<String> nodeSet = findNodes(area);
		return findEdges(area, nodeSet);
	}

	private static HashSet<String> findNodes(AreaToDraw area)
	{
		HashSet<String> nodeSet = new HashSet<String>();
		long startTime = System.currentTimeMillis();
		try {
			File fo = new File("XML/kdv_node_unload.xml");
			FileInputStream fos = new FileInputStream(fo);
			byte[] n = new byte[(int)fo.length()];
			fos.read(n);

			VTDGen vgNode = new VTDGen();
			vgNode.setDoc(n);
			vgNode.parse(false);

			VTDNav vnNode = vgNode.getNav();
			AutoPilot apNode = new AutoPilot(vnNode);

			double Xmax = area.getLargestX(); double Xmi = area.getSmallestX();
			double Ymax = area.getLargestY(); double Ymin = area.getSmallestY();

			apNode.selectXPath("//nodeCollection/node[X-COORD < "+Xmax+" and X-COORD > "+Xmi+" and Y-COORD < "+Ymax+" and Y-COORD > "+Ymin+"]/KDV");

			int count = 0;
			while (apNode.evalXPath() != -1) {
				int t = vnNode.getText();
				if(t!=-1 && vnNode.toNormalizedString(t) != "1")	{
					nodeSet.add(vnNode.toNormalizedString(t));
				}
				count++;
			}
			System.out.println("Total# of element "+count);
			fos.close();
			System.out.println("Size of nodeSet " + nodeSet.size());
		} catch (IOException | VTDException e){
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("nodeSet tager " + (endTime - startTime) + " milliseconds");
		return nodeSet;
	}

	private static HashSet<Edge> findEdges(AreaToDraw area, HashSet<String> nodeSet)
	{
		long startTime = System.currentTimeMillis();
		HashSet<Edge> edgeSet = new HashSet<Edge>();
		try {	

	
	

			long startTime1 = System.currentTimeMillis();
			VTDGen vg = new VTDGen();
			VTDNav vnEdge = vg.loadIndex("XML/edge_unload.vxl");			
			long endTime1 = System.currentTimeMillis();
			System.out.println("Loading Index takes: " + (endTime1 - startTime1) + " milliseconds");
		
			AutoPilot apEdge = new AutoPilot(vnEdge);

			int edgeObjectCount = 0; int count = 0; int type = 44;		
			String output = null;
			String[] edgeInfoArray = new String[5];

			apEdge.selectXPath("//roadSegmentCollection/roadSegment[TYP <= "+type+"]/*"); 
			while ((apEdge.evalXPath())!=-1) {

				int t = vnEdge.getText();
				if(t!=-1 && vnEdge.toNormalizedString(t) != "1")	{			  
					output = vnEdge.toNormalizedString(t);
				}
				edgeInfoArray[edgeObjectCount] = output;
				edgeObjectCount++;
				if(edgeObjectCount == 5)	{
					edgeObjectCount = 0;
					if(nodeSet.contains(edgeInfoArray[0]) || nodeSet.contains(edgeInfoArray[1]))		{
						edgeSet.add(new Edge(Integer.parseInt(edgeInfoArray[0]), Integer.parseInt(edgeInfoArray[1]), 
								Integer.parseInt(edgeInfoArray[2]), edgeInfoArray[3], Integer.parseInt(edgeInfoArray[4])));
						count++;
					}
				}


			}		
			System.out.println("Total# of elements "+count);
		}
		catch (IOException | VTDException e){
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
					Integer kdv = Integer.parseInt(vn.toString(vn.getText()));
					
					Double[] coords = new Double[2];
							
					vn.toElement(VTDNav.NEXT_SIBLING, "X-COORD"); 
					coords[0] = Double.parseDouble((vn.toString(vn.getText())));
					
					vn.toElement(VTDNav.NEXT_SIBLING, "Y-COORD"); 
					coords[1] = Double.parseDouble((vn.toString(vn.getText())));
					
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

	public static HashMap<Integer, Double[]> getNodeCoordinatesMap()
	{
		return nodeCoordinatesMap;
	}

	public static void main(String[] args)
	{		
		HashSet<Edge> set = findNodesToDraw(new AreaToDraw());	
		
		/*
		Iterator<Edge> iterator = set.iterator();
		Edge edge = null;

		while (iterator.hasNext()) {
			edge = iterator.next();
			System.out.println(edge.getRoadName());			
		}
		 */
	}
}
