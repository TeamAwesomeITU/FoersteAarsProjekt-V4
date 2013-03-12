package InputHandler.test;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AdressParserExistingAdressTester.class, AdressParserExpectedExceptionTester.class})
public class AdressParserAllTests {

	static ArrayList<String> tests = new ArrayList<String>();
	static ArrayList<String[]> expectedResults  = new ArrayList<String[]>();
	static ArrayList<String[]> testResults = new ArrayList<String[]>();
	
	
	public static void setupTest(String input, String[] expectedResultFail)
	{
		tests.add(input);
		expectedResults.add(expectedResultFail);		
	}
	
	@AfterClass
	public static void save()
	{
		SaveTestResults.saveResults(tests, expectedResults, testResults, "testresults.txt");
	}
}
