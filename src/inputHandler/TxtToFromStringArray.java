package inputHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Converts a .txt-file to a String[] or vice versa
 */
public class TxtToFromStringArray {

	/**
	 * Converts a .txt-file to a String[] and saves it
	 * 
	 * @param file The path to the .txt-file to convert
	 * @param name The name-to-be of the converted file
	 */
	public static void convertAndSaveTxtToArray(File file, String name) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		LineNumberReader lineNumReader = new LineNumberReader(reader);
		lineNumReader.skip(Long.MAX_VALUE);

		String[] arr = new String[lineNumReader.getLineNumber()];
		reader.close();

		reader = new BufferedReader(new FileReader(file));

		int currentLine = 0;
		String line;

		while((line = reader.readLine()) != null)
		{
			arr[currentLine] = line;
			currentLine++;
		}

		reader.close();

		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(name));
		out.writeObject(arr);
		out.close();        
	}

	/**
	 * Converts a String[] to a .txt-file and saves it
	 * 
	 * @param 	array The String[] to convert
	 * @param	name The name-to-be of the converted file
	 */
	public static void convertAndSaveArrayToTxt(String[] array, String name) throws Exception
	{
		FileOutputStream m_fos = new FileOutputStream(name);
		Writer out = new OutputStreamWriter(m_fos, "UTF-8");
		//To get the new line, i.e. "enter", so that a new line will be created with each String
		String newLineSeperator = System.getProperty("line.separator");

		for(String string : array)
		{
			string += newLineSeperator;
			out.write(new String(UnicodeUtil.convert(string.getBytes(), "UTF-8")));
		}

		out.close();
	}

	/**
	 * Converts a String[] to a .txt-file and saves it
	 * 
	 * @param 	file The path to the .txt-file to convert
	 * @return 	A String[] representation of the converted file
	 */
	public static String[] convertTxtToArray(File file) throws IOException
	{
		long startTime = System.currentTimeMillis();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		LineNumberReader lineNumReader = new LineNumberReader(reader);
		lineNumReader.skip(Long.MAX_VALUE);

		String[] arr = new String[lineNumReader.getLineNumber()];
		reader.close();

		reader = new BufferedReader(new FileReader(file));

		int currentLine = 0;
		String line;

		while((line = reader.readLine()) != null)
		{
			arr[currentLine] = line.toLowerCase();
			currentLine++;
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Parsing txt to array: Addressparser = " + (endTime - startTime) + " milliseconds");

		reader.close();
		return arr;
	}

	/**
	 * Reads a stored String[] and returns it
	 * 
	 * @param 	fileName The path to the stored String[]
	 * @return 	The read String[]
	 */
	public static String[] readArray(String fileName) throws IOException, ClassNotFoundException
	{
		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(fileName));
		String[] inArray = (String[]) inStream.readObject();
		inStream.close();

		return inArray;
	}

}
