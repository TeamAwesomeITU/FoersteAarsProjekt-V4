package inputHandler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import inputHandler.AddressSearch;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

import java.util.HashSet;

import mapCreationAndFunctions.data.Edge;

import org.junit.Test;

public class AddressSearchTest {


	/**
	 * Setup for the tests.
	 * @param The road name you want to find.
	 * @param The road number you want to find.
	 * @param The letter you want to find.
	 * @param The postal number you want to find.
	 * @param The city you want to find.
	 * @param The entire addresss written in a single line.
	 * @throws MalformedAddressException
	 * @throws NoAddressFoundException
	 */
	public  void addressParserTestNameNumberCity(String roadName, int roadNumber, String letters, int postalNumber, String cityName, String input) throws MalformedAddressException, NoAddressFoundException
	{
		HashSet<Integer> numbersOnEdge = new HashSet<>();
		HashSet<String> lettersOnEdge = new HashSet<>();
		AddressSearch as = new AddressSearch();
		as.searchForAdress(input);

		Edge[] foundEdges = as.getFoundEdges();

		for(Integer numbers: as.getFoundEdges()[0].containedNumbers()) 
		{
			numbersOnEdge.add(numbers);
		}
		for(String letter : as.getFoundEdges()[0].containedLetters())
		{
			lettersOnEdge.add(letter);
		}	
		assertEquals(1, foundEdges.length);
		assertEquals(roadName, as.getFoundEdges()[0].getRoadName());
		if(!lettersOnEdge.isEmpty()) 	
		{
		assertTrue(lettersOnEdge.contains(letters));
		}
		assertTrue(numbersOnEdge.contains(roadNumber));
		assertTrue(cityName, as.getFoundEdges()[0].getPostalNumberLeftCityName().equals(cityName) || as.getFoundEdges()[0].getPostalNumberRightCityName().equals(cityName));
		assertTrue(as.getFoundEdges()[0].getPostalNumberLeft() == postalNumber || as.getFoundEdges()[0].getPostalNumberRight() == postalNumber);

	}
	
	@Test
	
	public void testStandardInputStyle() {
		String roadName = "Vandelvej";
		int roadNumber = 10;
		String letter = "";
		int postalNumber = 4600;
		String cityName = "Køge";
		String input = "Vandelvej 10 4600 Køge";

		try {
			addressParserTestNameNumberCity(roadName, roadNumber, letter, postalNumber, cityName, input);
		} catch (MalformedAddressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			fail("No address was found");
		}
	}
	@Test
	public void testMixedInputStyle() {
		String roadName = "Strandvejen";
		int roadNumber = 133;
		String letter = "";
		int postalNumber = 3300;
		String cityName = "Frederiksværk";
		String input = "Frederiksværk 3300 Strandvejen 133";

		try {
			addressParserTestNameNumberCity(roadName, roadNumber, letter, postalNumber, cityName, input);
		} catch (MalformedAddressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			fail("No address was found");
		}
	}
	@Test
	/**
	 * 
	 */
	public void testComplicatedInputStyle() {
		String roadName = "Skaffervej";
		int roadNumber = 15;
		String letter = "";
		int postalNumber = 2400;
		String cityName = "København";
		String input = "15 Skaffervej, København 2400";

		try {
			addressParserTestNameNumberCity(roadName, roadNumber, letter, postalNumber, cityName, input);
		} catch (MalformedAddressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			fail("No address was found");
		}
	}
	@Test
	public void testUnlikelyInputStyle() {
		String roadName = "Vandelvej";
		int roadNumber = 10;
		String letter = "";
		int postalNumber = 4600;
		String cityName = "Køge";
		String input = "10 Køge 4600 Vandelvej";

		try {
			addressParserTestNameNumberCity(roadName, roadNumber, letter, postalNumber, cityName, input);
		} catch (MalformedAddressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			fail("No address was found");
		}
	}
	@Test
	public void testHighlyUnlikelyInputStyle() {
		String roadName = "Vandelvej";
		int roadNumber = 10;
		String letter = "";
		int postalNumber = 4600;
		String cityName = "Køge";
		String input = "10 Køge Vandelvej 4600";

		try {
			addressParserTestNameNumberCity(roadName, roadNumber, letter, postalNumber, cityName, input);
		} catch (MalformedAddressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			fail("No address was found");
		}
	}
	@Test (expected=MalformedAddressException.class)
	public void testShouldNeverHappenInputStyle() throws MalformedAddressException {
		String roadName = "Vandelvej";
		int roadNumber = 10;
		String letter = "";
		int postalNumber = 4600;
		String cityName = "Køge";
		String input = "10 4600 Vandelvej Køge 133 Frederiksværk";
		try {
			addressParserTestNameNumberCity(roadName, roadNumber, letter, postalNumber, cityName, input);
			
			
		} catch (NoAddressFoundException e) {
			fail("No address was found");
		}
	}
	@Test
	public void testIncludingLettersMixed() {
		String roadName = "Vrenstedvej";
		int roadNumber = 148;
		String letter = "k";
		int postalNumber = 9480;
		String cityName = "Løkken";
		String input = "Vrenstedvej 148 k 9480 Løkken";
		System.out.println();
		try {
			addressParserTestNameNumberCity(roadName, roadNumber, letter, postalNumber, cityName, input);
		} catch (MalformedAddressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			fail("No address was found");
		}
	}
	@Test
	public void testIncludingLettersUnlikely() {
		String roadName = "Vrenstedvej";
		int roadNumber = 148;
		String letter = "k";
		int postalNumber = 9480;
		String cityName = "Løkken";
		String input = "148k Vrenstedvej 9480 Løkken";
		System.out.println();
		try {
			addressParserTestNameNumberCity(roadName, roadNumber, letter, postalNumber, cityName, input);
		} catch (MalformedAddressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			fail("No address was found");
		}
	}
	@Test
	public void testIncludingLettersHighlyUnlikely() {
		String roadName = "Vrenstedvej";
		int roadNumber = 148;
		String letter = "k";
		int postalNumber = 9480;
		String cityName = "Løkken";
		String input = "Vrenstedvej 9480 Løkken 148k";
		System.out.println();
		try {
			addressParserTestNameNumberCity(roadName, roadNumber, letter, postalNumber, cityName, input);
		} catch (MalformedAddressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			fail("No address was found");
		}
	}
	@Test
	public void testIncludingLettersShouldNeverHappen() {
		String roadName = "Vrenstedvej";
		int roadNumber = 148;
		String letter = "k";
		int postalNumber = 9480;
		String cityName = "Løkken";
		String input = "Vrenstedvej 148 9480 k Løkken";
		System.out.println();
		try {
			addressParserTestNameNumberCity(roadName, roadNumber, letter, postalNumber, cityName, input);
		} catch (MalformedAddressException e) {
			fail("Input contains invalid characters");
		} catch (NoAddressFoundException e) {
			fail("No address was found");
		}
	}
}
