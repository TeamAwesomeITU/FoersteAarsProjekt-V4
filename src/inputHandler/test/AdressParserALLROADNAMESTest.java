package inputHandler.test;

import static org.junit.Assert.*;
import inputHandler.AddressSearch;
import inputHandler.exceptions.MalformedAdressException;
import inputHandler.exceptions.NoAddressFoundException;

import java.io.IOException;
import java.util.ArrayList;

import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;

import org.junit.Test;


/*
 * This test tests if AdressParser returns the correct roadname for every single road in the Edge[] containing all Edges in our program
 * If the test fails, an exception might be thrown by AddressSearch - this exception is caught, and it's message saved as a String in an ArrayList. This list is printed at the end of the test.
 */
public class AdressParserALLROADNAMESTest {

	/**
	 * 
	 * @throws IOException
	 * @throws MalformedAdressException
	 * @throws NoAddressFoundException
	 */
	@Test
	public void testALLROADNAMES() {

		Edge[] allEdges = DataHolding.getEdgeArray();
		AddressSearch addressSearch;		
		String roadName = "";
		ArrayList<String> exceptionsCaught = new ArrayList<>();

		for(Edge edgeOfAllEdges : allEdges)
		{
			roadName = edgeOfAllEdges.getRoadName().toLowerCase();

			if(!roadName.isEmpty())
			{
				addressSearch = new AddressSearch();
				try {
					addressSearch.searchForAdress(roadName);

					for(Edge edgeFound : addressSearch.getFoundEdges())
						assertEquals(roadName, edgeFound.getRoadName().toLowerCase());
					
				} catch (Exception e) {
					exceptionsCaught.add(e.getMessage() + ": " + roadName + " - " + edgeOfAllEdges.getiD());
				}
			}

		}
		
		for(String exception : exceptionsCaught)
			System.out.println(exception);		
		
		assertEquals("Number of exceptions caught", 0, exceptionsCaught.size());
	}

}
