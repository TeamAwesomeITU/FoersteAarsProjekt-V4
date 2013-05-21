package mapCreationAndFunctions.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mapCreationAndFunctions.AreaToDraw;
import mapCreationAndFunctions.exceptions.AreaIsNotWithinDenmarkException;
import mapCreationAndFunctions.exceptions.InvalidAreaProportionsException;
import mapCreationAndFunctions.exceptions.NegativeAreaSizeException;

import org.junit.Test;

/**
 * Testclass for the AreaToDraw Intersection method.
 */
public class AreaToDrawIntersectionTest {

	/**
	 * Tests if the Area is fully inside another Area.
	 */
	@Test
	public void testAreaIsFullyInsideOtherArea() {
		try {
			AreaToDraw area1 = new AreaToDraw(0.0, 5.0, 0.0, 5.0, false);
			AreaToDraw area2 = new AreaToDraw(1.0, 2.0, 1.0, 2.0, false);			
			assertTrue(area1.isAreaIntersectingWithArea(area2));
			assertTrue(area2.isAreaIntersectingWithArea(area1));
			
		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests if the area is partly inside another area.
	 */
	@Test
	public void testAreaIsPartlyInsideOtherArea() {
		try {
			AreaToDraw area1 = new AreaToDraw(0.0, 5.0, 0.0, 5.0, false);
			AreaToDraw area2 = new AreaToDraw(4.0, 7.0, 4.0, 7.0, false);			
			assertTrue(area1.isAreaIntersectingWithArea(area2));
			assertTrue(area2.isAreaIntersectingWithArea(area1));
			
		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Tests if the area is not inside another area.
	 */
	@Test
	public void testAreaIsNotInsideOtherArea() {
		try {
			AreaToDraw area1 = new AreaToDraw(0.0, 5.0, 0.0, 5.0, false);
			AreaToDraw area2 = new AreaToDraw(6.0, 10.0, 7.0, 12.0, false);			
			assertFalse(area1.isAreaIntersectingWithArea(area2));
			assertFalse(area2.isAreaIntersectingWithArea(area1));
			
		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e) {
			fail();
			e.printStackTrace();
		}
	}

}
