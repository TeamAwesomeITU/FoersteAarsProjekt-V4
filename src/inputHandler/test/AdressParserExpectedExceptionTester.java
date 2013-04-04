package inputHandler.test;

import static org.junit.Assert.*;
import inputHandler.AdressParser;
import inputHandler.exceptions.MalformedAdressException;

import org.junit.Test;



public class AdressParserExpectedExceptionTester {

	//The default message to use, when an exceptionExpectedTest fails
	static final String failed = "Expected exception, but didn't throw any";
	
	//The array to output, when an exceptionExpectedTest succeeds
	static final String[] expectedResultFail = new String[]{"MALFORMED ADRESS"};
	
	@Test
	//Reason: contains "?"
	public void questionMarkTest() {
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
	//Reason: contains nothing
	public void emptyInputTest() {
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
	//Reason: contains only a whitespace
	public void whitespaceTest() {
		String test = " ";	
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
	//Reason: contains "$"
	public void invalidCharactersTest() {
		String test = "mad skil$$";	
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
	//Reason: contains a lot of allowed input, with a whitespace in front
	public void nonsensibleAllowedInputTest1() {
		String test = " ....,,,,...,.,.,,  ";	
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
	//Reason: contains a lot of allowed input, with whitespaces in the back
	public void nonsensibleAllowedInputTest2() {
		String test = ",,,...	";	
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
	
	public void asserterException(String input, String[] expectedTestArray) throws MalformedAdressException
	{
		AdressParser adressParser = new AdressParser();
		adressParser.parseAdress(input);
	}

}
