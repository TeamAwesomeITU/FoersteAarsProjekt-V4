/**
 * 
 */
package mapDrawer;

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
	
	private static final HashSet<Integer> category1 = makeCategories(new Integer[]{1,2,22,31,32,41,42}); 
	private static final HashSet<Integer> category2 = makeCategories(new Integer[]{3,4,23,24,33,34,43,44}); 
	private static final HashSet<Integer> category3 = makeCategories(new Integer[]{0,5,13,25,35,45,46,95,99}); 
	private static final HashSet<Integer> category4 = makeCategories(new Integer[]{6,26,46}); 
	private static final HashSet<Integer> category5 = makeCategories(new Integer[]{8,10,11,28}); 
	private static final HashSet<Integer> category6 = makeCategories(new Integer[]{80}); 
	

	private RoadType(int category)
	{}

	public static Color getColor(int roadType)
	{
		RoadType rt = values()[getCategory(roadType)-1];
		return rt.color();

	}

	public static float getStroke(int roadType)
	{
		RoadType rt = values()[getCategory(roadType)-1];
		return rt.stroke();
	}
	
	private static HashSet<Integer> makeCategories(Integer[] categoryArr)
	{
		return new HashSet<Integer>(Arrays.asList(categoryArr));
	}
	
	@SuppressWarnings("unchecked")
	public static HashSet<Integer> getRoadTypesRelevantToZoomLevel(int numberOfWantedCategories)
	{
		//WHAT THE FUCK - MIND.EQUALS(BLOWN) == TRUE
		HashSet<Integer> relevantRoadTypeSet = new HashSet<>();
		System.out.println("Before everything something: " + relevantRoadTypeSet.size());
		relevantRoadTypeSet = (HashSet<Integer>) category6.clone();
		System.out.println("Before something: " + relevantRoadTypeSet.size());
		
		System.out.print("Draw categories: ");
		switch (numberOfWantedCategories) {
		case 4: relevantRoadTypeSet.addAll(category4); relevantRoadTypeSet.addAll(category5); System.out.print("4, ");
		case 3: relevantRoadTypeSet.addAll(category3); System.out.print("3, ");
		case 2: relevantRoadTypeSet.addAll(category2); System.out.print("2, ");
		case 1: relevantRoadTypeSet.addAll(category1); relevantRoadTypeSet.addAll(category2); System.out.println("1");
		System.out.println("After something: " + relevantRoadTypeSet.size());
		return relevantRoadTypeSet;
			
		default: return relevantRoadTypeSet;
		}
	}

	private static int getCategory(int roadType)
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

	public static void main(String[] args)
	{
		System.out.println(RoadType.getStroke(1));
	}
}
