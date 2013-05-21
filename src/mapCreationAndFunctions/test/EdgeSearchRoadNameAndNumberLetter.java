package mapCreationAndFunctions.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import inputHandler.EdgeSearch;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;
import mapCreationAndFunctions.data.Edge;

import org.junit.Test;

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
