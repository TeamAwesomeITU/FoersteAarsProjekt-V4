package mapDrawer.drawing;

import java.awt.Graphics;

public class Edge {
	
	//The ID of the Node from which the Edge goes from
	private final int fromNode;
	
	//The ID of the Node from which the Edge goes to
	private final int toNode;
	
	//The length of the Edge in meters
	private final double length;
	
	private final int iD;
	
	//The roadType of the Edge
	private final int roadType;
	
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
	private final char fromLeftLetter;
	
	//The end of the house letters on the	left
	private final char toLeftLetter;
	
	//The beginning of the house letters on the	right
	private final char fromRightLetter;
	
	//The end of the house letters on the right 
	private final char toRightLetter;
	
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

	public Edge(int fromNode, int toNode, double length, int iD, int roadType, String roadName, int fromLeftNumber,
			int toLeftNumber, int fromRightNumber, int toRightNumber, char fromLeftLetter, char toLeftLetter, char fromRightLetter,
			char toRightLetter, int postalNumberLeft, int postalNumberRight, int highWayTurnoff, double driveTime, String oneWay, int tjekID,
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
	}
	
	/**
	 * ONLY FOR TEST-PURPOSES
	 */
	public Edge(int fromNode, int toNode, int roadType, String roadName, int postalNumber)
	{
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.length = 0.0;
		this.iD = 1;
		this.roadType = roadType;
		this.roadName = roadName;
		this.fromLeftNumber = 1;
		this.toLeftNumber = 1;
		this.fromRightNumber = 1;
		this.toRightNumber = 1;
		this.fromLeftLetter = 'a';
		this.toLeftLetter = 'a';
		this.fromRightLetter = 'a';
		this.toRightLetter = 'a';
		this.postalNumberLeft = postalNumber;
		this.postalNumberRight = 1;
		this.highWayTurnoff = 1;
		this.driveTime = 0.0;
		this.oneWay = "rf";
		this.tjekID = 2;
		this.fromTurn = 2;
		this.toTurn = 2;
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
	public char getFromLeftLetter() {
		return fromLeftLetter;
	}

	/**
	 * @return the toLeftLetter of the Edge
	 */
	public char getToLeftLetter() {
		return toLeftLetter;
	}

	/**
	 * @return the fromRightLetter of the Edge
	 */
	public char getFromRightLetter() {
		return fromRightLetter;
	}

	/**
	 * @return the toRightLetter of the Edge
	 */
	public char getToRightLetter() {
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


}
