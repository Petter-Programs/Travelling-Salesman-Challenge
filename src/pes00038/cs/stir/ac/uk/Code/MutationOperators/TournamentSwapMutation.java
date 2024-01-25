package pes00038.cs.stir.ac.uk.dissertation.MutationOperators;

import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.City;
import pes00038.cs.stir.ac.uk.dissertation.Route;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class TournamentSwapMutation extends AdaptiveMutation {

	public Solution mutate(Solution sol) {
		
		City[] currentCities = sol.getGenome();

		// Generate two random points that gravitate towards being higher cost and make sure they are distinct
		int point1 = randomCityIndex(currentCities);
		int point2;
		
		do 
		point2 = randomCityIndex(currentCities);
		
		while(point1==point2);
		
		// Swap the cities
		City tempCity = currentCities[point1];
		currentCities[point1] = currentCities[point2];
		currentCities[point2] = tempCity;
		
		return sol;
		
	}
	
	private int randomCityIndex(City[] chooseFromCities)
	{
		int firstCity = ThreadLocalRandom.current().nextInt(1, chooseFromCities.length-1);
		int secondCity = ThreadLocalRandom.current().nextInt(1, chooseFromCities.length-1);
		
		int firstRouteCost = Integer.MAX_VALUE;
		int secondRouteCost = Integer.MAX_VALUE; 

		if(!chooseFromCities[firstCity].isDeadEnd(firstCity))
		{
			for(Route route : chooseFromCities[firstCity].getRoutes().get(firstCity))
			{
				if(route.getTo().getName().equals(chooseFromCities[firstCity+1].getName()))
				{
					firstRouteCost = route.getCost();
					break;
				}
			}
		}
		
		if(!chooseFromCities[secondCity].isDeadEnd(secondCity))
		{
			for(Route route : chooseFromCities[secondCity].getRoutes().get(secondCity))
			{
				if(route.getTo().getName().equals(chooseFromCities[secondCity+1].getName()))
				{
					secondRouteCost = route.getCost();
					break;
				}
			}
		}
		
		//Return with HIGHER cost -> higher probability of swapping higher-cost routes
		return firstRouteCost>=secondRouteCost ? firstCity : secondCity;
	}
	
}
