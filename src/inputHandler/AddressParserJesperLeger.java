package inputHandler;

import inputHandler.exceptions.MalformedAdressException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.CitySearch;
import mapCreationAndFunctions.search.EdgeSearch;

public class AddressParserJesperLeger {

	//The original input which contains everything
	private String originalInput = "";
	//The modified input, which only contains what is relevant to obtain suggestions
	private String modifiedInput = "";
	//Last input length
	private int lastInputLength = 0;

	private Edge[] lastSuggestedRoads;
	private String suggestedRoadsFoundByString = "";

	private City[] lastSuggestedCities;
	private String suggestedCitiesFoundByString = "";

	private boolean isRoadNameLocked;
	private String foundRoadName = "";
	private boolean isRoadNumberLocked;
	private int foundRoadNumber = -1;
	private boolean isRoadLetterLocked;
	private String foundRoadLetter = "";
	private Edge foundRoad;

	private boolean isCityLocked;
	private int foundCityPostalNumber = -1;
	private String foundCityName = "";
	private City foundCity;

	//The suggestions as Strings, ready to be presented to the user
	private String[] suggestionsArray;

	private String patternBadInput = "[^A-ZÆØÅÄÖa-zæøåéèöäüâ0-9,\\-.´:)/(& ]{1,100}";
	private	String patternBuilding = "(\\b\\d{1,3}[A-ZÆØÅa-zæøå]?\\b )|" +
			"\\b\\d{1,3}[^.]\\b}|" +
			"(\\b\\d{1,3}[A-ZÆØÅa-zæøå,]?\\b|" +
			"\\b\\d{1,3}[,]?\\b)";
	private	String patternBuildingNumber = "(\\d{1,3})";
	private String patternBuildingLetter = "[A-ZÆØÅa-zæøå]";

	public AddressParserJesperLeger()
	{
		isRoadNameLocked = false; isCityLocked = false; 
	}

	public String[] getSearchResults(String input) throws MalformedAdressException
	{
		Matcher validInput = Pattern.compile(patternBadInput).matcher(input);		
		if (validInput.find() || input.trim().isEmpty())
			throw new MalformedAdressException("MALFORMED ADRESS");

		setSearchString(input);
		calculateSuggestions();

		return suggestionsArray;

	}

	private void setSearchString(String input) throws MalformedAdressException
	{
		originalInput = input.trim().toLowerCase();

		//If the user enters more than one character between each search, the program has to search char by char
		if(originalInput.length() > lastInputLength+1)
			runAsIncrementalSearch(originalInput);
		
		else {
			lastInputLength++;
			if(isRoadNameLocked)
				if(!originalInput.contains(suggestedRoadsFoundByString)) //If the input does not contain the String which the Edges were found by anymore, new Edge suggestions needs to be made		
					resetRoadSearch();
				
				else
					modifiedInput = originalInput.replace(suggestedRoadsFoundByString, ""); //Delete the string, which the edges has been found by, from the originalinput

			if(isCityLocked)
				if(!originalInput.contains(suggestedCitiesFoundByString))
				{
					//If the input does not contain the String which the Cities were found by anymore, new City suggestions needs to be made
					resetCitySearch();
				}
				else
					modifiedInput = originalInput.replace(suggestedCitiesFoundByString, ""); //Delete the string, which the cities has been found by, from the originalinput

			if(!isCityLocked && !isRoadNameLocked)
				modifiedInput = originalInput;
		}
	}

	private void calculateSuggestions() throws MalformedAdressException
	{
		ArrayList<String> suggestionsList = new ArrayList<String>();

		//If we are not sure about the name of the road, keep searching for it
		if(!isRoadNameLocked)
		{
			setRoadNameSuggestions();
		}
		
		//If we are sure about the name of the road, search through its numbers and letters instead
		else {
			
		}



		if(!isCityLocked)
		{
			setCitySuggestions();
		}
		
		for(Edge edge : lastSuggestedRoads)
			suggestionsList.add(edge.toString());

		for(City city : lastSuggestedCities)
			suggestionsList.add(city.toString());

		evaluateSuggestions();

		suggestionsArray = suggestionsList.toArray(new String[suggestionsList.size()]);
	}


	private void setRoadNameSuggestions() throws MalformedAdressException
	{
		//Regex der splitter inputtet i vejnavn, tal og bogstav
		/*
		Matcher building = Pattern.compile(patternBuilding).matcher(modifiedInput);		
		System.out.println("Hvad sker der her");
		System.out.println(building.group(0));
		*/

		Edge[] possibleEdges = EdgeSearch.searchForRoadSuggestions(modifiedInput, -1, "");

		//If no matches could be found it must mean that the user are no longer entering an input, which could be a road name
		if(possibleEdges.length == 0 )
		{
			//If no matches is found and never has been with this input, create empty array.
			if(lastSuggestedRoads == null)
				lastSuggestedRoads = new Edge[0];

			else 
			{
				possibleEdges = lastSuggestedRoads; //If no suggestions available, use the last available suggestions
				isRoadNameLocked = true; //Lock the Edge
			}

		}

		//If there all found Edges have exactly the same road name
		else if(doesRoadNamesMatch(possibleEdges))
		{
			lastSuggestedRoads = possibleEdges;
			//foundRoad = possibleEdges[0];
			isRoadNameLocked = true;
			foundRoadName = modifiedInput;
			suggestedRoadsFoundByString = modifiedInput;
			//modifiedInput = modifiedInput.replace(suggestedRoadsFoundByString, "");
		}

		//If more than one matches are found
		else
		{
			lastSuggestedRoads = possibleEdges;
			suggestedRoadsFoundByString = modifiedInput; //Save the String, which the roads were found by
		}
	}
	
	private void setRoadNumberSuggestions() throws MalformedAdressException
	{
		Edge[] possibleEdges;
		
		try {
			possibleEdges = EdgeSearch.searchForRoadSuggestions(suggestedRoadsFoundByString, Integer.parseInt(modifiedInput), "");
		} catch (NumberFormatException e) { //If the modifiedInput could not be cast to an integer
			possibleEdges = new Edge[0];
		}
		
		

	}
	
	private void setRoadLetterSuggestions() throws MalformedAdressException
	{
		Edge[] possibleEdges = EdgeSearch.searchForRoadSuggestions(suggestedRoadsFoundByString, foundRoadNumber, modifiedInput);
	}

	private boolean doesRoadNamesMatch(Edge[] edgesToCheck)
	{
		String name = edgesToCheck[0].getRoadName();
		for (int i = 1; i < edgesToCheck.length; i++) 
			if(name.equalsIgnoreCase(edgesToCheck[i].getRoadName()))
				return false;
		return true;
	}

	private void setCitySuggestions()
	{
		City[] possibleCities;
		//If the input only contains numbers
		if(modifiedInput.matches("\\d+"))
			possibleCities = CitySearch.searchForCityPostalNumberSuggestions(modifiedInput);
		else 
			possibleCities = CitySearch.searchForCityNameSuggestions(modifiedInput);

		//If no matches could be found it must mean that the user are no longer entering an input, which could be a city name
		if(possibleCities.length == 0 )
		{
			//If no matches is found and never has been with this input, create empty array.
			if(lastSuggestedCities == null)
				lastSuggestedCities = new City[0];

			else 
			{
				possibleCities = lastSuggestedCities; //If no suggestions available, use the last available suggestions
				isCityLocked = true; //Lock the City
			}

		}

		//If there is a single 100% match
		else if(possibleCities.length == 1)
		{
			lastSuggestedCities = possibleCities;
			foundCity = possibleCities[0];
			isCityLocked = true;
			modifiedInput = modifiedInput.replace(suggestedCitiesFoundByString, "");
		}

		//If more than one matches are found
		else
		{
			lastSuggestedCities = possibleCities;
		}

		suggestedCitiesFoundByString = modifiedInput; //Save the String, which the cities were found by

	}

	private void evaluateSuggestions()
	{
		//Noget med, at hvis der f.eks. kun er fundet veje, og ingen byer, så ved vi at det kun er veje - ændr parametre 
	}

	private void runAsIncrementalSearch(String input) throws MalformedAdressException
	{
		for (int i = 1; i < input.length(); i++) 
			getSearchResults(input.substring(0, i));
		lastInputLength = input.length();		
	}
	
	private void resetRoadSearch()
	{
		isRoadNameLocked = false; isRoadNumberLocked = false; isRoadLetterLocked = false;
		foundRoad = null;
		lastSuggestedRoads = null;
	}

	private void resetCitySearch()
	{
		isCityLocked = false;
		foundCity = null;
		lastSuggestedCities = null;
	}


	public static void main(String[] args) throws MalformedAdressException {

		AddressParserJesperLeger ap = new AddressParserJesperLeger();

		for(String string : ap.getSearchResults("Køstrupvej 4"))
			System.out.println(string);



	}

}
