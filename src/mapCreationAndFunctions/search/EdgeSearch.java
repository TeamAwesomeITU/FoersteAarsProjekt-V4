package mapCreationAndFunctions.search;

import java.util.ArrayList;
import java.util.Iterator;

import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;

/**
 * Enables searching for Edges by road name, or by road name and postal number
 */
public class EdgeSearch  {

	private static TernarySearchTrie edgeSearchTrie = createEdgeSearchTrie();

	/**
	 * Creates a ternary search tree 
	 * @return the created ternary search tree.
	 */
	private static TernarySearchTrie createEdgeSearchTrie()
	{
		TernarySearchTrie tst = new TernarySearchTrie();

		//Excludes Edges with no name
		for(Edge edge : DataHolding.getEdgeArray())
			if(!edge.getRoadName().isEmpty())
				tst.put(edge.getRoadName(), edge.getiD());

		return tst;
	}

	/**
	 * Searches for the specified road name
	 * @param edgeToFind
	 * @return An array of all the Edges with the given road name.
	 */
	public static Edge[] searchForRoadName(String edgeToFind)
	{
		ArrayList<Integer> listOfFoundEdges = edgeSearchTrie.get(edgeToFind);

		Edge[] arrayOfFoundEdges = new Edge[listOfFoundEdges.size()];

		for (int i = 0; i < arrayOfFoundEdges.length; i++)
			arrayOfFoundEdges[i] = DataHolding.getEdge(listOfFoundEdges.get(i)); 

		return arrayOfFoundEdges;
	}

	/**
	 * Searches for road names, that starts with the given string. Uses a really fucking stupid way to obtain suggestions - but it works, and its speed is acceptable.
	 * @param edgeToFind The name of the Edge to find
	 * @return An array of all the Edges, that starts with the given String
	 */
	public static Edge[] getRoadNameSuggestions(String edgeToFind)
	{
		Iterable<String> roadNames = edgeSearchTrie.prefixMatch(edgeToFind);
		Iterator<String> roadNameIterator = roadNames.iterator();
		ArrayList<Edge> roadList = new ArrayList<>();

		while(roadNameIterator.hasNext())
		{
			String roadName = roadNameIterator.next();
			Edge[] edges = searchForRoadName(roadName);
			for(Edge edge : edges)
				roadList.add(edge);
		}

		return roadList.toArray(new Edge[roadList.size()]);
	}

	/**
	 * Returns the Edge with the given road name, inside a specified city. Assumes that a city only has a single road by the given name.
	 * @param edgeToFind The name of the road
	 * @param postalNumber The postal number, which the road belongs to
	 * @return The wanted Edge - null, if no match could be found
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
}
