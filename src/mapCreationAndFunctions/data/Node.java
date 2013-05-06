package mapCreationAndFunctions.data;

/**
 * Represents a point on the map, where an Edge is drawn to or from.
 */
public class Node {
	//This ID an unique identifier for this Node
	private int ID;
	
	//The x-coordinate of the Node
	private double xCoord;
	
	//The y-coordinate of the Node
	private double yCoord;
	
	private int[] edgeIDs;

	public Node(int ID, double xCoord, double yCoord, int[] edgeIDs)
	{
		this.ID = ID;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.edgeIDs = edgeIDs;
	}	

	/**
	 * 
	 * @return The Node's ID
	 */
	public int getID()
	{ return ID; }

	/**
	 * 
	 * @return The Node's x-coordinate
	 */
	public double getXCoord()
	{ return xCoord; }

	/**
	 * 
	 * @return The Node's y-coordinate
	 */
	public double getYCoord()
	{ return yCoord; }
	
	/**
	 * 
	 * @return The ID's of the Edges that goes to or from the Node
	 */
	public int[] getEdgeIDs()
	{ return edgeIDs; }
}