package inputHandler;

import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.EdgeSearch;
import inputHandler.exceptions.MalformedAdressException;

public class AddressSearch {
	
	private AdressParser ap;
	private String[] parsedInput;
	private Edge[] foundEdges;
	
	public AddressSearch()
	{
		ap = new AdressParser();		
	}
		
	public void searchForAdress(String input) throws MalformedAdressException
	{
		parseAddress(input);
		int roadNumber = -1;
		int postalNumber = -1;
		
		try {
			roadNumber = Integer.parseInt(parsedInput[1]);
		} catch (NumberFormatException e) {
			
		}
		
		try {
			roadNumber = Integer.parseInt(parsedInput[1]);
		} catch (NumberFormatException e) {
			
		}
		
		foundEdges = EdgeSearch.searchForRoadSuggestions(parsedInput[0], roadNumber, parsedInput[2], postalNumber, parsedInput[5]);
	}
	
	public Edge[] getFoundEdges()
	{
		return foundEdges;
	}
	
	private void parseAddress(String input) throws MalformedAdressException
	{
		parsedInput = ap.parseAdress(input);
	}
	
	public static void main( String[] args ) throws MalformedAdressException {
		AddressSearch adressSearch = new AddressSearch();
		adressSearch.searchForAdress("Vandelvej i Køge");
		
		for(Edge edge : adressSearch.getFoundEdges())
			System.out.println(edge);
	}


}
