package mapDrawer.drawing;

/**
 * Is used for saving the Edges with their coordinates and their roadtype.
 *
 */
public class EdgeLine {
	double startX, startY, endX, endY;
	int roadType;
	
	/**
	 * Constructs an EdgeLine from the input parameters.
	 * @param sX The x-coordinate of the Edge's starting point.
	 * @param sY The y-coordinate of the Edge's starting point.
	 * @param eX The x-coordinate of the Edge's end point.
	 * @param eY The y-coordinate of the Edge's end point.
	 * @param roadType The roadType of the Edge.
	 */
	public EdgeLine(double sX, double sY, double eX, double eY, int roadType) {
		startX = sX;
		startY = sY;
		endX = eX;
		endY = eY;
		this.roadType = roadType;
	}
	
	/**
	 * Returns the x-coordinate of the Edge's end point.
	 * @return The x-coordinate of the Edge's end point.
	 */
	public double getEndX() {
		return endX;
	}
	
	/**
	 * Returns the y-coordinate of the Edge's end point.
	 * @return The y-coordinate of the Edge's end point.
	 */
	public double getEndY() {
		return endY;
	}
	
	/**
	 * Returns the x-coordinate of the Edge's starting point.
	 * @return The x-coordinate of the Edge's starting point.
	 */
	public double getStartX() {
		return startX;
	}
	
	/**
	 * Returns the y-coordinate of the Edge's starting point.
	 * @return The y-coordinate of the Edge's starting point.
	 */
	public double getStartY() {
		return startY;
	}
	
	/**
	 * Returns the roadtype of the Edge
	 * @return The roadtype of the Edge
	 */
	public int getRoadType() {
		return roadType;
	}
}
