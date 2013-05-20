package inputHandler.test;

import static org.junit.Assert.*;
import inputHandler.AddressSearch;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

import java.io.IOException;
import java.util.ArrayList;

import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;

import org.junit.Test;

/**
 * This test tests if AddressParser returns the correct roadname for every single road in the Edge[] containing all Edges in our program
 * The test fails if an exception is thrown, or no Edges are found with the given name.
 *  If an exception thrown by AddressSearch - this exception is caught, and it's message saved as a String in an ArrayList. This list is printed at the end of the test.
 */
public class AdressParserAllRoadNamesTest {

	/**
	 * 
	 * @throws IOException
	 * @throws MalformedAddressException
	 * @throws NoAddressFoundException
	 */
	@Test
	public void testALLROADNAMES() {

		Edge[] allEdges = DataHolding.getEdgeArray();
		AddressSearch addressSearch;		
		String roadName = "";
		ArrayList<String> mistakesCaught = new ArrayList<>();

		for(Edge edgeOfAllEdges : allEdges)
		{
			roadName = edgeOfAllEdges.getRoadName().toLowerCase();

			if(!roadName.isEmpty())
			{
				addressSearch = new AddressSearch();
				try {
					addressSearch.searchForAdress(roadName);
					
					if(addressSearch.getFoundEdges().length == 0)
						mistakesCaught.add("Did not find anything with this input: " + roadName + ", " + edgeOfAllEdges.getiD());

					for(Edge edgeFound : addressSearch.getFoundEdges())
						assertEquals(roadName, edgeFound.getRoadName().toLowerCase());
					
				} catch (Exception e) {
					mistakesCaught.add(e.getMessage() + ": " + roadName + " - " + edgeOfAllEdges.getiD());
				}
			}

		}
		
		for(String mistake : mistakesCaught)
			System.out.println(mistake);		
		
		assertEquals("Number of mistakes caught", 0, mistakesCaught.size());
	}

}
