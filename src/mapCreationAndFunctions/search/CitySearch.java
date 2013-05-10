package mapCreationAndFunctions.search;

import java.util.ArrayList;
import java.util.Iterator;

import mapCreationAndFunctions.data.City;

/**
 * Enables searching for Cities by name 
 */
public class CitySearch  {

	private static TernarySearchTrie cityNameSearchTrie = createCityNameSearchTrie();
	private static TernarySearchTrie cityPostalNumberSearchTrie = createCityPostalNumberSearchTrie();

	/**
	 * Creates a TernarySearchTrie from data from the City class
	 * @return a TernarySearchTrie of Cities.
	 */
	private static TernarySearchTrie createCityNameSearchTrie()
	{
		TernarySearchTrie tst = new TernarySearchTrie();

		for(City city : City.getAllCities())
			tst.put(city.getCityName().toLowerCase(), city.getCityID());

		return tst;
	}
	
	/**
	 * Creates a TernarySearchTrie from data from the City class
	 * @return a TernarySearchTrie of Cities.
	 */
	private static TernarySearchTrie createCityPostalNumberSearchTrie()
	{
		TernarySearchTrie tst = new TernarySearchTrie();

		for(City city : City.getAllCities())
		{
			Iterator<Integer> iterator = city.getCityPostalNumbers().iterator();
			while(iterator.hasNext())
				tst.put(iterator.next().toString(), city.getCityID());
		}

		return tst;
	}

	/**
	 * Searches for Cities with the given
	 * @param cityToFind The name of the wanted City
	 * @return An array of all found Cities
	 */
	public static City[] searchForCityName(String cityToFind)
	{
		cityToFind = cityToFind.toLowerCase();
		ArrayList<Integer> listOfFoundCities = cityNameSearchTrie.get(cityToFind);

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
	public static City[] searchForCityNameSuggestions(String cityToFind)
	{
		cityToFind = cityToFind.toLowerCase();
		Iterable<String> cityNames = cityNameSearchTrie.prefixMatch(cityToFind);
		Iterator<String> cityNamesIterator = cityNames.iterator();
		ArrayList<City> foundCityList = new ArrayList<>();
		
		while(cityNamesIterator.hasNext())
		{
			String cityName = cityNamesIterator.next();
			City city = City.getCityByCityName(cityName);
			if(!(city == null))
				foundCityList.add(city);
		}

		return foundCityList.toArray(new City[foundCityList.size()]);
	}
	
	/**
	 * Searches for City postal numbers, that starts with the given number.
	 * @param cityToFind The postal number of the City to find
	 * @return The city that has the given postal number
	 */
	public static City searchForCityPostalNumber(String cityToFind)
	{
		return City.getCityByPostalNumber(Integer.parseInt(cityToFind));
	}
	
	/**
	 * Searches for City postal numbers, that starts with the given number.
	 * @param cityToFind The postal number of the City to find
	 * @return An array of all the Cities, that starts with the given postal number
	 */
	public static City[] searchForCityPostalNumberSuggestions(String cityToFind)
	{
		cityToFind = cityToFind.toLowerCase();
		Iterable<String> cityNumbers = cityPostalNumberSearchTrie.prefixMatch(cityToFind);
		Iterator<String> cityNumbersIterator = cityNumbers.iterator();
		ArrayList<City> foundCityList = new ArrayList<>();
		
		while(cityNumbersIterator.hasNext())
		{
			String cityNumber = cityNumbersIterator.next();
			City city = City.getCityByPostalNumber(Integer.parseInt(cityNumber));
			if(!(city == null))
				foundCityList.add(city);
		}

		return foundCityList.toArray(new City[foundCityList.size()]);
	}
	
	public static void main(String[] args) {
		/*
		City[] foundCityList = searchForCityNameSuggestions("k");
		for(City city : foundCityList)
			System.out.println(city.getCityName());
		
		System.out.println(searchForCityName("KØGE")[0].getCityName());
		*/
		
		for(City city : searchForCityPostalNumberSuggestions("46"))
				System.out.println(city.getCityName());
		
		System.out.println("BREAK---------------");
		
		for(City city : searchForCityPostalNumberSuggestions("24"))
			System.out.println(city.getCityName());
		
		System.out.println("BREAK---------------");
		
		for(City city : searchForCityPostalNumberSuggestions("0"))
			System.out.println(city.getCityName());
	}
}
