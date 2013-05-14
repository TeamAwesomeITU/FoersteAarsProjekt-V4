package txtEditingAndConversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class nodeTxt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new nodeTxt();
	}
	
	public nodeTxt() {
		makeEdgeTxtFromTXT();
	}
	
	@SuppressWarnings("unused")
	private static void makeEdgeTxtFromTXT()
	{
		try {				
			BufferedReader reader = new BufferedReader(new FileReader("XML/kdv_unload_1.txt"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream("XML/testEdge.txt"), "UTF-8"));
			
			//To skip the first line
			reader.readLine();
			
			String line;
			String header = "FromNodeID, ToNodeID, Length, ID, roadType, roadName, fromLeftNumber, toLeftNumber, fromRightNumber, toRightNumber," +
					" fromLeftLetter, toLeftLetter, fromRightLetter, toRightLetter, LpostalNumber, RpostalNumber, Highway-turnoff, Drivetime, Direction, FromTurn, ToTurn, Tjek-ID";
			System.out.println(header);
			writer.write(header + "\n");
			String newLine = "";
			String bs = "";
			int i = 0;
			while((line = reader.readLine()) != null)
			{
				String[] lp = null;
				lp = line.split(("(?<=[']|\\d|\\*)[,](?=[']|\\d|\\*)"));
				//System.out.println(lp[32] + " "+ lp[6]);
				newLine = 
						lp[0]+";"+
						lp[1]+";"+ 
						lp[2]+";"+
						lp[4]+";"+
						lp[5]+";"+
						lp[6]+";"+
						lp[7]+";"+
						lp[8]+";"+
						lp[9]+";"+
						lp[10]+";"+
						lp[11]+";"+
						lp[12]+";"+
						lp[13]+";"+
						lp[14]+";"+
						lp[17]+";"+
						lp[18]+";"+
						lp[23]+";"+
						lp[26]+";"+
						lp[27]+";"+
						lp[28]+";"+
						lp[29]+";"+
						lp[32];
				
				//System.out.println(newLine);
				newLine = newLine.replaceAll("\\'+", "");
				//System.out.println(newLine);
				i++;
				writer.write(newLine);
				writer.newLine();
			}
		
			writer.close();
			reader.close();			

			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
