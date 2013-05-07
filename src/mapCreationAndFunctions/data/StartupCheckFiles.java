package mapCreationAndFunctions.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;

import mapCreationAndFunctions.search.QuadTree;


@SuppressWarnings({"serial" ,"unused"})
@Deprecated
public class StartupCheckFiles implements Serializable {

	private static String storedObjectsLocation = "storedObjects/";
	private static String xmlFilesLocation = "XML/";

	private static String indexFileName = storedObjectsLocation + "index.lol";
	private static String quadTreeFileName = storedObjectsLocation + "quadTree.lol";
	private static String nodeArrayFileName = storedObjectsLocation + "coordinatesMap.lol";
	private static String edgeArrayFileName = storedObjectsLocation + "edgeSet.lol";

	//Which XML-files to check - the index file is expected to have the XML files stored in this order
	private static String[] xmlFilesToCheck = new String[]{xmlFilesLocation + "kdv_node_unload.xml", xmlFilesLocation + "kdv_unload_new.xml"};
	//The sums of the newly checked XML files, in the same order as the xmlFilesToCheck-array
	private static String[] newXMLFilesMD5sum = new String[xmlFilesToCheck.length];
	private static String[] storedXMLFilesMD5sum = loadIndex(indexFileName);

	private static final boolean shouldLoad = checkFilesBeforeStartup();

	//Not really necessary, is used to make sure that the objects are stored if needed
	private static boolean objectsStored = storeAllObjects();

	public static boolean checkFilesBeforeStartup()
	{		
		for(int i = 0; i < xmlFilesToCheck.length; i++)
		{
			newXMLFilesMD5sum[i] = generateCheckSum(xmlFilesToCheck[i]);
		}

		boolean shouldObjectsBeLoaded = true;

		//If the index file was loaded correctly, check it
		if(storedXMLFilesMD5sum != null)
		{
			for (int i = 0; i < xmlFilesToCheck.length; i++)
			{
				shouldObjectsBeLoaded = newXMLFilesMD5sum[i].equals(storedXMLFilesMD5sum[i]); 
			}
		}

		else 
			shouldObjectsBeLoaded = false;

		//If the stored objects should not be loaded, the XML files must have changed - make a new index
		if(!shouldObjectsBeLoaded)
			createIndexFileAndStoreObjects(newXMLFilesMD5sum);

		return shouldObjectsBeLoaded;
	}

	private static boolean storeAllObjects()
	{
		if(!shouldLoad)
		{
			storeQuadTree();
			storeNodeArray();
			storeEdgeArray();	
		}
		return true;
	}

	private static String[] loadIndex( String indexFileName )
	{		
		try {
			ObjectInputStream inStream;
			inStream = new ObjectInputStream(new FileInputStream(indexFileName));
			Object object = inStream.readObject();
			inStream.close();
			return (String[]) object;
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void storeQuadTree()
	{
		QuadTree qTree = QuadTree.getEntireQuadTree();
		storeObject(qTree, quadTreeFileName);		
	}

	
	private static void storeNodeArray()
	{
		Node[] nodeArray = DataHolding.getNodeArray();
		storeObject(nodeArray, nodeArrayFileName);		
	}

	private static void storeEdgeArray()
	{
		Edge[] edgeArray = DataHolding.getEdgeArray();
		storeObject(edgeArray, edgeArrayFileName);	
	}

	private static void storeObject(Object object, String fileNameAndPath)
	{		
		System.out.println("Storing object: " + fileNameAndPath);
		try {
			FileOutputStream fos = new FileOutputStream(fileNameAndPath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Object stored!");
	}

	private static void createIndexFileAndStoreObjects(String[] generatedSums)
	{
		storeObject(generatedSums, indexFileName);
	}

	private static String generateCheckSum(String fileName)
	{		
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(fileName);

			byte[] dataBytes = new byte[1024];

			int nread = 0; 
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			};
			byte[] mdbytes = md.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				String hex = Integer.toHexString(0xff & mdbytes[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			System.out.println("Digest(in hex format):: " + hexString.toString());

			fis.close();
			return hexString.toString();

		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	//IF NullPointerException happens here, it's because of the cast to something - add check for null
	public static QuadTree getStoredQuadTree()
	{
		return (QuadTree) loadFile(quadTreeFileName);	
	}

	public static Node[] getStoredNodeArray()
	{
		return (Node[]) loadFile(nodeArrayFileName);		
	}	

	public static Edge[] getStoredEdgeArray()
	{
		return (Edge[]) loadFile(edgeArrayFileName);	
	}

	private static Object loadFile(String fileName)
	{
		if(!shouldLoad)
			return null;
		else
		{
			System.out.println("Loading file: " + fileName);
			ObjectInputStream inStream;
			try {
				inStream = new ObjectInputStream(new FileInputStream(fileName));
				Object object = inStream.readObject();
				inStream.close();
				return object;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}

	}

	public static boolean runStartupCheck()
	{
		return shouldLoad;
	}
}
