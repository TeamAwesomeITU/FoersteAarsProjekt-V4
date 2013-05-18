package inputHandler;

import java.util.HashSet;

import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.EdgeSearch;
import inputHandler.exceptions.MalformedAdressException;
import inputHandler.exceptions.NoAddressFoundException;

public class AddressSearch {
	
	private AdressParser ap;
	private String[] parsedInput;
	private Edge[] foundEdges = new Edge[0];
	
	public AddressSearch()
	{
		ap = new AdressParser();		
	}
		
	public void searchForAdress(String input) throws MalformedAdressException, NoAddressFoundException
	{
		ap = new AdressParser();
		parseAddress(input);
		int roadNumber = -1;
		int postalNumber = -1;
		
		try {
			roadNumber = Integer.parseInt(parsedInput[1]);
		} catch (NumberFormatException e) {}
		
		try {
			postalNumber = Integer.parseInt(parsedInput[4]);
		} catch (NumberFormatException e) {}
		
		for(String string : parsedInput)
			System.out.println("STRING IN PARSED INPUT: " + string);
		
		foundEdges = EdgeSearch.searchForRoads(parsedInput[0], roadNumber, parsedInput[2], postalNumber, parsedInput[5]);
	}
	
	public Edge[] getFoundEdges()
	{
		return foundEdges;
	}
	
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
	
	public void clearResults()
	{ foundEdges = new Edge[0];	}
	
	private boolean doesRoadNamesMatch(Edge[] edgesToCheck)
	{
		String name = edgesToCheck[0].getRoadName();
		for (int i = 1; i < edgesToCheck.length; i++) 
			if(!name.equalsIgnoreCase(edgesToCheck[i].getRoadName()))
				return false;
		System.out.println("ALL ROADNAMES MATCHES");
		return true;
	}
	
	private boolean doesRoadsCityMatch(Edge[] edgesToCheck)
	{
		HashSet<Integer> relevantEdgeIDsLeft = edgesToCheck[0].getPostalNumberLeftCity().getCityRoadIDs();
		HashSet<Integer> relevantEdgeIDsRight = edgesToCheck[0].getPostalNumberRightCity().getCityRoadIDs();
		
		for(Edge edge : edgesToCheck)
		{
			System.out.println(edge.getPostalNumberLeft() + " " + edge.getPostalNumberRight());
			if(!relevantEdgeIDsLeft.contains(edge.getiD()) && !relevantEdgeIDsRight.contains(edge.getiD()))		
				return false;
		}
		System.out.println("ALL ROADS CITIES MATCHES");
		return true;
	}
	
	private void parseAddress(String input) throws MalformedAdressException, NoAddressFoundException
	{
		parsedInput = ap.parseAdress(input);
	}
}
