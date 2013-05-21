package inputHandler;

import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.Edge;

/**
 * This class is responsible for the handling of all the input from the user. 
 * When an address is entered into the GUI, it is parsed through
 * this class. Depending on the information parsed an array of strings is returned.
 */
public class AddressParser {

	private	String[] addressArray = new String[]{"","","","","",""};
	private	String pNumber = "(\\d{1,3})";
	private	String pFloor = "(\\b\\d{1,2}\\.)";	
	private	String pPost = "(\\b\\d{4,5})"; 			
	private String pBadInput =  "[^A-ZÆØÅÂÄÖa-zæøåéèöäüâ0-9,\\-.´:¨)/(& ]{1,100}";
	private String addressAfterDeletion = "";

	/** 
	 * This method calls method that isolates parts of the address string. It also handles invalid input. 
	 * @param address Is a String that is supposed to be the entire address written on a single line. 
	 * @return the array containing information regarding the address
	 * @throws MalformedAddressException
	 * @throws NoAddressFoundException 
	 */
	public String[] parseAddress(String address) throws MalformedAddressException, NoAddressFoundException {
		addressAfterDeletion = address.toLowerCase();

		Matcher badInput = match(pBadInput, address);
		if (badInput.find())																					/* 1 */
			throw new MalformedAddressException("Illegal characters found in address");
		else if(address.trim().isEmpty() || address == null)													/* 2 */								
			throw new NoAddressFoundException("No address to find was given");

		findRoadName(address);
		findCityName(addressAfterDeletion);
		findPostCode(addressAfterDeletion);
		if(!addressArray[0].isEmpty())																		/* 3 */
		{
			findFloorNumber(addressAfterDeletion);
			findRoadNumber(addressAfterDeletion);
			if(!addressArray[1].isEmpty()){
				findLetter(addressAfterDeletion);
			}
		}
		return addressArray;			     					
	}

	/**
	 * This method attempts to isolate the road name. 
	 * @param The entire address on a single line.
	 */
	private void findRoadName(String address){
		if(address.trim().isEmpty())																			/* 4 */																		
			return;

		String[] splitInput = address.split("\\s+");
		String possibleRoadName = "";
		String foundRoadName = "";
		String totalInput = "";
		int i = 0;
		boolean isResultFound = false;
		int wantedLength = splitInput.length;

		while(!isResultFound && i < splitInput.length && wantedLength >= 0)
		{		
			totalInput = "";
			for (int j = splitInput.length-wantedLength; j < splitInput.length; j++) 
				totalInput += splitInput[j].toLowerCase() + " ";

			totalInput = totalInput.trim();		
			possibleRoadName = EdgeSearch.searchForRoadNameLongestPrefix(totalInput);
			if(!possibleRoadName.isEmpty())																	/* 5 */					
			{
				Edge[] possibleEdges = EdgeSearch.searchForRoadName(possibleRoadName);

				if(possibleEdges.length != 0 && possibleRoadName.length() > foundRoadName.length())			/* 6 */
				{
					foundRoadName = possibleRoadName;
				}			
			}
			i++;
			wantedLength--;
		}
		addressArray[0] = foundRoadName;
		if(!foundRoadName.isEmpty())																		/* 7 */
		{
			addressAfterDeletion = addressAfterDeletion.replace(foundRoadName,"").trim();
		}
	}

	/**	This method uses the matcher to find the number and eventual letter of a building. 
	 * 	It then isolates the number and places this at the index of the road number. 
	 * @param substring of the address. 	
	 */
	private void findRoadNumber(String address) {
		Matcher eventualNumber = match(pNumber, address);
		if(eventualNumber.find()) 
		{																					
			addressAfterDeletion = addressAfterDeletion.replace(eventualNumber.group(), "");
			addressArray[1] = eventualNumber.group().trim();
		}
	}


	/**	
	 * Finds the floor of the address using a scanner and places this in the array.
	 * @param substring of the address
	 */
	private void findFloorNumber(String address) {
		String floorTemp = "";
		Matcher floor = match(pFloor, address);			
		if(floor.find()) 
		{																					/* 11 */
			floorTemp = floor.group();	
			match(pNumber, floorTemp);
			addressAfterDeletion = addressAfterDeletion.replace(floorTemp, "");
			Matcher tal = match(pNumber, floorTemp);		
			tal.find();																	
			addressArray[3] = tal.group().trim();
		}
	}

	/**	
	 * Returns the post code of the string and places it in the array.
	 * @param substring of the address.
	 */
	private void findPostCode(String address) {
		Matcher postcode = match(pPost, address);	
		if(postcode.find()) 
		{																				/* 12 */
			addressAfterDeletion = addressAfterDeletion.replace(postcode.group(),"").trim();
			addressArray[4] = postcode.group().trim(); 
		}
	}

	/**
	 * This method attempts to isolate the city name. 
	 * @param The entire address on a single line.
	 */
	private void findCityName(String address)
	{
		if(address.trim().isEmpty())																			/* 13 */
			return;

		String[] splitInput = address.split("\\s+");
		String possibleCityName = "";
		String foundCityName = "";
		String totalInput = "";
		int i = 0;
		boolean isResultFound = false;
		int wantedLength = splitInput.length;

		while(!isResultFound && i < splitInput.length && wantedLength >= 0)
		{		
			totalInput = "";
			for (int j = splitInput.length-wantedLength; j < splitInput.length; j++) 
				totalInput += splitInput[j].toLowerCase() + " ";

			totalInput = totalInput.trim();		
			possibleCityName = CitySearch.searchForCityNameLongestPrefix(totalInput);
			if(!possibleCityName.isEmpty())																	/* 14 */
			{
				City[] possibleCities = CitySearch.searchForCityName(possibleCityName);

				if(possibleCities.length != 0 && possibleCityName.length() > foundCityName.length())		/* 15 */
				{
					foundCityName = possibleCityName;
				}			
			}
			i++;
			wantedLength--;
		}
		addressArray[5] = foundCityName;
		addressAfterDeletion = addressAfterDeletion.replaceAll(foundCityName, "");
	}

	/**
	 * Isolates the letter if it is present. 
	 * @param A string containing what is left of the original string.
	 */
	private void findLetter(String address) {
		addressAfterDeletion = addressAfterDeletion.replaceAll(",", "").trim().replaceAll("sal", "").trim();
		if(addressAfterDeletion.contains("i") && addressAfterDeletion.split("\\s*").length>2)
		{
			addressAfterDeletion = addressAfterDeletion.replaceAll("i", "");
		}
		if(addressAfterDeletion.split("\\s*").length==2){
			addressArray[2] = addressAfterDeletion.split("\\s*")[1].toLowerCase().trim();
		}
	}

	/** 
	 * This method is a shortcut that saves lines when using a pattern in a matcher
	 * @param pattern Is a string that can either be a literal word or a regex pattern. 	
	 * @param A string the pattern should be applied to.
	 * @return A matcher containing the parameters. 
	 */
	private Matcher match(String pattern, String subjectString) {
		Matcher matcher = Pattern.compile(pattern).matcher(subjectString);
		return matcher;
	}

	public String[] getAddressArray() {return addressArray;}
}
