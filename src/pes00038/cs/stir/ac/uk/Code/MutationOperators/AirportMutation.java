package pes00038.cs.stir.ac.uk.dissertation.MutationOperators;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.City;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class AirportMutation extends AdaptiveMutation {

	public Solution mutate(Solution sol) {
		
		City[] currentCities = sol.getGenome();
		int totalCities = currentCities.length;

		int point = ThreadLocalRandom.current().nextInt(1, totalCities);
		
		City mutantCity = currentCities[point];
		City[] citiesInMutantArea = mutantCity.getArea().getCities();
		
		if(citiesInMutantArea.length>1)
		{
			ArrayList<City> potentialCities = new ArrayList<City>();
			for(City city : citiesInMutantArea)
			{
				if(!city.getName().equals(mutantCity.getName()) && !city.isDeadEnd(point))
					potentialCities.add(city);
			}
			
			if(potentialCities.size()>0)
			{
				int city = ThreadLocalRandom.current().nextInt(potentialCities.size());	
				currentCities[point] = potentialCities.get(city);	
			}

		}
		
		return sol;
		
	}


}
