package inputHandler.test;

import static org.junit.Assert.*;
import inputHandler.AdressParser;

import org.junit.Test;

public class AdressParserSpecialCharHandlingTest {

	@Test
	//Reason: contains "Â"
	public void nordicCharacterAATest() {
		String test = "Âbrinksgatan";
		String[] expectedResult = new String[]{"Âbrinksgatan", "", "", "", "", ""};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	
	@Test
	//Reason: contains "è"
	public void graveAccentTest() {
		String test = "Broholms Allè";
		String[] expectedResult = new String[]{"broholms allè", "", "", "", "", ""};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "é"
	public void acuteAccentTest() {
		String test = "Christian Greens Allé";
		String[] expectedResult = new String[]{"broholms allè", "", "", "", "", ""};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
		
	@Test
	//Reason: contains "ˆ"
	public void circumFlexTest() {
		String test = "Byahornsgrând";
		String[] expectedResult = new String[]{"byahornsgrând", "", "", "", "", ""};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "ä"
	public void umlautTest() {
		String test = "Citadellsvägen";
		String[] expectedResult = new String[]{"Citadellsvägen", "", "", "", "", ""};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "ü"
	public void umlautTest3() {
		String test = "Dückersgatan";
		String[] expectedResult = new String[]{"dückersgatan", "", "", "", "", ""};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "(" and ")" and "-"
	public void parenthesesAndDashTest() {
		String test = "Feggesund(Mors)-Arup(Thy)";
		String[] expectedResult = new String[]{"feggesund(mors)-arup(thy)", "", "", "", "", ""};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
		
	@Test
	//Reason: contains "&"
	public void andSignTest() {
		String test = "≈kerlund & Rausings v‰g";
		String[] expectedResult = new String[]{"≈kerlund & rausings v‰g", "", "", "", "", ""};
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

		} catch (Exception e) {
			testResult[0] = e.getMessage();	
			System.out.println("EXCEPTION WITH THIS INPUT: " + input + " , EXCEPTION: " + e.getMessage());
			fail();
		}
	}

}
