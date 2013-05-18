package inputHandler.test;

import static org.junit.Assert.*;
import inputHandler.AdressParser;
import inputHandler.exceptions.MalformedAdressException;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.Edge;

import org.junit.Test;

public class AddressParserTest {
	
	public void addressParserTestRoadNameSingleOccExpected(String roadName, String roadNumber, String cityName) throws MalformedAdressException
	{
		AdressParser ap = new AdressParser();
		ap.getSearchResults(roadName);
		
		Edge[] foundEdges = ap.getFoundEdges();
		City[] foundCities = ap.getFoundCities();
		
		assertEquals(1, foundEdges.length);
		//assertEquals(1, foundCities.length);
		
		for(Edge edge : foundEdges)
			System.out.println(edge);
		
		for(City city : foundCities)
			System.out.println(city);
		
		assertEquals(roadName, ap.getFoundEdges()[0].getRoadName());
		//assertEquals(cityName, ap.getFoundCities()[0].getCityName());
	}
	
	public void addressParserTestRoadNameNumberSingleOccExpected(String roadName, String roadNumber, String cityName) throws MalformedAdressException
	{
		AddressParserJesperLeger ap = new AddressParserJesperLeger();
		ap.getSearchResults(roadName + " " + roadNumber);
		
		Edge[] foundEdges = ap.getFoundEdges();
		City[] foundCities = ap.getFoundCities();
		
		assertEquals(1, foundEdges.length);
		//assertEquals(1, foundCities.length);
		
		for(Edge edge : foundEdges)
			System.out.println(edge);
		
		for(City city : foundCities)
			System.out.println(city);
		
		assertEquals(roadName, ap.getFoundEdges()[0].getRoadName());
		//assertEquals(cityName, ap.getFoundCities()[0].getCityName());
	}
	
	public void addressParserTestRoadNameNumberAndCitySingleOccExpected(String roadName, String roadNumber, String cityName) throws MalformedAdressException
	{
		AddressParserJesperLeger ap = new AddressParserJesperLeger();
		ap.getSearchResults(roadName + " " + roadNumber + " " + cityName);
		
		Edge[] foundEdges = ap.getFoundEdges();
		City[] foundCities = ap.getFoundCities();
		
		assertEquals(1, foundEdges.length);
		//assertEquals(1, foundCities.length);
		
		for(Edge edge : foundEdges)
			System.out.println(edge);
		
		for(City city : foundCities)
			System.out.println(city);
		
		assertEquals(roadName, ap.getFoundEdges()[0].getRoadName());
		//assertEquals(cityName, ap.getFoundCities()[0].getCityName());
	}

	@Test
	public void testSingleOccVandelvejNameNumberCity() {
		String roadName = "Vandelvej";
		String roadNumber = "10";
		String cityName = "Køge";
		
		try {
			addressParserTestRoadNameNumberAndCitySingleOccExpected(roadName, roadNumber, cityName);
		} catch (MalformedAdressException e) {
			fail();
		}
	}
	
	@Test
	public void testSingleOccVandelvejNameNumber() {
		String roadName = "Vandelvej";
		String roadNumber = "10";
		String cityName = "Køge";
		
		try {
			addressParserTestRoadNameNumberSingleOccExpected(roadName, roadNumber, cityName);
		} catch (MalformedAdressException e) {
			fail();
		}
	}

	
	@Test
	public void testSingleOccSkaffervejName() {
		String roadName = "Skaffervej";
		String roadNumber = "15";
		String cityName = "København NV";
		
		try {
			addressParserTestRoadNameSingleOccExpected(roadName, roadNumber, cityName);
		} catch (MalformedAdressException e) {
			fail();
		}
	}
	
	@Test
	public void testSingleOccSkaffervejNameNumberCity() {
		String roadName = "Skaffervej";
		String roadNumber = "15";
		String cityName = "København NV";
		
		try {
			addressParserTestRoadNameNumberAndCitySingleOccExpected(roadName, roadNumber, cityName);
		} catch (MalformedAdressException e) {
			fail();
		}
	}
	
	@Test
	public void testSingleOccSkaffervejNameNumber() {
		String roadName = "Skaffervej";
		String roadNumber = "15";
		String cityName = "København NV";
		
		try {
			addressParserTestRoadNameNumberSingleOccExpected(roadName, roadNumber, cityName);
		} catch (MalformedAdressException e) {
			fail();
		}
	}
}
