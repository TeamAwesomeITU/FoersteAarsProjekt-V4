package InputHandler.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SaveTestResults {

	public static void saveResults(ArrayList<String> tests, ArrayList<String[]> expectedResults, ArrayList<String[]> testResults, String filename)
	{
		try {
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);

			for (int i = 0; i < testResults.size(); i++) {
				System.out.println(testResults.size() + " " + i);
				String expectedResultFail = createStringFromArray(expectedResults.get(i));
				String actualResult = createStringFromArray(testResults.get(i));
				String isTestCorrect = "Incorrect";

				out.write("Input: '" + tests.get(i) + "'");
				out.newLine();
				out.write("Expected output: " + expectedResultFail);
				out.newLine();
				out.write("Output: " + actualResult);
				out.newLine(); 

				if (actualResult.equals(expectedResultFail)) {
					isTestCorrect = "Correct";
				}
				out.write("Output was: " + isTestCorrect);
				out.newLine(); out.newLine();		
			}
			out.close();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	private static String createStringFromArray(String[] array)
	{
		String completeString = "";
		for(int i = 0; i < array.length; i++)
		{
			String string = array[i];
			
			//If the string is null, then a # is added - otherwise, the string and a # is added to the final string
			// - UNLESS it's the last element in the array, then only the string is added.
			if(string == null)
				completeString += "#";
			else {
				if(i == array.length-1)
					completeString += (string);
				else
					completeString += (string + "#");
			}
		}
		return completeString;
	}
	
}
