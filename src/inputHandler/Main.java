package inputHandler;

public class Main {
	@SuppressWarnings("unused")
	public static void main( String[] args ) throws Exception {
		//File file = new File("kdv_node_unload.txt");

		AdressParser ap = new AdressParser();
		ap.parseAdress("jagtvej 109G 1.th 2200 København N"); // OBS - Ved addressParsing bliver th/tv ignoreret. Dette skal måske rettes til.
		}
}
 