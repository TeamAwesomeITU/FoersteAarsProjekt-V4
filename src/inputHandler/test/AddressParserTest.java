package inputHandler.test;

import static org.junit.Assert.*;
import inputHandler.AddressParserJesperLeger;
import inputHandler.exceptions.MalformedAdressException;

import org.junit.Test;

public class AddressParserTest {
	
	public void addressParserTestRoadNameNumberAndCitySingleOccExpected(String roadName, String roadNumber, String cityName) throws MalformedAdressException
	{
		AddressParserJesperLeger ap = new AddressParserJesperLeger();
		ap.setSearchResults(roadName + " " + roadNumber + " " + cityName);
		
		assertEquals(1, ap.getFoundEdges().length);
		assertEquals(1, ap.getFoundCities().length);
		
		assertEquals(roadName, ap.getFoundEdges()[0].getRoadName());
		assertEquals(cityName, ap.getFoundCities()[0].getCityName());
	}
	
	public void addressParserTestRoadNameNumberSingleOccExpected(String roadName, String roadNumber, String cityName) throws MalformedAdressException
	{
		AddressParserJesperLeger ap = new AddressParserJesperLeger();
		ap.setSearchResults(roadName + " " + roadNumber);
		
		assertEquals(1, ap.getFoundEdges().length);
		assertEquals(1, ap.getFoundCities().length);
		
		assertEquals(roadName, ap.getFoundEdges()[0].getRoadName());
		assertEquals(cityName, ap.getFoundCities()[0].getCityName());
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
