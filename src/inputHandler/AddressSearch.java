package inputHandler;

import java.util.HashSet;

import mapCreationAndFunctions.data.Edge;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

public class AddressSearch {
	
	private AddressParser ap;
	private String[] parsedInput;
	private Edge[] foundEdges = new Edge[0];

	/**
	 * Calls the addressParser which returns an array with the address. It then tries to find
	 * a specific edge. If not multiple edges are stored in foundEdge[]
	 * @param input The address String which is sent to the addressParser.
	 * @throws MalformedAddressException
	 * @throws NoAddressFoundException
	 */
	public void searchForAdress(String input) throws MalformedAddressException, NoAddressFoundException
	{
		ap = new AddressParser();
		parsedInput = ap.parseAddress(input);
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
	 * @return foundEdges[]
	 */
	public Edge[] getFoundEdges()
	{
		return foundEdges;
	}
	/**
	 * This method tries to find a specific edge from the edges gather in searchForAddress
	 * @return A single edge that matches input
	 * @throws NoAddressFoundException
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
	 * Resets foundEdges array.
	 */
	public void clearResults()
	{ foundEdges = new Edge[0];	}
	
	private boolean doesRoadNamesMatch(Edge[] edgesToCheck)
	{
		String name = edgesToCheck[0].getRoadName();
		for (int i = 1; i < edgesToCheck.length; i++) 
			if(!name.equalsIgnoreCase(edgesToCheck[i].getRoadName()))
				return false;
		return true;
	}
	/**
	 * Checks if all found edges has the same city.
	 * @param edgesToCheck An array of possible edge matches. 
	 * @return true if that have the same city. False if not.
	 */
	private boolean doesRoadsCityMatch(Edge[] edgesToCheck)
	{
		HashSet<Integer> relevantEdgeIDsLeft = edgesToCheck[0].getPostalNumberLeftCity().getCityRoadIDs();
		HashSet<Integer> relevantEdgeIDsRight = edgesToCheck[0].getPostalNumberRightCity().getCityRoadIDs();
		
		for(Edge edge : edgesToCheck)
			if(!relevantEdgeIDsLeft.contains(edge.getiD()) && !relevantEdgeIDsRight.contains(edge.getiD()))		
				return false;
		
		return true;
	}
}
