package mapCreationAndFunctions;

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

		TWO(80.0) {

			@Override
			protected HashSet<Integer> relevantRoadTypes() {
				return new HashSet<Integer>(RoadType.getRoadTypesRelevantToZoomLevel(2));

			}} ,


			THREE(40.0) {

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


					/**
					 * This mutator method changes the value of percentageofEntireMap each time it changes.
					 * @param percentageOfEntireMap A double representing what percentage of the map that is 
					 * currently shown. 
					 */
					private ZoomLevel(double percentageOfEntireMap)
					{
						this.percentageOfEntireMap = percentageOfEntireMap;
					}

					/**
					 * 
					 * @param areasPercentageOfEntireMap Percentage of map shown.
					 * @return HashSet of integers representing road types.
					 */
					public static HashSet<Integer> getlevel(double areasPercentageOfEntireMap)
					{
						ZoomLevel foundLevel = ONE;

						for(ZoomLevel zoom : values())						
							if(zoom.percentageOfEntireMap >= areasPercentageOfEntireMap)
								foundLevel = zoom;								

						return foundLevel.getRelevantRoadTypes();
					}
					/**
					 * Acsessor method.
					 * @return HashSet of integers representing road types
					 */
					private HashSet<Integer> getRelevantRoadTypes()
					{
						return this.relevantRoadTypes();
					}	
}
