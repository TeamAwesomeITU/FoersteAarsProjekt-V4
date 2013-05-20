package inputHandler.test;

import static org.junit.Assert.*;
import inputHandler.AddressParser;

import org.junit.Test;

public class AddressParserSpecialCharHandlingTest {
	
	@Test
	//Reason: contains "é"
	public void graveAccentTest() {
		String test = "Broholms Allé";
		String[] expectedResult = new String[]{"broholms allé", "", "", "", "", ""};
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "è"
	public void acuteAccentTest() {
		String test = "Elver Allè";
		String[] expectedResult = new String[]{"elver allè", "", "", "", "", ""};
		asserterCorrect(test, expectedResult);
	}	
		
	@Test
	//Reason: contains "ˆ"
	public void circumFlexTest() {
		String test = "Byahornsgrând";
		String[] expectedResult = new String[]{"byahornsgrând", "", "", "", "", ""};
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "ä"
	public void umlautTest() {
		String test = "Citadellsvägen";
		String[] expectedResult = new String[]{"citadellsvägen", "", "", "", "", ""};
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "ü"
	public void umlautTest3() {
		String test = "Dückersgatan";
		String[] expectedResult = new String[]{"dückersgatan", "", "", "", "", ""};
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "(" and ")" and "-"
	public void parenthesesAndDashTest() {
		String test = "Feggesund(Mors)-Arup(Thy)";
		String[] expectedResult = new String[]{"feggesund(mors)-arup(thy)", "", "", "", "", ""};
		asserterCorrect(test, expectedResult);
	}	
		
	@Test
	//Reason: contains "&"
	public void andSignTest() {
		String test = "Åkerlund & Rausings väg";
		String[] expectedResult = new String[]{"åkerlund & rausings väg", "", "", "", "", ""};
		asserterCorrect(test, expectedResult);
	}
	
	public void asserterCorrect(String input, String[] expectedTestArray)
	{
		AddressParser addressParser = new AddressParser();
		String[] testResult = new String[]{"", "", "", "", "", ""};
		try {
			testResult = addressParser.parseAddress(input);
			assertArrayEquals(expectedTestArray, testResult);

		} catch (Exception e) {
			System.out.println("EXCEPTION WITH THIS INPUT: " + input + " , EXCEPTION: " + e.getMessage());
			fail();
		}
	}

}
