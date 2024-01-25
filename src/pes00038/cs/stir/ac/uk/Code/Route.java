package pes00038.cs.stir.ac.uk.dissertation;

public class Route {
	
	private City fromCity;
	private City toCity;
	
	private int day;
	private int cost;
	
	public Route(City fromCity, City toCity, int day, int cost)
	{
		this.fromCity = fromCity;
		this.toCity = toCity;
		this.day = day;
		this.cost = cost;
	}
	
	public int getDay()
	{
		return day;
	}
	
	public City getTo()
	{
		return toCity;
	}
	
	public City getFrom()
	{
		return fromCity;
	}
	
	public int getCost()
	{
		return cost;
	}
	
	public Boolean isTheSameAs(Route route)
	{
		return route.getDay()==day && route.getFrom()==fromCity && route.getTo()==toCity;
	}
	
	@Override
	public String toString()
	{
		return "D" + day + " " + fromCity.getName() + "->" + toCity.getName();
	}
	
}
