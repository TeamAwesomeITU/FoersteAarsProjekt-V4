package Workshop.test;

import static org.junit.Assert.*;

import org.junit.Test;
import Workshop.Date;


public class DateTester {

	@Test(expected=RuntimeException.class)
	public void testEuropeanFormat() {
		new Date(30,12,2013);		
	}
	
	@Test
	public void testAmericanFormat() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testNegativeMonth() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testZeroMonth() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testLettersInDate() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testTooManyNumbers() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testWithoutZeroesInFront() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testWithZeroesInFront() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testYearWithMoreThanFourDigits() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testNegativeYear() {
		fail("Not yet implemented");
	}
	
	@Test
	public void test29thOfFebInNonLeapYear() {
		fail("Not yet implemented");
	}
	
	@Test
	public void test29thOfFebInLeapYear() {
		fail("Not yet implemented");
	}

}
