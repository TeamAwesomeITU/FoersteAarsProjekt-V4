package inputHandler;

import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

import java.util.ArrayList;
import java.util.HashSet;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.data.TernarySearchTrie;

/**
 * Enables searching for Edges with many different combinations of parameters.
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


		for(Edge edge : DataHolding.getEdgeArray())
			if(!edge.getRoadName().isEmpty()) //Excludes Edges with no name
				tst.put(edge.getRoadName().toLowerCase(), edge.getiD());

		return tst;
	}

	/**
	 * Searches for the specified road name
	 * @param roadToFind
	 * @return An array of all the Edges with the given road name.
	 */
	public static Edge[] searchForRoadName(String roadToFind)
	{
		if(roadToFind.trim().isEmpty())
			return new Edge[0];

		roadToFind = roadToFind.toLowerCase();
		ArrayList<Integer> listOfFoundEdges = edgeSearchTrie.get(roadToFind);

		Edge[] arrayOfFoundEdges = new Edge[listOfFoundEdges.size()];

		for (int i = 0; i < arrayOfFoundEdges.length; i++)
			arrayOfFoundEdges[i] = DataHolding.getEdge(listOfFoundEdges.get(i)); 

		return arrayOfFoundEdges;
	}

	/**
	 * Searches for the specified road name
	 * @param roadToFind
	 * @return The Edge with the longest prefix match for the given input
	 */
	public static String searchForRoadNameLongestPrefix(String roadToFind)
	{
		roadToFind = roadToFind.toLowerCase();
		return edgeSearchTrie.longestPrefixOf(roadToFind).trim();
	}

	/**
	 * Searches for Edges with the specific criterias
	 * @param roadName The name of the road
	 * @param roadNumber An optional number of the specific address - if no search on a number is wanted, enter -1 as the integer
	 * @param letter An optional letter of the specific address - if no search on a letter is wanted, enter an empty String as the letter parameter
	 * @param postalNumber An optional postal number of the specific address - if no search on a postal number is wanted, enter -1 as the integer
	 * @param cityName An optional String of the specific address' city name - if no search on a city name is wanted, enter an empty String as the letter parameter
	 * @return All of the Edges that fits the search criterias.
	 * @throws MalformedAddressException 
	 * @throws NoAddressFoundException 
	 */
	public static Edge[] searchForRoads(String roadName, int roadNumber, String letter, int postalNumber, String cityName) throws MalformedAddressException, NoAddressFoundException
	{
		checkInput(roadName, roadNumber, letter, postalNumber, cityName);

		//If postal number or city name are the only things that should be searched for
		if(roadName.isEmpty() && roadNumber == -1 && letter.isEmpty() && (postalNumber != -1 || !cityName.isEmpty()))
		{			
			//If city does not exist, return no roads
			if(postalNumber != -1 && !City.postalNumberExists(postalNumber))
				return new Edge[0];

			else if(postalNumber != -1 && City.postalNumberExists(postalNumber))
			{
				return City.getCityByPostalNumber(postalNumber).getCityRoads();		
			}


			else if(!cityName.isEmpty())
			{
				City city = City.getCityByCityName(cityName);
				if(city != null)
					return city.getCityRoads();
			}

		}

		roadName = roadName.toLowerCase();
		ArrayList<Edge> foundEdges = new ArrayList<Edge>();
		Edge[] possibleEdges = searchForRoadName(roadName);

		boolean betweenRoadNumbersRight;
		boolean betweenRoadNumbersLeft; 
		boolean betweenRoadLettersRight;
		boolean betweenRoadLettersLeft;
		boolean matchCityRight;
		boolean matchCityLeft;

		for(Edge edge : possibleEdges)
		{
			betweenRoadNumbersRight = isRoadNumberWithinRightSideOfEdge(edge, roadNumber);
			betweenRoadNumbersLeft = isRoadNumberWithinLeftSideOfEdge(edge, roadNumber);
			betweenRoadLettersRight = isRoadLetterWithinRightSideOfEdge(edge, letter);
			betweenRoadLettersLeft = isRoadLetterWithinLeftSideOfEdge(edge, letter);
			matchCityRight = isCityCorrectForEdge(edge, postalNumber, cityName);
			matchCityLeft = isCityCorrectForEdge(edge, postalNumber, cityName);

			if(betweenRoadNumbersRight && betweenRoadLettersRight)
			{
				if(matchCityRight || matchCityLeft)
					foundEdges.add(edge);
			}

			else if (betweenRoadNumbersLeft && betweenRoadLettersLeft)
			{
				if(matchCityRight || matchCityLeft)
					foundEdges.add(edge);
			}
		}


		return foundEdges.toArray(new Edge[foundEdges.size()]);
	}

	/**
	 * Searches for Edges with the specific criterias
	 * @param roadName The name of the road
	 * @param roadNumber An optional number of the specific address - if no search on a number is wanted, enter -1 as the integer
	 * @param letter An optional letter of the specific address - if no search on a letter is wanted, enter an empty String as the letter parameter
	 * @param postalNumber An optional postal number of the specific address - if no search on a postal number is wanted, enter -1 as the integer
	 * @param cityName An optional String of the specific address' city name - if no search on a city name is wanted, enter an empty String as the letter parameter
	 * @return All of the Edges that fits the search criterias.
	 * @throws MalformedAddressException 
	 * @throws NoAddressFoundException 
	 */
	private static void checkInput(String roadName, int roadNumber, String letter, int postalNumber, String cityName) throws NoAddressFoundException, MalformedAddressException
	{
		if(roadName.isEmpty() && roadNumber == -1 && letter.isEmpty() && postalNumber == -1 && cityName.isEmpty())
			throw new NoAddressFoundException("No address was found");
		else if(roadNumber == -1 && !letter.isEmpty())
			throw new MalformedAddressException("A road letter cannot be entered without having a road roadNumber");
		else if(roadNumber == 0 || postalNumber == 0)
			throw new MalformedAddressException("A road's road number or postal number cannot be 0");
		else if(!cityName.isEmpty() && postalNumber != -1)
			if(!CitySearch.doesCityNameMatchPostalNumber(cityName, postalNumber))
				throw new MalformedAddressException("Postal number and city name does not match");
	}

	private static boolean isCityCorrectForEdge( Edge edge, int postalNumber, String cityName )
	{ 
		//If the search should not include cities
		if(cityName.isEmpty() && postalNumber == -1)
			return true;

		if(!cityName.isEmpty() && City.cityNameExists(cityName))
			return City.getCityByCityName(cityName).containsRoad(edge.getiD());
		else if(City.postalNumberExists(postalNumber))
			return City.getCityByPostalNumber(postalNumber).containsRoad(edge.getiD()); 
		else {
			return false;
		}
	}


	/**
	 * Returns the Edges with a given road name, inside a specified city.
	 * @param roadToFind The name of the roads
	 * @param postalNumber The postal roadNumber, which the roads belongs to
	 * @return The wanted Edges - null, if no match could be found
	 */
	public static Edge[] searchForRoadNameInCity(String roadToFind, int postalNumber)
	{
		roadToFind = roadToFind.toLowerCase();
		ArrayList<Integer> listOfEdgeWithCorrectName = edgeSearchTrie.get(roadToFind);
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

		int toLeftNumber = edge.getToLeftNumber();
		int fromLeftNumber = edge.getFromLeftNumber();

		//Checks if the roadNumber is even or uneven, and if this matches the "evenness" of the numbers of this side of the road
		if(toLeftNumber != 0 && (toLeftNumber % 2) != (roadNumber % 2) || (fromLeftNumber != 0 && (fromLeftNumber % 2) != (roadNumber % 2)))
			return false; 
		return (fromLeftNumber <= roadNumber && toLeftNumber >= roadNumber );
	}

	private static boolean isRoadNumberWithinRightSideOfEdge(Edge edge, int roadNumber)
	{	
		//If the search should not include road number
		if(roadNumber == -1)
			return true;

		int toRightNumber = edge.getToRightNumber();
		int fromRightNumber = edge.getFromRightNumber();

		//Checks if the roadNumber is even or uneven, and if this matches the "evenness" of the numbers of this side of the road
		if(toRightNumber != 0 && (toRightNumber % 2) != (roadNumber % 2) || (fromRightNumber != 0 && (fromRightNumber % 2) != (roadNumber % 2)))
			return false; 
		return (fromRightNumber <= roadNumber && toRightNumber >= roadNumber );
	}

	private static boolean isRoadLetterWithinLeftSideOfEdge(Edge edge, String letter)
	{
		//If the search should not include letters
		if(letter.isEmpty()) 
			return true;

		//If there are entered more than one letter in the string, return false
		if(letter.length() > 1)
			return false;

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
}
