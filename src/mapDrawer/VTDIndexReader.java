package mapDrawer;

import java.io.IOException;
import java.util.HashSet;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

public class VTDIndexReader {

	public static void readXml() {
		VTDGen vg = new VTDGen();
		try{			
			VTDNav vn = vg.loadIndex("XML/edge_unload.vxl");
			AutoPilot ap = new AutoPilot(vn);


		}
		catch(IOException | VTDException e) {
			e.printStackTrace();
		}
	}

	private static HashSet<String> findNodes()
	{
		HashSet<String> nodeSet = new HashSet<String>();
		long startTime = System.currentTimeMillis();
		try {
			VTDGen vg =new VTDGen();
			AutoPilot ap = new AutoPilot();
			VTDNav vn = vg.loadIndex("XML/edge_unload.vxl");


			ap.selectXPath("//roadSegmentCollection/roadSegment");
	
			while (ap.evalXPath() != -1) {
				
			}
			System.out.println("Number of nodes sent to edges ");

		} catch (IOException | VTDException e){
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Confirm Node tager " + (endTime - startTime) + " milliseconds");
		System.out.println("___________________________________________");
		return nodeSet;
	}

}