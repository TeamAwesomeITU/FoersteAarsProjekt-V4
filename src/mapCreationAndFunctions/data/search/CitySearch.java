package mapCreationAndFunctions.data.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import mapCreationAndFunctions.data.City;

/**
 * Enables searching for Cities by name 
 */
public class CitySearch  {

	private static TernarySearchTrieCity citySearchTrie = createCitySearchTrie();

	private static TernarySearchTrieCity createCitySearchTrie()
	{
		TernarySearchTrieCity tst = new TernarySearchTrieCity();

		for(City city : City.getAllCities())
				tst.put(city.getCityName(), city.getCityPostalNumbers());

		return tst;
	}

	public static City searchForCityName(String edgeToFind)
	{
		HashSet<Integer> postalNumbers = citySearchTrie.get(edgeToFind);
		if(postalNumbers == null)
			return null;
		return City.getCityByPostalNumber(postalNumbers.iterator().next());
	}

	public static City[] getCityNameSuggestions(String edgeToFind)
	{
		Iterable<String> cityNames = citySearchTrie.prefixMatch(edgeToFind);
		Iterator<String> cityNameIterator = cityNames.iterator();
		ArrayList<City> citiesList = new ArrayList<>();
		
		while(cityNameIterator.hasNext())
			citiesList.add(City.getCityByCityName(cityNameIterator.next()));
			
		return citiesList.toArray(new City[citiesList.size()]);
	}
}
