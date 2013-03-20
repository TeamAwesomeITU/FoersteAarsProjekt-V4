package mapDrawer;

public class EdgeCoords {
	double startX, startY, endX, endY;
	
	public EdgeCoords(double sX, double sY, double eX, double eY) {
		startX = sX;
		startY = sY;
		endX = eX;
		endY = eY;
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
}
