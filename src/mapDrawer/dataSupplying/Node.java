package mapDrawer.dataSupplying;

public class Node {
	private int ID;
	private double xCoord;
	private double yCoord;

	public Node(int ID, double xCoord, double yCoord)
	{
		this.ID = ID;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}	

	public int getID()
	{ return ID; }

	public double getXCoord()
	{ return xCoord; }

	public double getYCoord()
	{ return yCoord; }
}