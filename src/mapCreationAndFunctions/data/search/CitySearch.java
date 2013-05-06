package mapCreationAndFunctions.data.search;

import java.util.ArrayList;
import java.util.Iterator;

import mapCreationAndFunctions.data.City;

/**
 * Enables searching for Cities by name 
 */
public class CitySearch  {

	private static TernarySearchTrie citySearchTrie = createCitySearchTrie();

	private static TernarySearchTrie createCitySearchTrie()
	{
		TernarySearchTrie tst = new TernarySearchTrie();

		for(City city : City.getAllCities())
			tst.put(city.getCityName(), city.getCityID());

		return tst;
	}

	public static City[] searchForCityName(String cityToFind)
	{
		ArrayList<Integer> listOfFoundCities = citySearchTrie.get(cityToFind);

		City[] arrayOfFoundCities = new City[listOfFoundCities.size()];

		for (int i = 0; i < arrayOfFoundCities.length; i++)
			arrayOfFoundCities[i] = City.getCityByID(listOfFoundCities.get(i)); 

		return arrayOfFoundCities;
	}

	/**
	 * Searches for City names, that starts with the given string.
	 * @param cityToFind The name of the City to find
	 * @return An array of all the Cities, that starts with the given String
	 */
	public static City[] getCityNameSuggestions(String cityToFind)
	{
		Iterable<String> cityNames = citySearchTrie.prefixMatch(cityToFind);
		Iterator<String> cityNamesIterator = cityNames.iterator();
		ArrayList<City> cityList = new ArrayList<>();
		
		while(cityNamesIterator.hasNext())
		{
			String cityName = cityNamesIterator.next();
			System.out.println("Trying to find city: " + cityName);
			City city = City.getCityByCityName(cityName);
			if(!(city == null))
				cityList.add(city);
		}

		return cityList.toArray(new City[cityList.size()]);
	}
}
