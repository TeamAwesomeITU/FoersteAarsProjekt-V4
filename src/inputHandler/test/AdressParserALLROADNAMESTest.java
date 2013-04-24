package inputHandler.test;

import static org.junit.Assert.assertEquals;
import inputHandler.AdressParser;
import inputHandler.exceptions.MalformedAdressException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;


/*
 * This test tests if AdressParser returns the correct roadname for every single road in road_names.txt.
 */
public class AdressParserALLROADNAMESTest {

	@Test
	public void testALLROADNAMES() throws IOException, MalformedAdressException {
		File file = new File("road_names.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String fileLine;
		AdressParser ap = new AdressParser();
		
		while((fileLine = reader.readLine()) != null)
			try {
				assertEquals(fileLine, ap.parseAdress(fileLine)[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		reader.close();
	}

}
