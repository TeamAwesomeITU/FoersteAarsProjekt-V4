package mapCreationAndFunctions.test;

import static org.junit.Assert.*;

import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.EdgeSearch;

import org.junit.Test;

public class EdgeSearchRoadNameOnlyTest {
	
	public void testNumberOfFoundRoadNames(String edgeToFind, int expectedFinds)
	{		
		Edge[] edgesFound = EdgeSearch.searchForRoadName(edgeToFind);
		assertEquals(expectedFinds, edgesFound.length);
		
		for(Edge edge : edgesFound)
			assertEquals(edge.getRoadName(), edgeToFind);
	}
	
	@Test
	public void testRoadnameNoWhitespacesNumber()
	{
		String edgeToFind = "Nørregade";
		//Number found by manual search in notepad++
		int expectedFinds = 1117;
		testNumberOfFoundRoadNames(edgeToFind, expectedFinds);
	}
	
	@Test
	public void testRoadNameSingleWhitespaceNumber()
	{
		String edgeToFind = "Kongens Vænge";
		//Number found by manual search in notepad++
		int expectedFinds = 48;
		testNumberOfFoundRoadNames(edgeToFind, expectedFinds);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWhiteSpaceAsRoadnameNumber()
	{
		testNumberOfFoundRoadNames(" ", 10000);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyRoadnameNumber()
	{
		testNumberOfFoundRoadNames("", 10000);
	}

}
