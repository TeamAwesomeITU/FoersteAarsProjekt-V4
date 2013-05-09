package inputHandler;

import inputHandler.exceptions.MalformedAdressException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.CitySearch;
import mapCreationAndFunctions.search.EdgeSearch;

public class AddressParserJesperLeger {
	
	private final String originalInput;
	private String modifiedInput;
	private Edge[] possibleRoads;
	private City[] possibleCitys;
	
	private String patternBadInput = "[^A-ZÆØÅÄÖa-zæøåéèöäüâ0-9,\\-.´:)/(& ]{1,100}";
		
	public AddressParserJesperLeger(String input)
	{
		originalInput = input.trim().toLowerCase();
	}
	
	private void getRoadWithEverythingSuggestions()
	{
		
	}
	
	private void getCitySuggestions()
	{
		
	}
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * FUCK THIS SHIT
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
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
		AddressParserJesperLeger ap = new AddressParserJesperLeger("Nørrebrogade");
		
		for(String string : ap.getSearchResults())
			System.out.println(string);
	}

}
