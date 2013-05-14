package mapCreationAndFunctions.search;

import inputHandler.exceptions.MalformedAdressException;

import java.util.ArrayList;
import java.util.Iterator;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;

/**
 * Enables searching for Edges by road name, or by road name and postal roadNumber
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
	 * Searches for Edges with the specific criterias
	 * @param roadName The name of the road
	 * @param roadNumber An optional number of the specific address - if no search on a number is wanted, enter -1 as the integer
	 * @param letter An optional letter of the specific address - if no search on a letter is wanted, enter an empty String as the letter parameter
	 * @param postalNumber An optional postal number of the specific address - if no search on a postal number is wanted, enter -1 as the integer
	 * @param cityName An optional String of the specific address' city name - if no search on a city name is wanted, enter an empty String as the letter parameter
	 * @return All of the Edges that fits the search criterias.
	 * @throws MalformedAdressException 
	 */
	public static Edge[] searchForRoadSuggestions(String roadName, int roadNumber, String letter, int postalNumber, String cityName) throws MalformedAdressException
	{
		if(roadName.isEmpty())
			throw new MalformedAdressException("No roadname was entered");
		if(roadNumber == -1 && !letter.isEmpty())
			throw new MalformedAdressException("A road letter cannot be entered without having a road roadNumber");
		if(roadNumber == 0 || postalNumber == 0)
			throw new MalformedAdressException("A road's road number or postal number cannot be 0");
		if(!cityName.isEmpty() && postalNumber != -1)
			if(!CitySearch.doesCityNameMatchPostalNumber(cityName, postalNumber))
				throw new MalformedAdressException("Postal number and city name does not match");

		roadName = roadName.toLowerCase();
		ArrayList<Edge> foundEdges = new ArrayList<Edge>();
		Iterator<String> iterator = edgeSearchTrie.prefixMatch(roadName).iterator();

		boolean betweenRoadNumbersRight;
		boolean betweenRoadNumbersLeft; 
		boolean betweenRoadLettersRight;
		boolean betweenRoadLettersLeft;
		boolean matchCityRight;
		boolean matchCityLeft;

		while(iterator.hasNext())
		{
			Edge[] edges = searchForRoadName(iterator.next().toLowerCase());
			for(Edge edge : edges)
			{
				betweenRoadNumbersRight = isRoadNumberWithinRightSideOfEdge(edge, roadNumber);
				betweenRoadNumbersLeft = isRoadNumberWithinLeftSideOfEdge(edge, roadNumber);
				betweenRoadLettersRight = isRoadLetterWithinRightSideOfEdge(edge, letter);
				betweenRoadLettersLeft = isRoadLetterWithinLeftSideOfEdge(edge, letter);
				matchCityRight = isCityCorrectForRightSideOfEdge(edge, postalNumber, cityName);
				matchCityLeft = isCityCorrectForLeftSideOfEdge(edge, postalNumber, cityName);
				
//				System.out.println("betweenRoadLettersRight: " + betweenRoadLettersRight);
//				System.out.println("betweenRoadNumbersLeft: " + betweenRoadNumbersLeft);
//				System.out.println("betweenRoadLettersRight: " + betweenRoadLettersRight);
//				System.out.println("betweenRoadLettersLeft: " + betweenRoadLettersLeft);
//				System.out.println("matchCityRight: " + matchCityRight);
//				System.out.println("matchCityLeft: " + matchCityLeft);

				if(betweenRoadNumbersRight && betweenRoadLettersRight && matchCityRight)
				{
						foundEdges.add(edge);
				}
				
				else if (betweenRoadNumbersLeft && betweenRoadLettersLeft && matchCityLeft)
				{
						foundEdges.add(edge);
				}
			}
		}

		return foundEdges.toArray(new Edge[foundEdges.size()]);
	}

	private static boolean isCityCorrectForLeftSideOfEdge( Edge edge, int postalNumber, String cityName )
	{ 
		//If the search should not include cities
		if(cityName.isEmpty() && postalNumber == -1)
			return true;
		
		if(!cityName.isEmpty())
			return City.getCityByCityName(cityName).getCityPostalNumbers().contains(edge.getPostalNumberLeft());
		else
			return edge.getPostalNumberLeft() == postalNumber; 
	}

	private static boolean isCityCorrectForRightSideOfEdge( Edge edge, int postalNumber, String cityName )
	{ 
		//If the search should not include cities
		if(cityName.isEmpty() && postalNumber == -1)
			return true;
		
		if(!cityName.isEmpty())
			return City.getCityByCityName(cityName).getCityPostalNumbers().contains(edge.getPostalNumberRight());
		else
			return edge.getPostalNumberRight() == postalNumber; 
	}

	/**
	 * Returns the Edges with a given road name, inside a specified city.
	 * @param edgeToFind The name of the roads
	 * @param postalNumber The postal roadNumber, which the roads belongs to
	 * @return The wanted Edges - null, if no match could be found
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
	 * Searches for Edges with a specific roadname, roadNumber and an optional letter. If no search on a letter is wanted, enter an empty String as the letter parameter,
	 * @param roadName The name of the road
	 * @param roadNumber The roadNumber of the specific address
	 * @param letter An optional letter of the specific address - if no search on a letter is wanted, enter an empty String as the letter parameter,
	 * @return All of the Edges that fits the search criterias.
	 */

	private static boolean isRoadNumberWithinLeftSideOfEdge(Edge edge, int roadNumber)
	{ 
		//If the search should not include road number
		if(roadNumber == -1)
			return true;
		
		//Checks if the roadNumber is even or uneven, and if this matches the "evenness" of the numbers of this side of the road
//		if(edge.getToLeftNumber() != 0 && (edge.getToLeftNumber() % 2) != (roadNumber % 2) || (edge.getFromLeftNumber() != 0 && (edge.getFromLeftNumber() % 2) != (roadNumber % 2)))
//			System.out.println("roadNumber was not equal to this sides evenness");//return false;
//		System.out.println(edge.getFromLeftNumber() + "<= " + roadNumber + ", " + edge.getToLeftNumber() + ">=" +  roadNumber);
		return (edge.getFromLeftNumber() <= roadNumber && edge.getToLeftNumber() >= roadNumber );
	}

	private static boolean isRoadNumberWithinRightSideOfEdge(Edge edge, int roadNumber)
	{	
		//If the search should not include road number
		if(roadNumber == -1)
			return true;
		
		//Checks if the roadNumber is even or uneven, and if this matches the "evenness" of the numbers of this side of the road
//		if(edge.getToRightNumber() != 0 && (edge.getToRightNumber() % 2) != (roadNumber % 2) || (edge.getFromRightNumber() != 0 && (edge.getFromRightNumber() % 2) != (roadNumber % 2)))
//			System.out.println("roadNumber was not equal to this sides evenness");//return false;
//		System.out.println(edge.getFromRightNumber() + "<= " + roadNumber + ", " + edge.getToRightNumber() + ">=" +  roadNumber);
		return (edge.getFromRightNumber() <= roadNumber && edge.getToRightNumber() >= roadNumber );
	}

	private static boolean isRoadLetterWithinLeftSideOfEdge(Edge edge, String letter)
	{
		//If the search should not include letters
		if(letter.isEmpty()) 
			return true;
		
		//If there are entered more than one letter in the string, return false
		if(letter.length() > 1)
			return false;
		
		//System.out.println("Noget med road letter left side");
		String fromLetter = edge.getFromLeftLetter().toLowerCase();
		String toLetter = edge.getToLeftLetter().toLowerCase();
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
				return (fromLetter.charAt(0) == letterChar);
			else
				return (toLetter.charAt(0) == letterChar);
		}
	}

	private static boolean isRoadLetterWithinRightSideOfEdge(Edge edge, String letter)
	{
		//If the search should not include letters
		if(letter.isEmpty()) 
			return true;
		
		//If there are entered more than one letter in the string, return false
		if(letter.length() > 1)
			return false;
		
//		System.out.println("Noget med road letter right side");
		String fromLetter = edge.getFromRightLetter().toLowerCase();
		String toLetter = edge.getToRightLetter().toLowerCase();
		char letterChar = letter.charAt(0);	

		if(fromLetter.isEmpty() && toLetter.isEmpty())
			return false;

		//If the char should be checked within an interval
		else if(!fromLetter.isEmpty() && !toLetter.isEmpty())
			return (fromLetter.charAt(0) >= letterChar && toLetter.charAt(0) <= letterChar );

		else
		{
			if(!fromLetter.isEmpty())
				return (fromLetter.charAt(0) == letterChar);
			else
				return (toLetter.charAt(0) == letterChar);
		}
	}


	public static void main(String[] args) throws MalformedAdressException
	{		
		/*
		Edge[] foundEdges = searchForRoadSuggestions("Nørregade", 2, "", -1, "");
		System.out.println(foundEdges.length);

		for(Edge edge : foundEdges)
			System.out.println(edge);		

		Edge[] foundEdges2 = searchForRoadSuggestions("Nørregade", -1, "", -1, "");
		System.out.println(foundEdges2.length);

		for(Edge edge : foundEdges2)
			System.out.println(edge);

		Edge[] foundEdges3 = searchForRoadSuggestions("Stadionvej", 4, "A", -1, "");
		System.out.println(foundEdges3.length);

		for(Edge edge : foundEdges3)
			System.out.println(edge + " " + edge.getiD());

		Edge[] foundEdges4 = searchForRoadSuggestions("Næstvedvej", 24, "A", -1, "");
		System.out.println(foundEdges4.length);

		for(Edge edge : foundEdges4)
			System.out.println(edge + " " + edge.getiD());

		
		Edge[] foundEdges5 = searchForRoadSuggestions("Næstvedvej", 24, "I", 4230);
		System.out.println(foundEdges5.length);

		for(Edge edge : foundEdges5)
			System.out.println(edge + " " + edge.getiD());


		HashSet<Integer> foundShittyPostalNumberSet = new HashSet<>();
		int leftNumber = 0;
		int rightNumber = 0;
		for(Edge edge : DataHolding.getEdgeArray())
		{
			leftNumber = edge.getPostalNumberLeft();
			rightNumber = edge.getPostalNumberRight();

			try {
				System.out.println(City.getCityNameByPostalNumber(leftNumber));
			} catch (NullPointerException e) {
				foundShittyPostalNumberSet.add(leftNumber);
			}

			try {
				System.out.println(City.getCityNameByPostalNumber(rightNumber));
			} catch (NullPointerException e) {
				foundShittyPostalNumberSet.add(rightNumber);
			}
		}

		Iterator<Integer> iterator = foundShittyPostalNumberSet.iterator();
		while (iterator.hasNext()) {
			System.out.print(", " + iterator.next());			
		}
		 */
		
		Edge[] foundEdges5 = searchForRoadSuggestions("Stadionvej", 2, "b 6", -1, "");
		System.out.println(foundEdges5.length);

		for(Edge edge : foundEdges5)
			System.out.println(edge + " " + edge.getiD());
	}
}
