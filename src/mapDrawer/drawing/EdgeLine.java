package mapDrawer.drawing;

/**
 * Is used for saving the edges with their coordinates and their roadtype.
 *
 */
public class EdgeLine {
	double startX, startY, endX, endY;
	int roadType;
	
	public EdgeLine(double sX, double sY, double eX, double eY, int roadType) {
		startX = sX;
		startY = sY;
		endX = eX;
		endY = eY;
		this.roadType = roadType;
	}
	
	/**
	 * returns endX coordinate
	 */
	public double getEndX() {
		return endX;
	}
	
	/**
	 * returns endY coordinate
	 */
	public double getEndY() {
		return endY;
	}
	
	/**
	 * returns startX coordinate
	 */
	public double getStartX() {
		return startX;
	}
	
	/**
	 * returns startY coordinate
	 */
	public double getStartY() {
		return startY;
	}
	
	/**
	 * Returns the roadtype
	 * @return
	 */
	public int getRoadType() {
		return roadType;
	}
}
