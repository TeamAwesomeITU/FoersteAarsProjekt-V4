package InputHandler.test;

import static org.junit.Assert.*;

import org.junit.Test;

import InputHandler.exceptions.MalformedAdressException;

import InputHandler.AdressParser;


public class WhiteboxTesting {
	
	//The default message to use, when an exceptionExpectedTest fails
	static final String failed = "Expected exception, but didn't throw any";
	
	//The array to output, when an exceptionExpectedTest succeeds
	static final String[] expectedResultFail = new String[]{"MALFORMED ADRESS"};
	
	@Test
	//Reason: contains an empty string
	public void datasetA() {
		String test = "";	
		AdressParserAllTests.setupTest(test, expectedResultFail);

		String[] testResult = new String[]{""};
		try {
			asserterException(test, expectedResultFail);
			testResult[0] = failed;	
			fail(failed);
		} catch (MalformedAdressException e) {
			testResult[0] = e.getMessage();	
		}	
		finally {
			AdressParserAllTests.testResults.add(testResult);
			assertArrayEquals(expectedResultFail, testResult);
		}
	}
	
	@Test
	//Reason: contains bullshit
	public void datasetB() {
		String test = "Are you kidding me?";
		AdressParserAllTests.setupTest(test, expectedResultFail);

		String[] testResult = new String[]{""};
		try {
			asserterException(test, expectedResultFail);
			testResult[0] = failed;	
			fail(failed);
		} catch (MalformedAdressException e) {
			testResult[0] = e.getMessage();	
		}
		finally {
			AdressParserAllTests.testResults.add(testResult);
			assertArrayEquals(expectedResultFail, testResult);
		}
	}
	@Test
	//Reason: contains Something with road name
	public void datasetC() {
		String test = "Rued Langgaards vej";
		String[] expectedResult = new String[]{"Rued Langgaards vej", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}
	
	@Test
	//Reason: contains Something without a road name
	public void datasetD() {
		String test = "København";
		String[] expectedResult = new String[]{null, null, null, null, null, "København"};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}
	
	@Test
	//Reason: contains Input that doesn’t match anything in the txt file
	public void datasetE() {
		String test = "TeamAwesome";
		String[] expectedResult = new String[]{null, null, null, null, null, "TeamAwesome"};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}
	
	@Test
	//Reason: contains Something with a digit followed by a comma
	public void datasetF() {
		String test = "Rued Langgaards vej 7, 5.";
		String[] expectedResult = new String[]{"Rued Langgaards vej", "7", null, "5", null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}
	
	@Test
	//Reason: contains Incomplete match
	public void datasetG() {
		String test = "Rued Langgaardsvej";
		String[] expectedResult = new String[]{null, null, null, null, null, "Rued Langgaardsvej"};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}
	
	@Test
	//Reason: contains Input with building letter
	public void datasetH() {
		String test = "Rued Langgaards vej 7A, 5. sal";
		String[] expectedResult = new String[]{"Rued Langgaards vej", "7", "A", "5", null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}
	
	@Test
	//Reason: contains Input with a four digit number (zip code)
	public void datasetI() {
		String test = "Rued Langgaards vej 7A, 5. sal 2300";
		String[] expectedResult = new String[]{"Rued Langgaards vej", "7", "A", "5", "2300", null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}
	
	@Test
	//Reason: contains Input with a valid city
	public void datasetJ() {
		String test = "Rued Langgaards vej 7A, 5. sal 2300 København S";
		String[] expectedResult = new String[]{"Rued Langgaards vej", "7", "A", "5", "2300", "København S"};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}
	
	@Test
	//Reason: contains Input with more than one match in the text file, where the 
	//correct match is stored first in the match-array
	public void datasetK() {
		String test = "A. E. Hansensvej";
		String[] expectedResult = new String[]{"A. E. Hansensvej", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}
	
	@Test
	//Reason: contains Input with more than one match in the text file, where the 
	//correct match is stored second in the match-array. (“Rue”) is stored at the first index
	public void datasetL() {
		String test = "Rue de Louis";
		String[] expectedResult = new String[]{"Rue de Louis", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}
	
	public void asserterCorrect(String input, String[] expectedTestArray)
	{
		AdressParser adressParser = new AdressParser();
		String[] testResult = new String[]{""};
		try {
			testResult = adressParser.parseAdress(input);
			assertArrayEquals(expectedTestArray, testResult);

		} catch (MalformedAdressException e) {
			testResult[0] = e.getMessage();	
			fail();
		}
		finally {
			AdressParserAllTests.testResults.add(testResult);
		}
	}
	
	public void asserterException(String input, String[] expectedTestArray) throws MalformedAdressException
	{
		AdressParser adressParser = new AdressParser();
		adressParser.parseAdress(input);
	}

}
