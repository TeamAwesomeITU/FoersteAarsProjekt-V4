package mapCreationAndFunctions.data.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import mapCreationAndFunctions.data.City;

public class CitySearch  {

	private static TernarySearchTrieCity citySearchTrie = createCitySearchTrie();

	private static TernarySearchTrieCity createCitySearchTrie()
	{
		TernarySearchTrieCity tst = new TernarySearchTrieCity();

		//Excludes Edges with no name
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

	public static void main( String[] args )
	{
		double startTime = System.currentTimeMillis();
		City foundCity = searchForCityName("Køge");
		double endTime = System.currentTimeMillis();		
		System.out.println("Search took: " + (endTime-startTime) + "miliseconds");
		//System.out.println(foundCity.getCityName());
		
		City[] citiesList = getCityNameSuggestions("Køb");
		
		for(City city : citiesList)
			System.out.println(city.getCityName());
		
	}
}
