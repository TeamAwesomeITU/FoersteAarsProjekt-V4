package mapDrawer;

import java.util.Arrays;
import java.util.HashSet;

/*
 * Holds seven different levels of zoom, which indicate which type of roads to draw. 
 * The roadtypes are determined from the percentage of the entire map, which the specified area fills.
 * Returns a String to use in a XPath search through an XML-file.
 */
public enum ZoomLevel {
	
	
	
	ONE(100.0) {

		@Override
		protected HashSet<Integer> XPath() {
			Integer[] MaxZoom = new Integer[]{1,2,3,4,21,22,34,24,31,32,33,41,42,80,99};
			return new HashSet<Integer>(Arrays.asList(MaxZoom));
		}} ,
		
	TWO(65.0) {

		@Override
		protected HashSet<Integer> XPath() {	    
			Integer[] MaxMediumZoom = new Integer[]{1,2,3,4,5,25,35,24,21,22,34,31,32,33,41,42,80,99};
			return new HashSet<Integer>(Arrays.asList(MaxMediumZoom));

				}} ,
		
		
	THREE(20) {

		@Override
		protected HashSet<Integer> XPath() {
			Integer[] MinMediumZoom = new Integer[]{1,2,3,4,5,25,11,35,21,22,24,34,31,32,33,41,42,80,99};
			return new HashSet<Integer>(Arrays.asList(MinMediumZoom));

				}} ,		
	FOUR(2) {

		@Override
		protected HashSet<Integer> XPath() {
			Integer[] MinZoom = new Integer[]{1,2,3,4,21,22,34,31,32,33,41,42,5,25,35,6,26,8,10,11,28,80,99};
			return new HashSet<Integer>(Arrays.asList(MinZoom));

				}};		
	
	private double percentageOfEntireMap;
	
	protected abstract HashSet<Integer> XPath();
	
	
	
	private ZoomLevel(double percentageOfEntireMap)
	{
		this.percentageOfEntireMap = percentageOfEntireMap;
	}
	
	/*
	 * 
	 * @return The XPath String to use to search the XML-file for the wanted road types.
	 */
	public static HashSet<Integer> getlevel(double percentageOfEntireMap)
	{
		ZoomLevel foundLevel = ONE;
		
		for(ZoomLevel zoom : values())
		{
			if(zoom.percentageOfEntireMap >= percentageOfEntireMap)
				foundLevel = zoom;			
		}
		
		return foundLevel.getXPathString();
	}
	
	private HashSet<Integer> getXPathString()
	{
		return this.XPath();
	}	
}
