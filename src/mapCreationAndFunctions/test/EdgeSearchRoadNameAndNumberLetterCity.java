package mapCreationAndFunctions.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import inputHandler.EdgeSearch;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;
import mapCreationAndFunctions.data.Edge;

import org.junit.Test;

/**
 * Test for EdgeSearch with road name, number, letter and City.
 */
public class EdgeSearchRoadNameAndNumberLetterCity {
	
	public void testRoadNameNumberCity(String edgeToFind, int roadNumber, String letter, int expectedFinds, int postalNumber, String cityName)
	{		
		Edge[] edgesFound;
		try {
			edgesFound = EdgeSearch.searchForRoads(edgeToFind, roadNumber, letter, postalNumber, cityName);

			assertEquals(expectedFinds, edgesFound.length);
			
			for(Edge edge : edgesFound)
			{
				assertEquals(edge.getRoadName(), edgeToFind);
			}
			
		} catch (MalformedAddressException | NoAddressFoundException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Test for a road with one occurence, postal number and no letter.
	 */
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
	
	/**
	 * Test for roand with one letter, postal number in To and From interval.
	 */
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
	
	/**
	 * Test for a road with two different letters in To and From Interval. 
	 */
	@Test
	public void testRoadnameTwoDifferentLettersInToFromInterval()
	{
		String edgeToFind = "Stadionvej";
		int roadNumber = 10;
		
		//A letter in between the two biggest and smallest letters in the interval
		String letter = "D";
		int postalNumber = -1;
		String cityName = "";
		
		//roadNumber found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameNumberCity(edgeToFind, roadNumber, letter, expectedFinds, postalNumber, cityName);
	}

	/**
	 * Test for road name with two same letters in To and From interval.
	 */
	@Test
	public void testRoadnameTwoSameLettersInToFromInterval()
	{
		String edgeToFind = "Stadionvej";
		int roadNumber = 56;
		
		//The actual letter in the interval
		String letter = "B";
		int postalNumber = -1;
		String cityName = "";
		
		//roadNumber found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameNumberCity(edgeToFind, roadNumber, letter, expectedFinds, postalNumber, cityName);
	}
	
	/**
	 * Test for road name with two same numbers in To and From interval.
	 */
	@Test
	public void testRoadnameTwoSameNumbersInToFromInterval()
	{
		String edgeToFind = "Stadionvej";
		int roadNumber = 2;
		
		//The actual letter in the interval
		String letter = "B";
		int postalNumber = -1;
		String cityName = "";
		
		//roadNumber found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameNumberCity(edgeToFind, roadNumber, letter, expectedFinds, postalNumber, cityName);
	}
}
