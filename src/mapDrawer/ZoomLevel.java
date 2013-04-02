package mapDrawer;

import java.util.Arrays;
import java.util.HashSet;

/*
 * Holds seven different levels of zoom, which indicate which type of roads to draw. 
 * The roadtypes are determined from the percentage of the entire map, which the specified area fills.
 * Returns a String to use in a getRelevantRoadTypes search through an XML-file.
 */
public enum ZoomLevel {	
	
	ONE(100.0) {

		@Override
		protected HashSet<Integer> relevantRoadTypes() {
			return new HashSet<Integer>(RoadType.getRoadTypesRelevantToZoomLevel(1));
		}} ,
		
	TWO(65.0) {

		@Override
		protected HashSet<Integer> relevantRoadTypes() {
			return new HashSet<Integer>(RoadType.getRoadTypesRelevantToZoomLevel(2));

				}} ,
		
		
	THREE(20.0) {

		@Override
		protected HashSet<Integer> relevantRoadTypes() {
			return new HashSet<Integer>(RoadType.getRoadTypesRelevantToZoomLevel(3));

				}} ,		
	FOUR(2.0) {

		@Override
		protected HashSet<Integer> relevantRoadTypes() {
			return new HashSet<Integer>(RoadType.getRoadTypesRelevantToZoomLevel(4));

				}};		
	
	private double percentageOfEntireMap;
	
	protected abstract HashSet<Integer> relevantRoadTypes();
	
	
	
	private ZoomLevel(double percentageOfEntireMap)
	{
		this.percentageOfEntireMap = percentageOfEntireMap;
	}
	
	/*
	 * 
	 * @return The getRelevantRoadTypes String to use to search the XML-file for the wanted road types.
	 */
	public static HashSet<Integer> getlevel(double areasPercentageOfEntireMap)
	{
		ZoomLevel foundLevel = ONE;
		
		for(ZoomLevel zoom : values())
		{
			if(zoom.percentageOfEntireMap >= areasPercentageOfEntireMap)
			{
				foundLevel = zoom;			
				System.out.println("changed zoomlevel to: " + zoom);
			}
		}
		
		return foundLevel.getRelevantRoadTypes();
	}
	
	private HashSet<Integer> getRelevantRoadTypes()
	{
		return this.relevantRoadTypes();
	}	
}
