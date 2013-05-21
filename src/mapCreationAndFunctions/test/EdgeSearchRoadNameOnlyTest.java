package mapCreationAndFunctions.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import inputHandler.EdgeSearch;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;
import mapCreationAndFunctions.data.Edge;

import org.junit.Test;

/**
 * Test for search for road name only.
 */
public class EdgeSearchRoadNameOnlyTest {
	
	/**
	 * Test for number of found road names.
	 * @param edgeToFind The edge to find.
	 * @param expectedFinds The expected find.
	 */
	public void testNumberOfFoundRoadNames(String edgeToFind, int expectedFinds)
	{		
		Edge[] edgesFound = EdgeSearch.searchForRoadName(edgeToFind);
		assertEquals(expectedFinds, edgesFound.length);
		
		for(Edge edge : edgesFound)
			//Test if the found Edge's road name is equal to what we are searching for
			assertEquals(edge.getRoadName().toLowerCase(), edgeToFind.toLowerCase());
	}
	
	/**
	 * Test for number of found road names suggestions.
	 * @param edgeToFind The edge to find.
	 * @param expectedFinds The expected finds.
	 */
	public void testNumberOfFoundRoadNamesSuggestions(String edgeToFind, int expectedFinds)
	{		
		Edge[] edgesFound;
		try {
			edgesFound = EdgeSearch.searchForRoads(edgeToFind, -1, "", -1, "");
			assertEquals(expectedFinds, edgesFound.length);
			
			for(Edge edge : edgesFound)
				//Test if the found Edge's road name actually contains what we are searching for
				assertTrue(edge.getRoadName().toLowerCase().contains(edgeToFind.toLowerCase()));
		} catch (MalformedAddressException | NoAddressFoundException e) {
			fail();
			e.printStackTrace();
		}

	}
	
	/**
	 * Test for road name with no whitespaces and a number.
	 */
	@Test
	public void testRoadnameNoWhitespacesNumber()
	{
		String edgeToFind = "Nørregade";
		//Number found by manual search
		int expectedFinds = 1117;
		testNumberOfFoundRoadNames(edgeToFind, expectedFinds);
	}
	
	/**
	 * Test for road name with no whitespace and a number suggestions.
	 */
	@Test
	public void testRoadnameNoWhitespacesNumberSuggestions()
	{
		String edgeToFind = "Nørregade";
		//Number found by manual search
		int expectedFinds = 1122;
		testNumberOfFoundRoadNamesSuggestions(edgeToFind, expectedFinds);
	}
	
	/**
	 * Test for road name with no whitespcae and a number, in lowercase.
	 */
	@Test
	public void testRoadNameNoWhitespaceNumberLowerCase()
	{
		String edgeToFind = "nørregade";
		//Number found by manual search
		int expectedFinds = 1117;
		testNumberOfFoundRoadNames(edgeToFind, expectedFinds);
	}
	
	/**
	 * Test for road name with no whitespace and a number, in lowercase suggestions.
	 */
	@Test
	public void testRoadNameNoWhitespaceNumberLowerCaseSuggestions()
	{
		String edgeToFind = "nørregade";
		//Number found by manual search
		int expectedFinds = 1122;
		testNumberOfFoundRoadNamesSuggestions(edgeToFind, expectedFinds);
	}
	
	/**
	 * Test for road name with single whitespace and a number.
	 */
	@Test
	public void testRoadNameSingleWhitespaceNumber()
	{
		String edgeToFind = "Kongens Vænge";
		//Number found by manual search
		int expectedFinds = 48;
		testNumberOfFoundRoadNames(edgeToFind, expectedFinds);
	}
	
	/**
	 * Test for road name with single whitespace and a number, in lowercase. 
	 */
	@Test
	public void testRoadNameSingleWhitespaceNumberLowerCase()
	{
		String edgeToFind = "kongens vænge";
		//Number found by manual search
		int expectedFinds = 48;
		testNumberOfFoundRoadNames(edgeToFind, expectedFinds);
	}
	
	/**
	 * Test for a road name as whitespace and a number.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testWhiteSpaceAsRoadnameNumber()
	{
		testNumberOfFoundRoadNames(" ", 10000);
	}
	
	/**
	 * Test for empty road name and a number.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyRoadnameNumber()
	{
		testNumberOfFoundRoadNames("", 10000);
	}
	
	/**
	 * Test for suggestions for Nø.
	 */
	public void testSuggestions1()
	{
		String edgeToFind = "Nø";
		//Number found by manual search
		int expectedFinds = 7569;
		testNumberOfFoundRoadNamesSuggestions(edgeToFind, expectedFinds);
	}
	
	/**
	 * Test for suggestions for B.
	 */
	public void testSuggestions2()
	{
		String edgeToFind = "B";
		//Number found by manual search
		int expectedFinds = 63455;
		testNumberOfFoundRoadNamesSuggestions(edgeToFind, expectedFinds);
	}

}
