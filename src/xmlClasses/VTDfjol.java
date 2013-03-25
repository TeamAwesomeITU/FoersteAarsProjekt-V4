package xmlClasses;

import com.ximpleware.*;
import java.io.*;

public class VTDfjol {
	
	
	
public static void main(String[] args) throws VTDException {
		
		try {
			//parseNode();
			parseEdge();
		} catch (NavException | XPathParseException
				| XPathEvalException | ParseException | IOException e) {
			
			e.printStackTrace();
		}
		
	}

	public void VTD() {
		
	}
	
	public static void parseEdge() throws IOException, EncodingException, VTDException, EntityException, ParseException {
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
	apEdge.selectXPath("//roadSegmentCollection/roadSegment[TYP = 48 ]/FNODE"); 
	
	
	
	int result = -1;
	int count = 0;
	while ((result=apEdge.evalXPath())!=-1) {
		//System.out.println(""+result+"");
		//System.out.println("Element name ==> "+vnEdge.toString(result));
		int t = vnEdge.getText();
		// String temp = vn.toNormalizedString(t); 
		 if(t!=-1 && vnEdge.toNormalizedString(t) != "1")			  
			//System.out.println("Text ==>"+vnEdge.toNormalizedString(t));
		//System.out.println("\n====================================");
		count++;
	}
	System.out.println("Total# of element"+count);
	fis.close();
	}	


	public static void parseNode() throws IOException, EncodingException, VTDException, EntityException, ParseException {
		File fo = new File("XML/kdv_node_unload.xml");
		FileInputStream fos = new FileInputStream(fo);
		byte[] n = new byte[(int)fo.length()];
		fos.read(n);

		VTDGen vgNode = new VTDGen();
		vgNode.setDoc(n);
		vgNode.parse(true);

		VTDNav vnNode = vgNode.getNav();
		AutoPilot apNode = new AutoPilot(vnNode);
		
		int Xma=490000; int Xmi=480000; int Yma=7000000; int Ymi=6000000;

		apNode.declareXPathNameSpace("", "");
		apNode.selectXPath("//nodeCollection/node[X-COORD < "+Xma+" and X-COORD > "+Xmi+" and Y-COORD < "+Yma+" and Y-COORD > "+Ymi+"]/*");


		int result = -1;
		int count = 0;
		while ((result=apNode.evalXPath())!=-1) {
			//System.out.println(""+result+"");
			//System.out.println("Element name ==> "+vnNode.toString(result));
			int t = vnNode.getText();
	// String temp = vn.toNormalizedString(t); 
			if(t!=-1 && vnNode.toNormalizedString(t) != "1")			  
				//System.out.println("Text ==>"+vnNode.toNormalizedString(t));
			//System.out.println("\n====================================");
			count++;
		}
		System.out.println("Total# of element"+count);
		fos.close();
	}	
}

