package inputHandler;

import java.util.HashSet;

import inputHandler.exceptions.MalformedAdressException;
import inputHandler.exceptions.NoAddressFoundException;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.EdgeSearch;

public class AddressSuggester {
	
	private Edge[] foundEdges;
	private String enteredRoadName;
	private int enteredRoadNumber;
	private String enteredRoadLetter;
	private String enteredCityName;
	private int enteredPostalNumber;
	
	public void AddressSuggester()
	{ }
	
	public Edge[] getFoundEdges() throws MalformedAdressException, NoAddressFoundException
	{ 
		calculateResults();
		return foundEdges; 
	}
	
	private void calculateResults() throws MalformedAdressException, NoAddressFoundException
	{ foundEdges = EdgeSearch.searchForRoadSuggestions(enteredRoadName, enteredRoadNumber, enteredRoadLetter, enteredPostalNumber, enteredCityName); }
		
	public void enterRoadNumber(int roadNumber)
	{ this.enteredRoadNumber = roadNumber; }

	public void enterRoadLetter(String roadLetter)
	{ this.enteredRoadLetter = roadLetter; }

	public void enterCityName(String cityName)
	{ this.enteredCityName = cityName; }

	public void enterPostalNumber(int postalNumber)
	{ this.enteredPostalNumber = postalNumber; }
	
	public void enterRoadName(String roadName)
	{ enteredRoadName = roadName; }	
		
	public Integer[] getPossibleNumbers()
	{
		HashSet<Integer> possibleNumbersSet = new HashSet<>();
//		for(Edge edge : foundEdges)
//			possibleNumbersSet.addAll(edge.);

		return possibleNumbersSet.toArray(new Integer[possibleNumbersSet.size()]);
	}
	
	public String[] getPossibleLetters()
	{
		return null;
	}

}
