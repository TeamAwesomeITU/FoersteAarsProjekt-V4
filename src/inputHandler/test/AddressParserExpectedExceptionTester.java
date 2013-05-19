package inputHandler.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import inputHandler.AddressParser;
import inputHandler.exceptions.MalformedAddressException;
import inputHandler.exceptions.NoAddressFoundException;

import org.junit.Test;



public class AddressParserExpectedExceptionTester {

	public void asserterException(String input) throws MalformedAddressException, NoAddressFoundException
	{
		AddressParser addressParser = new AddressParser();
		addressParser.parseAddress(input);
	}

	@Test (expected=MalformedAddressException.class)
	//Reason: contains "?"
	public void questionMarkTest() throws MalformedAddressException {
		String test = "Are you kidding me?";

		try {
			asserterException(test);
			//fail("No exception was caught with this input: " + test);

		} catch (NoAddressFoundException e) {
			e.printStackTrace();
			fail("Wrong exception was caught" + e.getClass());
		}		

	}

	@Test (expected=NoAddressFoundException.class)
	//Reason: contains nothing
	public void emptyInputTest() throws NoAddressFoundException {
		String test = "";

		try {
			asserterException(test);
			fail("No exception was caught with this input: " + test);
		} catch (MalformedAddressException e) {
			fail("Wrong exception was caught" + e.getClass());
		}
	}

	@Test (expected=NoAddressFoundException.class)
	//Reason: contains only a whitespace
	public void whitespaceTest() throws NoAddressFoundException {
		String test = " ";

		try {
			asserterException(test);
			fail("No exception was caught with this input: " + test);
		} catch (MalformedAddressException e) {
			fail("Wrong exception was caught" + e.getClass());
		}
	}

	@Test (expected=MalformedAddressException.class)
	//Reason: contains "$"
	public void invalidCharactersTest() throws MalformedAddressException {
		String test = "mad skil$$";	

		try {
			asserterException(test);
			fail("No exception was caught with this input: " + test);
		} catch (NoAddressFoundException e) {
			fail("Wrong exception was caught: " + e.getClass());
		}
	}

	@Test (expected=NoAddressFoundException.class)
	//Reason: contains a lot of allowed input, with a whitespace in front
	public void nonsensibleAllowedInputTest1() throws NoAddressFoundException {
		String test = " ....,,,,...,.,.,,  ";	

		try {
			asserterException(test);
			fail("No exception was caught with this input: " + test);
		} catch (MalformedAddressException e) {
			fail("Wrong exception was caught: " + e.getClass());
		}
	}

	@Test (expected=NoAddressFoundException.class)
	//Reason: contains a lot of allowed input, with whitespaces in the back
	public void nonsensibleAllowedInputTest2() throws NoAddressFoundException {
		String test = ",,,...	";	

		try {
			asserterException(test);
			fail("No exception was caught with this input: " + test);
		} catch (MalformedAddressException e) {
			fail("Wrong exception was caught: " + e.getClass());
		}
	}
}
