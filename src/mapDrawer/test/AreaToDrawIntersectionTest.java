package mapDrawer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mapDrawer.AreaToDraw;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.InvalidAreaProportionsException;
import mapDrawer.exceptions.NegativeAreaSizeException;

import org.junit.Test;

public class AreaToDrawIntersectionTest {

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
