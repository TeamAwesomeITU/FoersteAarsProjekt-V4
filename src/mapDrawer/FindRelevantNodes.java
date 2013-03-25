package mapDrawer;

import java.io.IOException;
import java.util.HashSet;
import com.ximpleware.AutoPilot;
import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
/*
 * Locates the Edges that lies within the AreaToDraw
 */
public class FindRelevantNodes {

	private static final VTDNav vnEdge = indexEdgeLoad();
	private static final VTDNav vnNode = indexNodeLoad();

	public static HashSet<Edge> findNodesToDraw(AreaToDraw area)
	{
		//Doesn't use a HashSet of Integer, since XPath uses Strings as "input"
		HashSet<Integer> nodeSet = findNodes(area);
		return findEdges(area, nodeSet);
	}

	private static HashSet<Integer> findNodes(AreaToDraw area)
	{
		HashSet<Integer> nodeSet = new HashSet<Integer>();
		try {
			long startTime = System.currentTimeMillis();
			VTDNav vn = vnNode;			
			AutoPilot ap = new AutoPilot(vn);
			ap.selectXPath("//nodeCollection/node");

			double Xmax = area.getLargestX(); double Xmin = area.getSmallestX();
			double Ymax = area.getLargestY(); double Ymin = area.getSmallestY();
			double YCOORD = 0.0; double XCOORD = 0.0; int count =0;

			while (ap.evalXPath() != -1) {
				vn.toElement(VTDNav.FIRST_CHILD, "Y-COORD"); 
				YCOORD = vn.parseDouble(vn.getText());
				if(YCOORD < Ymax && YCOORD > Ymin) {
					vn.toElement(VTDNav.PREV_SIBLING, "X-COORD"); 
					XCOORD = vn.parseDouble(vn.getText());
					if(XCOORD < Xmax && XCOORD > Xmin) {					
						vn.toElement(VTDNav.PREV_SIBLING, "KDV");				
						nodeSet.add(vn.parseInt(vn.getText()));
						count++;
					}
				}
				vn.toElement(VTDNav.PARENT);
			}
			System.out.println("Number of nodes sent to edges "+ count);
			long endTime = System.currentTimeMillis();
			System.out.println("Confirm Node tager " + (endTime - startTime) + " milliseconds");

		} catch (VTDException e){
			e.printStackTrace();
		}
		System.out.println("___________________________________________");
		return nodeSet;

	}

	private static HashSet<Edge> findEdges(AreaToDraw area, HashSet<Integer> nodeSet)
	{
		long startTime = System.currentTimeMillis();
		HashSet<Edge> edgeSet = new HashSet<Edge>();
		try {			
			if(nodeSet.isEmpty()){
				System.out.println("No nodes within the given coordinates");
			}
			else {							
				VTDNav vn = vnEdge;
				System.out.println("DONE LOADING");
				AutoPilot ap = new AutoPilot(vn); 
				int type = 2;
				ap.selectXPath("//roadSegmentCollection/roadSegment[TYP <= "+type+"]");							
				String FNODE = ""; String TNODE = ""; 
				int TYP = 0;   String ROAD = ""; 
				int POST = 0;
				int count1 = 0;
				while((ap.evalXPath())!=-1)
				{
					vn.toElement(VTDNav.FIRST_CHILD, "FNODE");
					FNODE = vn.toString(vn.getText());
					vn.toElement(VTDNav.NEXT_SIBLING, "TNODE");
					TNODE = vn.toString(vn.getText());
					count1++;
					if(nodeSet.contains(FNODE)||nodeSet.contains(TNODE)) {
						vn.toElement(VTDNav.NEXT_SIBLING, "TYP");
						TYP = vn.parseInt(vn.getText());
						vn.toElement(VTDNav.NEXT_SIBLING, "VEJNAVN");
						ROAD = vn.toString(vn.getText());
						vn.toElement(VTDNav.NEXT_SIBLING, "H_POSTNR");
						POST = vn.parseInt(vn.getText());
						edgeSet.add(new Edge(Integer.parseInt(FNODE),Integer.parseInt(TNODE),TYP,ROAD,POST));				
					}
					vn.toElement(VTDNav.PARENT); 
				} 
				ap.resetXPath();
				System.out.println("Number of FNODES and TNODES Checked "+count1);				
			}
		}
		catch (VTDException e){
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Confirm Edge takes " + (endTime - startTime) + " milliseconds");
		System.out.println("Edges sent " + edgeSet.size());
		System.out.println("Done with edges");
		System.out.println("___________________________________________");
		return edgeSet;
	} 

	public static void main(String[] args)
	{		
		long startTime = System.currentTimeMillis();
		findNodesToDraw(new AreaToDraw());		
		long endTime = System.currentTimeMillis();
		System.out.println("Total " + (endTime - startTime) + " milliseconds");
		System.out.println("___________________________________________");
	}
	
	private static VTDNav indexEdgeLoad(){
		long startTime = System.currentTimeMillis();
		VTDNav vn = null;

		try{
			VTDGen vg = new VTDGen();
			vn = vg.loadIndex("XML/edge_unload.vxl");
		} catch(IOException | VTDException e) {
			e.printStackTrace();

		}
		long endTime = System.currentTimeMillis();
		System.out.println("Load Edge Index: " + (endTime - startTime) + " milliseconds");
		return vn;
	}

	private static VTDNav indexNodeLoad() {
		long startTime = System.currentTimeMillis();
		VTDNav vn = null;

		try{
			VTDGen vg = new VTDGen();
			vn = vg.loadIndex("XML/Node_unload.vxl");
		} catch(IOException | VTDException e) {
			e.printStackTrace();

		}
		long endTime = System.currentTimeMillis();
		System.out.println("Load Node Index " + (endTime - startTime) + " milliseconds");
		return vn;
	}	

}