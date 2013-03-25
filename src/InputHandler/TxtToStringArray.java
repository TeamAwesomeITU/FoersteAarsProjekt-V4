package InputHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class TxtToStringArray {

	public static void convertAndSaveTxtToArray(File file, String name) throws IOException
	{
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
			arr[currentLine] = line;
			currentLine++;
		}

		reader.close();

		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(name));
		out.writeObject(arr);
		out.close();        
	}

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

	public static String[] convertTxtToArray(File file) throws IOException
	{
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

		reader.close();
		return arr;
	}

	public static String[] readArray(String fileName) throws IOException, ClassNotFoundException
	{
		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(fileName));
		String[] inArray = (String[]) inStream.readObject();
		inStream.close();

		return inArray;
	}

}
