package mapDrawer.drawing;

public class Edge {
	
	private int fromNode;
	private int toNode;
	private int roadType;
	private String roadName;
	//Postal number for the right side of the road!
	private int postalNumber;
	
	public Edge(int fromNode, int toNode, int roadType, String roadName, int postalNumber)
	{
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.roadType = roadType;
		this.roadName = roadName;
		this.postalNumber = postalNumber;
	}
	
	public int getFromNode()
	{
		return fromNode;
	}
	
	public int getToNode()
	{
		return toNode;
	}

	
	public int getRoadType()
	{
		return roadType;
	}

	
	public String getRoadName()
	{
		return roadName;
	}

	
	public int getPostalNumber()
	{
		return postalNumber;
	}

}
