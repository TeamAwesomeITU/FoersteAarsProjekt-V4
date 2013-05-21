/**
 * 
 */
package mapCreationAndFunctions;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;


/**
 * 
 *
 */
public enum RoadType {

	HIGHWAY(1) {

		@Override
		protected Color color() {
			return Color.red.darker();
		}

		@Override
		protected float stroke() {			
			return 3;
		}
	}
	,

	PRIMARYWAY(2) {

		@Override
		protected Color color() {
			return Color.blue.darker();
		}

		@Override
		protected float stroke() {			
			return 2;
		}
	} ,

	OTHER(3) {

		@Override
		protected Color color() {
			return Color.black;
		}

		@Override
		protected float stroke() {			
			return 1;
		}
	} ,

	PATHS(4) {

		@Override
		protected Color color() {
			return Color.black;
		}

		@Override
		protected float stroke() {			
			return 1;
		}
	} ,

	OTHERROADS(5) {

		@Override
		protected Color color() {
			return Color.green.darker();
		}

		@Override
		protected float stroke() {			
			return 1;
		}
	} ,

	FERRY(6) {

		@Override
		protected Color color() {
			return Color.pink;
		}

		@Override
		protected float stroke() {			
			return 1;
		}
	} ,

	COASTLINE(7) {

		@Override
		protected Color color() {
			return Color.gray;
		}

		@Override
		protected float stroke() {			
			return 1;
		}
	} ;

	protected abstract Color color();

	protected abstract float stroke();
	//Categories that each gets a different colour
	private static final HashSet<Integer> category1 = makeCategories(new Integer[]{1,2,21,22,31,32,41,42}); 
	private static final HashSet<Integer> category2 = makeCategories(new Integer[]{3,4,23,24,33,34,43,44}); 
	private static final HashSet<Integer> category3 = makeCategories(new Integer[]{0,5,13,25,35,45,46,95,99}); 
	private static final HashSet<Integer> category4 = makeCategories(new Integer[]{6,26,46}); 
	private static final HashSet<Integer> category5 = makeCategories(new Integer[]{8,10,11,28}); 
	private static final HashSet<Integer> category6 = makeCategories(new Integer[]{80}); 


	private RoadType(int category)
	{}

	/**
	 * Return the colour of the road type
	 * @param roadType int representing a road type
	 * @return A colour
	 */
	public static Color getColor(int roadType)
	{
		RoadType rt = values()[getCategory(roadType)-1];
		return rt.color();

	}
	/**
	 * Returns a stroke for a given road type.
	 * @param roadType integer representing a road type
	 * @return A float representing the thickness of a line. 
	 */
	public static float getStroke(int roadType)
	{
		RoadType rt = values()[getCategory(roadType)-1];
		return rt.stroke();
	}
	/**
	 * Creates a HashSet from an Array of integers
	 * @param categoryArr An array of integers representing road types
	 * @return A HashSet containing the array given in the parameter.
	 */
	private static HashSet<Integer> makeCategories(Integer[] categoryArr)
	{
		return new HashSet<Integer>(Arrays.asList(categoryArr));
	}
	/**
	 * This method contains a switch that, given an integer between 1-4 returns a HashSet 
	 * of road types.
	 * @param numberOfWantedCategories An integer determining which case to use.
	 * @return A HashSet containing the road types to be shown on the map.
	 */
	@SuppressWarnings("unchecked")
	public static HashSet<Integer> getRoadTypesRelevantToZoomLevel(int numberOfWantedCategories)
	{
		//WHAT THE FUCK - MIND.EQUALS(BLOWN) == TRUE
		HashSet<Integer> relevantRoadTypeSet = new HashSet<Integer>();
		relevantRoadTypeSet = (HashSet<Integer>) category6.clone();

		switch (numberOfWantedCategories) {
		case 4: relevantRoadTypeSet.addAll(category4); relevantRoadTypeSet.addAll(category5);
		case 3: relevantRoadTypeSet.addAll(category3);
		case 2: relevantRoadTypeSet.addAll(category2);
		case 1: relevantRoadTypeSet.addAll(category1); relevantRoadTypeSet.addAll(category2);
		return relevantRoadTypeSet;

		default: return relevantRoadTypeSet;
		}
	}
	/**
	 * This method returns an integer representing a category if you give it an integer representing
	 * a road type. 
	 * @param roadType Integer representing a road type
	 * @return An integer representing a category.
	 */
	public static int getCategory(int roadType)
	{
		if(category1.contains(roadType)) return 1;
		if(category2.contains(roadType)) return 2;
		if(category3.contains(roadType)) return 3;
		if(category4.contains(roadType)) return 4;
		if(category5.contains(roadType)) return 5;
		if(category6.contains(roadType)) return 6;
		//If coastline or mistake
		else
			return 7;
	}
}
