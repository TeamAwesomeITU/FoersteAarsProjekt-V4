package inputHandler;

import java.util.Arrays;

/**
 * Performs a binary search in an array of Strings
 */
@Deprecated
public class BinarySearchStringInArray
{
	/**
	 * Performs a binary search in an array of Strings
	 * @param input The String to search for
	 * @param arr The array to search in
	 * @return The found String - returns null, if nothing is found.
	 */
	public String search(String input, String[] arr)
	{
		input.matches("\\w.*");
		String[] splitInput = (input.split("\\s+"));

		String possibleResult;
		String totalInput = "";

		int resultFoundAt;
		boolean isResultFound = false;
		int i = 1;

		//The amount of strings of the splitInput, we wish to search for
		int wantedLength = splitInput.length;

		while(!isResultFound && i < splitInput.length && wantedLength >= 0)
		{			
			totalInput = "";

			for (int j = splitInput.length-wantedLength; j < splitInput.length; j++) {
				totalInput += splitInput[j].toLowerCase() + " ";
			}
			totalInput = totalInput.trim();
			System.out.println("Searching for: " + totalInput);

			resultFoundAt = Arrays.binarySearch(arr, totalInput);
			System.out.println("Results should be at: " + resultFoundAt);

			if(resultFoundAt == -1)
			{
				System.out.println("resultFoundAt: -1");
				possibleResult = arr[0];
			}
			
			if(resultFoundAt < 0 && resultFoundAt != -1)
			{
				System.out.println("resultFoundAt: < 0");
				//-2 because binarySearch returns the negative index - 1 ... so in order to compensate for the fact, that
				// array-indicies in java start at 0 and the return of negative index - 1 , we subtract 2.
				possibleResult = arr[Math.abs((resultFoundAt))-2];
			}
			else
			{
				System.out.println("resultFoundAt: > 0");
				possibleResult = arr[resultFoundAt];
			}

			System.out.println("Might be: " + possibleResult);

			//If the input contains the possible result, then it is the correct result!
			if(input.toLowerCase().contains(possibleResult.toLowerCase()))
			{
				isResultFound = true;
				System.out.println("Result found!");
				System.out.println("Vejnavn: " + possibleResult);
				return possibleResult;
			}
			i++;
			wantedLength--;
		}

		System.out.println("A roadname was not found in the input");
		return null;
	}
}



