package mapDrawer;

public class Node {
	private int ID;
	private double xCoord;
	private double yCoord;

	//ID of the Edges, that goes to or from the Node
	private int[] edgesFrom;
	private int[] edgesTo;

	public Node(int ID, double xCoord, double yCoord, int[] edgesFrom, int[] edgesTo)
	{
		this.ID = ID;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.edgesFrom = edgesFrom;
		this.edgesTo = edgesTo;
	}	

	public int getID()
	{ return ID; }

	public double getXCoord()
	{ return xCoord; }

	public double getYCoord()
	{ return yCoord; }

	public int[] getEdgesFrom()
	{ return edgesFrom; }

	public int[] getEdgesTo()
	{ return edgesTo; }		
}