package xmlClasses;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLwriter {
	String filename = "xml/kdv_unload0.xml";
	
	  /**
	   * Constructor calls txtOutput that will create the txt from the xml.
	   * @throws XPathExpressionException
	   * @throws IOException
	   * @throws ParserConfigurationException
	   * @throws SAXException
	   */
	
	  public XMLwriter() throws XPathExpressionException, IOException, 
	  ParserConfigurationException, SAXException {
		  	//txtOutput();
	  }
	  
	  
	
  	  /**
  	   * This method uses a for-loop with a nested for-loop. The first for-loop selects
  	   * the proper xml-file and creates a DOM document. It also initializes the XPATH
  	   * expression. The nested loop then evaluates that expression against the document
  	   * and writes the return value from the expression to a txt-file using a bufferedReader. 
  	   * The method also has a counter that for each iteration of the nested loop counts 1. 
  	   * This should print the amount of edges in our XML-files. 
  	   * @throws IOException
  	   * @throws ParserConfigurationException
  	   * @throws SAXException
  	   * @throws XPathExpressionException
  	   */
	  public void txtOutput() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException{   
		    int currentXML = 0;		//A counter used to change the xml being used.  
		    int edgeCounter = 0;	//Counts the edges of final txt-document. 
		    BufferedWriter out = new BufferedWriter(new FileWriter("file.txt"));	//A writer that creates the txt.		    
			XPathExpression expr = xpathFactory();		//Stores the XPATH-expression in expr. 		    
		    for (int j = 0;j<101; j++){	
		        Document doc = docBuilder(currentXML);	//Creates a DOM-document. currentXML determines what version.			    
			    Object result = expr.evaluate(doc, XPathConstants.NODESET);		
			    NodeList nodes = (NodeList) result;		      	
		    	int FNODEcount = 0; int TNODEcount = 1; int LENGTHcount = 2;	//Variables used to determine at what index TNODE, FNODE and LENGTH is at. 		    
		    		for (int i = 0; i < nodes.getLength(); i++) {
		    			if(FNODEcount < nodes.getLength()){
		    				out.write(nodes.item(FNODEcount).getNodeValue()+ " " //Writes to the txt-file.
		    						+nodes.item(TNODEcount).getNodeValue()+ " "
		    						+nodes.item(LENGTHcount).getNodeValue()+"\n");
		    						}
		    			FNODEcount+=3; TNODEcount+=3; LENGTHcount+=3;	
		    			edgeCounter++;		
		 
		    	}
		    	currentXML++;		
		    }
		    System.out.println(edgeCounter/3);		//We need to divide the counter by 3 since we have 3 colums in each iteration of the nested loop.    
		    out.close();        		    
	  }
	  /**
	   * This method creates a DOM-document using an xml-file. The parameter determines
	   * which xml-file to choose. We have 100 xml-files at around 5mb each. This is 
	   * to avoid memory issues since the domFactory creates a single document using 
	   * the entire xml. This is too big a task with an xml of 500mb. This way it only 
	   * creates a document using a 5mb xml-file.  
	   * @param i Represents what xml-file to choose.
	   * @return A DOM document that can be evaluated agaisnt an XPATH expression. 
	   * @throws ParserConfigurationException
	   * @throws SAXException
	   * @throws IOException
	   */
	  public Document docBuilder(int i)   
			throws ParserConfigurationException, SAXException, IOException{  		  
		    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		    domFactory.setNamespaceAware(true); 
		    DocumentBuilder builder = domFactory.newDocumentBuilder();
		    Document doc = builder.parse("xml/kdv_unload"+ i + ".xml");		    
		    return doc;
	  }
	  /**
	   * This method creates an instance of an XPATH expression. 
	   * It is mainly seperated from the txtOutput() method to keep the semantics relatively
	   * simple.
	   * @return A complied version of the XPATH-expression to evaluated against the DOM-document. 
	   * @throws XPathExpressionException
	   */
	  public XPathExpression xpathFactory() throws XPathExpressionException {
		    XPathFactory factory = XPathFactory.newInstance();
		    XPath xpath = factory.newXPath();
		    XPathExpression expr
		     = xpath.compile("//roadSegment/FNODE/text()|//roadSegment/TNODE/text()|//roadSegment/LENGTH/text()");		    
		    return expr;
	  }
}