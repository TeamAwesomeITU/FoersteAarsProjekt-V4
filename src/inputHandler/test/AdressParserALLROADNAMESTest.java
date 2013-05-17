package inputHandler.test;

import static org.junit.Assert.assertEquals;
import inputHandler.AddressSearch;
import inputHandler.AdressParser;
import inputHandler.exceptions.MalformedAdressException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;

import org.junit.Test;


/*
 * This test tests if AdressParser returns the correct roadname for every single road in road_names.txt.
 */
public class AdressParserALLROADNAMESTest {

	@Test
	public void testALLROADNAMES() throws IOException, MalformedAdressException {
		
		Edge[] allEdges = DataHolding.getEdgeArray();
		AddressSearch addressSearch;		
		
		for(Edge edge : allEdges)
		{
			String roadName = edge.getRoadName();
			addressSearch = new AddressSearch();
			addressSearch.searchForAdress("roadName");
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		String fileLine;
		AdressParser ap = new AdressParser();
		
		while((fileLine = reader.readLine()) != null)
			try {
				assertEquals(fileLine, ap.parseAdress(fileLine)[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		reader.close();
	}

}
