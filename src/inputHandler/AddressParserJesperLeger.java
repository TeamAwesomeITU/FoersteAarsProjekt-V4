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

	private Edge[] lastSuggestedRoadsByNames;
	private Edge[] lastSuggestedRoadsByNumbers;
	private Edge[] lastSuggestedRoadsByLetters;
	private int numberOfCurrentSuggestedRoads;
	private String suggestedRoadNamesFoundByString;
	private String suggestedRoadNumbersFoundByString;
	private String suggestedRoadLettersFoundByString;

	private boolean isRoadNameLocked;
	private String foundRoadName;
	private boolean isRoadNumberLocked;
	private int foundRoadNumber;
	private boolean isRoadLetterLocked;
	private String foundRoadLetter;
	private Edge foundRoad;

	private City[] lastSuggestedCitiesByNames;
	private City[] lastSuggestedCitiesByPostalNumbers;
	private int numberOfCurrentSuggestedCities;
	private String suggestedCitiesFoundByString;

	private boolean isCityNameLocked;
	private boolean isCityPostalNumberLocked;
	private int foundCityPostalNumber;
	private String foundCityName;
	private City foundCity;

	//The suggestions as Strings, ready to be presented to the user
	private String[] suggestionsArray;

	private String patternBadInput = "[^A-ZÆØÅÄÖa-zæøåéèöäüâ0-9,\\-.´:)/(& ]{1,100}";

	public AddressParserJesperLeger()
	{
		resetRoadSearchTotal();
		resetCitySearch();
	}

	public String[] setSearchResults(String input) throws MalformedAdressException
	{
		Matcher validInput = Pattern.compile(patternBadInput).matcher(input);		
		if (validInput.find() || input.trim().isEmpty())
			throw new MalformedAdressException("MALFORMED ADRESS");

		//TODO IF THE INPUT CONTAINS THE PREVIOUSLY GIVEN SUGGESTIONS , DONT DO THE INCREMENTAL SEARCH!!!!
		//If the user enters more than one character between each search, the program has to search char by char
		if(input.length() > lastInputLength+1)
			runAsIncrementalSearch(input);

		else {			
			setSearchString(input);
			calculateSuggestions();
		}

		return suggestionsArray;

	}

	public Edge[] getFoundEdges()
	{ return lastSuggestedRoadsByNames; }

	public City[] getFoundCities()
	{ return lastSuggestedCitiesByNames; }

	private void setSearchString(String input) throws MalformedAdressException
	{
		originalInput = input.trim().toLowerCase();
		modifiedInput = originalInput;

		lastInputLength++;
		if(isRoadNameLocked)
		{
			if(!originalInput.contains(suggestedRoadNamesFoundByString)) 
			{
				System.out.println("Could no longer find input: " + suggestedRoadNamesFoundByString);
				resetRoadSearchTotal(); //If the input does not contain the String which the Edges were found by anymore, new Edge suggestions needs to be made		
			}
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
			{
				System.out.println("Could no longer find input: " + suggestedRoadNumbersFoundByString);
				resetRoadSearchNumberAndLetter(); //If the input does not contain the String which the Edges were found by anymore, new Edge suggestions needs to be made		
			}
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
			{
				System.out.println("Could no longer find input: " + suggestedRoadLettersFoundByString);
				resetRoadSearchLetter(); //If the input does not contain the String which the Edges were found by anymore, new Edge suggestions needs to be made		
			}

			else
				modifiedInput = modifiedInput.replaceFirst(suggestedRoadLettersFoundByString, "").trim(); //Delete the string, which the edges has been found by, from the originalinput
			System.out.println("Found suggestedRoadLettersFoundByString: " + suggestedRoadLettersFoundByString + ", deleting it from the input" );
			System.out.println("Result of deletion is: " + modifiedInput);
		}


		if(isCityNameLocked)
		{
			if(!originalInput.contains(suggestedCitiesFoundByString))
			{
				System.out.println("Could no longer find input: " + suggestedCitiesFoundByString);
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

		for(Edge edge : lastSuggestedRoadsByNames)
			suggestionsList.add(edge.toString());

		System.out.println("Size of lastSuggestedRoadsByName: " + lastSuggestedRoadsByNames.length);

		for(City city : lastSuggestedCitiesByNames)
			suggestionsList.add(city.toString());



		suggestionsArray = suggestionsList.toArray(new String[suggestionsList.size()]);
	}

	private void setRoadSuggestions() throws MalformedAdressException
	{
		Edge[] possibleEdges = EdgeSearch.searchForRoadSuggestions(foundRoadName, foundRoadNumber, foundRoadLetter, foundCityPostalNumber, foundCityName);

		//If results are found with this input
		if(possibleEdges.length > 0)
		{
			lastSuggestedRoadsByNames = possibleEdges;
		}

		//If no results are found, use the last suggested results and lock the next String, if the String was not empty
		else {

		}
	}

	private void setRoadNameSuggestions() throws MalformedAdressException
	{
		Edge[] possibleEdges;
		
		//If the search should depend on the found City
		if(isCityNameLocked)
			possibleEdges = EdgeSearch.searchForRoadSuggestions(modifiedInput, -1, "", foundCityPostalNumber, foundCityName);
			
		else
			possibleEdges = EdgeSearch.searchForRoadSuggestions(modifiedInput, -1, "", -1, "");
		
		numberOfCurrentSuggestedRoads = possibleEdges.length;
		System.out.println("Number of found edges: " + possibleEdges.length);
		System.out.println("MODIFIED INPUT: " + modifiedInput);
		//If no matches could be found it must mean that the user are no longer entering an input, which could be a road name
		if(numberOfCurrentSuggestedRoads == 0 )
		{
			//If no matches is found and never has been with this input, don't do anything
		}

		//If there has been found Edges
		else
		{
			lastSuggestedRoadsByNames = possibleEdges;
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
			if(suggestedRoadNumbersFoundByString.isEmpty())
			{
				numberOfCurrentSuggestedRoads = 0;
				//				System.out.println("LOCKING ROAD NUMBER: " + suggestedRoadNumbersFoundByString);
				//				modifiedInput = modifiedInput.replaceFirst(suggestedRoadNumbersFoundByString, "").trim();
				//				isRoadNumberLocked = true;
			}
			else {
				System.out.println("LOCKING ROAD NAME");
				isRoadNumberLocked = true;
				modifiedInput = modifiedInput.replaceFirst(suggestedRoadNumbersFoundByString, "").trim();
				foundRoadNumber = Integer.parseInt(suggestedRoadNumbersFoundByString);
				System.out.println("MODIFIED INPUT AFTER LOCKING ROAD NAME: " + modifiedInput);
			}
			return;
		}

		//If there is only numbers in the input, search for it
		else {

			Edge[] possibleEdges = EdgeSearch.searchForRoadSuggestions(suggestedRoadNamesFoundByString, Integer.parseInt(modifiedInput), "", -1, "");

			numberOfCurrentSuggestedRoads = possibleEdges.length;

			if(numberOfCurrentSuggestedRoads > 0)
			{
				lastSuggestedRoadsByNames = possibleEdges;
				suggestedRoadNumbersFoundByString = modifiedInput;
			}
		}

		//lastSuggestedRoadsByName = possibleEdges;
	}

	private void setRoadLetterSuggestions() throws MalformedAdressException
	{
		System.out.println("MODIFIED INPUT BEFORE SEARCHING FOR ROAD LETTERS: " + modifiedInput);
		Edge[] possibleEdges = EdgeSearch.searchForRoadSuggestions(suggestedRoadNamesFoundByString, foundRoadNumber, modifiedInput, -1, "");
		numberOfCurrentSuggestedRoads = possibleEdges.length;

		if(numberOfCurrentSuggestedRoads > 0)
		{
			System.out.println("FOUND MATCHING ROAD LETTERS WITH THE MODIFIED INPUT: " + modifiedInput);
			lastSuggestedRoadsByNames = possibleEdges;
			foundRoadLetter = suggestedRoadNumbersFoundByString;
		}
	}

	private boolean doesRoadNamesMatch(Edge[] edgesToCheck)
	{
		String name = edgesToCheck[0].getRoadName();
		for (int i = 1; i < edgesToCheck.length; i++) 
			if(!name.equalsIgnoreCase(edgesToCheck[i].getRoadName()))
				return false;
		System.out.println("ALL ROADNAMES MATCHES");
		return true;
	}

	private void setCitySuggestions()
	{
		City[] possibleCities;

		//If the input only contains numbers with between 4 and 5 digits
		if(modifiedInput.matches("\\d+{4,5}"))
		{
			possibleCities = CitySearch.searchForCityPostalNumberSuggestions(modifiedInput);

			//If the road name has been locked, run through the found edges and check for a match in them
			if(isRoadNameLocked)
			{
				ArrayList<City> possibleCitiesList = new ArrayList<>();				
				for(City city : possibleCities)
					for(Edge edge : lastSuggestedRoadsByNames)
						if(city.getCityPostalNumbers().contains(edge.getPostalNumberLeft()) || city.getCityPostalNumbers().contains(edge.getPostalNumberRight()))
							possibleCitiesList.add(city);
				possibleCities = possibleCitiesList.toArray(new City[possibleCitiesList.size()]);
			}
		}

		//If the input contains something that is not numbers between 4 and 5 digits
		else {
			possibleCities = CitySearch.searchForCityNameSuggestions(modifiedInput);

			//If the road name has been locked, run through the found edges and check for a match in them
			if(isRoadNameLocked)
			{
				ArrayList<City> possibleCitiesList = new ArrayList<>();				
				for(City city : possibleCities)
					for(Edge edge : lastSuggestedRoadsByNames)
						if(city.getCityName().equals(edge.getPostalNumberLeftCityName()) || city.getCityName().equals(edge.getPostalNumberRightCityName()))
							possibleCitiesList.add(city);
				possibleCities = possibleCitiesList.toArray(new City[possibleCitiesList.size()]);
			}
		}				

		System.out.println("Number of possible cities found: " + possibleCities.length);

		//If no matches could be found it must mean that the user are no longer entering an input, which could be a city name
		if(possibleCities.length == 0 )
		{
			possibleCities = lastSuggestedCitiesByNames; //If no suggestions available, use the last available suggestions
			//isCityNameLocked = true; //Lock the City	
		}

		//If there is a single 100% match
		else if(possibleCities.length == 1)
		{
			lastSuggestedCitiesByNames = possibleCities;
			foundCity = possibleCities[0];
			//isCityNameLocked = true;
			suggestedCitiesFoundByString = modifiedInput;
		}

		//If more than one matches are found
		else
		{
			lastSuggestedCitiesByNames = possibleCities;
			suggestedCitiesFoundByString = modifiedInput; //Save the String, which the cities were found by
		}	

	}

	private void evaluateSuggestions()
	{
		//Noget med, at hvis der f.eks. kun er fundet veje, og ingen byer, så ved vi at det kun er veje - ændr parametre 
		//Hvis der kun er fundet resultater på en enkelt ting, så fastlås den og slet alle andre forslag, der endnu ikke er fastlåste

/*
		System.out.println("STATUS------------------------");
		System.out.print("isCityNameLocked: " + isCityNameLocked + ", ");
		System.out.print("isRoadNameLocked: " + isRoadNameLocked + ", ");
		System.out.print("isRoadNumberLocked: " + isRoadNumberLocked + ", ");
		System.out.println("isRoadLetterLocked: " + isRoadLetterLocked + ", ");
		System.out.print("lastSuggestedCitiesByNames.length: " + lastSuggestedCitiesByNames.length + ", ");
		System.out.print("numberOfCurrentSuggestedCities: " + numberOfCurrentSuggestedCities + ", ");
		System.out.print("lastSuggestedRoadsByName.length: " + lastSuggestedRoadsByNames.length + ", ");
		System.out.print("numberOfCurrentSuggestedRoads: " + numberOfCurrentSuggestedRoads + ", ");

		System.out.println("STATUS------------------------");
*/
		//If the road name have not been locked yet, check if it should be
		if(!isRoadNameLocked)	
		{
			//If no cities are suggested and one or more roads are, it must be a road
			if(lastSuggestedRoadsByNames.length >= 1 && numberOfCurrentSuggestedRoads == 0 && numberOfCurrentSuggestedCities == 0)
			{				
				if(doesRoadNamesMatch(lastSuggestedRoadsByNames))
				{
					System.out.println("LOCKING ROADNAME: " + suggestedRoadNamesFoundByString);
					isRoadNameLocked = true;
					foundRoadName = lastSuggestedRoadsByNames[0].getRoadName();
					//suggestedRoadNamesFoundByString = modifiedInput;					
				}

				if(!isCityNameLocked)
					resetCitySearch();
			}
		}

		//If the road has been locked, check if road number or letter should be locked
		else {
			if (!isRoadNumberLocked) 
			{
				if(!suggestedRoadNumbersFoundByString.isEmpty() && lastSuggestedRoadsByNames.length >= 1 && numberOfCurrentSuggestedRoads == 0 && numberOfCurrentSuggestedCities == 0)
				{
					System.out.println("LOCKING ROAD NUMBER: " + suggestedRoadNumbersFoundByString);
					isRoadNumberLocked = true;
					foundRoadNumber = Integer.parseInt(suggestedRoadNumbersFoundByString);

					if(!isCityNameLocked)
						resetCitySearch();
				}

			}
			else
			{
				if(!isRoadLetterLocked)
				{
					if(!suggestedRoadLettersFoundByString.isEmpty() && lastSuggestedRoadsByNames.length >= 1 && numberOfCurrentSuggestedRoads == 0 && numberOfCurrentSuggestedCities == 0)
					{
						System.out.println("LOCKING ROAD LETTER");
						isRoadLetterLocked = true;
						foundRoadLetter = suggestedRoadLettersFoundByString;

						if(!isCityNameLocked)
							resetCitySearch();
					}

				}
			}
		}

		//If the city name have not been locked yet, check if it should be
		if(!isCityNameLocked)
		{
			if (lastSuggestedCitiesByNames.length >= 1 && numberOfCurrentSuggestedCities == 0 && numberOfCurrentSuggestedRoads == 0 )
			{
				if (numberOfCurrentSuggestedCities == 1) 
				{
					isCityNameLocked = true;
					foundCityName = lastSuggestedCitiesByNames[0].getCityName();
				}
				suggestedCitiesFoundByString = modifiedInput;

				if(!isRoadNameLocked)
					resetRoadSearchTotal();		
				else {
					if(!isRoadNumberLocked)
					{
						resetRoadSearchNumberAndLetter();
					}
					else {
						if(!isRoadLetterLocked)
							resetRoadSearchLetter();
					}
				}
			}
		}



		/*
		//If there is a 100% match on a city and nothing else
		if(isCityNameLocked && foundCity == null && numberOfCurrentSuggestedCities == 1 && numberOfCurrentSuggestedRoads == 0)
		{
			foundCity = lastSuggestedCitiesByNames[0];
		}

		//If there is a 100% match on a road and nothing else
		if(isRoadNameLocked && foundRoad == null && numberOfCurrentSuggestedRoads == 1 && numberOfCurrentSuggestedCities == 0)
		{
			foundRoad = lastSuggestedRoadsByName[0];
		}
		 */
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
		foundRoadName = ""; foundRoadLetter = ""; foundRoadNumber = -1;
		suggestedRoadNamesFoundByString = ""; suggestedRoadNumbersFoundByString = ""; suggestedRoadLettersFoundByString = "";		
		lastSuggestedRoadsByNames = new Edge[0]; foundRoad = null;
	}

	private void resetRoadSearchNumberAndLetter()
	{
		System.out.println("Reset of road number and letter");
		isRoadNumberLocked = false; isRoadLetterLocked = false;
		foundRoadLetter = ""; foundRoadNumber = -1;
		suggestedRoadNumbersFoundByString = ""; suggestedRoadLettersFoundByString = "";		
		//lastSuggestedRoadsByName = new Edge[0]; foundRoad = null;
	}

	private void resetRoadSearchLetter()
	{
		System.out.println("Reset of road letter");
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

	public static void main(String[] args) throws MalformedAdressException {

		AddressParserJesperLeger ap = new AddressParserJesperLeger();
		/*
		for(String string : ap.setSearchResults("V"))
			System.out.println(string);		

		for(String string : ap.setSearchResults("Va"))
			System.out.println(string);

		ap.setSearchResults("V");
		ap.setSearchResults("Va");
		ap.setSearchResults("Vandelvej 10");
		 */	
		//		for(String string : ap.setSearchResults("Vandelvej 10"))
		//			System.out.println(string);

		for(String string : ap.setSearchResults("Vandelvej Køge"))
			System.out.println(string);

		//		for(String string : ap.setSearchResults("Vandelvej 10 Køge"))
		//			System.out.println(string);

		//				for(String string : ap.setSearchResults("Stadionvej 2 B 6752"))
		//					System.out.println(string);
	}

}
