package xmlClasses;

import com.ximpleware.*;
import com.ximpleware.EOFException;
import com.ximpleware.xpath.*;
import java.io.*;

public class VTDfjol {
	
	
	
public static void main(String[] args) throws VTDException {
		
		try {
			parse();
		} catch (NavException | XPathParseException
				| XPathEvalException | ParseException | IOException e) {
			
			e.printStackTrace();
		}
		
	}

	
	public static void parse() throws IOException, EncodingException, VTDException, EntityException, ParseException {
	File f = new File("XML/kdv_unload.xml");
	FileInputStream fis = new FileInputStream(f);
	byte[] b = new byte[(int)f.length()];
	fis.read(b);

	VTDGen vg = new VTDGen();
	vg.setDoc(b);
	vg.parse(true);
	
	VTDNav vn = vg.getNav();
	AutoPilot ap = new AutoPilot(vn);
	
	ap.declareXPathNameSpace("", "");
	//bookstore/book[price>35.00]/title
	ap.selectXPath("//node/KDV");
	
	int result = -1;
	int count = 0;
	while ((result=ap.evalXPath())!=-1) {
		//(System.out.println(""+result+"");
		//System.out.println("Element name ==> "+vn.toString(result));
		int t = vn.getText();
		 String temp = vn.toNormalizedString(t); 
		 if(t!=-1 && temp != "1")			  
			//System.out.println("Text ==>"+temp);
		//System.out.println("\n====================================");
		count++;
	}
	System.out.println("Total# of element"+count);
	fis.close();
	}
	
}
