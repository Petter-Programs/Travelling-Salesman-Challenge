package pes00038.cs.stir.ac.uk.dissertation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Evaluator {

	public static final int NOT_ALL_DESTINATIONS_PENALTY = 1_000_000; //Not all destinations are in Solution
	public static final int ILLEGAL_ROUTE_PENALTY = 1_000_000; //A route specified was illegal
	public static final int WRONG_DESTINATION_PENALTY = 1_000_000; //The solution ended in the wrong destination
	
	public int getTotalCost(Solution solution)
	{
		HashSet<String> areasFound = new HashSet<String>();
		
		int totalCost = 0;
		City[] genes = solution.getGenome();
		
		City startCity = solution.getStartCity();
		
		String startAreaName = startCity.getArea().getName(); 
		String finishAreaName = genes[genes.length-1].getArea().getName();
		
		//This should never happen, unless there is a bug
		if(!startCity.getName().equals(genes[0].getName()))
		{
			Debug.printAndWrite("WARNING Actual start city was different from required start city");
			Debug.printAndWrite("Expected " + startCity.getName() + " got " + genes[0].getName());
		}
		
		if(!startAreaName.equals(finishAreaName))
		totalCost += WRONG_DESTINATION_PENALTY;
		
		for(int i = 0; i<genes.length-1; i++)
		{
			int day = i+1;
//			FIRST DAY IS 1
			
			City currentCity = genes[i];
			City nextCity = genes[i+1];
			
			Area currentArea = currentCity.getArea();
			
			boolean alreadyExist = !areasFound.add(currentArea.getName());
			
			// Re-visited an area
			if(alreadyExist)
			{
				totalCost += NOT_ALL_DESTINATIONS_PENALTY;
				Debug.writeDebug("Evaluator: an area already existed (" + currentArea.getName() + ")");
			}
			
			HashMap<Integer, List<Route>> routes = currentCity.getRoutes();
			
			Boolean legal = false;
			
			if(routes.containsKey(day))
			{
				List<Route> routesOnDay = routes.get(day);
				for(Route route : routesOnDay)
				{
					if(route.getTo().getName().equals(nextCity.getName()))
					{
						totalCost+=route.getCost();
						legal = true;
						break;
					}
				}
			}
			
			if(!legal)
			{
				totalCost += ILLEGAL_ROUTE_PENALTY;
				Debug.writeDebug("Evaluator: illegal route from " + currentCity.getName() + " to " + nextCity.getName() + " on " + day);				
			}
			
		}
		
		return totalCost;
		
	}
	
	/*
	 * Check whether or not a solution is valid, that is;
	 * It starts in the correct area
	 * It ends in the area it started
	 * It never visits an area twice, except the starting area
	 * Each city visited has an available route on the day to the next city in the genome
	 */
	public boolean isValidSolution(Solution solution)
	{
		HashSet<String> areasFound = new HashSet<String>();
		
		City[] genes = solution.getGenome();
		City startCity = solution.getStartCity();
		
		String startAreaName = startCity.getArea().getName(); 
		String finishAreaName = genes[genes.length-1].getArea().getName();
		
		if(!startCity.getName().equals(genes[0].getName()))
			return false;
		
		if(!startAreaName.equals(finishAreaName))
			return false;
		
		//Loop through every city except the last one to verify that a route exists
		for(int i = 0; i<genes.length-1; i++)
		{
			int day = i+1;
			
			City currentCity = genes[i];
			City nextCity = genes[i+1];

			Area currentArea = currentCity.getArea();
			
			boolean alreadyExist = !areasFound.add(currentArea.getName());
			
			// Area (not including last) was already in solution
			if(alreadyExist)
				return false;
			
			HashMap<Integer, List<Route>> routes = currentCity.getRoutes();
			
			Boolean legal = false;
			
			if(routes.containsKey(day))
			{
				List<Route> routesOnDay = routes.get(day);
				for(Route route : routesOnDay)
				{
					if(route.getTo().getName().equals(nextCity.getName()))
					{
						legal = true;
						break;
					}
				}
			}
			
			if(!legal)
				return false;
			
		}
		
		// All checks passed!
		return true;
	}
	
}
