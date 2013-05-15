package mapCreationAndFunctions.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import mapCreationAndFunctions.search.CitySearch;

/**
 * This class is implemented to be able to deal with many different cities with many different postal numbers.
 * Each individual City object contains the name of the city as a String, and a HashSet<Integer>, which holds all of the postal numbers that belongs to the city.
 *
 */
public class City {

	//The path to the file from which to create Cities
	private static String cityFileName = "XML/postalNumbersAndCityNamesFINAL.txt";

	//A HashMap where the postal number is the key, and the corresponding City is the value
	private static HashMap<Integer, City> cityHashMap = new HashMap<Integer, City>();

	//An ArrayList of all Cities
	private static ArrayList<City> allCitiesList = new ArrayList<City>();

	//The City's name
	private String cityName;

	//A HashSet of all the postal numbers which belong to the city
	private HashSet<Integer> postalNumbers = new HashSet<Integer>();
	
	//A HashSet of all Edge's ID's that lies within this city
	private HashSet<Integer> cityRoadsSet = new HashSet<Integer>();

	//The ID of the City
	private int cityID;

	//The ID count of the cities;
	private static int cityIDcount = 1;

	//TODO Fucking bad programming - this boolean only exists to ensure the creation of the other fields - FIX!
	private static boolean initilalized = createCities();
	
	//An array of all the postal numbers, that exists in our data file, but not in our postal number file
	private static int[] nonExistingNumbers = new int[]{0, 1405, 8103, 1097, 6,
		1099, 1262, 5563, 1399, 676, 1639, 8117, 9392, 1778, 8685, 1748, 1783, 8454, 8125, 1109, 8458, 8126, 8373, 8797};
	
	/**
	 * Creates a City with the input cityName and one of its postal numbers
	 * @param cityName The name of the City
	 * @param postalNumber A postal number of the city
	 */
	private City(String cityName, Integer postalNumber)
	{
		this.cityName = cityName;
		this.cityID = cityIDcount++;
		postalNumbers.add(postalNumber);
	}

	/**
	 * Check if a City with the given name exists
	 * @param cityName The name to check for
	 * @return True if the city already exists
	 */
	private static boolean cityNameAlreadyExists(String cityName)
	{ 
		boolean exists = false;
		for(City city : allCitiesList)
			if(city.getCityName().equalsIgnoreCase(cityName))
				exists = true;
		return exists;
	}

	/**
	 * Returns the name of the City
	 * @return the name of the City
	 */
	public String getCityName()
	{ return this.cityName; }

	/**
	 * Returns the postal numbers of the City in a HashSet<Integer>
	 * @return the postal numbers of the City in a HashSet<Integer>
	 */
	public HashSet<Integer> getCityPostalNumbers()
	{ return this.postalNumbers; }

	/**
	 * Returns the ID of the City
	 * @return the ID of the City
	 */
	public int getCityID()
	{ return this.cityID; }

	/**
	 * Adds a postal number to the City's current postal numbers
	 * @param postalNumberToAdd the postal number to add
	 */
	private void addPostalNumberToCity(Integer postalNumberToAdd)
	{ postalNumbers.add(postalNumberToAdd); }
	
	private void addRoadToCity(int edgeID)
	{ cityRoadsSet.add(edgeID); }
	
	public static void addRoadToRelevantCity(int postalNumber, int edgeID)
	{
		if(postalNumberExists(postalNumber))
			getCityByPostalNumber(postalNumber).addRoadToCity(edgeID);
	}
	
	public HashSet<Integer> getCityRoadIDs()
	{ return cityRoadsSet; }
	
	public Edge[] getCityRoads()
	{
		HashSet<Integer> edgeIDsSet = getCityRoadIDs();
		Iterator<Integer> iterator = edgeIDsSet.iterator();
		Edge[] edges = new Edge[edgeIDsSet.size()];
		int currentIndex = 0;
		while (iterator.hasNext())
			edges[currentIndex++] = DataHolding.getEdge(iterator.next());
		
		return edges;		
	}
	
	public boolean containsRoad(int edgeID)
	{ return cityRoadsSet.contains(edgeID); }

	/**
	 * Finds the City with the given postal number
	 * @param postalNumber the City's postal number
	 * @return The City with the corresponding postal number - returns null, if nothing could be found.
	 */
	public static City getCityByPostalNumber(int postalNumber)
	{
		if(!postalNumberExists(postalNumber))
			return null;
		else
			return cityHashMap.get(postalNumber);
	}
	
	public static boolean postalNumberExists(int postalNumber)
	{
		for(int number : nonExistingNumbers)
			if(postalNumber == number)
				return false;
		return true;
	}
	
	
	public static boolean cityNameExists(String cityName)
	{ return CitySearch.searchForCityName(cityName).length > 0;	}
	
	
	/**
	 * Finds the name of the City with the given postal number
	 * @param postalNumber the name of the City with the given postal number
	 * @return the City's name - if the postal number is 0, it will return "CITY POSTAL NUMBER DOES NOT BELONG TO A CITY"
	 */
	public static String getCityNameByPostalNumber(int postalNumber)
	{
		String cityName = "";
		
		try {
			cityName = getCityByPostalNumber(postalNumber).getCityName();
		} catch (NullPointerException e) {
			cityName = "CITY POSTAL NUMBER DOES NOT BELONG TO A CITY";
		}
	
		return cityName;
	}

	/**
	 * Finds the City with the given name
	 * @param cityName the wanted City's name
	 * @return the City with the corresponding name - if no City could be found, null is returned
	 */
	public static City getCityByCityName(String cityName)
	{
		for(City city : allCitiesList)
			if(city.getCityName().equalsIgnoreCase(cityName))
				return city;
		return null;
	}

	public static City getCityByID(int ID)
	{ return allCitiesList.get(ID-1); }

	/**
	 * Creates all of the Cities
	 * @return true if the creation went well
	 */
	private static boolean createCities()
	{
		try {			
			long s = System.currentTimeMillis();
			File file = new File(cityFileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			String line;
			String[] lineParts;
			Integer postalNumber; 
			String cityName;
			City city;

			while((line = reader.readLine()) != null)
			{
				lineParts = line.split("\\s");
				postalNumber = Integer.parseInt(lineParts[0]);
				cityName = lineParts[1];

				for(int i = 2; i < lineParts.length; i++)
					cityName += " " + lineParts[i];

				if(!cityNameAlreadyExists(cityName))
				{
					city = new City(cityName, postalNumber);
					cityHashMap.put(postalNumber, city);
					allCitiesList.add(city);
				}

				else
				{
					city = getCityByCityName(cityName);
					city.addPostalNumberToCity(postalNumber);
					cityHashMap.put(postalNumber, city);
				}				
			}

			reader.close();		
			long t = System.currentTimeMillis();
			System.out.println("Creation of CitySomeThingJesper took " + (t-s));
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
	}

	public static ArrayList<City> getAllCities()
	{
		if(initilalized == false)
			createCities();
		return allCitiesList;
	}
	@Override
	public String toString() {
		return this.cityName;
	}	

	public static void main(String[] args) {
		createCities();
		
		for(int number : nonExistingNumbers)
			System.out.println(number);
			
		
	}
}