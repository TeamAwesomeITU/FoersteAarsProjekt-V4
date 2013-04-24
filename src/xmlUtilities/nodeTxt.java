package xmlUtilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import mapDrawer.drawing.Edge;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

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
	public void reader() {
		try {
			File file = new File("XML/kdv_node_unload.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));

			reader.readLine();
			String line;
			
			BufferedWriter out = new BufferedWriter(new FileWriter("JespersMorErGrim.txt"));
			VTDGen vgEdge = new VTDGen();
			if(vgEdge.parseFile("XML/kdv_unload_Graph.xml", false)) {

				VTDNav vnEdge = vgEdge.getNav();
				AutoPilot apEdge = new AutoPilot(vnEdge);
				apEdge.selectXPath("//RSC/RS");

				String temp = "";
				String id = "";
				int FNODE = 0; int TNODE = 0; 
				while((line = reader.readLine()) != null)
				{
					String[] lineParts = line.split("\\,");
					Integer KDV = Integer.parseInt(lineParts[2]);
					temp = "";
					id = "";
					while((apEdge.evalXPath())!=-1)
					{						
						vnEdge.toElement(VTDNav.FC, "FN");
						FNODE = vnEdge.parseInt(vnEdge.getText());
						vnEdge.toElement(VTDNav.NS, "TN");
						TNODE = vnEdge.parseInt(vnEdge.getText());
						if(FNODE == KDV || TNODE == KDV){
							vnEdge.toElement(VTDNav.PARENT);
							vnEdge.toElement(VTDNav.LC, "ID");
							id = id + vnEdge.toString(vnEdge.getText()) + " ";
						}						
						vnEdge.toElement(VTDNav.PARENT); 
					} 
					temp = lineParts[2]+","+ lineParts[3]+","+ lineParts[4]+","+ id;
					out.write(temp +  "\n");
					apEdge.resetXPath();
			}
			}
			out.close();
			reader.close();
		} catch (IOException | XPathEvalException | NavException | XPathParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void makeEdgeTxtFromTXT()
	{
		try {				
			HashSet<Edge> edgeSet = new HashSet<Edge>();
			
			BufferedReader reader = new BufferedReader(new FileReader("XML/kdv_unload_1.txt"));
			BufferedWriter writer = new BufferedWriter(new FileWriter("XML/edge.txt"));
			
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
