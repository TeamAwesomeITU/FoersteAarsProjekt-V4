package mapCreationAndFunctions.test;

import static org.junit.Assert.assertEquals;
import inputHandler.CitySearch;
import mapCreationAndFunctions.data.City;

import org.junit.Test;

public class CitySearchTest {


	public void testCitySearch(String cityToFind, int expectedFinds)
	{
		City[] cities = CitySearch.searchForCityName(cityToFind);
		
		assertEquals(expectedFinds, cities.length);
	}
	
	public void testCitySearchSuggestions(String cityToFind, int expectedFinds)
	{
		City[] cities = CitySearch.searchForCityNameSuggestions(cityToFind);
		
		assertEquals(expectedFinds, cities.length);
	}
	
	@Test
	public void testKoebenhavn()
	{
		String cityToFind = "København";
		int expectedIdenticalFinds = 0;
		testCitySearch(cityToFind, expectedIdenticalFinds);
		
		//Manually counted
		int expectedSuggestions = 8;
		testCitySearchSuggestions(cityToFind, expectedSuggestions);
	}
	
	@Test
	public void testKoebenhavnNV()
	{
		String cityToFind = "København NV";
		int expectedIdenticalFinds = 1;
		testCitySearch(cityToFind, expectedIdenticalFinds);
		
		//Manually counted
		int expectedSuggestions = 1;
		testCitySearchSuggestions(cityToFind, expectedSuggestions);
	}
	
	
}
