package inputHandler.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import inputHandler.AddressParser;

import org.junit.Test;

public class AddressParserExistingAddressTester {
	@Test
	//Reason: contains only roadname
	public void onlyRoadNameTest() {
		String test = "Korsvejs Allé";
		String[] expectedResult = new String[]{"korsvejs allé", "", "", "", "", ""};
		asserterCorrect(test, expectedResult);
	}

	@Test
	//Reason: contains roadname and number
	public void roadNameAndNumberTest() {
		String test = "Korsvejs Allé 17";
		String[] expectedResult = new String[]{"korsvejs allé", "17", "", "", "", ""};
		asserterCorrect(test, expectedResult);
	}

	@Test
	//Reason: contains roadname, number and letter
	public void numberAndLetterTest() {
		String test = "Korsvejs Allé 17B";
		String[] expectedResult = new String[]{"korsvejs allé", "17", "b", "", "", ""};
		asserterCorrect(test, expectedResult);
	}

	@Test
	//Reason: contains roadname, number, letter and story
	public void letterAndStoryTest() {
		String test = "Korsvejs Allé 17B 5.";
		String[] expectedResult = new String[]{"korsvejs allé", "17", "b", "5", "", ""};
		asserterCorrect(test, expectedResult);
	}

	@Test
	//Reason: contains roadname, number, letter, story and postal code
	public void storyAndPostalCodeTest() {
		String test = "Korsvejs Allé 17B 5. 5500";
		String[] expectedResult = new String[]{"korsvejs allé", "17", "b", "5", "5500", ""};
		asserterCorrect(test, expectedResult);
	}

	@Test
	//Reason: contains everything to fill out the array
	public void fullArrayTest() {
		String test = "Korsvejs Allé 17B 5. 5500 Middelfart";
		String[] expectedResult = new String[]{"korsvejs allé", "17", "b", "5", "5500", "middelfart"};
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: contains only roadname and city, separated by an "i"
	public void inputSeperatedByAnITest() {
		String test = "Griffenfeldsgade i København";
		String[] expectedResult = new String[]{"griffenfeldsgade", "", "", "", "", "københavn"};
		asserterCorrect(test, expectedResult);
	}	

	
	@Test
	//Reason: roadname contains numbers and a period, and roadname and city is separated by an "i"
	public void roadNameWithNumberAndSeperatedByAnITest() {
		String test = "10. Februar Vej i Tarm";
		String[] expectedResult = new String[]{"10. februar vej", "", "", "", "", "tarm"};
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: roadname contains numbers and a period
	public void roadNameWithNumberAndStory() {
		String test = "10. Februar Vej 7, 5.";
		String[] expectedResult = new String[]{"10. februar vej", "7", "", "5", "", ""};
		asserterCorrect(test, expectedResult);
	}	
	
	@Test
	//Reason: roadname contains single letters followed by dots
	public void singleLettersFollowedByDotsTest() {
		String test = "Kong Chr.D. X s Bro 7, 5., 2300 København";
		String[] expectedResult = new String[]{"kong chr.d. x s bro", "7", "", "5", "2300", "københavn"};
		asserterCorrect(test, expectedResult);
	}	

	@Test
	//Reason: roadname contains single letters not followed by dots
	public void singleLettersNOTfollowedByDotsTest() {
		String test = "A P Møller Kollegiet 10, 12., 2300 København";
		String[] expectedResult = new String[]{"a p møller kollegiet", "10", "", "12", "2300", "københavn"};
		asserterCorrect(test, expectedResult);
	}	
	
	public void asserterCorrect(String input, String[] expectedTestArray)
	{
		AddressParser addressParser = new AddressParser();
		String[] testResult = new String[]{""};
		try {
			testResult = addressParser.parseAddress(input);
			assertArrayEquals(expectedTestArray, testResult);

		} catch (Exception e) {
			testResult[0] = e.getMessage();	
			System.out.println("EXCEPTION WITH THIS INPUT: " + input + " , EXCEPTION: " + e.getMessage());
			fail();
		}
	}
	

}
