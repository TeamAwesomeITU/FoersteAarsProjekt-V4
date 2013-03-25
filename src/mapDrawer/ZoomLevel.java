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
			return "lolONE";
		}} ,
		
	TWO(75.0) {

		@Override
		protected String XPath() {
			return "lolTWO";
		}} ,
		
	THREE(56.25) {

		@Override
		protected String XPath() {
			return "lolTHREE";
		}} ,
		
	FOUR(42.1875) {

		@Override
		protected String XPath() {
			return "lolFOUR";
		}} ,
		
	FIVE(31.640624) {

		@Override
		protected String XPath() {
			return "lolFIVE";
		}} ,
		
	SIX(23.73) {

		@Override
		protected String XPath() {
			return "lolSIX";
		}} ,
		
	SEVEN(17.79) {

		@Override
		protected String XPath() {
			return "LOLSEVEN and BELOW";
		}}	;	
	
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
