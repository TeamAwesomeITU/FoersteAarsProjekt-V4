package InputHandler.test;

import static org.junit.Assert.*;

import org.junit.Test;

import InputHandler.exceptions.MalformedAdressException;

import InputHandler.AdressParser;

public class AdressParserExistingAdressTester {
	@Test
	//Reason: contains only roadname
	public void onlyRoadNameTest() {
		String test = "Rued Langgaards Vej";
		String[] expectedResult = new String[]{"Rued Langgaards Vej", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}

	@Test
	//Reason: contains roadname and number
	public void roadNameAndNumberTest() {
		String test = "Rued Langgaards Vej 7";
		String[] expectedResult = new String[]{"Rued Langgaards Vej", "7", null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}

	@Test
	//Reason: contains roadname, number and letter
	public void numberAndLetterTest() {
		String test = "Rued Langgaards Vej 7A";
		String[] expectedResult = new String[]{"Rued Langgaards Vej", "7", "A", null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}

	@Test
	//Reason: contains roadname, number, letter and story
	public void letterAndStoryTest() {
		String test = "Rued Langgaards Vej 7A, 5.";
		String[] expectedResult = new String[]{"Rued Langgaards Vej", "7", "A", "5", null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}

	@Test
	//Reason: contains roadname, number, letter, story and postal code
	public void storyAndPostalCodeTest() {
		String test = "Rued Langgaards Vej 7A, 5., 2300";
		String[] expectedResult = new String[]{"Rued Langgaards Vej", "7", "A", "5", "2300", null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}

	@Test
	//Reason: contains everything to fill out the array
	public void fullArrayTest() {
		String test = "Rued Langgaards Vej 7A, 5., 2300 K�benhavn S";
		String[] expectedResult = new String[]{"Rued Langgaards Vej", "7", "A", "5", "2300", "K�benhavn S"};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains only roadname and city, separated by an "i"
	public void inputSeperatedByAnITest() {
		String test = "Rued Langgaards Vej i K�benhavn";
		String[] expectedResult = new String[]{"Rued Langgaards Vej", null, null, null, null, "K�benhavn"};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	

	
	@Test
	//Reason: roadname contains numbers and a period, and roadname and city is separated by an "i"
	public void roadNameWithNumberAndSeperatedByAnITest() {
		String test = "10. Februar Vej i Tarm";
		String[] expectedResult = new String[]{"10. Februar Vej", null, null, null, null, "Tarm"};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: roadname contains numbers and a period
	public void roadNameWithNumberAndStory() {
		String test = "10. Februar Vej 7, 5.";
		String[] expectedResult = new String[]{"10. Februar Vej", "7", null, "5", null, null};

		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: roadname contains single letters followed by dots
	public void singleLettersFollowedByDotsTest() {
		String test = "Kong Chr.D. X s Bro 7, 5., 2300 K�benhavn S";
		String[] expectedResult = new String[]{"Kong Chr.D. X s Bro", "7", null, "5", "2300", "K�benhavn S"};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	

	@Test
	//Reason: roadname contains single letters not followed by dots
	public void singleLettersNOTfollowedByDotsTest() {
		String test = "A P M�ller Kollegiet 10, 12., 2300 K�benhavn S";
		String[] expectedResult = new String[]{"A P M�ller Kollegiet", "10", null, "12", "2300", "K�benhavn S"};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "�"
	public void nordicCharacterAETest() {
		String test = "� Towt";
		String[] expectedResult = new String[]{"� Towt", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "�"
	public void nordicCharacterOETest() {
		String test = "�. Hassingvej";
		String[] expectedResult = new String[]{"�. Hassingvej", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "�"
	public void nordicCharacterAATest() {
		String test = "�brinksgatan";
		String[] expectedResult = new String[]{"�brinksgatan", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	
	@Test
	//Reason: contains "�"
	public void accentTest() {
		String test = "Broholms All�";
		String[] expectedResult = new String[]{"Broholms All�", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "�"
	public void antiAccenctTest() {
		String test = "Chr. Rasmussens All�";
		String[] expectedResult = new String[]{"Chr. Rasmussens All�", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "�"
	public void umlautTest1() {
		String test = "Cisel�rgatan";
		String[] expectedResult = new String[]{"Cisel�rgatan", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "�"
	public void umlautTest2() {
		String test = "Citadellsv�gen";
		String[] expectedResult = new String[]{"Citadellsv�gen", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "�"
	public void umlautTest3() {
		String test = "D�ckersgatan";
		String[] expectedResult = new String[]{"D�ckersgatan", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains "(" and ")" and "-"
	public void parenthesesAndDashTest() {
		String test = "Feggesund(Mors)-Arup(Thy)";
		String[] expectedResult = new String[]{"Feggesund(Mors)-Arup(Thy)", null, null, null, null, null};
		AdressParserAllTests.setupTest(test, expectedResult);
		asserterCorrect(test, expectedResult);
	}	
		
	@Test
	//Reason: contains "&"
	public void andSignTest() {
		String test = "�kerlund & Rausings v�g";
		String[] expectedResult = new String[]{"�kerlund & Rausings v�g", null, null, null, null, null};
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
}
