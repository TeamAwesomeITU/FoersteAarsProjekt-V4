package mapCreationAndFunctions.data.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;


public class EdgeSearch  {

	private static TernarySearchTrie edgeSearchTrie = createEdgeSearchTrie();
	
	private static TernarySearchTrie createEdgeSearchTrie()
	{
		TernarySearchTrie tst = new TernarySearchTrie();
		
		for(Edge edge : DataHolding.getEdgeArray())
			tst.put(edge.getRoadName(), edge.getiD());
		
		return tst;
	}
	
	private static Edge[] createEdgeArrayByRoadName(Edge[] arr)
	{
	}	
	
	private static Edge[] createEdgeArrayByRoadTypeCategory(Edge[] arr)
	{
	}
	
	//Returner kun den første Edge - dvs. hvis der er flere forekomster af navnet, finder den kun den første!! Smider desuden IndexOutOfBounds-exception, hvis vejen ikke kan findes
	public static Edge[] searchForRoadName(String edgeToFind)
	{
	}
	
	
	//Returner kun den første Edge - dvs. hvis der er flere forekomster af navnet, finder den kun den første!!
	public static Edge searchForRoadNameInCity(String edgeToFind, int postalNumber)
	{
	}	
	
	public static void main( String[] args )
	{
		System.out.println(edgeSearchTrie.get("Nørregade"));
	}
}
