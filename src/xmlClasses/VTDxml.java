package xmlClasses;

// Example adapted from the documentation at vtd-xml.sourceforge.net
// and the code samples: http://vtd-xml.sourceforge.net/codeSample/cs1.html

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ximpleware.*;

public class VTDxml {
		
		public static void parse(String name) throws IOException, NavException, XPathParseException, XPathEvalException, ParseException{
			 File f = new File(name);
			 FileInputStream fis = new FileInputStream(f);
			 byte[] b = new byte[(int) f.length()];
			 fis.read(b);
			 VTDGen vg = new VTDGen();

			 vg.setDoc(b);
			 vg.parse(true); // set namespace awareness to true
			 
			 // Objects to assist navigation in XML
			 VTDNav vn = vg.getNav(); 
			 AutoPilot ap = new AutoPilot(vn);
			 ap.bind(vn);
			 
			 int count = 0;
			 int t;
			 		 
			 //First of all select element. The VTD Navigator VTDNav will iterate over all 
			 //such elements
			 ap.selectElement("rcp:recipe");
			 while(ap.iterate()){
				
				 int i = vn.getAttrVal("id"); //get id attribute from recipe
				 System.out.println("RECIPE ID:"+ vn.toNormalizedString(i)); //prints the ID
				 
				 //get first child that is a recipe title
				 vn.toElement(VTDNav.FIRST_CHILD,"rcp:title");
				 
				 //getText returns an index in the XML document so take this index t and
				 //then if t!=-1 print the text of the respective element
				 if ((t=vn.getText())!= -1)
					 System.out.println(" Recipe title:"+vn.toNormalizedString(t));
				 vn.toElement(VTDNav.NEXT_SIBLING); //navigates to next sibling on the XML tree
				 if ((t=vn.getText())!= -1)
					 System.out.println("\tDate:"+vn.toNormalizedString(t));
				  
			 }
			 
			 
			 //run an XPath expression that selects the recipe with the specified  
			 //title and output all the descendants of this recipe
			 ap.declareXPathNameSpace("rcp","http://www.brics.dk/ixwt/recipes");	
			 ap.selectXPath("//rcp:title[text()='Linguine Pescadoro']/..");
			 
			 int result;
			 
			 //fos is an outputstream to write to a separate file the output 
			 //of the XPath query
			 FileOutputStream fos = new FileOutputStream("out.xml");

			 
			 //write preamble
			 fos.write("<?xml version='1.1' encoding='utf-8'?>\n".getBytes());
			 fos.write("<rcp:selected xmlns:rcp=\"http://www.brics.dk/ixwt/recipes\">\n".getBytes());
			 
			 //write individual results to file
			 while((result = ap.evalXPath())!=-1){
				 /*
			        // The following code retrieves element name, with vn.toString(result), 
			       	// moves document cursor to the first element that satisfies the XPath expression
				 	// result is a numerical id of the node
			       	 System.out.print(""+result+" ");
			         System.out.print("Element name ==> "+vn.toString(result));
			       */
				 
			         //the next three lines retrieve element text if there exists one
			         int t1 = vn.getText(); // get the index of the text (char data or CDATA)
			         if (t1!=-1)			// use index to retrieve element text
			                System.out.println(" Text ==> "+vn.toNormalizedString(t1));
			         
			         //retrieves elements fragment for the XML and outputs it to the file
			         long l = vn.getElementFragment();
			         
			         fos.write(b, (int)l, (int)(l>>32));		         
			         count++;
			 }
			 
			 ap.resetXPath();
			 System.out.println("Total # of element "+count);
			 
			 //close XML file tags
			 fos.write("\n</rcp:selected>".getBytes());
			 fis.close();
			 fos.close();
			 b=null;
		}

	public static void main(String[] args) {
		
		try {
			parse("XML/recipes.xml");
		} catch (NavException | XPathParseException
				| XPathEvalException | ParseException | IOException e) {
			
			e.printStackTrace();
		}
		
	}

}
