package pes00038.cs.stir.ac.uk.dissertation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Loader {

	private static String DATA_PATH = Settings.DATASET_PATH;

	private static FileReader file;
	private static BufferedReader reader;
	
	private static Boolean isLoaded;

	private static HashMap<String, City> cities;
	private static City startingCity;
	
	private static HashSet<Area> areasFound = new HashSet<Area>();
	
	private static int areasToVisit;
	
	private static String datasetName;
		
	private static void loadData()
	{
		try 
		{
			file = new FileReader(DATA_PATH);
			datasetName = new File(DATA_PATH).getName();
			reader = new BufferedReader(file);
			isLoaded = true;
			
			System.out.println("Loaded file");
		}
		catch (Exception e)
		{
			Debug.printAndWrite("Something went wrong loading file");
		}
	}
	
	private static void readCitiesAndAreas(int areasToVisit) throws IOException
	{
		//AREA, CITY LOADING
		
			for(int i = 0; i<areasToVisit; i++)
			{
				String areaName = reader.readLine().trim();
				String[] cityList = reader.readLine().split(" ");
				int numberOfCities = cityList.length;
				
				Area area = new Area(areaName, numberOfCities);
				
				areasFound.add(area);
				
				for(String cityName : cityList)
				{
					cityName = cityName.trim();
					City newCity = new City(cityName, area);
					area.addCity(newCity);
					
					Debug.writeDebug("Add new city \"" + cityName + "\" , from city: \"" + newCity.getName() + "\"");
					
					cities.put(cityName, newCity);

				}
				
			}	

	}

	private static void readRoutes() throws IOException
	{
			String routeString = reader.readLine();
			
			while(routeString != null)
			{
				String[] routeInfo = routeString.split(" ");
				String fromCityString = routeInfo[0];
				String toCityString = routeInfo[1];
				int day = Integer.parseInt(routeInfo[2]);
				int cost = Integer.parseInt(routeInfo[3]);
				
				City fromCity = cities.get(fromCityString);
				City toCity = cities.get(toCityString);
				
				
				Debug.writeDebug("Route from \"" + fromCityString + "\" to \"" + toCityString + "\"");
				Debug.writeDebug("From city loaded: " + fromCity.getName());
				Debug.writeDebug("To city loaded: " + toCity.getName());
				
				if(day>0)
				{
					toCity.reachedOnDay(day+1, fromCity);
					Route newRoute = new Route(fromCity, toCity, day, cost);
					fromCity.addRoute(newRoute);
				}
				else
				{
					for(int i = 1; i<=areasToVisit; i++)
					{
						toCity.reachedOnDay(i, fromCity);
						Route newRoute = new Route(fromCity, toCity, i, cost);
						fromCity.addRoute(newRoute);
					}
				}


				routeString = reader.readLine();
			}

	}

	
	
	private static void readData()
	{
		
		if(!isLoaded)
		return;
		
		cities = new HashMap<String, City>();
				
		try {
			
			String firstLine = reader.readLine();
			String[] firstLineData = firstLine.split(" ");
			
			areasToVisit = Integer.parseInt(firstLineData[0]);
			String startingCityString = firstLineData[1];
						
			//AREA, CITY LOADING
			readCitiesAndAreas(areasToVisit);
			
			//ROUTE LOADING
			readRoutes();
			
			startingCity = cities.get(startingCityString);
			
			System.out.println("Read data from file");
			
		} catch (IOException e) {
			Debug.printAndWrite("Something went wrong reading data: " + e);
			cities = null;
		}
	}
	
	public static Boolean loadFromFile()
	{
		loadData();
		readData();
		return isLoaded && cities!=null;
	}
	
	public static HashMap<String, City> getCities()
	{
		return cities;
	}
	
	public static City getStartingCity()
	{
		return startingCity;
	}
	
	public static HashSet<Area> getAllAreas()
	{
		return areasFound;
	}
	
	public static int getAreasToVisit()
	{
		return areasToVisit;
	}
	
	public static City getCityFromName(String cityName)
	{
		return cities.get(cityName);
	}
	
	public static Area getAreaFromName(String areaName)
	{
		for(Area area : areasFound)
			if(area.getName().equals(areaName))
				return area;
		
		return null;
	}
	
	public static void setDataSetPath(String dataSetPath)
	{
		DATA_PATH = dataSetPath;
	}
	
	public static String getDataSetName()
	{
		return datasetName;
	}
	
	public static boolean oneAirportPerArea()
	{
		return cities.size() == areasToVisit;
	}
	
}
