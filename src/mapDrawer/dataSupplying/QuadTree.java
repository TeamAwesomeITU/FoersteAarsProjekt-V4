package mapDrawer.dataSupplying;

import java.util.HashSet;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import mapDrawer.AreaToDraw;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.NegativeAreaSizeException;
import mapDrawer.exceptions.InvalidAreaProportionsException;

public class QuadTree {

	private static QuadTree qTree = makeQuadTreeFromXML();	

	//The maximum amount of Nodes, that each QuadTree can hold
	private final static int QUADTREE_CAPACITY = 4;

	//An array of the contained Nodes
	private Node[] nodeArray = new Node[QUADTREE_CAPACITY];
	
	//Number of nodes in the QuadTree
	private int numberOfQuadTreeNodes;

	//The boundaries of this quadtree
	private AreaToDraw area;

	//Four sub QuadTrees, "Leafs"
	private QuadTree northWestNode;
	private QuadTree northEastNode;
	private QuadTree southWestNode;
	private QuadTree southEastNode;

	private QuadTree(AreaToDraw area)
	{
		this.area = area;
		numberOfQuadTreeNodes = 0;
	}

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

	private void subdivide() throws NegativeAreaSizeException, AreaIsNotWithinDenmarkException, InvalidAreaProportionsException {
	
		double midPointX = area.getWidth()/2 + area.getSmallestX();
		double midPointY = area.getHeight()/2 + area.getSmallestY();

		AreaToDraw northWestArea = new AreaToDraw(area.getSmallestX(), midPointX, midPointY, area.getLargestY());
		AreaToDraw northEastArea = new AreaToDraw(midPointX, area.getLargestX(), midPointY, area.getLargestY());
		AreaToDraw southWestArea = new AreaToDraw(area.getSmallestX(), midPointX, area.getSmallestY(), midPointY);
		AreaToDraw southEastArea = new AreaToDraw(midPointX, area.getLargestX(), area.getSmallestY(), midPointY);

		northWestNode = new QuadTree(northWestArea);
		northEastNode = new QuadTree(northEastArea);
		southWestNode = new QuadTree(southWestArea);
		southEastNode = new QuadTree(southEastArea);
	}

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

					if(quadTree.insert(new Node(KDV, xCoord, yCoord)))
						count++;

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
	
	public static HashSet<Node> searchAreaForNodes(AreaToDraw area)
	{
		return qTree.search(area);
	}
	
	public static HashSet<Integer> searchAreaForNodeIDs(AreaToDraw area)
	{
		return qTree.searchForNodeIDs(area);
	}

	public static void main(String[] args) throws NegativeAreaSizeException, AreaIsNotWithinDenmarkException, InvalidAreaProportionsException
	{	
		AreaToDraw area = new AreaToDraw();
		
		long startTime = System.currentTimeMillis();
		HashSet<Node> nodeSet = QuadTree.searchAreaForNodes(area);
		long endTime = System.currentTimeMillis();
		System.out.println("Size of retrieved HashSet<Node>: " + nodeSet.size());
		System.out.println("At retrieve alle nodes fra QuadTree tager " + (endTime - startTime) + " milliseconds");
		
		long startTime2 = System.currentTimeMillis();
		nodeSet = QuadTree.searchAreaForNodes(area);
		long endTime2 = System.currentTimeMillis();
		System.out.println("Size of retrieved HashSet<Node>: " + nodeSet.size());
		System.out.println("At retrieve alle nodes fra QuadTree tager " + (endTime2 - startTime2) + " milliseconds");
		
		long startTime3 = System.currentTimeMillis();
		nodeSet = QuadTree.searchAreaForNodes(area);
		long endTime3 = System.currentTimeMillis();
		System.out.println("Size of retrieved HashSet<Node>: " + nodeSet.size());
		System.out.println("At retrieve alle nodes fra QuadTree tager " + (endTime3 - startTime3) + " milliseconds");
		
		long startTime4 = System.currentTimeMillis();
		nodeSet = QuadTree.searchAreaForNodes(area);
		long endTime4 = System.currentTimeMillis();
		System.out.println("Size of retrieved HashSet<Node>: " + nodeSet.size());
		System.out.println("At retrieve alle nodes fra QuadTree tager " + (endTime4 - startTime4) + " milliseconds");
		
		double maxXHalf = (area.getWidth()/2) + area.getSmallestX();
		long startTime5 = System.currentTimeMillis();
		nodeSet = QuadTree.searchAreaForNodes(new AreaToDraw(maxXHalf, area.getLargestX(), area.getSmallestY(), area.getLargestY()));
		long endTime5 = System.currentTimeMillis();
		System.out.println("Size of retrieved HashSet<Node>: " + nodeSet.size());
		System.out.println("At retrieve 1/2 af kortets nodes fra QuadTree tager " + (endTime5 - startTime5) + " milliseconds");
		
		
		double maxYQuarter = (area.getHeight()/2) + area.getSmallestY();		
				
		long startTime6 = System.currentTimeMillis();
		nodeSet = QuadTree.searchAreaForNodes(new AreaToDraw(area.getSmallestX(), maxXHalf, area.getSmallestY(), maxYQuarter));
		long endTime6 = System.currentTimeMillis();
		System.out.println("Size of retrieved HashSet<Node>: " + nodeSet.size());
		System.out.println("At retrieve 1/4 af kortets nodes fra QuadTree tager " + (endTime6 - startTime6) + " milliseconds");
		
		
	}


}
