package mapDrawer.drawing;

/**
 * This class represents an Edge, i.e. a road segment.
 */
public class Edge {
	
	//The ID of the Node from which the Edge goes from
	private int fromNode;
	//The ID of the Node from which the Edge goes to
	private int toNode;
	//The roadType of the Edge
	private int roadType;
	//The roadName of the Edge
	private String roadName;
	//Postal number for the right side of the road!
	private int postalNumber;
	
	/**
	 * Constructs an Edge with the input parameters
	 * @param fromNode The ID of the Node from which the Edge goes from
	 * @param toNode The ID of the Node from which the Edge goes to
	 * @param roadType The roadType of the Edge
	 * @param roadName The roadName of the Edge
	 * @param postalNumber Postal number for the right side of the Edge
	 */
	public Edge(int fromNode, int toNode, int roadType, String roadName, int postalNumber)
	{
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.roadType = roadType;
		this.roadName = roadName;
		this.postalNumber = postalNumber;
	}
	
	/**
	 * Gets the ID of the Node, from which the Edge is started
	 * @return The ID of the Node, from which the Edge is started
	 */
	public int getFromNode()
	{
		return fromNode;
	}
	
	/**
	 * Gets the ID of the Node, from which the Edge is ended
	 * @return The ID of the Node, from which the Edge is ended
	 */
	public int getToNode()
	{
		return toNode;
	}

	/**
	 * Gets the road type of the Edge
	 * @return The road type of the Edge
	 */
	public int getRoadType()
	{
		return roadType;
	}

	/**
	 * Gets road name of the Edge
	 * @return The road name of the Edge
	 */
	public String getRoadName()
	{
		return roadName;
	}

	/**
	 * Gets postal number of the Edge
	 * @return The postal number of the Edge
	 */
	public int getPostalNumber()
	{
		return postalNumber;
	}

}
