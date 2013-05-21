package inputHandler;

import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

import java.util.HashSet;

import mapCreationAndFunctions.data.Edge;

/**
 * Enables searching for adresses with a single String
 *
 */
public class AddressSearch {
	
	//The AddressParser, which transforms the String into a String[] of roadname, number etc.
	private AddressParser ap;
	//The input recieved from the AddressParser
	private String[] parsedInput;
	//The Edges, that corresponds with the transformed input
	private Edge[] foundEdges = new Edge[0];
	
	public AddressSearch()
	{
		ap = new AddressParser();		
	}
		
	/**
	 * Finds all the Edges that corresponds with the given input and saves them in an array.
	 * @param input
	 * @throws MalformedAddressException
	 * @throws NoAddressFoundException
	 */
	public void searchForAdress(String input) throws MalformedAddressException, NoAddressFoundException
	{
		ap = new AddressParser();
		parseAddress(input);
		int roadNumber = -1;
		int postalNumber = -1;
		
		try {
			roadNumber = Integer.parseInt(parsedInput[1]);
		} catch (NumberFormatException e) {}
		
		try {
			postalNumber = Integer.parseInt(parsedInput[4]);
		} catch (NumberFormatException e) {}
				
		foundEdges = EdgeSearch.searchForRoads(parsedInput[0], roadNumber, parsedInput[2], postalNumber, parsedInput[5]);
	}
	
	/**
	 * Gets all of the found Edges
	 * @return all of the found Edges
	 */
	public Edge[] getFoundEdges()
	{
		return foundEdges;
	}
	
	/**
	 * Gets a single Edge to use for navigation - if the search for Edges have found different Edges in different cities, an exception is thrown.
	 * @return The first of the found Edges
	 * @throws NoAddressFoundException Is thrown if the search for Edges have found different Edges in different cities
	 */
	public Edge getEdgeToNavigate() throws NoAddressFoundException
	{
		if(foundEdges.length == 0)
			throw new NoAddressFoundException("No address was found with this input");
				
		if(doesRoadNamesMatch(foundEdges) && doesRoadsCityMatch(foundEdges))
			return foundEdges[0];
		else {
			throw new NoAddressFoundException("Several roads in different cities were found - please enter a more specific adress");
		}
			
	}
	
	/**
	 * Clears the search for Edges
	 */
	public void clearResults()
	{ foundEdges = new Edge[0];	}
	
	/**
	 * Checks if all given Edges have the same road name
	 * @param edgesToCheck The Edges to check
	 * @return True, if all given Edges have the same road name
	 */
	private boolean doesRoadNamesMatch(Edge[] edgesToCheck)
	{
		String name = edgesToCheck[0].getRoadName();
		for (int i = 1; i < edgesToCheck.length; i++) 
			if(!name.equalsIgnoreCase(edgesToCheck[i].getRoadName()))
				return false;
		System.out.println("ALL ROADNAMES MATCHES");
		return true;
	}
	
	/**
	 * Checks if all given Edges lie within the same City
	 * @param edgesToCheck The Edges to check
	 * @return True, if all given Edges lie within the same City
	 */
	private boolean doesRoadsCityMatch(Edge[] edgesToCheck)
	{
		HashSet<Integer> relevantEdgeIDsLeft = edgesToCheck[0].getPostalNumberLeftCity().getCityRoadIDs();
		HashSet<Integer> relevantEdgeIDsRight = edgesToCheck[0].getPostalNumberRightCity().getCityRoadIDs();
		
		for(Edge edge : edgesToCheck)
			if(!relevantEdgeIDsLeft.contains(edge.getiD()) && !relevantEdgeIDsRight.contains(edge.getiD()))		
				return false;
		
		System.out.println("ALL ROADS CITIES MATCHES");
		return true;
	}
	
	/**
	 * Parses the input with the AddressParser
	 * @param input The input to parse
	 * @throws MalformedAddressException Is thrown if the input contains invalid characters.
	 * @throws NoAddressFoundException Is thrown if no address could be found with the given input.
	 */
	private void parseAddress(String input) throws MalformedAddressException, NoAddressFoundException
	{
		parsedInput = ap.parseAddress(input);
	}
}
