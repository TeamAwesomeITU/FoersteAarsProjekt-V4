package inputHandler;

import java.util.TreeSet;

import inputHandler.exceptions.MalformedAdressException;
import inputHandler.exceptions.NoAddressFoundException;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.EdgeSearch;

@Deprecated
public class AddressSuggester {

	private Edge[] foundEdges = new Edge[0];
	private String enteredRoadName = "";
	private int enteredRoadNumber = -1;
	private String enteredRoadLetter = "";
	private String enteredCityName = "";
	private int enteredPostalNumber = -1;

	public AddressSuggester()
	{ }

	public Edge[] getFoundEdges() throws MalformedAdressException, NoAddressFoundException
	{ 
		System.out.println("Number of found edges: " + foundEdges.length);
		return foundEdges; 
	}

	private void calculateResults() throws MalformedAdressException, NoAddressFoundException
	{ 
		foundEdges = EdgeSearch.searchForRoadSuggestions(enteredRoadName, enteredRoadNumber, enteredRoadLetter, enteredPostalNumber, enteredCityName);
		System.out.println("NUMBER OF FOUND EDGES: " + foundEdges.length);
	}

	public void enterRoadNumber(String number) throws MalformedAdressException, NoAddressFoundException
	{
		if(!number.isEmpty())
			this.enteredRoadNumber = Integer.parseInt(number); 
		else
			this.enteredRoadNumber = -1;
		calculateResults();
	}

	public void enterRoadLetter(String roadLetter) throws MalformedAdressException, NoAddressFoundException
	{ this.enteredRoadLetter = roadLetter; calculateResults();}

	public void enterCityName(String cityName) throws MalformedAdressException, NoAddressFoundException
	{ this.enteredCityName = cityName; calculateResults();}

	public void enterPostalNumber(String postalNumber) throws MalformedAdressException, NoAddressFoundException
	{
		if(!postalNumber.isEmpty())
			this.enteredPostalNumber = Integer.parseInt(postalNumber);
		else
			this.enteredPostalNumber = -1;
		calculateResults();
	}

	public void enterRoadName(String roadName) throws MalformedAdressException, NoAddressFoundException
	{ enteredRoadName = roadName; calculateResults(); }	

	public String[] getPossibleRoadNames()
	{
		TreeSet<String> possibleRoadNamesSet = new TreeSet<>();
		for(Edge edge : foundEdges)
			possibleRoadNamesSet.add(edge.getRoadName());

		return possibleRoadNamesSet.toArray(new String[possibleRoadNamesSet.size()]);
	}

	public String[] getPossibleRoadNumbers()
	{
		TreeSet<String> possibleCitiesSet = new TreeSet<>();
		for(Edge edge : foundEdges)
			for(Integer number : edge.containedNumbers())
				possibleCitiesSet.add(number.toString());

		return possibleCitiesSet.toArray(new String[possibleCitiesSet.size()]);
	}

	public String[] getPossibleRoadLetters()
	{
		TreeSet<String> possibleLettersSet = new TreeSet<>();
		for(Edge edge : foundEdges)
			for(String letter : edge.containedLetters())
				possibleLettersSet.add(letter);

		return possibleLettersSet.toArray(new String[possibleLettersSet.size()]);
	}

	public String[] getPossibleCityNames()
	{
		TreeSet<String> possibleCitiesSet = new TreeSet<>();
		for(Edge edge : foundEdges)
		{
			possibleCitiesSet.add(edge.getPostalNumberLeftCityName());
			possibleCitiesSet.add(edge.getPostalNumberRightCityName());
		}

		return possibleCitiesSet.toArray(new String[possibleCitiesSet.size()]);
	}

	public String[] getPossiblePostalNumbers()
	{
		TreeSet<String> possibleCitiesSet = new TreeSet<>();
		for(Edge edge : foundEdges)
		{
			possibleCitiesSet.add(edge.getPostalNumberLeft() + "");
			possibleCitiesSet.add(edge.getPostalNumberRight() + "");
		}

		return possibleCitiesSet.toArray(new String[possibleCitiesSet.size()]);
	}
	
	public static void main(String[] args) throws MalformedAdressException, NoAddressFoundException {
		AddressSuggester as = new AddressSuggester();
		as.enterCityName("Køge");
		//as.enterRoadName("Vandelvej");
		
		System.out.println("----------------------------------");
		
		for(String string : as.getPossibleRoadNames())
			System.out.println(string);
		
		for(String number : as.getPossibleRoadNumbers())
			System.out.println(number);
		
		for(String letter : as.getPossibleRoadLetters())
			System.out.println(letter);
		
		System.out.println("----------------------------------");
	}

}
