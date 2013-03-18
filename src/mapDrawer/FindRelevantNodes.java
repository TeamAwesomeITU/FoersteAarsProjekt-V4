package mapDrawer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import com.ximpleware.AutoPilot;

import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
/*
 * Locates the Edges that lies within the AreaToDraw
 */
public class FindRelevantNodes {

	public static HashSet<Edge> findNodesToDraw(AreaToDraw area)
	{
		//Doesn't use a HashSet of Integer, since XPath uses Strings as "input"
		HashSet<String> nodeSet = findNodes(area);
		return findEdges(area, nodeSet);
	}

	private static HashSet<String> findNodes(AreaToDraw area)
	{
		HashSet<String> nodeSet = new HashSet<String>();

		try {
			File fo = new File("XML/kdv_node_unload.xml");
			FileInputStream fos = new FileInputStream(fo);
			byte[] n = new byte[(int)fo.length()];
			fos.read(n);

			VTDGen vgNode = new VTDGen();
			vgNode.setDoc(n);
			vgNode.parse(true);

			VTDNav vnNode = vgNode.getNav();
			AutoPilot apNode = new AutoPilot(vnNode);

			double Xma = area.getLargestX();
			double Xmi = area.getSmallestX();
			double Yma = area.getLargestY();
			double Ymi = area.getSmallestY();

			apNode.declareXPathNameSpace("", "");
			apNode.selectXPath("//nodeCollection/node[X-COORD < "+Xma+" and X-COORD > "+Xmi+" and Y-COORD < "+Yma+" and Y-COORD > "+Ymi+"]/KDV");

			int count = 0;
			while (apNode.evalXPath() != -1) {
				int t = vnNode.getText();

				if(t!=-1 && vnNode.toNormalizedString(t) != "1")
					nodeSet.add(vnNode.toNormalizedString(t));

				count++;
			}
			System.out.println("Total# of element "+count);
			fos.close();

			System.out.println("Size of nodeSet " + nodeSet.size());

		} catch (IOException | VTDException e){
			e.printStackTrace();
		}

		return nodeSet;

	}

	private static HashSet<Edge> findEdges(AreaToDraw area, HashSet<String> nodeSet)
	{
		HashSet<Edge> edgeSet = new HashSet<Edge>();

		try {
			area.getPercentageOfEntireMap();

			File fi = new File("XML/kdv_unload_omskaaretEdition.xml");
			FileInputStream fis = new FileInputStream(fi);
			byte[] b = new byte[(int)fi.length()];
			fis.read(b);

			VTDGen vgEdge = new VTDGen();
			vgEdge.setDoc(b);
			vgEdge.parse(true);

			VTDNav vnEdge = vgEdge.getNav();
			AutoPilot apEdge = new AutoPilot(vnEdge);
			apEdge.declareXPathNameSpace("", "");
			
			int edgeObjectCount = 0;
			int count = 0;
			int type = 1;
			String node = null;
			String output = null;
			String[] edgeInfoArray = new String[5];

			Iterator<String> iterator = nodeSet.iterator();			
			while(iterator.hasNext()) {
				node = iterator.next();
				edgeObjectCount = 0;
				apEdge.selectXPath("//roadSegmentCollection/roadSegment[TYP <= 4 and FNODE=539147 or TNODE = 538398]/*"); 
				while ((apEdge.evalXPath())!=-1) {
					
					int t = vnEdge.getText();
					if(t!=-1 && vnEdge.toNormalizedString(t) != "1")			  
						output = vnEdge.toNormalizedString(t);
					edgeInfoArray[edgeObjectCount] = output;
					edgeObjectCount++;
					
					if(edgeObjectCount == 5)
					{
						edgeObjectCount = 0;
						edgeSet.add(new Edge(Integer.parseInt(edgeInfoArray[0]), Integer.parseInt(edgeInfoArray[1]), 
								Integer.parseInt(edgeInfoArray[2]), edgeInfoArray[3], Integer.parseInt(edgeInfoArray[4])));
						System.out.println("LOL");
						count++;
					}
					
				}
				
				//System.out.println("Total# of element"+count);
			}		
			fis.close();
		}
		catch (IOException | VTDException e){
			e.printStackTrace();
		}
		return edgeSet;
	}

	public static void main(String[] args)
	{		
		findNodesToDraw(new AreaToDraw());		
	}

}
