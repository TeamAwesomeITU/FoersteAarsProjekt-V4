package mapDrawer.test;

import static org.junit.Assert.*;

import mapDrawer.AreaToDraw;
import mapDrawer.exceptions.AreaNegativeSizeException;

import org.junit.Test;

public class AreaToDrawIntersectionTest {

	@Test
	public void testAreaIsFullyInsideOtherArea() {
		try {
			AreaToDraw area1 = new AreaToDraw(0.0, 5.0, 0.0, 5.0);
			AreaToDraw area2 = new AreaToDraw(1.0, 2.0, 1.0, 2.0);			
			assertTrue(area1.isAreaIntersectingWithArea(area2));
			assertTrue(area2.isAreaIntersectingWithArea(area1));
			
		} catch (AreaNegativeSizeException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAreaIsPartlyInsideOtherArea() {
		try {
			AreaToDraw area1 = new AreaToDraw(0.0, 5.0, 0.0, 5.0);
			AreaToDraw area2 = new AreaToDraw(4.0, 7.0, 4.0, 7.0);			
			assertTrue(area1.isAreaIntersectingWithArea(area2));
			assertTrue(area2.isAreaIntersectingWithArea(area1));
			
		} catch (AreaNegativeSizeException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAreaIsNotInsideOtherArea() {
		try {
			AreaToDraw area1 = new AreaToDraw(0.0, 5.0, 0.0, 5.0);
			AreaToDraw area2 = new AreaToDraw(6.0, 10.0, 7.0, 12.0);			
			assertFalse(area1.isAreaIntersectingWithArea(area2));
			assertFalse(area2.isAreaIntersectingWithArea(area1));
			
		} catch (AreaNegativeSizeException e) {
			fail();
			e.printStackTrace();
		}
	}

}
