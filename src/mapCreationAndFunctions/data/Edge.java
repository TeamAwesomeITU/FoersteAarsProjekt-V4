package mapCreationAndFunctions.data;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import mapCreationAndFunctions.RoadType;

public class Edge {
	
	//The ID of the Node from which the Edge goes from
	private final int fromNode;
	
	//The ID of the Node from which the Edge goes to
	private final int toNode;
	
	//The length of the Edge in meters
	private final double length;
	
	//The ID of the Edge
	private final int iD;
	
	//The roadType of the Edge
	private final int roadType;
	
	//The roadTypeCategory of the Edge
	private final int roadTypeCategory;
	
	//The roadName of the Edge
	private final String roadName;
	
	//The beginning of the house numbers on the	left
	private final int fromLeftNumber;
	
	//The end of the house numbers on the left
	private final int toLeftNumber;
	
	////The beginning of the house numbers on the right
	private final int fromRightNumber;
	
	////The end of the house numbers on the right
	private final int toRightNumber;
	
	//The beginning of the house letters on the	left
	private final String fromLeftLetter;
	
	//The end of the house letters on the	left
	private final String toLeftLetter;
	
	//The beginning of the house letters on the	right
	private final String fromRightLetter;
	
	//The end of the house letters on the right 
	private final String toRightLetter;
	
	//The postal numbers on the left
	private final int postalNumberLeft;
	
	//The postal numbers on the left
	private final int postalNumberRight;
	
	//The ID of the turnoff, if the Edge has one.
	private final int highWayTurnoff;
	
	//Estimated driving time
	private final double driveTime;
	
	//tf = one way in the direction To-From, ft = one way in the direction From-To,	n = No driving allowed,	<blank> = No restrictions
	private final String oneWay;
	
	//Road ID to use with turn restrictions
	private final int tjekID;
	
	//Road ID to use with turn restrictions
	private final int fromTurn;
	
	//Road ID to use with turn restrictions
	private final int toTurn;
	
	/**
	 * 
	 * @param fromNode The ID of the Node, the Edge goes from
	 * @param toNode The ID of the Node, the Edge goes to
	 * @param length The length of the Edge in meters
	 * @param iD The ID of the Edge
	 * @param roadType The Edge's road type
	 * @param roadName The road name of the Edge
	 * @param fromLeftNumber The first number of the house numbers on the left side of the Edge
	 * @param toLeftNumber The last number of the house numbers on the left side of the Edge
	 * @param fromRightNumber The first number of the house numbers on the right side of the Edge
	 * @param toRightNumber The last number of the house numbers on the right side of the Edge
	 * @param fromLeftLetter The lowest letter of the house letters on the left side of the Edge
	 * @param toLeftLetter The highest letter of the house letters on the left side of the Edge
	 * @param fromRightLetter The lowest letter of the house letters on the right side of the Edge
	 * @param toRightLetter The highest letter of the house letters on the right side of the Edge
	 * @param postalNumberLeft The postal number of the left side of the Edge
	 * @param postalNumberRight The postal number of the right side of the Edge
	 * @param highWayTurnoff The ID of the highway turnoff
	 * @param driveTime The time it takes to pass the road, in minutes
	 * @param oneWay tf = one way in the direction To-From, ft = one way in the direction From-To,	n = No driving allowed,	<blank> = No restrictions
	 * @param tjekID //TODO wtf goes here?
	 * @param fromTurn //TODO wtf goes here?
	 * @param toTurn //TODO wtf goes here?
	 */
	public Edge(int fromNode, int toNode, double length, int iD, int roadType, String roadName, int fromLeftNumber,
			int toLeftNumber, int fromRightNumber, int toRightNumber, String fromLeftLetter, String toLeftLetter, String fromRightLetter,
			String toRightLetter, int postalNumberLeft, int postalNumberRight, int highWayTurnoff, double driveTime, String oneWay, int tjekID,
			int fromTurn, int toTurn) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.length = length;
		this.iD = iD;
		this.roadType = roadType;
		this.roadName = roadName;
		this.fromLeftNumber = fromLeftNumber;
		this.toLeftNumber = toLeftNumber;
		this.fromRightNumber = fromRightNumber;
		this.toRightNumber = toRightNumber;
		this.fromLeftLetter = fromLeftLetter;
		this.toLeftLetter = toLeftLetter;
		this.fromRightLetter = fromRightLetter;
		this.toRightLetter = toRightLetter;
		this.postalNumberLeft = postalNumberLeft;
		this.postalNumberRight = postalNumberRight;
		this.highWayTurnoff = highWayTurnoff;
		this.driveTime = driveTime;
		this.oneWay = oneWay;
		this.tjekID = tjekID;
		this.fromTurn = fromTurn;
		this.toTurn = toTurn;
		roadTypeCategory = RoadType.getCategory(roadType);
	}

	/**
	 * @return the fromNode of the Edge
	 */
	public int getFromNode() {
		return fromNode;
	}

	/**
	 * @return the toNode of the Edge
	 */
	public int getToNode() {
		return toNode;
	}

	/**
	 * @return the length of the Edge
	 */
	public double getLength() {
		return length;
	}

	/**
	 * @return the iD of the Edge
	 */
	public int getiD() {
		return iD;
	}

	/**
	 * @return the roadType of the Edge
	 */
	public int getRoadType() {
		return roadType;
	}

	/**
	 * @return the roadName of the Edge
	 */
	public String getRoadName() {
		return roadName;
	}

	/**
	 * @return the fromLeftNumber of the Edge
	 */
	public int getFromLeftNumber() {
		return fromLeftNumber;
	}

	/**
	 * @return the toLeftNumber of the Edge
	 */
	public int getToLeftNumber() {
		return toLeftNumber;
	}

	/**
	 * @return the fromRightNumber of the Edge
	 */
	public int getFromRightNumber() {
		return fromRightNumber;
	}

	/**
	 * @return the toRightNumber of the Edge
	 */
	public int getToRightNumber() {
		return toRightNumber;
	}

	/**
	 * @return the fromLeftLetter of the Edge
	 */
	public String getFromLeftLetter() {
		return fromLeftLetter;
	}

	/**
	 * @return the toLeftLetter of the Edge
	 */
	public String getToLeftLetter() {
		return toLeftLetter;
	}

	/**
	 * @return the fromRightLetter of the Edge
	 */
	public String getFromRightLetter() {
		return fromRightLetter;
	}

	/**
	 * @return the toRightLetter of the Edge
	 */
	public String getToRightLetter() {
		return toRightLetter;
	}

	/**
	 * @return the postalNumberLeft of the Edge
	 */
	public int getPostalNumberLeft() {
		return postalNumberLeft;
	}

	/**
	 * @return the postalNumberRight of the Edge
	 */
	public int getPostalNumberRight() {
		return postalNumberRight;
	}
	
	/**
	 * @return the postalNumberLeft of the Edge
	 */
	public String getPostalNumberLeftCityName() {
		return City.getCityNameByPostalNumber(postalNumberLeft);
	}

	/**
	 * @return the postalNumberRight of the Edge
	 */
	public String getPostalNumberRightCityName() {
		return City.getCityNameByPostalNumber(postalNumberRight);
	}

	/**
	 * @return the highWayTurnoff of the Edge
	 */
	public int getHighWayTurnoff() {
		return highWayTurnoff;
	}

	/**
	 * @return the driveTime of the Edge
	 */
	public double getDriveTime() {
		return driveTime;
	}

	/**
	 * @return the oneWay of the Edge
	 */
	public String getOneWay() {
		return oneWay;
	}

	/**
	 * @return the tjekID of the Edge
	 */
	public int getTjekID() {
		return tjekID;
	}

	/**
	 * @return the fromTurn of the Edge
	 */
	public int getFromTurn() {
		return fromTurn;
	}

	/**
	 * @return the toTurn of the Edge
	 */
	public int getToTurn() {
		return toTurn;
	}
	
	/**
	 * 
	 * @return the category of which the Edge's road type belong to
	 */
	public int getRoadTypeCategory()
	{
		return roadTypeCategory;
	}
	
	/**
	 * Returns a graphic Line2D representation of the Edge
	 * @param coordconverter The CoordConverter with which the coordinates should be converted
	 * @return A Line2D representation of the Edge
	 */
	public Line2D getLine2DToDraw(CoordinateConverter coordconverter)
	{
		Node fromNode = DataHolding.getNode(getFromNode());
		Node toNode = DataHolding.getNode(getToNode());
		
		return new Line2D.Double(coordconverter.UTMToPixelCoordX(fromNode.getXCoord()),
				coordconverter.UTMToPixelCoordY(fromNode.getYCoord()), coordconverter.UTMToPixelCoordX(toNode.getXCoord()),
				coordconverter.UTMToPixelCoordY(toNode.getYCoord()));
	}
	
	
	//TODO lad vær med at lave en line2d for hver Edge - lav i stedet en intersectmetode i edge?
	public boolean intersects(Rectangle2D rectangle2d)
	{
		Node fromNode = DataHolding.getNode(getFromNode());
		Node toNode = DataHolding.getNode(getToNode());
		Line2D line2d = new Line2D.Double(fromNode.getXCoord(), fromNode.getYCoord(), toNode.getXCoord(), toNode.getYCoord());		
		return line2d.intersects(rectangle2d);
	}
	

}
