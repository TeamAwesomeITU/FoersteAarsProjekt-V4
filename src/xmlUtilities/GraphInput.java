package xmlUtilities;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

public class GraphInput {

	
	public static void main(String[] args) {
		graphXML();

	}
	
	public static void graphXML() {
		try {	
			VTDGen vgEdge = new VTDGen();
			if(vgEdge.parseZIPFile("XML/kdv_unload_Graph.zip", "", false))
				//("XML/kdv_unload_Graph.xml", false)) {
			{
				VTDNav vnEdge = vgEdge.getNav();
				AutoPilot apEdge = new AutoPilot(vnEdge);
				apEdge.selectXPath("//RSC/RS");							
				int FNODE,TNODE,TYP,ID = 0;   
				String roadName, OneWay = ""; 
				double LENGTH,DriveTime = 0.0;

				while((apEdge.evalXPath())!=-1)
				{
					vnEdge.toElement(VTDNav.FIRST_CHILD, "FN");
					FNODE = vnEdge.parseInt(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "TN");
					TNODE = vnEdge.parseInt(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "LNGT");
					LENGTH = vnEdge.parseDouble(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "TYP");
					TYP = vnEdge.parseInt(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "RN");
					roadName = vnEdge.toString(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "DrTi");
					DriveTime = vnEdge.parseDouble(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "O-W");
					OneWay = vnEdge.toString(vnEdge.getText());
					vnEdge.toElement(VTDNav.NEXT_SIBLING, "ID");
					ID = vnEdge.parseInt(vnEdge.getText());
					
					/*
					 * You can do your shit here guys. Brug variablerne.
					 */
					

					vnEdge.toElement(VTDNav.PARENT); 
					System.out.println(LENGTH + " " +  roadName + " " +  ID);
				} 
				apEdge.resetXPath();
			}
		}
		catch (VTDException e){
			e.printStackTrace();

	}

	}}
