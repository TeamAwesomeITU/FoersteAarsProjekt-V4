package mapDrawer.dataSupplying;

import java.util.Arrays;
import java.util.Comparator;

import mapDrawer.RoadType;
import mapDrawer.drawing.Edge;

public class EdgeSort  {

	static final Comparator<Edge> ID_ORDER = new Comparator<Edge>() {
		public int compare(Edge e1, Edge e2) {
			Integer ID1 = new Integer(e1.getiD());
			Integer ID2 = new Integer(e2.getiD());
			return ID1.compareTo(ID2);
		}
	};
	
	static final Comparator<Edge> ROADNAME_ORDER = new Comparator<Edge>() {
		public int compare(Edge e1, Edge e2) {
			return e1.getRoadName().compareTo(e2.getRoadName());
		}
	};

	static final Comparator<Edge> ROADTYPECATEGORY_ORDER = new Comparator<Edge>() {
		public int compare(Edge e1, Edge e2) {
			Integer cat1 = new Integer(e1.getRoadTypeCategory());
			Integer cat2 = new Integer(e2.getRoadTypeCategory());
			return cat1.compareTo(cat2);

		}
	};
	
	public static void main( String[] args ) {
		Edge[] edges = DataHolding.getEdgeArray();
		
		int wantedPrintNumber = 10;
		
		for (int i = 0; i < wantedPrintNumber; i++) {
			System.out.println(edges[i].getiD());
		}
		
		Arrays.sort(edges, ID_ORDER);
		
		for (int i = 0; i < wantedPrintNumber; i++) {
			System.out.println(edges[i].getiD());
		}
		
		Arrays.sort(edges, ROADNAME_ORDER);
		
		for (int i = 0; i < wantedPrintNumber; i++) {
			System.out.println(edges[i].getRoadName());
		}
		
		
		Arrays.sort(edges, ROADTYPECATEGORY_ORDER);
		
		for (int i = 0; i < wantedPrintNumber; i++) {
			System.out.println(edges[i].getRoadTypeCategory());
		}
	}
}
