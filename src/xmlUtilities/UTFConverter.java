package xmlUtilities;

import java.io.*;

public class UTFConverter {
	public static void transform(File source, File target) throws IOException {
	    BufferedReader br = null;
	    BufferedWriter bw = null;
	    try{
	        br = new BufferedReader(new FileReader(source));
	        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "UTF-8"));
	        char[] buffer = new char[16384];
	        int read;
	        while ((read = br.read(buffer)) != -1)
	            bw.write(buffer, 0, read);
	    } finally {
	        try {
	            if (br != null)
	                br.close();
	        } finally {
	            if (bw != null)
	                bw.close();
	        }
	    }
	}
	
	public static void main(String[] args) {
		File file1 = new File("road_names.txt");
		File target1 = new File("road_names_test.txt");
		try {
			UTFConverter.transform(file1, target1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
