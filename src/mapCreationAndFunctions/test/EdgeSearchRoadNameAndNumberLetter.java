package mapCreationAndFunctions.test;

import static org.junit.Assert.*;

import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.EdgeSearch;

import org.junit.Test;

public class EdgeSearchRoadNameAndNumberLetter {
	
	public void testRoadNameAndNumber(String edgeToFind, int number, String letter, int expectedFinds)
	{		
		Edge[] edgesFound = EdgeSearch.searchForRoadNameNumberAndLetter(edgeToFind, number, letter);
		assertEquals(expectedFinds, edgesFound.length);
		
		for(Edge edge : edgesFound)
		{
			assertEquals(edge.getRoadName(), edgeToFind);
		}
	}
	
	@Test
	public void testRoadnameOnlyOneOccurenceNoLetter()
	{
		String edgeToFind = "Vandelvej";
		int number = 10;
		String letter = "";
		
		//Number found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameAndNumber(edgeToFind, number, letter, expectedFinds);
	}
	
	@Test
	public void testRoadnameOnlyOneLetterInToFromInterval()
	{
		String edgeToFind = "Gammel Køgegård";
		int number = 3;
		String letter = "A";
		
		//Number found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameAndNumber(edgeToFind, number, letter, expectedFinds);
	}
	
	@Test
	public void testRoadnameTwoDifferentLettersInToFromInterval()
	{
		String edgeToFind = "Stadionvej";
		int number = 10;
		
		//A letter in between the two biggest and smallest letters in the interval
		String letter = "D";
		
		//Number found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameAndNumber(edgeToFind, number, letter, expectedFinds);
	}

	@Test
	public void testRoadnameTwoSameLettersInToFromInterval()
	{
		String edgeToFind = "Stadionvej";
		int number = 56;
		
		//The actual letter in the interval
		String letter = "B";
		
		//Number found by manual search in notepad++
		int expectedFinds = 1;
		testRoadNameAndNumber(edgeToFind, number, letter, expectedFinds);
	}
}
