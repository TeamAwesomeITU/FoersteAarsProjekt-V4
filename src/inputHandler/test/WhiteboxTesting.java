package inputHandler.test;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import inputHandler.AddressParser;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class WhiteboxTesting {
	
	private AddressParser addressParser;
	private String[] expectedResult;
	private String[] actualInput;

	@Before
	public void setUp(){
		addressParser = new AddressParser();
		expectedResult = new String[6];
		actualInput = new String[]{"", "", "", "", "", ""};
	}
	
	/**
	 * 
	 */
	@Test
	public void dataSetC(){
		try {
			actualInput[0] = "Nørregade";
			expectedResult = addressParser.parseAddress("Nørregade");
			assertArrayEquals(expectedResult, actualInput);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	@After
	public void tearDown(){
		addressParser = null;
		expectedResult = null;
		actualInput = null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//	//The default message to use, when an exceptionExpectedTest fails
//	static final String failed = "Expected exception, but didn't throw any";
//	
//	//The array to output, when an exceptionExpectedTest succeeds
//	static final String[] expectedResultFail = new String[]{"MALFORMED ADRESS"};
//	
//	@Test
//	//Reason: contains an empty string
//	public void datasetA() {
//		String test = "";	
//
//		String[] testResult = new String[]{""};
//		try {
//			asserterException(test, expectedResultFail);
//			testResult[0] = failed;	
//			fail(failed);
//		} catch (MalformedAddressException | NoAddressFoundException e) {
//			testResult[0] = e.getMessage();	
//		}	
//		finally {
//			assertArrayEquals(expectedResultFail, testResult);
//		}
//	}
//	
//	@Test
//	//Reason: contains bullshit
//	public void datasetB() {
//		String test = "Are you kidding me?";
//		String[] testResult = new String[]{""};
//		try {
//			asserterException(test, expectedResultFail);
//			testResult[0] = failed;	
//			fail(failed);
//		} catch (MalformedAddressException | NoAddressFoundException e) {
//			testResult[0] = e.getMessage();	
//		}
//		finally {
//			assertArrayEquals(expectedResultFail, testResult);
//		}
//	}
//	@Test
//	//Reason: contains Something with road name
//	public void datasetC() {
//		String test = "Rued Langgaards vej";
//		String[] expectedResult = new String[]{"Rued Langgaards vej", "", "", "", "", ""};
//		asserterCorrect(test, expectedResult);
//	}
//	
//	@Test
//	//Reason: contains Something without a road name
//	public void datasetD() {
//		String test = "K�benhavn";
//		String[] expectedResult = new String[]{"", "", "", "", "", "K�benhavn"};
//		asserterCorrect(test, expectedResult);
//	}
//	
//	@Test
//	//Reason: contains Input that doesn�t match anything in the txt file
//	public void datasetE() {
//		String test = "TeamAwesome";
//		String[] expectedResult = new String[]{"", "", "", "", "", "TeamAwesome"};
//		asserterCorrect(test, expectedResult);
//	}
//	
//	@Test
//	//Reason: contains Something with a digit followed by a comma
//	public void datasetF() {
//		String test = "Rued Langgaards vej 7, 5.";
//		String[] expectedResult = new String[]{"Rued Langgaards vej", "7", "", "5", "", ""};
//		asserterCorrect(test, expectedResult);
//	}
//	
//	@Test
//	//Reason: contains Incomplete match
//	public void datasetG() {
//		String test = "Rued Langgaardsvej";
//		String[] expectedResult = new String[]{"", "", "", "", "", "Rued Langgaardsvej"};
//		asserterCorrect(test, expectedResult);
//	}
//	
//	@Test
//	//Reason: contains Input with building letter
//	public void datasetH() {
//		String test = "Rued Langgaards vej 7A, 5. sal";
//		String[] expectedResult = new String[]{"Rued Langgaards vej", "7", "A", "5", "", ""};
//		asserterCorrect(test, expectedResult);
//	}
//	
//	@Test
//	//Reason: contains Input with a four digit number (zip code)
//	public void datasetI() {
//		String test = "Rued Langgaards vej 7A, 5. sal 2300";
//		String[] expectedResult = new String[]{"Rued Langgaards vej", "7", "A", "5", "2300", ""};;
//		asserterCorrect(test, expectedResult);
//	}
//	
//	@Test
//	//Reason: contains Input with a valid city
//	public void datasetJ() {
//		String test = "Rued Langgaards vej 7A, 5. sal 2300 K�benhavn S";
//		String[] expectedResult = new String[]{"Rued Langgaards vej", "7", "A", "5", "2300", "K�benhavn S"};
//		asserterCorrect(test, expectedResult);
//	}
//	
//	@Test
//	//Reason: contains Input with more than one match in the text file, where the 
//	//correct match is stored first in the match-array
//	public void datasetK() {
//		String test = "A. E. Hansensvej";
//		String[] expectedResult = new String[]{"A. E. Hansensvej", "", "", "", "", ""};
//		asserterCorrect(test, expectedResult);
//	}
//	
//	@Test
//	//Reason: contains Input with more than one match in the text file, where the 
//	//correct match is stored second in the match-array. (�Rue�) is stored at the first index
//	public void datasetL() {
//		String test = "Rue de Louis";
//		String[] expectedResult = new String[]{"Rue de Louis", "", "", "", "", ""};
//		asserterCorrect(test, expectedResult);
//	}
//	
//	public void asserterCorrect(String input, String[] expectedTestArray)
//	{
//		AddressParser addressParser = new AddressParser();
//		String[] testResult = new String[]{""};
//		try {
//			testResult = addressParser.parseAddress(input);
//			assertArrayEquals(expectedTestArray, testResult);
//
//		} catch (MalformedAddressException | NoAddressFoundException e) {
//			testResult[0] = e.getMessage();	
//			fail();
//		}
//	}
//	
//	public void asserterException(String input, String[] expectedTestArray) throws MalformedAddressException, NoAddressFoundException
//	{
//		AddressParser addressParser = new AddressParser();
//		addressParser.parseAddress(input);
//	}

}
