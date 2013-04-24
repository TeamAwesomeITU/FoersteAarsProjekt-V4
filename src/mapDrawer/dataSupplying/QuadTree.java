package mapDrawer.dataSupplying;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import mapDrawer.AreaToDraw;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.NegativeAreaSizeException;
import mapDrawer.exceptions.InvalidAreaProportionsException;

/**
 *  A QuadTree containing up to four Nodes, and if more Nodes are added, the QuadTree is split into four QuadTrees, each having a quarter of the original QuadTree's area, and the Node is inserted in one of them.
 */
public class QuadTree {

	private static QuadTree qTree;	

	//The maximum amount of Nodes, that each QuadTree can hold
	private final static int QUADTREE_CAPACITY = 4;

	//An array of the contained Nodes
	private Node[] nodeArray = new Node[QUADTREE_CAPACITY];
	
	//Number of nodes in the QuadTree
	private int numberOfQuadTreeNodes;

	//The boundaries of this QuadTree
	private AreaToDraw area;

	//Four sub QuadTrees, "Leafs"
	private QuadTree northWestNode;
	private QuadTree northEastNode;
	private QuadTree southWestNode;
	private QuadTree southEastNode;
	
	/**
	 * Creates a QuadTree with the specified area
	 * @param area The area for which the QuadTree should be constructed
	 */
	private QuadTree(AreaToDraw area)
	{
		this.area = area;
		numberOfQuadTreeNodes = 0;
	}

	/**
	 * Attempts to insert a Node in the QuadTree
	 * @param node The Node to be inserted
	 * @return If the Node could be inserted in this QuadTree, this method returns true.
	 */
	private boolean insert(Node node)
	{
		//If the Node is not inside this QuadTree's area, don't add it
		if(!area.isNodeInsideArea(node))
			return false;

		//If there is room for an extra Node in the current QuadTree's array, put it in and count up the number of Nodes
		if(numberOfQuadTreeNodes < QUADTREE_CAPACITY)
		{
			nodeArray[numberOfQuadTreeNodes++] = node;
			return true;
		}

		if (northWestNode == null)
			try {
				subdivide();
			} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e) {
				e.printStackTrace();
			} 

		if (northWestNode.insert(node)) return true;
		if (northEastNode.insert(node)) return true;
		if (southWestNode.insert(node)) return true;
		if (southEastNode.insert(node)) return true;

		//If something goes really wrong, return false
		return false;
	}

	/**
	 * Divides the current QuadTree's area into four areas, which each is exactly a quarter of the current QuadTrees area.
	 * @throws NegativeAreaSizeException
	 * @throws AreaIsNotWithinDenmarkException
	 * @throws InvalidAreaProportionsException
	 */
	private void subdivide() throws NegativeAreaSizeException, AreaIsNotWithinDenmarkException, InvalidAreaProportionsException {
	
		double midPointX = area.getWidth()/2 + area.getSmallestX();
		double midPointY = area.getHeight()/2 + area.getSmallestY();

		AreaToDraw northWestArea = new AreaToDraw(area.getSmallestX(), midPointX, midPointY, area.getLargestY(), false);
		AreaToDraw northEastArea = new AreaToDraw(midPointX, area.getLargestX(), midPointY, area.getLargestY(), false);
		AreaToDraw southWestArea = new AreaToDraw(area.getSmallestX(), midPointX, area.getSmallestY(), midPointY, false);
		AreaToDraw southEastArea = new AreaToDraw(midPointX, area.getLargestX(), area.getSmallestY(), midPointY, false);

		northWestNode = new QuadTree(northWestArea);
		northEastNode = new QuadTree(northEastArea);
		southWestNode = new QuadTree(southWestArea);
		southEastNode = new QuadTree(southEastArea);
	}

	/**
	 * Finds all of the Nodes which are contained within a specified area.
	 * @param area The area to search for Nodes.
	 * @return A HashSet of all Nodes found within the specified area.
	 */
	private HashSet<Node> search(AreaToDraw area)
	{
		HashSet<Node> foundNodeSet= new HashSet<Node>();

		if(!area.isAreaIntersectingWithArea(this.area))
			return foundNodeSet; //Returns an empty set

		for(int i = 0; i < numberOfQuadTreeNodes; i++)
		{
			if(area.isNodeInsideArea(nodeArray[i]))
				foundNodeSet.add(nodeArray[i]);
		}

		//If this QuadTree has no other QuadTrees, stop
		if(northWestNode == null)
			return foundNodeSet;

		//Otherwise, add the points from the underlying QuadTrees
		foundNodeSet.addAll(northWestNode.search(area));
		foundNodeSet.addAll(northEastNode.search(area));
		foundNodeSet.addAll(southWestNode.search(area));
		foundNodeSet.addAll(southEastNode.search(area));

		return foundNodeSet;
	}
	
	/**
	 * Finds the ID's of all Nodes which are contained within a specified area.
	 * @param area The area to search for Nodes.
	 * @return A HashSet of the ID's of all Nodes found within the specified area.
	 */
	private HashSet<Integer> searchForNodeIDs(AreaToDraw area)
	{
		HashSet<Integer> foundNodeSet= new HashSet<Integer>();

		if(!area.isAreaIntersectingWithArea(this.area))
			return foundNodeSet; //Returns an empty set

		for(int i = 0; i < numberOfQuadTreeNodes; i++)
		{
			if(area.isNodeInsideArea(nodeArray[i]))
				foundNodeSet.add(nodeArray[i].getID());
		}

		//If this QuadTree has no other QuadTrees, stop
		if(northWestNode == null)
			return foundNodeSet;

		//Otherwise, add the points from the underlying QuadTrees
		foundNodeSet.addAll(northWestNode.searchForNodeIDs(area));
		foundNodeSet.addAll(northEastNode.searchForNodeIDs(area));
		foundNodeSet.addAll(southWestNode.searchForNodeIDs(area));
		foundNodeSet.addAll(southEastNode.searchForNodeIDs(area));

		return foundNodeSet;
	}

	
	
	
	/**
	 * Creates a QuadTree from an XML file.
	 * @return The created QuadTree
	 */
	private static QuadTree makeQuadTreeFromXML()
	{
		AreaToDraw area = new AreaToDraw();
		
		QuadTree quadTree = new QuadTree(area);

		long startTime = System.currentTimeMillis();
		try {
			VTDGen vgNode = new VTDGen();
			
			if(vgNode.parseFile("XML/kdv_node_unload.xml", false)) {
				VTDNav vnNode = vgNode.getNav();
				AutoPilot apNode = new AutoPilot(vnNode);

				double yCoord = 0.0; double xCoord = 0.0; int KDV = 0;
				apNode.selectXPath("//nodeCollection/node");

				int count = 0;
				while (apNode.evalXPath() != -1) {
					vnNode.toElement(VTDNav.FIRST_CHILD, "Y-COORD"); 
					yCoord = vnNode.parseDouble(vnNode.getText());

					vnNode.toElement(VTDNav.PREV_SIBLING, "X-COORD"); 
					xCoord = vnNode.parseDouble(vnNode.getText());

					vnNode.toElement(VTDNav.PREV_SIBLING, "KDV");		
					KDV = vnNode.parseInt(vnNode.getText());

					//if(quadTree.insert(new Node(KDV, xCoord, yCoord)))
					//	count++;

					vnNode.toElement(VTDNav.PARENT);
				}
				System.out.println("Total# of element in large QuadTree: " + count);
			}
		} catch (VTDException e){
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("QuadTree creation tager " + (endTime - startTime) + " milliseconds");
		return quadTree;
	}
	
	
	
	private static QuadTree makeQuadTreeAndNodeMapFromTXT()
	{

		try {				
			AreaToDraw area = new AreaToDraw();	
			QuadTree quadTree = new QuadTree(area);
			
			//File file = new File("XML/kdv_node_unload.txt");
			File file = new File("XML/kdv_node_unload.txt_modified.txt");			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			//To skip the first line
			reader.readLine();
			
			String line;
			
			while((line = reader.readLine()) != null)
			{
				String[] lineParts = line.split("\\,");
				Integer KDV = Integer.parseInt(lineParts[0]);
				Double[] coords = new Double[]{Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[2])};	
				
				//Edge ID's is pulled out
				String[] edgeIDsAsStrings = lineParts[3].split("\\s+");
				int[] edgeIDs = new int[edgeIDsAsStrings.length];				
				for (int i = 0; i < edgeIDsAsStrings.length; i++) {
					edgeIDs[i] = Integer.parseInt(edgeIDsAsStrings[i]);
				}
								
				quadTree.insert(new Node(KDV, coords[0], coords[1], edgeIDs));
			}
				
			reader.close();
			
			//noget her giver synk issues - fordi QuadTree stadig kører og laver nodemap, imens nodemaps initializer tjekker om den er null, hvilket den er men ikke må være
			//FindRelevantEdges.initializeNodeCoordinatesMap(nodeMap);
			return quadTree;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	/**
	 * Finds all of the Nodes which are contained within a specified area.
	 * @param area The area to search for Nodes.
	 * @return A HashSet of all Nodes found within the specified area.
	 */
	public static HashSet<Node> searchAreaForNodes(AreaToDraw area)
	{
		return getEntireQuadTree().search(area);
	}
	
	/**
	 * Finds the ID's of all Nodes which are contained within a specified area.
	 * @param area The area to search for Nodes.
	 * @return A HashSet of the ID's of all Nodes found within the specified area.
	 */
	public static HashSet<Integer> searchAreaForNodeIDs(AreaToDraw area)
	{
		return getEntireQuadTree().searchForNodeIDs(area);
	}
		
	/**
	 * Returns the entire static QuadTree
	 * @return The entire static QuadTree
	 */
	public static QuadTree getEntireQuadTree()
	{
		initializeQuadTree();
		return qTree;
	}
	
	private static void initializeQuadTree()
	{
		if(qTree == null)
			qTree = makeQuadTreeAndNodeMapFromTXT();
			//qTree = makeQuadTreeFromXML();
		else
			return;
	}
	
	public static class QuadTreeCreation implements Runnable {
		
		public QuadTreeCreation()
		{	}
		
		public void run() 
		{
			initializeQuadTree();
		}		
	}
}
