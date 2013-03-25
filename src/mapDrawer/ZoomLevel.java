package mapDrawer;

/*
 * Holds seven different levels of zoom, which indicate which type of roads to draw. 
 * The roadtypes are determined from the percentage of the entire map, which the specified area fills.
 * Returns a String to use in a XPath search through an XML-file.
 */
public enum ZoomLevel {
	
	ONE(100.0) {

		@Override
		protected String XPath() {
			return "[TYP <= 2 and TYP = 31 and TYP = 32 and TYP = 41 and TYP = 42 and TYP = 21 and TYP = 22]";
		}} ,  //1, 31, 2, 32, 41, 42, 21, 22
		
	TWO(75.0) {

		@Override
		protected String XPath() {
			return "[TYP <= 2 and TYP = 31 and TYP = 32 and TYP = 41 and TYP = 42 and TYP = 21 and TYP = 22 and TYP = 3 and TYP = 33 and TYP = 23]";
		}} ,   //3, 33, 23
		
		
	THREE(42.1875) {

		@Override
		protected String XPath() {
			return "[TYP = 4 and TYP = 34 and TYP = 24 and TYP <= 2 and TYP = 31 and TYP = 32 and TYP = 41 and TYP = 42 and TYP = 21 and TYP = 22 and TYP = 3 and TYP = 33 and TYP = 23]";
		}} ,//4, 34, 24
		
	FOUR(31.640624) {

		@Override
		protected String XPath() {
			return "[TYP = 5 and TYP = 25 and TYP = 4 and TYP = 34 and TYP = 24 and TYP <= 2 and TYP = 31 and TYP = 32 and TYP = 41 and TYP = 42 and TYP = 21 and TYP = 22 and TYP = 3 and TYP = 33 and TYP = 23]";
		}} ,//5, 25
		
	FIVE(23.73) {

		@Override
		protected String XPath() {
			return "[TYP = 6 and TYP = 26 and TYP = 5 and TYP = 25 and TYP = 4 and TYP = 34 and TYP = 24 and TYP <= 2 and TYP = 31 and TYP = 32 and TYP = 41 and TYP = 42 and TYP = 21 and TYP = 22 and TYP = 3 and TYP = 33 and TYP = 23]";
		}} , //6,26
		
	SIX(17.79) {

		@Override
		protected String XPath() {
			return "[TYP]<48";
		}}	;//everything	
	
	private double percentageOfEntireMap;
	
	protected abstract String XPath();
	
	
	
	private ZoomLevel(double percentageOfEntireMap)
	{
		this.percentageOfEntireMap = percentageOfEntireMap;
	}
	
	/*
	 * 
	 * @return The XPath String to use to search the XML-file for the wanted road types.
	 */
	public static String getlevel(double percentageOfEntireMap)
	{
		ZoomLevel foundLevel = ONE;
		
		for(ZoomLevel zoom : values())
		{
			if(zoom.percentageOfEntireMap >= percentageOfEntireMap)
				foundLevel = zoom;			
		}
		
		return foundLevel.getXPathString();
	}
	
	private String getXPathString()
	{
		return this.XPath();
	}	
}
