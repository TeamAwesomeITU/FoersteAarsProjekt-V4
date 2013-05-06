package mapCreationAndFunctions.data.search;

import java.util.ArrayList;
import java.util.Iterator;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.Edge;

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
		ArrayList<Integer> listOfFoundEdges = citySearchTrie.get(cityToFind);

		City[] arrayOfFoundCities = new City[listOfFoundEdges.size()];

		for (int i = 0; i < arrayOfFoundCities.length; i++)
			arrayOfFoundCities[i] = City.getCityByID(listOfFoundEdges.get(i)); 

		return arrayOfFoundCities;
	}

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
			cityList.add(City.getCityByCityName(cityName));
		}

		return cityList.toArray(new City[cityList.size()]);
	}
}
