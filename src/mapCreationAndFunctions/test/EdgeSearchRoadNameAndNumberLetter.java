package mapCreationAndFunctions.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import inputHandler.EdgeSearch;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;
import mapCreationAndFunctions.data.Edge;

import org.junit.Test;

/**
 * Testclass for EdgeSearch with road name, number & letter.
 */
public class EdgeSearchRoadNameAndNumberLetter {
	
	public void testRoadNameAndNumber(String edgeToFind, int roadNumber, String letter, int expectedFinds, int postalNumber)
	{		
		Edge[] edgesFound;
		try {
			edgesFound = EdgeSearch.searchForRoads(edgeToFind, roadNumber, letter, postalNumber, "");

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
	 * Test for a raod with one occurence, number and no letter.
	 */
	@Test
	public void testRoadnameOnlyOneOccurenceNoLetter()
	{
		String edgeToFind = "Vandelvej";
		int roadNumber = 10;
		String letter = "";
		int postalNumber = -1;
		
		//roadNumber found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameAndNumber(edgeToFind, roadNumber, letter, expectedFinds, postalNumber);
	}
	
	/**
	 * Test for a road with on letter in To and From interval.
	 */
	@Test
	public void testRoadnameOnlyOneLetterInToFromInterval()
	{
		String edgeToFind = "Gammel Køgegård";
		int roadNumber = 3;
		String letter = "A";
		int postalNumber = -1;
		
		//roadNumber found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameAndNumber(edgeToFind, roadNumber, letter, expectedFinds, postalNumber);
	}
	
	/**
	 * Test roadname with two different letter in To and From interval.
	 */
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
		testRoadNameAndNumber(edgeToFind, roadNumber, letter, expectedFinds, postalNumber);
	}

	/**
	 * Test for road, two same letters in To and From interval.
	 */
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
		testRoadNameAndNumber(edgeToFind, roadNumber, letter, expectedFinds, postalNumber);
	}
	
	/**
	 * Test for road, two same numbers in To and From interval.
	 */
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
		testRoadNameAndNumber(edgeToFind, roadNumber, letter, expectedFinds, postalNumber);
	}
}
