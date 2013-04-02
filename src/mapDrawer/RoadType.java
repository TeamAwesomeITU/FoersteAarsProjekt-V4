/**
 * 
 */
package mapDrawer;

import java.awt.Color;

/**
 * 
 *
 */
public enum RoadType {

	HIGHWAY(1) {
		
		@Override
		protected Color color() {
			return Color.red;
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
			return Color.blue;
		}
		
		@Override
		protected float stroke() {			
			return 2;
		}
	} ,

	SECONDARYWAY(3) {

		@Override
		protected Color color() {
			return Color.black;
		}

		@Override
		protected float stroke() {			
			return 1;
		}
	} ,

	OTHER(4) {

		@Override
		protected Color color() {
			return Color.green;
		}

		@Override
		protected float stroke() {			
			return 1;
		}
	} ,

	FERRY(5) {

		@Override
		protected Color color() {
			return Color.cyan;
		}

		@Override
		protected float stroke() {			
			return 1;
		}
	} ,

	COASTLINE(6) {

		@Override
		protected Color color() {
			return Color.gray;
		}

		@Override
		protected float stroke() {			
			return 3;
		}
	} ;

	

	protected abstract Color color();

	protected abstract float stroke();

	
	private RoadType(int category)
	{
		
	}

	public static Color getColor(int roadType)
	{
		RoadType rt = values()[getCategory(roadType)];
		return rt.color();

	}

	public static float getStroke(int roadType)
	{
		RoadType rt = values()[getCategory(roadType)];
		return rt.stroke();
	}

	private static int getCategory(int roadType)
	{
		switch (roadType)
		{
		case 1:
		case 31:
		case 41: return 1;

		case 2:
		case 3:
		case 32:
		case 33:
		case 42:
		case 43: return 2;

		case 4:
		case 34:
		case 44: return 3;

		case 5:
		case 6:
		case 11:
		case 10:
		case 13:
		case 35:
		case 45:
		case 46:			
		case 99: return 4;

		case 80: return 5;

		//Coastline or mistakes
		default: return 6;
		}
	}
}
