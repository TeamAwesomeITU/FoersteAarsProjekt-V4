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
	 * Searches for road names, that starts with the given string. Uses a really fucking stupid way to obtain suggestions - but it works, and its speed is acceptable.
	 * @param edgeToFind The name of the Edge to find
	 * @return An array of all the Edges, that starts with the given String
	 */
	public static Edge[] searchForRoadNameSuggestions(String edgeToFind)
	{
		edgeToFind = edgeToFind.toLowerCase();
		Iterable<String> roadNames = edgeSearchTrie.prefixMatch(edgeToFind);
		Iterator<String> roadNameIterator = roadNames.iterator();
		ArrayList<Edge> roadList = new ArrayList<>();

		while(roadNameIterator.hasNext())
		{
			String roadName = roadNameIterator.next().toLowerCase();
			Edge[] edges = searchForRoadName(roadName);
			for(Edge edge : edges)
				roadList.add(edge);
		}

		return roadList.toArray(new Edge[roadList.size()]);
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
	public static Edge[] searchForRoadNameNumberAndLetter(String roadName, int number, String letter)
	{
		roadName = roadName.toLowerCase();
		ArrayList<Edge> foundEdges = new ArrayList<Edge>();
		Edge[] possibleEdges = searchForRoadName(roadName);
		System.out.println(possibleEdges.length);

		boolean betweenNumbersRight;
		boolean betweenNumbersLeft; 
		int edgeNumber = 1;

		for(Edge edge : possibleEdges)
		{
			System.out.println(edgeNumber++);
			
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

		return foundEdges.toArray(new Edge[foundEdges.size()]);
	}

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


	public static void main(String[] args)
	{		
		
		Edge[] foundEdges = searchForRoadNameInCity("Nørre Boulevard", 4600);
		System.out.println(foundEdges.length);
		for(Edge edge : foundEdges)
			System.out.println(edge.toStringNumberAndLetterInfo());
		
		Edge[] foundEdges2 = searchForRoadNameInCity("Stadionvej", 6650);
		System.out.println(foundEdges2.length);
		for(Edge edge : foundEdges2)
			System.out.println(edge.toStringNumberAndLetterInfo());
		
		System.out.println('A' < 'B');
		System.out.println('A' < 'Æ');
		System.out.println('A' < 'Ø');
		System.out.println('A' < 'Å');
		System.out.println('Æ' < 'Ø');
		System.out.println('Æ' < 'Å');
		System.out.println('Ø' < 'Å');
		
		char letterChar = 'A';
		char letterChar2 = 'Ø';
		char letterChar3 = 'Å';
		
		System.out.println(letterChar + 0);
		System.out.println(letterChar2 + 0);
		System.out.println(letterChar3 + 0);
		
		System.out.println('E' > letterChar);
		System.out.println('A' < letterChar);
	}
}
