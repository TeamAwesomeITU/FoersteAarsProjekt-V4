package inputHandler.test;

import static org.junit.Assert.*;
import inputHandler.AddressParser;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is our whitebox testing. The data sets can be found in our appendix.
 */
public class WhiteboxTesting {
	
	private AddressParser addressParser;
	private String[] actualInput;
	private String[] expectedResult;

	
	/**
	 * initializes the arrays and address parser before all tests
	 */
	@Before
	public void setUp(){
		addressParser = new AddressParser();
		actualInput = new String[6];
		expectedResult= new String[]{"", "", "", "", "", ""};
	}
	
	/**
	 * Test to see if the parser throws the correct exception
	 * @throws MalformedAddressException
	 */
	@Test (expected=MalformedAddressException.class)
	public void dataSetA() throws MalformedAddressException{
		try {
			String test = "#?=%/=!><";
			actualInput = addressParser.parseAddress(test);
		} catch (NoAddressFoundException e) {
			e.printStackTrace();
			fail("Wrong exception was caught" + e.getClass());
		}
	}
	
	/**
	 * Test to see if the parser throws the correct exception
	 * @throws MalformedAddressException
	 */
	@Test (expected=MalformedAddressException.class)
	public void dataSetA2() throws MalformedAddressException{
		try {
			String test = "Are you kidding me?";
			actualInput = addressParser.parseAddress(test);
		} catch (NoAddressFoundException e) {
			e.printStackTrace();
			fail("Wrong exception was caught" + e.getClass());
		}
	}
	
	/**
	 * Test to see if the parser throws the correct exception
	 * @throws NoAddressFoundException
	 */
	@Test (expected=NoAddressFoundException.class)
	public void dataSetB() throws NoAddressFoundException{
		try {
			String test = "";
			actualInput = addressParser.parseAddress(test);
			assertArrayEquals(actualInput, expectedResult);
		} catch (MalformedAddressException e) {
			e.printStackTrace();
			fail("Wrong exception was caught" + e.getClass());
		}
	}
	
	
	/**
	 * Test for only road name
	 */
	@Test
	public void dataSetC(){
		try {
			expectedResult[0] = "nørregade";
			actualInput = addressParser.parseAddress("nørregade");
			assertArrayEquals(actualInput, expectedResult);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test for road name, number, floor and city
	 */
	@Test
	public void dataSetD(){
		try {
			expectedResult = new String[]{"nørregade", "7", "", "5", "", "køge"};
			actualInput = addressParser.parseAddress("nørregade 7, 5. sal, køge");
			assertArrayEquals(actualInput, expectedResult);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test for road name, number, postal and city
	 */
	@Test
	public void dataSetE(){
		try {
			expectedResult = new String[]{"nørregade", "7", "", "", "4600", "køge"};
			actualInput = addressParser.parseAddress("nørregade7 4600 køge");
			assertArrayEquals(actualInput, expectedResult);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test for road name, number and floor
	 */
	@Test
	public void dataSetF(){
		try {
			expectedResult = new String[]{"nørregade", "7", "", "5", "", ""};
			actualInput = addressParser.parseAddress("nørregade 7 5.");
			assertArrayEquals(actualInput, expectedResult);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test for road name, number, letter and city
	 */
	@Test
	public void dataSetG(){
		try {
			expectedResult = new String[]{"nørregade", "7", "a", "", "", "køge"};
			actualInput = addressParser.parseAddress("nørregade 7a køge");
			assertArrayEquals(actualInput, expectedResult);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test for road name and city
	 */
	@Test
	public void dataSetH(){
		try {
			expectedResult = new String[]{"nørregade", "", "", "", "", "køge"};
			actualInput = addressParser.parseAddress("nørregade i køge");
			assertArrayEquals(actualInput, expectedResult);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test for road name, number, letter, floor, postal and city
	 */
	@Test
	public void dataSetI(){
		try {
			expectedResult = new String[]{"nørregade", "7", "a", "5", "4600", "køge"};
			actualInput = addressParser.parseAddress("nørregade 7a 5. sal 4600 køge");
			assertArrayEquals(actualInput, expectedResult);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test for only city
	 */
	@Test
	public void dataSetJ(){
		try {
			expectedResult = new String[]{"", "", "", "", "", "køge"};
			actualInput = addressParser.parseAddress("køge");
			assertArrayEquals(actualInput, expectedResult);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test for misspelled address
	 */
	@Test
	public void dataSetK(){
		try {
			expectedResult = new String[]{"", "", "", "", "", ""};
			actualInput = addressParser.parseAddress("nærregade i kæge");
			assertArrayEquals(actualInput, expectedResult);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tears down the setup. Makes sure all test has a new arrays and addressparsers.
	 */
	@After
	public void tearDown(){
		addressParser = null;
		actualInput = null;
		expectedResult = null;
	}
	
}