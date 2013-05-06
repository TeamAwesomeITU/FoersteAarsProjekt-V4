package mapDrawer.dataSupplying;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import mapDrawer.drawing.Edge;

public class EdgeSearch  {

	@Deprecated
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
	
	private static Edge[] edgeArrayByRoadName = createEdgeArrayByRoadName(DataHolding.getEdgeArray());
	private static Edge[] edgeArrayByRoadTypeCategory = createEdgeArrayByRoadTypeCategory(DataHolding.getEdgeArray());
	
	private static Edge[] createEdgeArrayByRoadName(Edge[] arr)
	{
		Edge[] newArr = arr.clone();
		Arrays.sort(newArr, ROADNAME_ORDER);
		
		ArrayList<Edge> actualRoadsWithNames = new ArrayList<Edge>();
		
		//Removes the Edges, that doesn't have an actual roadName
		for(Edge edge : newArr)
			if(!edge.getRoadName().isEmpty())
				actualRoadsWithNames.add(edge);
		
		System.out.println("actualRoadsWithNames: " + actualRoadsWithNames.size());
		
		return actualRoadsWithNames.toArray(new Edge[actualRoadsWithNames.size()]);
	}	
	
	private static Edge[] createEdgeArrayByRoadTypeCategory(Edge[] arr)
	{
		Edge[] newArr = arr.clone();
		Arrays.sort(newArr, ROADTYPECATEGORY_ORDER);
		return newArr;
	}
	
	//Returner kun den første Edge - dvs. hvis der er flere forekomster af navnet, finder den kun den første!! Smider desuden IndexOutOfBounds-exception, hvis vejen ikke kan findes
	public static Edge[] searchForRoadName(String edgeToFind)
	{
		//FANGER KUN VEJNAVNET, HVIS DET ER STAVET HELT KORREKT - KOMBINER DENNE KLASSE MED BINARYSEARCHSTRINGINARRAY
		int resultAt = Arrays.binarySearch(edgeArrayByRoadName, edgeToFind, ROADNAME_ORDER);
		ArrayList<Edge> listOfResults = new ArrayList<Edge>();
		boolean foundAllResults = false;
		
		//As long as all results have not been found and the binary search actually have found a result.
		while(!foundAllResults && resultAt >= 0)
		{
			Edge edge = edgeArrayByRoadName[resultAt++];
			if(edgeToFind.equals(edge.getRoadName()))
				listOfResults.add(edge);
			else
				foundAllResults = true;
		}
			
		return listOfResults.toArray(new Edge[listOfResults.size()]);
	}
	
	
	//Returner kun den første Edge - dvs. hvis der er flere forekomster af navnet, finder den kun den første!!
	public static Edge searchForRoadNameInCity(String edgeToFind, int postalNumber)
	{
		//FANGER KUN VEJNAVNET, HVIS DET ER STAVET HELT KORREKT - KOMBINER DENNE KLASSE MED BINARYSEARCHSTRINGINARRAY
		int resultAt = Arrays.binarySearch(edgeArrayByRoadName, edgeToFind);
		return(edgeArrayByRoadName[resultAt]);
	}	
	
	public static void main( String[] args )
	{
		
		Edge[] foundEdges = searchForRoadName("Nørregade");
		for (Edge edge : foundEdges)
			System.out.println(edge.getRoadName());

	}
}
