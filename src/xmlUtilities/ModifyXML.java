package xmlUtilities;

import com.ximpleware.*;

import java.io.*;

public class ModifyXML { 
	public static void main(String[] s) throws Exception{

		VTDGen vgNode = new VTDGen(); // Instantiate VTDGen
		VTDGen vgEdge = new VTDGen();
		
	

		VTDNav vnNode = vgNode.getNav();

		VTDNav vnEdge = vgEdge.getNav();
		XMLModifier xm = new XMLModifier(vnNode); //Instantiate XMLModifier

		if (vgNode.parseFile("XML/kdv_node_unload.xml",false)){
			if (vgEdge.parseFile("XML/kdv_unload_Graph.xml",false)){
				
				

				AutoPilot apEdge = new AutoPilot(vnEdge);
				AutoPilot apNode = new AutoPilot(vnNode);
				apNode.selectXPath("//nodeCollection/node");
				apEdge.selectXPath("//RSC/RS");

				int kdv = 0, FnodeInEdges = 0;
				
				String references = "";
				while((apNode.evalXPath())!=-1) {
					vnNode.toElement(VTDNav.FIRST_CHILD, "KDV");
					kdv = vnNode.parseInt(vnNode.getText());

					while((apEdge.evalXPath())!=-1) {
						vnEdge.toElement(VTDNav.FIRST_CHILD, "FN");
						FnodeInEdges = vnEdge.parseInt(vnEdge.getText());
						
						vnEdge.toElement(VTDNav.PARENT);
						
						if(kdv == FnodeInEdges)	{
							vnEdge.toElement(VTDNav.FIRST_CHILD, "DAV_DK-ID");
							references = references + vnEdge.parseInt(vnEdge.getText());
							vnEdge.toElement(VTDNav.PARENT);
						}
					}
					xm.insertAfterHead("<name>"+references+"</name>");
					vnNode.toElement(VTDNav.PARENT);					
				}
			}
			xm.output(new FileOutputStream("test.xml"));
		}
	}
}	


