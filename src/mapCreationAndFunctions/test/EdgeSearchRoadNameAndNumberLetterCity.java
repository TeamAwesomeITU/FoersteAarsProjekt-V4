package mapCreationAndFunctions.test;

import static org.junit.Assert.*;
import inputHandler.exceptions.MalformedAdressException;

import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.EdgeSearch;

import org.junit.Test;

public class EdgeSearchRoadNameAndNumberLetterCity {
	
	public void testRoadNameNumberCity(String edgeToFind, int roadNumber, String letter, int expectedFinds, int postalNumber, String cityName)
	{		
		Edge[] edgesFound;
		try {
			edgesFound = EdgeSearch.searchForRoadSuggestions(edgeToFind, roadNumber, letter, postalNumber, cityName);

			assertEquals(expectedFinds, edgesFound.length);
			
			for(Edge edge : edgesFound)
			{
				assertEquals(edge.getRoadName(), edgeToFind);
			}
			
		} catch (MalformedAdressException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRoadnameOnlyOneOccurenceNoLetter()
	{
		String edgeToFind = "Vandelvej";
		int roadNumber = 10;
		String letter = "";
		int postalNumber = 4600;
		String cityName = "Køge";
		
		//roadNumber found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameNumberCity(edgeToFind, roadNumber, letter, expectedFinds, postalNumber, cityName);
	}
	
	@Test
	public void testRoadnameOnlyOneLetterInToFromInterval()
	{
		String edgeToFind = "Gammel Køgegård";
		int roadNumber = 3;
		String letter = "A";
		int postalNumber = 4600;
		String cityName = "Køge";
		
		//roadNumber found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameNumberCity(edgeToFind, roadNumber, letter, expectedFinds, postalNumber, cityName);
	}
	
	@Test
	public void testRoadnameTwoDifferentLettersInToFromInterval()
	{
		String edgeToFind = "Stadionvej";
		int roadNumber = 10;
		
		//A letter in between the two biggest and smallest letters in the interval
		String letter = "D";
		int postalNumber = -1;
		
		//roadNumber found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameNumberCity(edgeToFind, roadNumber, letter, expectedFinds, postalNumber, cityName);
	}

	@Test
	public void testRoadnameTwoSameLettersInToFromInterval()
	{
		String edgeToFind = "Stadionvej";
		int roadNumber = 56;
		
		//The actual letter in the interval
		String letter = "B";
		int postalNumber = -1;
		
		//roadNumber found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameNumberCity(edgeToFind, roadNumber, letter, expectedFinds, postalNumber, cityName);
	}
	
	@Test
	public void testRoadnameTwoSameNumbersInToFromInterval()
	{
		String edgeToFind = "Stadionvej";
		int roadNumber = 2;
		
		//The actual letter in the interval
		String letter = "B";
		int postalNumber = -1;
		
		//roadNumber found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameNumberCity(edgeToFind, roadNumber, letter, expectedFinds, postalNumber, cityName);
	}
}
