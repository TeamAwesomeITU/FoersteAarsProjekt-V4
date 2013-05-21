package inputHandler;

import java.util.ArrayList;
import java.util.Iterator;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.TernarySearchTrie;

/**
 * Enables searching for Cities by name 
 */
public class CitySearch  {

	//A TernarySearchTrie of all City names
	private static TernarySearchTrie cityNameSearchTrie = createCityNameSearchTrie();
	
	//A TernarySearchTrie of all City postal numbers
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
		if(cityToFind.trim().isEmpty())
			return new City[0];
		
		cityToFind = cityToFind.toLowerCase();
		ArrayList<Integer> listOfFoundCities = cityNameSearchTrie.get(cityToFind);

		City[] arrayOfFoundCities = new City[listOfFoundCities.size()];

		for (int i = 0; i < arrayOfFoundCities.length; i++)
			arrayOfFoundCities[i] = City.getCityByID(listOfFoundCities.get(i)); 

		return arrayOfFoundCities;
	}
	
	/**
	 * Searches for the specified city name
	 * @param cityToFind
	 * @return The City name with the longest prefix match for the given input
	 */
	public static String searchForCityNameLongestPrefix(String cityToFind)
	{			
		cityToFind = cityToFind.toLowerCase();
		String longestPrefix = cityNameSearchTrie.longestPrefixOf(cityToFind);
		
		return longestPrefix.trim();
	}

	/**
	 * Searches for City names, that starts with the given string.
	 * @param cityToFind The name of the City to find
	 * @return An array of all the Cities, that starts with the given String
	 */
	public static City[] searchForCityNameSuggestions(String cityToFind)
	{
		if(cityToFind.trim().isEmpty())
			return new City[0];
		
		cityToFind = cityToFind.toLowerCase();
		ArrayList<String> cityNames = cityNameSearchTrie.prefixMatch(cityToFind);
		ArrayList<City> foundCityList = new ArrayList<>();
		
		for(String cityName : cityNames)
		{
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
	public static City[] searchForCityPostalNumberSuggestions(int postalNumberToFind)
	{
		ArrayList<String> cityNumbers = cityPostalNumberSearchTrie.prefixMatch(postalNumberToFind + "");
		ArrayList<City> foundCityList = new ArrayList<>();
		
		for(String cityNumber : cityNumbers)
		{
			City city = City.getCityByPostalNumber(Integer.parseInt(cityNumber));
			if(!(city == null))
				foundCityList.add(city);
		}

		return foundCityList.toArray(new City[foundCityList.size()]);
	}
		
	/**
	 * Checks if the City name matches the postal number
	 * @param cityName The name of the City to check
	 * @param postalNumber The postal number of the City
	 * @return True, if the City name matches the postal number
	 */
	public static boolean doesCityNameMatchPostalNumber(String cityName, int postalNumber)
	{ return City.getCityByCityName(cityName).getCityPostalNumbers().contains(postalNumber); }
}
