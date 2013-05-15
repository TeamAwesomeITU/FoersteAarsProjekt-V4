package inputHandler;

import java.util.HashSet;
import java.util.TreeSet;

import inputHandler.exceptions.MalformedAdressException;
import inputHandler.exceptions.NoAddressFoundException;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.EdgeSearch;

public class AddressSuggester {

	private Edge[] foundEdges = new Edge[0];
	private String enteredRoadName = "";
	private int enteredRoadNumber = -1;
	private String enteredRoadLetter = "";
	private String enteredCityName = "";
	private int enteredPostalNumber = -1;

	public void AddressSuggester()
	{ }

	public Edge[] getFoundEdges() throws MalformedAdressException, NoAddressFoundException
	{ 
		calculateResults();
		System.out.println("Number of found edges: " + foundEdges.length);
		return foundEdges; 
	}

	private void calculateResults() throws MalformedAdressException, NoAddressFoundException
	{ foundEdges = EdgeSearch.searchForRoadSuggestions(enteredRoadName, enteredRoadNumber, enteredRoadLetter, enteredPostalNumber, enteredCityName); }

	public void enterRoadNumber(int roadNumber) throws MalformedAdressException, NoAddressFoundException
	{ this.enteredRoadNumber = roadNumber; calculateResults();}

	public void enterRoadLetter(String roadLetter) throws MalformedAdressException, NoAddressFoundException
	{ this.enteredRoadLetter = roadLetter; calculateResults();}

	public void enterCityName(String cityName) throws MalformedAdressException, NoAddressFoundException
	{ this.enteredCityName = cityName; calculateResults();}

	public void enterPostalNumber(int postalNumber) throws MalformedAdressException, NoAddressFoundException
	{ this.enteredPostalNumber = postalNumber; calculateResults();}

	public void enterRoadName(String roadName) throws MalformedAdressException, NoAddressFoundException
	{ enteredRoadName = roadName; calculateResults(); }	

	public String[] getPossibleRoadNames()
	{
		TreeSet<String> possibleRoadNamesSet = new TreeSet<>();
		for(Edge edge : foundEdges)
			possibleRoadNamesSet.add(edge.getRoadName());

		return possibleRoadNamesSet.toArray(new String[possibleRoadNamesSet.size()]);
	}

	public Integer[] getPossibleRoadNumbers()
	{
		TreeSet<Integer> possibleCitiesSet = new TreeSet<>();
		for(Edge edge : foundEdges)
			for(int number : edge.containedNumbers())
				possibleCitiesSet.add(number);
		
		int[] numberArray = new int[possibleCitiesSet.size()];
		

		return possibleCitiesSet.toArray(new Integer[possibleCitiesSet.size()]);
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

	public Integer[] getPossiblePostalNumbers()
	{
		TreeSet<Integer> possibleCitiesSet = new TreeSet<>();
		for(Edge edge : foundEdges)
		{
			possibleCitiesSet.add(edge.getPostalNumberLeft());
			possibleCitiesSet.add(edge.getPostalNumberRight());
		}

		return possibleCitiesSet.toArray(new Integer[possibleCitiesSet.size()]);
	}
	
	public static void main(String[] args) throws MalformedAdressException, NoAddressFoundException {
		AddressSuggester as = new AddressSuggester();
		as.enterCityName("Køge");
		//as.enterRoadName("Vandelvej");
		
		System.out.println("----------------------------------");
		
		for(String string : as.getPossibleRoadNames())
			System.out.println(string);
		
		for(Integer number : as.getPossibleRoadNumbers())
			System.out.println(number);
		
		for(String letter : as.getPossibleRoadLetters())
			System.out.println(letter);
		
		System.out.println("----------------------------------");
	}

}
