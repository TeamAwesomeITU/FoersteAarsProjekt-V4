package inputHandler.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import inputHandler.AddressParser;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

import org.junit.Test;



public class AddressParserExpectedExceptionTester {

	//The default message to use, when an exceptionExpectedTest fails
	static final String failed = "Expected exception, but didn't throw any";
	
	//The array to output, when an exceptionExpectedTest succeeds
	static final String[] expectedResultFail = new String[]{"MALFORMED ADRESS"};
	
	@Test
	//Reason: contains "?"
	public void questionMarkTest() {
		String test = "Are you kidding me?";
		AddressParserAllTests.setupTest(test, expectedResultFail);

		String[] testResult = new String[]{""};
		try {
			asserterException(test, expectedResultFail);
			testResult[0] = failed;	
			fail(failed);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			testResult[0] = e.getMessage();	
		}
		finally {
			AddressParserAllTests.testResults.add(testResult);
			assertArrayEquals(expectedResultFail, testResult);
		}
	}

	@Test
	//Reason: contains nothing
	public void emptyInputTest() {
		String test = "";	
		AddressParserAllTests.setupTest(test, expectedResultFail);

		String[] testResult = new String[]{""};
		try {
			asserterException(test, expectedResultFail);
			testResult[0] = failed;	
			fail(failed);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			testResult[0] = e.getMessage();	
		}	
		finally {
			AddressParserAllTests.testResults.add(testResult);
			assertArrayEquals(expectedResultFail, testResult);
		}
	}

	@Test
	//Reason: contains only a whitespace
	public void whitespaceTest() {
		String test = " ";	
		AddressParserAllTests.setupTest(test, expectedResultFail);

		String[] testResult = new String[]{""};
		try {
			asserterException(test, expectedResultFail);
			testResult[0] = failed;			
			fail(failed);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			testResult[0] = e.getMessage();	
		}	
		finally {
			AddressParserAllTests.testResults.add(testResult);
			assertArrayEquals(expectedResultFail, testResult);
		}
	}
	
	@Test
	//Reason: contains "$"
	public void invalidCharactersTest() {
		String test = "mad skil$$";	
		AddressParserAllTests.setupTest(test, expectedResultFail);

		String[] testResult = new String[]{""};
		try {
			asserterException(test, expectedResultFail);
			testResult[0] = failed;			
			fail(failed);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			testResult[0] = e.getMessage();	
		}	
		finally {
			AddressParserAllTests.testResults.add(testResult);
			assertArrayEquals(expectedResultFail, testResult);
		}
	}
		
	@Test
	//Reason: contains a lot of allowed input, with a whitespace in front
	public void nonsensibleAllowedInputTest1() {
		String test = " ....,,,,...,.,.,,  ";	
		AddressParserAllTests.setupTest(test, expectedResultFail);

		String[] testResult = new String[]{""};
		try {
			asserterException(test, expectedResultFail);
			testResult[0] = failed;			
			fail(failed);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			testResult[0] = e.getMessage();	
		}	
		finally {
			AddressParserAllTests.testResults.add(testResult);
			assertArrayEquals(expectedResultFail, testResult);
		}
	}
	
	@Test
	//Reason: contains a lot of allowed input, with whitespaces in the back
	public void nonsensibleAllowedInputTest2() {
		String test = ",,,...	";	
		AddressParserAllTests.setupTest(test, expectedResultFail);

		String[] testResult = new String[]{""};
		try {
			asserterException(test, expectedResultFail);
			testResult[0] = failed;			
			fail(failed);
		} catch (MalformedAddressException | NoAddressFoundException e) {
			testResult[0] = e.getMessage();	
		}	
		finally {
			AddressParserAllTests.testResults.add(testResult);
			assertArrayEquals(expectedResultFail, testResult);
		}
	}
	
	public void asserterException(String input, String[] expectedTestArray) throws MalformedAddressException, NoAddressFoundException
	{
		AddressParser addressParser = new AddressParser();
		addressParser.parseAddress(input);
	}

}
