package pes00038.cs.stir.ac.uk.dissertation.MutationOperators;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.City;
import pes00038.cs.stir.ac.uk.dissertation.Route;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class DifferentRouteMutation extends AdaptiveMutation {

	public Solution mutate(Solution sol) {
		
		City[] currentCities = sol.getGenome();
		int totalCities = currentCities.length;
		
		HashMap<Integer,List<Route>> routes;
		List<Route> routesOnDay = null;
		int point = -1;
		
		while(routesOnDay==null)
		{
			point = ThreadLocalRandom.current().nextInt(totalCities-2);
			City randomCity = currentCities[point];
			
			routes = randomCity.getRoutes();
			
			if(routes != null)
			{
				routesOnDay = routes.get(point+1);
			}
		}
		
		int randomRoute = ThreadLocalRandom.current().nextInt(routesOnDay.size());
		
		City randomTarget = routesOnDay.get(randomRoute).getTo();
		
		if(!randomTarget.isDeadEnd(point+1) && !randomTarget.getName().equals(currentCities[point+1].getName()))
		{
			int newCityIndex = -1;
			City temp = currentCities[point+1];
			City newCity = routesOnDay.get(randomRoute).getTo();
					
			for(int i = 1; i<currentCities.length; i++)
			{
				if(currentCities[i].getArea().getName().equals(newCity.getArea().getName()))
				{
					newCityIndex = i;
					break;
				}
			}
			
			currentCities[point+1] = newCity;
			
			if(newCityIndex>-1)
				currentCities[newCityIndex] = temp;

		}
		
		return sol;
		
	}


}
