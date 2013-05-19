package inputHandler.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;

import org.hamcrest.Matcher;
import org.junit.matchers.JUnitMatchers;
import inputHandler.AddressSearch;
import inputHandler.AdressParser;
import inputHandler.exceptions.MalformedAdressException;
import inputHandler.exceptions.NoAddressFoundException;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.Edge;

import org.junit.Test;

public class AddressParserTest {

	//I MIGHT BE A TAD USELESS
	/*
	public void addressParserTestRoadNameSingleOccExpected(String roadName, String input) throws MalformedAdressException, NoAddressFoundException
	{
		AddressSearch as = new AddressSearch();
		as.searchForAdress(input);

		Edge[] foundEdges = as.getFoundEdges();


		assertEquals(1, foundEdges.length);

		for(Edge edge : foundEdges)
			System.out.println(edge);

		assertEquals(roadName, as.getFoundEdges()[0].getRoadName());

	}
	 */

	public void addressParserTestNameNumberCity(String roadName, int roadNumber, int postalNumber, String cityName, String input) throws MalformedAdressException, NoAddressFoundException
	{
		HashSet<Integer> numbersOnEdge = new HashSet<>();
		AddressSearch as = new AddressSearch();
		as.searchForAdress(input);

		Edge[] foundEdges = as.getFoundEdges();


		assertEquals(1, foundEdges.length);

		for(Edge edge : foundEdges)
			System.out.println(edge);

		for(Integer numbers: as.getFoundEdges()[0].containedNumbers()) {
			numbersOnEdge.add(numbers);
		}

		assertEquals(roadName, as.getFoundEdges()[0].getRoadName());
		assertTrue(numbersOnEdge.contains(roadNumber));
		assertEquals("Cityname does not equal postalNumber left", cityName, as.getFoundEdges()[0].getPostalNumberLeftCityName());
		assertEquals("Cityname does not equal postalNumber Right", cityName, as.getFoundEdges()[0].getPostalNumberRightCityName());
		assertEquals("Postalnumber does not equal postalNumber left", postalNumber, as.getFoundEdges()[0].getPostalNumberLeft());
		assertEquals("Postalnumber does not equal postalNumber Right", postalNumber, as.getFoundEdges()[0].getPostalNumberRight());

	}

	@Test
	public void testStandardInputStyle() {
		String roadName = "Vandelvej";
		int roadNumber = 10;
		int postalNumber = 4600;
		String cityName = "Køge";
		String input = "Vandelvej 10 4600 Køge";

		try {
			addressParserTestNameNumberCity(roadName, roadNumber, postalNumber, cityName, input);
		} catch (MalformedAdressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			// TODO Auto-generated catch block
			fail("No address was found");
		}
	}

	@Test
	public void testMixedInputStyle() {
		String roadName = "Strandvejen";
		int roadNumber = 133;
		int postalNumber = 3300;
		String cityName = "Frederiksværk";
		String input = "Frederiksværk 3300 Strandvejen 133";

		try {
			addressParserTestNameNumberCity(roadName, roadNumber, postalNumber, cityName, input);
		} catch (MalformedAdressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			// TODO Auto-generated catch block
			fail("No address was found");
		}

	}

	@Test
	public void testComplicatedInputStyle() {
		String roadName = "Skaffervej";
		int roadNumber = 15;
		int postalNumber = 2400;
		String cityName = "København";
		String input = "15 Skaffervej, København 2400";

		try {
			addressParserTestNameNumberCity(roadName, roadNumber, postalNumber, cityName, input);
		} catch (MalformedAdressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			// TODO Auto-generated catch block
			fail("No address was found");
		}
	}
	@Test
	public void testUnlikelyInputStyle() {
		String roadName = "Vandelvej";
		int roadNumber = 10;
		int postalNumber = 4600;
		String cityName = "Køge";
		String input = "10 Køge 4600 Vandelvej";

		try {
			addressParserTestNameNumberCity(roadName, roadNumber, postalNumber, cityName, input);
		} catch (MalformedAdressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			// TODO Auto-generated catch block
			fail("No address was found");
		}
	}
	@Test
	public void testHighlyUnlikelyInputStyle() {
		String roadName = "Vandelvej";
		int roadNumber = 10;
		int postalNumber = 4600;
		String cityName = "Køge";
		String input = "10 Køge Vandelvej 4600";

		try {
			addressParserTestNameNumberCity(roadName, roadNumber, postalNumber, cityName, input);
		} catch (MalformedAdressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			// TODO Auto-generated catch block
			fail("No address was found");
		}
	}
	@Test
	public void testShouldNeverHappenInputStyle() {
		String roadName = "Vandelvej";
		int roadNumber = 10;
		int postalNumber = 4600;
		String cityName = "Køge";
		String input = "10 4600 Vandelvej Køge 133 Frederiksværk";
		try {
			addressParserTestNameNumberCity(roadName, roadNumber, postalNumber, cityName, input);
		} catch (MalformedAdressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			// TODO Auto-generated catch block
			fail("No address was found");
		}
	}
}
