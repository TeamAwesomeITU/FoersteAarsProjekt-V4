package mapDrawer.dataSupplying;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class City {
	
	//The path to the file from which to create Cities
	private static String cityFileName = "XML/postalNumbersAndCityNames_refined.txt";
	
	//A HashMap where the postal number is the key, and the corresponding City is the value
	private static HashMap<Integer, City> cityHashMap = new HashMap<Integer, City>();
	
	//An ArrayList of all Cities
	private static ArrayList<City> allCitiesList = new ArrayList<City>();
	
	//The City's name
	private String cityName;
	
	//A HashSet of all the postal numbers which belong to the city
	private HashSet<Integer> postalNumbers = new HashSet<Integer>();
		
	//TODO Fucking bad programming - this boolean only exists to ensure the creation of the other fields - FIX!
	@SuppressWarnings("unused")
	private static boolean initilalized = createCities();
	
	
	/**
	 * Creates a City with the input cityName and one of its postal numbers
	 * @param cityName The name of the City
	 * @param postalNumber A postal number of the city
	 */
	private City(String cityName, Integer postalNumber)
	{
		this.cityName = cityName;
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
			if(city.getCityName().equals(cityName))
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
	 * Adds a postal number to the City's current postal numbers
	 * @param postalNumberToAdd the postal number to add
	 */
	private void addPostalNumberToCity(Integer postalNumberToAdd)
	{ postalNumbers.add(postalNumberToAdd); }
	
	/**
	 * Finds the City with the given postal number
	 * @param postalNumber the City's postal number
	 * @return The City with the corresponding postal number - returns null, if nothing could be found.
	 */
	public static City getCityByPostalNumber(int postalNumber)
	{ return cityHashMap.get(postalNumber); }
	
	/**
	 * Finds the name of the City with the given postal number
	 * @param postalNumber the name of the City with the given postal number
	 * @return the City's name - if the postal number is 0, it will return "CITY POSTAL NUMBER DOES NOT BELONG TO A CITY"
	 */
	public static String getCityNameByPostalNumber(int postalNumber)
	{
		if(postalNumber == 0)
			return "CITY POSTAL NUMBER DOES NOT BELONG TO A CITY";
		else
			return cityHashMap.get(postalNumber).getCityName();
	}
	
	/**
	 * Finds the City with the given name - HAVE TO SPELL THE CITY NAME 100% RIGHT IN ORDER FOR THIS TO WORK
	 * @param cityName the wanted City's name
	 * @return the City with the corresponding name - if no City could be found, null is returned
	 */
	public static City getCityByCityName(String cityName)
	{
		for(City city : allCitiesList)
			if(city.getCityName().equals(cityName))
				return city;
		
		return null;
		/* TODO Implementer binarySearch for swag's skyld - ellers lad v�r
		int resultAt = Arrays.binarySearch(allCitiesListArray, cityName);
		return allCitiesListArray[resultAt];
		*/
	}
	
	/**
	 * Creates all of the Cities
	 * @return true if the creation went well
	 */
	private static boolean createCities()
	{
		try {				
			//ArrayList<City> allCitiesList = new ArrayList<City>();

			File file = new File(cityFileName);
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line;
			String[] lineParts;
			Integer postalNumber; 
			String cityName;
			City city;

			while((line = reader.readLine()) != null)
			{
				//TODO Hvis nogen har en id� til en regex, der kun splitter op til f�rste space -s� skriv den her og erstat den der for i = 2 l�kke med noget andet, flottere
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
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	public static void main(String[] args) {
		
		System.out.println(allCitiesList.size());
		for(City city : allCitiesList)
		{
			System.out.println("Cityname: " + city.getCityName());
			Iterator<Integer> iterator = city.getCityPostalNumbers().iterator();
			while(iterator.hasNext())
				System.out.print(iterator.next() + ", ");
			System.out.println("");
		}
		
		System.out.println(getCityByPostalNumber(1100).getCityName());
		System.out.println(getCityByPostalNumber(9990).getCityName());	
	}
}