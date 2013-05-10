package mapCreationAndFunctions.search;

import inputHandler.exceptions.MalformedAdressException;

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
				tst.put(edge.getRoadName().toLowerCase(), edge.getiD());

		return tst;
	}

	/**
	 * Searches for the specified road name
	 * @param edgeToFind
	 * @return An array of all the Edges with the given road name.
	 */
	public static Edge[] searchForRoadName(String edgeToFind)
	{
		edgeToFind = edgeToFind.toLowerCase();
		ArrayList<Integer> listOfFoundEdges = edgeSearchTrie.get(edgeToFind);

		Edge[] arrayOfFoundEdges = new Edge[listOfFoundEdges.size()];

		for (int i = 0; i < arrayOfFoundEdges.length; i++)
			arrayOfFoundEdges[i] = DataHolding.getEdge(listOfFoundEdges.get(i)); 

		return arrayOfFoundEdges;
	}

	/**
	 * Searches for Edges with a specific roadname, an optional number and an optional letter. If no search on a letter is wanted, enter an empty String as the letter parameter,
	 * @param roadName The name of the road
	 * @param number TODO
	 * @param letter An optional letter of the specific address - if no search on a letter is wanted, enter -1 as the integer
	 * @param letter An optional letter of the specific address - if no search on a letter is wanted, enter an empty String as the letter parameter
	 * @return All of the Edges that fits the search criterias.
	 * @throws MalformedAdressException 
	 */
	public static Edge[] searchForRoadSuggestions(String roadName, int number, String letter) throws MalformedAdressException
	{
		if(roadName.isEmpty())
			throw new MalformedAdressException("No roadname was entered");
		if(number == -1 && !letter.isEmpty())
			throw new MalformedAdressException("A road letter cannot be entered without having a road number");
		if(number == 0)
			throw new MalformedAdressException("A road number cannot be 0");

		roadName = roadName.toLowerCase();
		ArrayList<Edge> foundEdges = new ArrayList<Edge>();
		Iterable<String> possibleEdges = edgeSearchTrie.prefixMatch(roadName);
		Iterator<String> iterator = possibleEdges.iterator();

		boolean betweenNumbersRight;
		boolean betweenNumbersLeft; 
		int edgeNumber = 1;

		while(iterator.hasNext())
		{
			Edge[] edges = searchForRoadName(iterator.next().toLowerCase());
			System.out.println(edgeNumber++);

			for(Edge edge : edges)
			{
				//If the search should only include road name
				if(number == -1)
					foundEdges.add(edge);
				
				//If the search should include road number
				else
				{
					betweenNumbersRight = isRoadNumberWithinRightSideOfEdge(edge, number);
					betweenNumbersLeft = isRoadNumberWithinLeftSideOfEdge(edge, number);

					System.out.println(betweenNumbersLeft + " " + betweenNumbersRight);

					//If the given number is NOT in the Edge's interval
					if(!betweenNumbersRight && !betweenNumbersLeft )
						System.out.println("This edge is NOT in ANY of the correct intervals");

					//If the Edge only has correct numbers on the right side's interval
					else if(betweenNumbersRight && !betweenNumbersLeft )
					{
						System.out.println("This edge is in the correct interval of the right side!");
						if(letter.isEmpty())
							foundEdges.add(edge);

						else
							if(isRoadLetterWithinRightSideOfEdge(edge, letter))
								foundEdges.add(edge);
					}

					//If the Edge only has correct numbers on the left side's interval
					else if(betweenNumbersLeft && !betweenNumbersRight )
					{
						System.out.println("This edge is in the correct interval of the left side!");
						if(letter.isEmpty())
							foundEdges.add(edge);

						else
							if(isRoadLetterWithinLeftSideOfEdge(edge, letter))
								foundEdges.add(edge);
					}

					//Both sides of the Edge have the number in their intervals
					else
					{
						System.out.println("This edge is in the correct interval of both sides!");
						if(letter.isEmpty())
							foundEdges.add(edge);

						else
							if(isRoadLetterWithinLeftSideOfEdge(edge, letter) || isRoadLetterWithinRightSideOfEdge(edge, letter))
								foundEdges.add(edge);
					}
				}
			}
		}

		return foundEdges.toArray(new Edge[foundEdges.size()]);
	}

	/**
	 * Returns the Edge with the given road name, inside a specified city.
	 * @param edgeToFind The name of the road
	 * @param postalNumber The postal number, which the road belongs to
	 * @return The wanted Edge - null, if no match could be found
	 */
	public static Edge[] searchForRoadNameInCity(String edgeToFind, int postalNumber)
	{
		edgeToFind = edgeToFind.toLowerCase();
		ArrayList<Integer> listOfEdgeWithCorrectName = edgeSearchTrie.get(edgeToFind);
		ArrayList<Edge> foundEdges = new ArrayList<Edge>();

		for(Integer ID : listOfEdgeWithCorrectName)
		{
			Edge foundEdge = DataHolding.getEdge(ID);
			if(foundEdge.getPostalNumberLeft() == postalNumber || foundEdge.getPostalNumberRight() == postalNumber)
				foundEdges.add(foundEdge);
		}

		return foundEdges.toArray(new Edge[foundEdges.size()]);
	}	

	/**
	 * Searches for Edges with a specific roadname, number and an optional letter. If no search on a letter is wanted, enter an empty String as the letter parameter,
	 * @param roadName The name of the road
	 * @param number The number of the specific address
	 * @param letter An optional letter of the specific address - if no search on a letter is wanted, enter an empty String as the letter parameter,
	 * @return All of the Edges that fits the search criterias.
	 */

	private static boolean isRoadNumberWithinLeftSideOfEdge(Edge edge, int number)
	{ 
		//Checks if the number is even or uneven, and if this matches the "evenness" of the numbers of this side of the road
		if(edge.getToLeftNumber() != 0 && (edge.getToLeftNumber() % 2) != (number % 2) || (edge.getFromLeftNumber() != 0 && (edge.getFromLeftNumber() % 2) != (number % 2)))
			System.out.println("Number was not equal to this sides evenness");//return false;
		System.out.println(edge.getFromLeftNumber() + "<= " + number + ", " + edge.getToLeftNumber() + ">=" +  number);
		return (edge.getFromLeftNumber() <= number && edge.getToLeftNumber() >= number );
	}

	private static boolean isRoadNumberWithinRightSideOfEdge(Edge edge, int number)
	{	
		//Checks if the number is even or uneven, and if this matches the "evenness" of the numbers of this side of the road
		if(edge.getToRightNumber() != 0 && (edge.getToRightNumber() % 2) != (number % 2) || (edge.getFromRightNumber() != 0 && (edge.getFromRightNumber() % 2) != (number % 2)))
			System.out.println("Number was not equal to this sides evenness");//return false;
		System.out.println(edge.getFromRightNumber() + "<= " + number + ", " + edge.getToRightNumber() + ">=" +  number);
		return (edge.getFromRightNumber() <= number && edge.getToRightNumber() >= number );
	}

	private static boolean isRoadLetterWithinLeftSideOfEdge(Edge edge, String letter)
	{
		System.out.println("Noget med road letter left side");
		String fromLetter = edge.getFromLeftLetter();
		String toLetter = edge.getToLeftLetter();
		char letterChar = letter.charAt(0);	

		//If no letters are found on this side of the road
		if(fromLetter.isEmpty() && toLetter.isEmpty())
			return false;

		//If the char should be checked within an interval
		else if(!fromLetter.isEmpty() && !toLetter.isEmpty())
			return (fromLetter.charAt(0) >= letterChar && toLetter.charAt(0) <= letterChar );

		else
		{
			if(!fromLetter.isEmpty())
				return (fromLetter.charAt(0) >= letterChar);
			else
				return (toLetter.charAt(0) <= letterChar);
		}
	}

	private static boolean isRoadLetterWithinRightSideOfEdge(Edge edge, String letter)
	{
		System.out.println("Noget med road letter right side");
		String fromLetter = edge.getFromRightLetter();
		String toLetter = edge.getToRightLetter();
		char letterChar = letter.charAt(0);	

		if(fromLetter.isEmpty() && toLetter.isEmpty())
			return false;

		//If the char should be checked within an interval
		else if(!fromLetter.isEmpty() && !toLetter.isEmpty())
			return (fromLetter.charAt(0) >= letterChar && toLetter.charAt(0) <= letterChar );

		else
		{
			if(!fromLetter.isEmpty())
				return (fromLetter.charAt(0) >= letterChar);
			else
				return (toLetter.charAt(0) <= letterChar);
		}
	}


	public static void main(String[] args) throws MalformedAdressException
	{		
		Edge[] foundEdges = searchForRoadSuggestions("Nørregade", 2, "");
		System.out.println(foundEdges.length);
		/*
		for(Edge edge : foundEdges)
			System.out.println(edge.getRoadName());
		*/
		
		Edge[] foundEdges2 = searchForRoadSuggestions("Nørregade", -1, "");
		System.out.println(foundEdges2.length);
		/*
		for(Edge edge : foundEdges2)
			System.out.println(edge.getRoadName());
			*/
	}
}
