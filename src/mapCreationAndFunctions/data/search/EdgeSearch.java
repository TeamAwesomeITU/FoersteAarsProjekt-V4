package mapCreationAndFunctions.data.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;


public class EdgeSearch  {

	private static TernarySearchTrieEdge edgeSearchTrie = createEdgeSearchTrie();

	private static TernarySearchTrieEdge createEdgeSearchTrie()
	{
		TernarySearchTrieEdge tst = new TernarySearchTrieEdge();

		//Excludes Edges with no name
		for(Edge edge : DataHolding.getEdgeArray())
			if(!edge.getRoadName().isEmpty())
				tst.put(edge.getRoadName(), edge.getiD());

		return tst;
	}

	public static Edge[] searchForRoadName(String edgeToFind)
	{
		ArrayList<Integer> listOfFoundEdges = edgeSearchTrie.get(edgeToFind);

		Edge[] arrayOfFoundEdges = new Edge[listOfFoundEdges.size()];

		for (int i = 0; i < arrayOfFoundEdges.length; i++)
			arrayOfFoundEdges[i] = DataHolding.getEdge(listOfFoundEdges.get(i)); 

		return arrayOfFoundEdges;
	}

	/*
	public static Edge[] getRoadNameSuggestions(String edgeToFind)
	{
		Iterable<String> roadNames = edgeSearchTrie.prefixMatch(edgeToFind);
		Iterator<String> roadNameIterator = roadNames.iterator();
		ArrayList<Edge> roadList = new ArrayList<>();
		
		while(roadNameIterator.hasNext())
			roadList.add(roadNameIterator.next()));
			
		return roadList.toArray(new Edge[roadList.size()]);
	}
	*/

	/**
	 * Assumes that a city only has a single road by the given name
	 * @param edgeToFind
	 * @param postalNumber
	 * @return
	 */
	public static Edge searchForRoadNameInCity(String edgeToFind, int postalNumber)
	{
		ArrayList<Integer> listOfFoundEdges = edgeSearchTrie.get(edgeToFind);
		for(Integer ID : listOfFoundEdges)
		{
			Edge foundEdge = DataHolding.getEdge(ID);
			if(foundEdge.getPostalNumberLeft() == postalNumber || foundEdge.getPostalNumberRight() == postalNumber)
				return foundEdge;
		}

		return null;
	}	

	public static void main( String[] args )
	{
		double startTime = System.currentTimeMillis();
		Edge[] foundEdges = searchForRoadName("Nørregade");
		double endTime = System.currentTimeMillis();
		
		System.out.println("Search took: " + (endTime-startTime) + "miliseconds");

		//Edge edge = searchForRoadNameInCity("Nørregade", 4600);


		//System.out.println(edge.getRoadName());
		
		for(Edge edge : foundEdges)
			System.out.println(edge.getiD());
		
	}
}
