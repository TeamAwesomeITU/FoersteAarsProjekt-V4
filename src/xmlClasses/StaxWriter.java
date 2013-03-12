package xmlClasses;
import java.io.*;
import java.util.Scanner;

import javax.xml.stream.*;


public class StaxWriter {
  private String configFile;
  private String inputTextFile;
  private String collectionName;
  private String nodeName;
  private String delimiter;
  
  public StaxWriter(String configFile, String inputTextFile, String collectionName, String nodeName, String delimiter) {
	  this.configFile = configFile;
	  this.inputTextFile = inputTextFile;
	  this.collectionName = collectionName;
	  this.nodeName = nodeName;
	  this.delimiter = delimiter;
  }

  public void saveConfig() throws Exception {
    // Create a XMLOutputFactory
    XMLOutputFactory opF = XMLOutputFactory.newInstance();
    
    // Create XMLEventWriter
    XMLStreamWriter streamWriter = opF.createXMLStreamWriter(new FileOutputStream(configFile +0+ ".xml"), "UTF-8");
    
    // Create and write Start Tag
    streamWriter.writeStartDocument("UTF-8", "1.0");
    
    // Create config open tag
    streamWriter.writeDTD("\n");
    streamWriter.writeStartElement(collectionName);
    streamWriter.writeDTD("\n");
    
    //Creates a node with its values
    createValues(streamWriter);
  }
  
  private void createValues(XMLStreamWriter streamWriter) throws XMLStreamException, IOException {
	  File file = new File(inputTextFile);
	  BufferedReader reader = new BufferedReader(new FileReader(file));
	  String line = new String();
		
	  line = reader.readLine();
	  Scanner scanValues = new Scanner(line);
	  scanValues.useDelimiter(delimiter);
	  int k = 0;
	  String[] values = new String[34];
	
	  while(scanValues.hasNext()) {
		 String tmp = scanValues.next();
		 if(tmp.contains("#")) {
			 tmp = tmp.substring(0, tmp.length()-1);
		 }
		  values[k] = tmp;
		  k++;
	  }
	  scanValues.close();
	  if(inputTextFile.equals("kdv_unload.txt")) 	{
		  int fileNumber = 1;
		  int n = 0;
		  while((line = reader.readLine()) != null) {
			  createNodesForKDVunload(line, streamWriter, values);
			  if(n>0)
			  if(n%8000 == 0) {
				  streamWriter.writeEndElement();
				  streamWriter.writeEndDocument();
				  streamWriter.close();
				  XMLOutputFactory opF = XMLOutputFactory.newInstance();
				  streamWriter = opF.createXMLStreamWriter(new FileOutputStream(configFile + fileNumber + ".xml"), "UTF-8");
				  fileNumber++;
				  streamWriter.writeStartDocument("UTF-8", "1.0");
				    
				  streamWriter.writeDTD("\n");
				  streamWriter.writeStartElement(collectionName);
				  streamWriter.writeDTD("\n");
			  }
			  n++;
		  }
	  }
	  if(inputTextFile.equals("kdv_node_unload.txt")) {
		  while((line = reader.readLine()) != null) {
			  createNodesForKDVnodeunload(line, streamWriter, values);	  
		  }
		    streamWriter.writeEndElement();
		    streamWriter.writeEndDocument();
		    streamWriter.close();
	  }
	  reader.close();
  }
  
  private void createNode(XMLStreamWriter streamWriter, String name,
      String value) throws XMLStreamException {

    // Create Start node
    streamWriter.writeDTD("\t");
    streamWriter.writeDTD("\t");
    streamWriter.writeStartElement(name);
    
    // Create Content
    streamWriter.writeCharacters(value);
    
    // Create End node
    streamWriter.writeEndElement();
    streamWriter.writeDTD("\n");

  }
  
  private void createNodesForKDVunload(String line, XMLStreamWriter streamWriter, String[] values) throws XMLStreamException {
	  Scanner scan = new Scanner(line); 
	  scan.useDelimiter("(?<=[']|\\d|\\*)[,](?=[']|\\d|\\*)");    	      
	  int[] intArray = {3,4,15,16,19,20,21,22,30,31};
	  int x = 0;
	  streamWriter.writeDTD("\t");
	  streamWriter.writeStartElement(nodeName);
	  streamWriter.writeDTD("\n");
		  while(values[x]!=null) {
			  if(isItDifferentFrom(x, intArray) == true && scan.hasNext()) {
				  createNode(streamWriter, values[x], scan.next());
			  }
			  else if(scan.hasNext())
				  scan.next();
			  x++;
		  }
		  streamWriter.writeDTD("\t");
		  streamWriter.writeEndElement();
		  streamWriter.writeDTD("\n");
		  scan.close();
  }
  
  private boolean isItDifferentFrom(int value, int[] intArray) {
	  boolean isDifferent = true;
	  for(int i = 0; i<intArray.length; i++) 
		  if(value == intArray[i])
			  isDifferent = false;
	  
	  return isDifferent;
  }
  
  private void createNodesForKDVnodeunload(String line, XMLStreamWriter streamWriter, String[] values) throws XMLStreamException {
	  Scanner scan = new Scanner(line); 
	  scan.useDelimiter("(?<=[']|\\d|\\*)[,](?=[']|\\d|\\*)");
	  int[] intArray = new int[] {0, 2};
	  int x = 0;
			  streamWriter.writeDTD("\t");
			  streamWriter.writeStartElement(nodeName);
			  streamWriter.writeDTD("\n");
			  while(values[x]!=null) {
				  if(isItDifferentFrom(x, intArray) == true) {
					  createNode(streamWriter, values[x], scan.next());
				  }
				  else {
					  scan.next();
				  }
				  x++;
			  }
			  streamWriter.writeDTD("\t");
			  streamWriter.writeEndElement();
			  streamWriter.writeDTD("\n");
	  scan.close();
  }
} 