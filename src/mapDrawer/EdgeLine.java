package mapDrawer;

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
	
	public double getEndX() {
		return endX;
	}
	
	public double getEndY() {
		return endY;
	}
	
	public double getStartX() {
		return startX;
	}
	
	public double getStartY() {
		return startY;
	}
	public int getRoadType() {
		return roadType;
	}
}
