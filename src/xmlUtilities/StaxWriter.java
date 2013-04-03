package xmlUtilities;
import java.io.*;
import java.util.Scanner;

import javax.xml.stream.*;


public class StaxWriter {
  private String configFile;
  private String inputTextFile;
  private String collectionName;
  private String nodeName;
  private String delimiter;
  /**
   * Is used for writing XML files.
   * @param configFile is the name of the output xml file.
   * @param inputTextFile is the name of the input txt file which is to be written from.
   * @param collectionName is the name of the overall collection of elements in the xml file.
   * @param nodeName is the name of the element in the collection.
   * @param delimiter is the delimiter that is used for seperating the subelement names from each other. It's these names
   * that are written in the first line of the txt file.
   */
  public StaxWriter(String configFile, String inputTextFile, String collectionName, String nodeName, String delimiter) {
	  this.configFile = configFile;
	  this.inputTextFile = inputTextFile;
	  this.collectionName = collectionName;
	  this.nodeName = nodeName;
	  this.delimiter = delimiter;
  }

  /**
   * Starts the creation the xml file, by initializing the xml file, and starts the writing for elements in the xml file.
 * @throws XMLStreamException is thrown.
 * @throws IOException is thrown.
   */
  public void saveConfig() throws XMLStreamException, IOException  {
    // Create a XMLOutputFactory
    XMLOutputFactory opF = XMLOutputFactory.newInstance();
    
    // Create XMLEventWriter
    XMLStreamWriter streamWriter = opF.createXMLStreamWriter(new FileOutputStream(configFile +".xml"), "UTF-8");
    
    // Create and write Start Tag
    streamWriter.writeStartDocument("UTF-8", "1.0");
    
    // Create config open tag
    streamWriter.writeDTD("\n");
    streamWriter.writeStartElement(collectionName);
    streamWriter.writeDTD("\n");
    
    //Creates a node with its values
    createValues(streamWriter);
  }
  
  /**
   * finds the subelements in the txt file and saves them for later use. Also, when the xml file is complete, this method ends the file.
   * @param streamWriter is the one handling the xml writing.
   * @throws XMLStreamException if the streamwriter makes an error.
   * @throws IOException is thrown.
   */
  private void createValues(XMLStreamWriter streamWriter) throws XMLStreamException, IOException {
	  File file = new File(inputTextFile);
	  BufferedReader reader = new BufferedReader(new FileReader(file));
	  String line = new String();
	  
	  if((line = reader.readLine()) != null) {
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
			  while((line = reader.readLine()) != null) {
				  createNodesForKDVunload(line, streamWriter, values);
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
  }
  
  /**
   * Creates a node, or element, with a given name in the xml file.
   * @param streamWriter the xml writer.
   * @param name the name of the element.
   * @param value the value for the element.
   * @throws XMLStreamException is the error thrown if the streamWriter doesnt work as expected.
   */
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
  
  /**
   * Handles creating nodes for "big" xml file. 
   * @param line is the line from which the value for the node, or element, is read from.
   * @param streamWriter is the one handling the writing of the xml file.
   * @param values is the array containing all of the different values extracted from the xml file earlier.
   * notice, it contains all values, even though they're not all used.
   * @throws XMLStreamException is thrown by the streamWriter if it makes an error.
   */
  private void createNodesForKDVunload(String line, XMLStreamWriter streamWriter, String[] values) throws XMLStreamException {
	  Scanner scan = new Scanner(line); 
	  scan.useDelimiter("(?<=[']|\\d|\\*)[,](?=[']|\\d|\\*)");    	
	  int[] intArray = {2,3,4,7,8,9,10,11,12,13,14,15,16,17,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33};
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
  
  /**
   * It checks if the element is one of the elements that isnt going to be used.
   * @param value th value to be checked.
   * @param intArray is the array containing the values that are not going to be used for the xml file.
   * @return the boolean answer.
   */
  private boolean isItDifferentFrom(int value, int[] intArray) {
	  boolean isDifferent = true;
	  for(int i = 0; i<intArray.length; i++) 
		  if(value == intArray[i])
			  isDifferent = false;
	  
	  return isDifferent;
  }
  
  /**
   * Handles creating nodes for "small" xml file. 
   * @param line is the line from which the value for the node, or element, is read from.
   * @param streamWriter is the one handling the writing of the xml file.
   * @param values is the array containing all of the different values extracted from the xml file earlier.
   * notice, it contains all values, even though they're not all used.
   * @throws XMLStreamException is thrown by the streamWriter if it makes an error.
   */
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