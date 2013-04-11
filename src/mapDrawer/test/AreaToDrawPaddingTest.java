package mapDrawer.test;

import static org.junit.Assert.*;

import mapDrawer.AreaToDraw;
import mapDrawer.exceptions.AreaIsNotWithinDenmarkException;
import mapDrawer.exceptions.InvalidAreaProportionsException;
import mapDrawer.exceptions.NegativeAreaSizeException;

import org.junit.Test;

public class AreaToDrawPaddingTest {

	@Test
	public void testAreaToDrawPadding1() {
		double xMin = 438000;
		double xMax = xMin+((905000-xMin)/2);
		double yMin = 6047000;
		double yMax = yMin+((6408000-yMin)/4);
		
		double correctXMin = 438000;
		double correctXMax = 671500;
		double correctYMin = 6047000;
		double correctYMax = 6227500;
		
		System.out.println("SmallestX: " + xMin);
		System.out.println("LargestX: " + xMax);
		System.out.println("SmallestY: " + yMin);
		System.out.println("LargestY: " + yMax);
		
		AreaToDraw area;
		try {
			area = new AreaToDraw(xMin, xMax, yMin, yMax, true);
			
			double newXmin = area.getSmallestX();
			double newXmax = area.getLargestX();
			double newYmin = area.getSmallestY();
			double newYmax = area.getLargestY();
			
			System.out.println("newSmallestX: " + newXmin);
			System.out.println("newLargestX: " + newXmax);
			System.out.println("newSmallestY: " + newYmin);
			System.out.println("newLargestY: " + newYmax);
			
			assertTrue((area.getSmallestX() == correctXMin));
			assertTrue((area.getLargestX() == correctXMax));
			assertTrue((area.getSmallestY() == correctYMin));
			assertTrue((area.getLargestY() == correctYMax));
			
			assertTrue(area.getWidthHeightRelation() == AreaToDraw.getWidthHeightRelationOfEntireMap());
			
		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e) {
			fail();
			e.printStackTrace();
		}
		
	}

	@Test
	public void testAreaToDrawPadding2() {
		double xMin = 438000;
		double xMax = xMin+((905000-xMin)/4);
		double yMin = 6047000;
		double yMax = yMin+((6408000-yMin)/2);
		
		double correctXMin = 438000;
		double correctXMax = 671500;
		double correctYMin = 6047000;
		double correctYMax = 6227500;
		
		System.out.println("SmallestX: " + xMin);
		System.out.println("LargestX: " + xMax);
		System.out.println("SmallestY: " + yMin);
		System.out.println("LargestY: " + yMax);
		
		AreaToDraw area;
		try {
			area = new AreaToDraw(xMin, xMax, yMin, yMax, true);
			
			double newXmin = area.getSmallestX();
			double newXmax = area.getLargestX();
			double newYmin = area.getSmallestY();
			double newYmax = area.getLargestY();
			
			System.out.println("newSmallestX: " + newXmin);
			System.out.println("newLargestX: " + newXmax);
			System.out.println("newSmallestY: " + newYmin);
			System.out.println("newLargestY: " + newYmax);
			
			assertTrue((area.getSmallestX() == correctXMin));
			assertTrue((area.getLargestX() == correctXMax));
			assertTrue((area.getSmallestY() == correctYMin));
			assertTrue((area.getLargestY() == correctYMax));
			
			assertTrue(area.getWidthHeightRelation() == AreaToDraw.getWidthHeightRelationOfEntireMap());
			
		} catch (NegativeAreaSizeException | AreaIsNotWithinDenmarkException | InvalidAreaProportionsException e) {
			fail();
			e.printStackTrace();
		}
		
	}

}
