package pes00038.cs.stir.ac.uk.dissertation.MutationOperators;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.City;
import pes00038.cs.stir.ac.uk.dissertation.Route;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

//Improved-Efficiency Tournament Seeker Mutation
public class LegalSwapMutation extends AdaptiveMutation {
	
	public Solution mutate(Solution sol) {

		City[] currentCities = sol.getGenome();
		
		boolean foundSwap = false;
		int swapIndex = -1;
		
		int randomIndex = randomCityIndex(currentCities);

		int noSwapCount = 0;
		
		while(!foundSwap)
		{			
			noSwapCount++;
			if(noSwapCount>100)
				return sol;
			
			City currentCity = currentCities[randomIndex];
			City nextCity = currentCities[randomIndex+1];
			
			//Clone to avoid modifying the original list
			List<String> waysToReachNextList = nextCity.getReachedOnDay(randomIndex+1);
			
			if(waysToReachNextList!=null)
			{
				ArrayList<String> waysToReachNext = new ArrayList<String>(waysToReachNextList);
				
				waysToReachNext.remove(currentCity.getName());
	
				int start = ThreadLocalRandom.current().nextInt(1, currentCities.length - 1);
				
				for (int j = start; j < currentCities.length-1; j++) {
					
					if(foundSwap)
						break;
					
					if (waysToReachNext.contains(currentCities[j].getName())) {
						
	//					ArrayList<String> waysToReachSwapNext = new ArrayList<String>(currentCities[j+1].getReachedOnDay(j+1));
	//					if(waysToReachSwapNext.contains(currentCity.getName()))
	//					{
							foundSwap = true;
							swapIndex = j;
	//					}
					}
				}
	
				for (int j = 1; j < start; j++) {
					
					if(foundSwap)
						break;
					
					if (waysToReachNext.contains(currentCities[j].getName())) {
						
						foundSwap = true;
						swapIndex = j;
						
					}
				}
			
			}
			
			if(!foundSwap)
				randomIndex = randomCityIndex(currentCities);
			
		}
		
		City temp = currentCities[randomIndex];
		currentCities[randomIndex] = currentCities[swapIndex];
		currentCities[swapIndex] = temp;

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
