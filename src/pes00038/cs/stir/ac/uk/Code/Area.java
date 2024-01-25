package pes00038.cs.stir.ac.uk.dissertation;

public class Area {
	
	private String areaName;
	private City[] citiesInArea;
	
	private int citiesAdded;
	
	public Area(String areaName, int numberOfCities)
	{
		this.areaName = areaName;
		citiesInArea = new City[numberOfCities];
	}
	
	public void addCity(City toAdd)
	{
		citiesInArea[citiesAdded] = toAdd;
		citiesAdded++;
	}
	
	public City[] getCities()
	{
		return citiesInArea;
	}
	
	public String getName()
	{
		return areaName;
	}
	
}
