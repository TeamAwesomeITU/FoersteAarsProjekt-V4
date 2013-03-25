package mapDrawer;

import java.io.IOException;

import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;

public class VTDindexer {

	VTDindexer() {}
	
	public static void main(String[] args) {
		try {
		VTDGen vg = new VTDGen();
		if(vg.parseFile("XML/kdv_node_unload.xml", false)) {
			vg.writeIndex("XML/node_unload.vxl");
		}
		}
		catch(IOException | VTDException e) {
			e.printStackTrace();
		}
	}
}
