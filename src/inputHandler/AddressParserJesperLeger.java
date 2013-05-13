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
	private int numberOfCurrentSuggestedRoads;
	private String suggestedRoadNamesFoundByString = "";
	private String suggestedRoadNumbersFoundByString = "";
	private String suggestedRoadLettersFoundByString = "";

	private boolean isRoadNameLocked;
	private String foundRoadName = "";
	private boolean isRoadNumberLocked;
	private int foundRoadNumber = -1;
	private boolean isRoadLetterLocked;
	private String foundRoadLetter = "";
	private Edge foundRoad;

	private City[] lastSuggestedCities;
	private int numberOfCurrentSuggestedCities;
	private String suggestedCitiesFoundByString = "";

	private boolean isCityNameLocked;
	private boolean isCityPostalNumberLocked;
	private int foundCityPostalNumber = -1;
	private String foundCityName = "";
	private City foundCity;

	//The suggestions as Strings, ready to be presented to the user
	private String[] suggestionsArray;

	private String patternBadInput = "[^A-ZÆØÅÄÖa-zæøåéèöäüâ0-9,\\-.´:)/(& ]{1,100}";

	public AddressParserJesperLeger()
	{
		isRoadNameLocked = false; isCityNameLocked = false; 
	}

	public String[] getSearchResults(String input) throws MalformedAdressException
	{
		Matcher validInput = Pattern.compile(patternBadInput).matcher(input);		
		if (validInput.find() || input.trim().isEmpty())
			throw new MalformedAdressException("MALFORMED ADRESS");

		//If the user enters more than one character between each search, the program has to search char by char
		if(input.length() > lastInputLength+1)
			runAsIncrementalSearch(input);

		else {			
			setSearchString(input);
			calculateSuggestions();
		}

		return suggestionsArray;

	}

	private void setSearchString(String input) throws MalformedAdressException
	{
		originalInput = input.trim().toLowerCase();
		modifiedInput = originalInput;

		lastInputLength++;
		if(isRoadNameLocked)
		{
			if(!originalInput.contains(suggestedRoadNamesFoundByString)) 
				resetRoadSearchTotal(); //If the input does not contain the String which the Edges were found by anymore, new Edge suggestions needs to be made		

			else
			{
				modifiedInput = modifiedInput.replaceFirst(suggestedRoadNamesFoundByString, "").trim(); //Delete the string, which the edges has been found by, from the originalinput
				System.out.println("Found suggestedRoadNamesFoundByString: " + suggestedRoadNamesFoundByString + ", deleting it from the input" );
				System.out.println("Result of deletion is: " + modifiedInput);
			}
		}

		if(isRoadNumberLocked)
		{
			if(!originalInput.contains(suggestedRoadNumbersFoundByString)) 
				resetRoadSearchNumberAndLetter(); //If the input does not contain the String which the Edges were found by anymore, new Edge suggestions needs to be made		

			else
			{
				modifiedInput = modifiedInput.replaceFirst(suggestedRoadNumbersFoundByString, "").trim(); //Delete the string, which the edges has been found by, from the originalinput
				System.out.println("Found suggestedRoadNumbersFoundByString: " + suggestedRoadNumbersFoundByString + ", deleting it from the input" );
				System.out.println("Result of deletion is: " + modifiedInput);
			}
		}
		
		if(isRoadLetterLocked)
		{
			if(!originalInput.contains(suggestedRoadLettersFoundByString)) 
				resetRoadSearchLetter(); //If the input does not contain the String which the Edges were found by anymore, new Edge suggestions needs to be made		

			else
				modifiedInput = modifiedInput.replaceFirst(suggestedRoadLettersFoundByString, "").trim(); //Delete the string, which the edges has been found by, from the originalinput
			System.out.println("Found suggestedRoadLettersFoundByString: " + suggestedRoadLettersFoundByString + ", deleting it from the input" );
			System.out.println("Result of deletion is: " + modifiedInput);
		}


		if(isCityNameLocked)
		{
			if(!originalInput.contains(suggestedCitiesFoundByString))
			{
				//If the input does not contain the String which the Cities were found by anymore, new City suggestions needs to be made
				resetCitySearch();
			}
			else
				modifiedInput = modifiedInput.replaceFirst(suggestedCitiesFoundByString, "").trim(); //Delete the string, which the cities has been found by, from the originalinput
		}



		//		if(!isCityNameLocked && !isRoadNameLocked)
		//			modifiedInput = originalInput;


		//modifiedInput = originalInput;
		//System.out.println("suggestedRoadNamesFoundByString: " + suu);
		System.out.println("Searching for: " + modifiedInput);
	}

	private void calculateSuggestions() throws MalformedAdressException
	{
		ArrayList<String> suggestionsList = new ArrayList<String>();

		//If the modified input does not contain anything to search for, do not
		if(modifiedInput.trim().isEmpty())
			return;

		else {
			//If we are not sure about the name of the road, keep searching for it
			if(!isRoadNameLocked)
			{
				setRoadNameSuggestions();
			}


			//If we are sure about the name of the road, search through its numbers and letters instead
			else {
				if (!isRoadNumberLocked)
					setRoadNumberSuggestions();

				if (isRoadNumberLocked && !isRoadLetterLocked)
					setRoadLetterSuggestions();

			}


			if(!isCityNameLocked)
			{
				setCitySuggestions();
			}

			evaluateSuggestions();
		}

		for(Edge edge : lastSuggestedRoads)
			suggestionsList.add(edge.toString());

		System.out.println("Size of lastSuggestedRoads: " + lastSuggestedRoads.length);

		for(City city : lastSuggestedCities)
			suggestionsList.add(city.toString());



		suggestionsArray = suggestionsList.toArray(new String[suggestionsList.size()]);
	}


	private void setRoadNameSuggestions() throws MalformedAdressException
	{
		Edge[] possibleEdges = EdgeSearch.searchForRoadSuggestions(modifiedInput, -1, "");
		numberOfCurrentSuggestedRoads = possibleEdges.length;
		System.out.println("Number of found edges: " + possibleEdges.length);
		System.out.println("MODIFIED INPUT: " + modifiedInput);
		//If no matches could be found it must mean that the user are no longer entering an input, which could be a road name
		if(numberOfCurrentSuggestedRoads == 0 )
		{
			//If no matches is found and never has been with this input, create empty array.
			if(lastSuggestedRoads == null)
				lastSuggestedRoads = new Edge[0];
		}

		//If there's more found Edges
		else
		{
			System.out.println("ALL ROADNAMES MATCHES");
			lastSuggestedRoads = possibleEdges;
			//foundRoad = possibleEdges[0];
			//isRoadNameLocked = true;
			foundRoadName = modifiedInput;
			suggestedRoadNamesFoundByString = modifiedInput;
			//modifiedInput = modifiedInput.replace(suggestedRoadNamesFoundByString, "");
		}
	}

	private void setRoadNumberSuggestions() throws MalformedAdressException
	{
		//If the input does not contain numbers, it is not a road number
		if(!modifiedInput.matches("\\d+"))
		{
			//If roadnumbers have been suggested before, use them
			if(!suggestedRoadNumbersFoundByString.isEmpty())
			{
				isRoadNumberLocked = true;
			}
			return;
		}


		Edge[] possibleEdges;
		int possibleNumber = -1;
		try {
			possibleNumber = Integer.parseInt(modifiedInput);
			possibleEdges = EdgeSearch.searchForRoadSuggestions(suggestedRoadNamesFoundByString, possibleNumber, "");
		} catch (NumberFormatException e) { //If the modifiedInput could not be cast to an integer
			System.out.println("NUMBERFORMAT EXCEPTION THROWN");
			possibleEdges = new Edge[0];
		}

		numberOfCurrentSuggestedCities = possibleEdges.length;

		if(numberOfCurrentSuggestedCities > 0)
		{
			lastSuggestedRoads = possibleEdges;
			suggestedRoadNumbersFoundByString = modifiedInput;
		}

		lastSuggestedRoads = possibleEdges;
	}

	private void setRoadLetterSuggestions() throws MalformedAdressException
	{
		Edge[] possibleEdges = EdgeSearch.searchForRoadSuggestions(suggestedRoadNamesFoundByString, foundRoadNumber, modifiedInput);
		numberOfCurrentSuggestedRoads = possibleEdges.length;
		
		if(numberOfCurrentSuggestedRoads > 0)
		{
			System.out.println("FOUND MATCHING ROAD LETTERS WITH THE MODIFIED INPUT: " + modifiedInput);
			lastSuggestedRoads = possibleEdges;
			suggestedRoadLettersFoundByString = foundRoadLetter;;
		}
	}

	private boolean doesRoadNamesMatch(Edge[] edgesToCheck)
	{
		String name = edgesToCheck[0].getRoadName();
		for (int i = 1; i < edgesToCheck.length; i++) 
			if(!name.equalsIgnoreCase(edgesToCheck[i].getRoadName()))
				return false;
		return true;
	}

	private void setCitySuggestions()
	{
		City[] possibleCities;
		//If the input only contains numbers with between 3 and 5 digits
		if(modifiedInput.matches("\\d+{3,5}"))
			possibleCities = CitySearch.searchForCityPostalNumberSuggestions(modifiedInput);
		else 
			possibleCities = CitySearch.searchForCityNameSuggestions(modifiedInput);

		System.out.println("Number of possible cities found: " + possibleCities.length);

		//If no matches could be found it must mean that the user are no longer entering an input, which could be a city name
		if(possibleCities.length == 0 )
		{
			//If no matches is found and never has been with this input, create empty array.
			if(lastSuggestedCities == null)
				lastSuggestedCities = new City[0];

			else 
			{
				possibleCities = lastSuggestedCities; //If no suggestions available, use the last available suggestions
				//isCityNameLocked = true; //Lock the City
			}

		}

		//If there is a single 100% match
		else if(possibleCities.length == 1)
		{
			lastSuggestedCities = possibleCities;
			foundCity = possibleCities[0];
			//isCityNameLocked = true;
			suggestedCitiesFoundByString = modifiedInput;
		}

		//If more than one matches are found
		else
		{
			lastSuggestedCities = possibleCities;
			suggestedCitiesFoundByString = modifiedInput; //Save the String, which the cities were found by
		}		
	}

	private void evaluateSuggestions()
	{
		//Noget med, at hvis der f.eks. kun er fundet veje, og ingen byer, så ved vi at det kun er veje - ændr parametre 

		//Hvis der kun er fundet resultater på en enkelt ting, så fastlås den og slet alle andre forslag, der endnu ikke er fastlåste

		//If the city name have not been locked yet, check if it should be
		if(!isCityNameLocked)
		{
			if (numberOfCurrentSuggestedCities >= 1 && numberOfCurrentSuggestedRoads == 0 )
			{
				if (numberOfCurrentSuggestedCities == 1) 
				{
					isCityNameLocked = true;
					foundCityName = lastSuggestedCities[0].getCityName();
				}
				suggestedCitiesFoundByString = modifiedInput;
				
				if(!isRoadNameLocked)
					resetRoadSearchTotal();				
			}
		}

		//If the road name have not been locked yet, check if it should be
		if(!isRoadNameLocked)	
		{
			//If no cities are suggested and one or more roads are, it must be a road
			if(numberOfCurrentSuggestedRoads >= 1 && numberOfCurrentSuggestedRoads == 0 && numberOfCurrentSuggestedCities == 0)
			{				
				if(doesRoadNamesMatch(lastSuggestedRoads))
				{
					System.out.println("LOCKING ROADNAME");
					isRoadNameLocked = true;
					foundRoadName = lastSuggestedRoads[0].getRoadName();
					suggestedRoadNumbersFoundByString = modifiedInput;
				}
				
				if(!isCityNameLocked)
					resetCitySearch();
			}
		}
		
		//If the road has been locked, check if road number or letter should be locked
		else {
			if (!isRoadNumberLocked) 
			{
				if(lastSuggestedRoads.length > 0 && numberOfCurrentSuggestedRoads == 0 && numberOfCurrentSuggestedCities == 0)
				{
					System.out.println("LOCKING ROAD NUMBER");
					isRoadNumberLocked = true;
					foundRoadNumber = Integer.parseInt(suggestedRoadNumbersFoundByString);
				}
				
			}
			else
			{
				if(!isRoadLetterLocked)
				{
					if(numberOfCurrentSuggestedRoads >= 1 && numberOfCurrentSuggestedCities == 0)
					{
						System.out.println("LOCKING ROAD LETTER");
						isRoadLetterLocked = true;
						foundRoadLetter = suggestedRoadLettersFoundByString;
					}
				}
			}
		}
		
		//If there is a 100% match on a city and nothing else
		if(isCityNameLocked && foundCity == null && numberOfCurrentSuggestedCities == 1 && numberOfCurrentSuggestedRoads == 0)
		{
			foundCity = lastSuggestedCities[0];
		}
		
		//If there is a 100% match on a road and nothing else
		if(isRoadNameLocked && foundRoad == null && numberOfCurrentSuggestedRoads == 1 && numberOfCurrentSuggestedCities == 0)
		{
			foundRoad = lastSuggestedRoads[0];
		}
	}

	private void runAsIncrementalSearch(String input) throws MalformedAdressException
	{
		System.out.println("Running incremental search");
		for (int i = 1; i <= input.length(); i++) 
		{
			setSearchString(input.substring(0, i));		
			lastInputLength = input.length();	
			calculateSuggestions();
		}
	}

	private void resetRoadSearchTotal()
	{
		System.out.println("Total reset of roads");
		isRoadNameLocked = false; isRoadNumberLocked = false; isRoadLetterLocked = false;
		suggestedRoadNamesFoundByString = ""; suggestedRoadNumbersFoundByString = ""; suggestedRoadLettersFoundByString = "";		
		lastSuggestedRoads = null; foundRoad = null;
	}

	private void resetRoadSearchNumberAndLetter()
	{
		System.out.println("Reset of road's numbers and letters");
		isRoadNumberLocked = false; isRoadLetterLocked = false;
		suggestedRoadNumbersFoundByString = ""; suggestedRoadLettersFoundByString = "";	
		lastSuggestedRoads = null; foundRoad = null;
	}

	private void resetRoadSearchLetter()
	{
		System.out.println("Reset of road's letters");
		isRoadLetterLocked = false;
		suggestedRoadLettersFoundByString = "";	
		lastSuggestedRoads = null; foundRoad = null;
	}

	private void resetCitySearch()
	{
		System.out.println("Reset of cities");
		isCityNameLocked = false;
		suggestedCitiesFoundByString = "";
		lastSuggestedCities = null; foundCity = null;
	}

	public static void main(String[] args) throws MalformedAdressException {

		AddressParserJesperLeger ap = new AddressParserJesperLeger();
		/*
		for(String string : ap.getSearchResults("V"))
			System.out.println(string);		

		for(String string : ap.getSearchResults("Va"))
			System.out.println(string);

		ap.getSearchResults("V");
		ap.getSearchResults("Va");
		ap.getSearchResults("Vandelvej 10");
		 */	
		//		for(String string : ap.getSearchResults("Vandelvej 10"))
		//			System.out.println(string);

		//		for(String string : ap.getSearchResults("Vandelvej Køge"))
		//			System.out.println(string);

		//		for(String string : ap.getSearchResults("Vandelvej 10 Køge"))
		//			System.out.println(string);

		for(String string : ap.getSearchResults("Stadionvej 2 B 6752"))
			System.out.println(string);
	}

}
