package pes00038.cs.stir.ac.uk.dissertation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class City {
	
	private Area inArea;
	private String cityName;
	
	private HashMap<Integer, List<Route>> routesPerDay; //TreeMap sorted by keys
	private HashMap<Integer, List<String>> reachedPerDay;
	
	public City(String cityName, Area inArea)
	{
		routesPerDay = new HashMap<Integer, List<Route>>();
		reachedPerDay = new HashMap<Integer, List<String>>();
		this.cityName = cityName;
		this.inArea = inArea;
	}

	public String getName()
	{
		return cityName;
	}
	
	public Area getArea()
	{
		return inArea;
	}
	
	public void addRoute(Route route)
	{
		int day = route.getDay();
		
		if(!routesPerDay.containsKey(day))
		{
			List<Route> routeList = new ArrayList<Route>();
			routesPerDay.put(day, routeList);
		}
		
		routesPerDay.get(day).add(route);
	}
		
	public HashMap<Integer, List<Route>> getRoutes()
	{
		return routesPerDay;
	}
	
	public Boolean isDeadEnd(int day)
	{
		return !routesPerDay.containsKey(day);
	}
	
	public void reachedOnDay(int day, City city)
	{		
		if(!reachedPerDay.containsKey(day))
		{
			List<String> cities = new ArrayList<String>();
			reachedPerDay.put(day, cities);
		}
		
		reachedPerDay.get(day).add(city.getName());
	}
	
	public List<String> getReachedOnDay(int day)
	{
		return reachedPerDay.get(day);
	}
}
