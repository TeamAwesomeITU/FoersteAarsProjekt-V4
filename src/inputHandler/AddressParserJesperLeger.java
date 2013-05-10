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
	
	private String originalInput;
	private String modifiedInput;
	private Edge[] lastSuggestedRoads;
	private String suggestedRoadsFoundByString;
	
	private City[] lastSuggestedCities;
	private String suggestedCitiesFoundByString;
	
	private boolean isEdgeLocked;
	private Edge foundEdge;
	
	private boolean isCityLocked;
	private City foundCity;
	
	private String patternBadInput = "[^A-ZÆØÅÄÖa-zæøåéèöäüâ0-9,\\-.´:)/(& ]{1,100}";
		
	public AddressParserJesperLeger()
	{
		isEdgeLocked = false; isCityLocked = false; 
	}
	
	
	public void setString(String input)
	{
		originalInput = input.trim().toLowerCase();
	}
	
	public String[] getSuggestions()
	{
		ArrayList<String> suggestionsList = new ArrayList<>();
		
		if(!isEdgeLocked)
		{
			//LAV EN searchForRoadNameSuggestions og en searchForRoadName DER TAGER HØJDE FOR ALT
			Edge[] possibleEdges = EdgeSearch.searchForRoadNameSuggestions(modifiedInput);
			
			//If no matches could be found
			if(possibleEdges.length == 0 )
			{
				possibleEdges = lastSuggestedRoads; //If no suggestions available, use the last available suggestions
				suggestedRoadsFoundByString = modifiedInput;
				isEdgeLocked = true;
			}
			
			//If there is a single 100% match
			else if(possibleEdges.length == 1)
			{
				foundEdge = possibleEdges[0];
				suggestedRoadsFoundByString = modifiedInput;
				isEdgeLocked = true;
			}
				
			//If more than one matches are found
			else {
				lastSuggestedRoads = possibleEdges;
			}
			
			for(Edge edge : possibleEdges)
				suggestionsList.add(edge.toString());
		}
		//1. get suggestions road name - check length. If length > 1, fill list with suggestions.
		
		if(!isCityLocked)
		{
			City[] possibleCities;
			//If the input only contains numbers
			if(modifiedInput.matches("\\d+"))
				possibleCities = CitySearch.searchForCityPostalNumberSuggestions(modifiedInput);
			else 
				possibleCities = CitySearch.searchForCityNameSuggestions(modifiedInput);
		}
		
		
		
		
		return suggestionsList.toArray(new String[suggestionsList.size()]);
	}
	
	
	private void getRoadWithEverythingSuggestions()
	{
		
	}
	
	private void getCitySuggestions()
	{
		
	}
		
	public String[] getSearchResults() throws MalformedAdressException
	{
		Matcher validInput = Pattern.compile(patternBadInput).matcher(originalInput);		
		if (validInput.find() || originalInput.trim().isEmpty() || originalInput == null)
			throw new MalformedAdressException("MALFORMED ADRESS");
		
		String input = originalInput;
		
		Edge[] possibleEdges = EdgeSearch.searchForRoadNameSuggestions(input);

		if(possibleEdges.length > 0)
			System.out.println("It could be a road name!");
			//It could be a road name! Cut this string from the 
		
		City[] possibleCities = CitySearch.searchForCityNameSuggestions(input);
		
		if(possibleCities.length > 0)
			System.out.println("It could be a city name!");
			//It could be a city name! Add the results
		
		return new String[1];
		
	}
	
	public static void main(String[] args) throws MalformedAdressException {
		/*
		AddressParserJesperLeger ap = new AddressParserJesperLeger("Nørrebrogade");
		
		for(String string : ap.getSearchResults())
			System.out.println(string);
			*/
		
				
	}

}
