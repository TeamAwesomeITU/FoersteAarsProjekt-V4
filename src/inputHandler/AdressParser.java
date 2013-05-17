package inputHandler;


import inputHandler.exceptions.MalformedAdressException;
import inputHandler.exceptions.NoAddressFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.CitySearch;
import mapCreationAndFunctions.search.EdgeSearch;


/**
 * This class is responsible to handle all the input from the user. 
 * When an address is entered into the gui, it is parsed through
 * this class. It is checked in the textfield containing all the
 * road names. If it finds a match it returns it as an array of strings.
 */
public class AdressParser {

	private	String[] adressArray = new String[]{"","","","","",""};
	private	String pBuilding = "(\\b\\d{1,3}[A-ZÆØÅa-zæøå]?\\b )|" +
			"\\b\\d{1,3}[^.]\\b}|" +
			"(\\b\\d{1,3}[A-ZÆØÅa-zæøå,]?\\b|" +
			"\\b\\d{1,3}[,]?\\b)";
	private	String pTal = "(\\d{1,3})";
	private	String pFloor = "(\\b\\d{1,2}\\.)";	
	private	String pPost = "(\\b\\d{4,5})"; 			
	private	String pLetter = "[A-ZÆØÅa-zæøå]";
	private String pBadInput =  "[^A-ZÆØÅÄÖa-zæøåéèöäüâ0-9,\\-.´:¨)/(& ]{1,100}";
	private String pDelimiters = "sal|etage|plan|th|tv|\\,|\\.|\\bi\\b|\\bpå\\b";
	private	String numberLetter = ""; // Gemmer vejnummeret med tal og bogstav.
	private String addressAfterDeletion = "";

	/**
	 * This is the constructor for the class. It makes a binary search of the road_names.txt
	 * and then sorts it.
	 */
	public AdressParser()	{

	}

	/** This method checks if input is valid and whether it contains a number or the letter i.
	 * 	If it does not it places the address in index 0 as the name of the road. If it does 
	 * 	the else-statment will try to find and place the parts of the address in their proper
	 * 	places. 
	 * @param address Is a String that is supposed to be the entire address written on a single line. 
	 * @return the array containing information regarding the address
	 * @throws MalformedAdressException
	 * @throws NoAddressFoundException 
	 */
	public String[] parseAdress(String address) throws MalformedAdressException, NoAddressFoundException {
		//Is the input valid?			
		address = address.toLowerCase();
		Matcher badInput = match(pBadInput, address);
		if (badInput.find())
			throw new MalformedAdressException("Illegal characters found in address");
		else if(address.trim().isEmpty() || address == null){								/* 1 */
			throw new NoAddressFoundException("No address to find was given");
		}

		//TODO FIX DEN HER REGEX eller slet svinet
		//		if(match("\\d|\\,|\\bi\\b", address).find() == false){											/* 2 */
		//			adressArray[0] = address.trim();
		//			System.out.println(address);
		//		}

		//		else
		{			
			addressAfterDeletion = address;
			findRoadName(address);
			//Only checks for roadnumber, roadletter and floornumber, if a valid adress is found
			if(!adressArray[0].isEmpty())												/* 2 */
			{
				findFloorNumber(addressAfterDeletion);
				findRoadNumber(addressAfterDeletion);
				findRoadLetter(addressAfterDeletion);				
			}
			findPostCode(addressAfterDeletion);
			findCityName(addressAfterDeletion);
		}

		return adressArray;			     					
	}

	/** This method is a shortcut that saves lines when using a pattern in a matcher
	 * 
	 * @param pattern Is a string that can either be a literal word or a regex pattern. 	
	 * @param input Whatever input you want. Most likely the address. Also called a subjectstring.
	 * @return A matcher with a pattern and a subjectstring which you "apply" the pattern to. 
	 */
	private Matcher match(String pattern, String input) {
		Matcher matcher = Pattern.compile(pattern).matcher(input);	//"\\[0-9]*\\[X-x]{1}\\b"	
		return matcher;
	}

	/** This method is used for finding the road name. This is done by searching through the txt file containing all roadnames in Denmark.
	 * The method splits the input string and the road name up. Both are split up at whitespaces and each are put in different arrays.
	 * Then every entry in the road name array is held up against the input array, and if all entries in the road name array are matched in the input array,
	 * The road name array's entries are then made a string again, and saved in a tmpStorage array. This is because, sometimes some road names consists in different length,
	 * for instance A. E. Hansensvej and Hansensvej. In the end, the longest string in tmpStorage must be the road name that the user gave as input.
	 * The longest string is then saved in the address array.
	 * 
	 * @param input Address string
	 */

	private void findRoadName(String input)
	{
		System.out.println("INPUT TO FIND ROADNAME BY: " + input);

		String[] splitInput = input.split("\\s+");
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

			System.out.println("totalInput: " + totalInput);

			possibleRoadName = EdgeSearch.searchForRoadNameLongestPrefix(totalInput);
			System.out.println("MIGHT BE THIS ROAD: " + possibleRoadName);

			if(!possibleRoadName.isEmpty())
			{
				Edge[] possibleEdges = EdgeSearch.searchForRoadName(possibleRoadName);

				if(possibleEdges.length != 0 && possibleRoadName.length() > foundRoadName.length())
				{
					foundRoadName = possibleRoadName;
				}			
			}
			i++;
			wantedLength--;
		}

		//		String actualRoadName = "";
		//		if(!possibleRoadName.isEmpty())
		//		{
		//			Edge[] possibleEdges = EdgeSearch.searchForRoadName(possibleRoadName);
		//			System.out.println("NUMBER OF FOUNDS EDGES" + possibleEdges.length);
		//			//If theres actually any roads with this name, take this as the name - otherwise, set it as an empty String
		//			actualRoadName = (possibleEdges.length > 0) ? possibleEdges[0].getRoadName().toLowerCase() : "";
		//		}

		System.out.println("ROADNAME: " + foundRoadName);
		adressArray[0] = foundRoadName;

		if(!foundRoadName.isEmpty())
		{
			System.out.println("Address left BEFORE roadname was found: " + addressAfterDeletion);
			addressAfterDeletion = addressAfterDeletion.replace(foundRoadName,"").trim();
		}
		System.out.println("Address left AFTER roadname was found: " + addressAfterDeletion);
	}

	/**	This method uses the matcher to find the number and eventual letter of a building. 
	 * 	It then isolates the number and places this at the index of the road number. 
	 * @param s Address string	
	 */
	private void findRoadNumber(String s) {
		Matcher buildingNr = match(pBuilding, s);
		if(buildingNr.find()) {																/* 9 */
			//NumberLetter gemmer bygningsnummeret et eventuelt bogstav. 
			numberLetter = buildingNr.group();
			Matcher tal = match(pTal, numberLetter);

			//Finder tallet i numberLetter og gemmer det p� index 1 i arrayet.
			if(tal.find()){																	/* 10 */				
				addressAfterDeletion = addressAfterDeletion.replace(tal.group(), "");
				System.out.println("Bygningstal: " + tal.group());
				adressArray[1] = tal.group().trim();
			}
		}
	}
	/** Uses the number and eventual letter found in roadNumber() and isolates the letter
	 * 	using a matcher. Places the letter in the array. 
	 * 
	 * @param s	Address string
	 */
	private void findRoadLetter(String s) {
		//Benytter numberLetter og finder bogstavet som den gemmer p� index 2 i arrayet.
		Matcher buildingLetter = match(pLetter, numberLetter);	
		if(buildingLetter.find()) {								   				   					/* 11 */
			addressAfterDeletion = addressAfterDeletion.replace(buildingLetter.group(), "");
			System.out.println("Bogstav: " + buildingLetter.group());
			adressArray[2] = buildingLetter.group().trim(); 
		}				
	}
	/**	Finds the floor of the address using a scanner and places this in the array.
	 * 
	 * @param s Address string
	 */
	private void findFloorNumber(String s) {
		// Bruger pattern pFloor til at finde etagen/sal/plan og gemm det p� index 3.
		String floorTemp = "";

		Matcher floor = match(pFloor, s);			
		if(floor.find()) {																		/* 12 */
			floorTemp = floor.group();
			match(pTal, floorTemp);
			addressAfterDeletion = addressAfterDeletion.replace(floorTemp, "");
			Matcher tal = match(pTal, floorTemp);		
			tal.find();																	
			System.out.println(tal.group() + ". etage");
			adressArray[3] = tal.group().trim();


		}


	}
	/**	Returns the postcode of the string and places it in the array.
	 * 
	 * @param s Address string
	 */
	private void findPostCode(String s) {
		// Bruger pattern pPost til at finde postnummeret og gemme det på index 4 i arrayet.
		Matcher postcode = match(pPost, s);	
		if(postcode.find()) {																	/* 13 */
			System.out.println("Postnummer: " + postcode.group());
			addressAfterDeletion = addressAfterDeletion.replace(postcode.group(),"").trim();
			adressArray[4] = postcode.group().trim(); 

			//If the string is not empty, even after the city was found, try finding the road name again
			//			if(!addressAfterDeletion.isEmpty() && adressArray[0].isEmpty())
			//				findRoadName(addressAfterDeletion);
		}
	}
	/**	Finds the name of the city by applying the patterns used to find the 
	 * 	previous parts of the address. After applying them, and some additional 
	 * 	patterns it replaces them with whitespace. Then the extra whitespace
	 * 	is removed and whatever remains will be the name of the city.  
	 * 
	 * @param s	Address string
	 */
	private void findCityName(String s) {
		String vejnavn = adressArray[0];
		System.out.println("LOOKING FOR CITYNAME IN THIS INPUT: " + s);
		String cityString = s.replaceAll(vejnavn, "").
				replaceAll(pPost, "").
				replaceAll(pBuilding, "").
				replaceAll(pFloor, "").
				replaceAll(pDelimiters, "").trim();

		if(!cityString.isEmpty()) {													/* 14 */
			String possibleCityName = CitySearch.searchForCityNameLongestPrefix(cityString);
			City[] possibleCities = CitySearch.searchForCityName(possibleCityName);
			//If theres actually any roads with this name, take this as the name - otherwise, set it as an empty String
			String actualCityName = (possibleCities.length > 0) ? possibleCities[0].getCityName().toLowerCase() : "";
			adressArray[5] = actualCityName;
			System.out.println("Bynavn: " + actualCityName);	
			addressAfterDeletion = cityString.replace(actualCityName,"").trim();

			//If the string is not empty, even after the city was found, try finding the road name again
			//			if(!addressAfterDeletion.isEmpty() && adressArray[0].isEmpty())
			//				findRoadName(addressAfterDeletion);
		}
	}

	public String[] getAdressArray(){
		return adressArray;
	}

	public static void main( String[] args ) throws MalformedAdressException, NoAddressFoundException {
		AdressParser aParser = new AdressParser();
		//aParser.parseAdress("Vandelvej 10, 4600 Køge");
		aParser.parseAdress("Strandvejen 133");

		for(String string : aParser.getAdressArray())
			System.out.println(string);
	}


}
