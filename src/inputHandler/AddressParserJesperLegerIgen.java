package inputHandler;

import inputHandler.exceptions.MalformedAdressException;
import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.EdgeSearch;

public class AddressParserJesperLegerIgen {
	
	private String roadName;
	private int roadNumber;
	private String roadLetter;
	
	private boolean lockedRoadName;
	private boolean lockedRoadNumber;
	private boolean lockedRoadLetter;
	
	private int cityPostalNumber;
	private String cityName;
	
	public AddressParserJesperLeger()
	{
		resetRoadSearchTotal();
		resetCitySearch();
	}
	
	private void setRoadSuggestions() throws MalformedAdressException
	{
		Edge[] possibleEdges = EdgeSearch.searchForRoadSuggestions(foundRoadName, foundRoadNumber, foundRoadLetter, foundCityPostalNumber, foundCityName);
		
		//If results are found with this input
		if(possibleEdges.length > 0)
		{
			
		}
		
		//If no results are found, use the last suggested results and lock the next String, if the String was not empty
		else {
			
		}
	}
	
	private void setCitySuggestions() throws MalformedAdressException
	{
	
	}

	private void resetRoadSearchTotal()
	{
		System.out.println("Total reset of roads");
		isRoadNameLocked = false; isRoadNumberLocked = false; isRoadLetterLocked = false;
		foundRoadName = ""; foundRoadLetter = ""; foundRoadNumber = -1;
		suggestedRoadNamesFoundByString = ""; suggestedRoadNumbersFoundByString = ""; suggestedRoadLettersFoundByString = "";		
		lastSuggestedRoadsByNames = new Edge[0]; foundRoad = null;
	}

	private void resetRoadSearchNumberAndLetter()
	{
		System.out.println("Total reset of roads");
		isRoadNumberLocked = false; isRoadLetterLocked = false;
		foundRoadLetter = ""; foundRoadNumber = -1;
		suggestedRoadNumbersFoundByString = ""; suggestedRoadLettersFoundByString = "";		
		//lastSuggestedRoadsByName = new Edge[0]; foundRoad = null;
	}

	private void resetRoadSearchLetter()
	{
		System.out.println("Total reset of roads");
		isRoadLetterLocked = false;
		foundRoadLetter = "";;
		suggestedRoadLettersFoundByString = "";		
		//lastSuggestedRoadsByName = new Edge[0]; foundRoad = null;
	}

	private void resetCitySearch()
	{
		System.out.println("Reset of cities");
		isCityNameLocked = false;
		foundCityName = ""; foundCityPostalNumber = -1;
		suggestedCitiesFoundByString = "";
		lastSuggestedCitiesByNames = new City[0]; foundCity = null;
	}
}
