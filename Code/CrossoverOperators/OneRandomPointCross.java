package pes00038.cs.stir.ac.uk.dissertation.CrossoverOperators;

import java.util.concurrent.ThreadLocalRandom;

import Unused.AdaptiveCrossover;
import pes00038.cs.stir.ac.uk.dissertation.City;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class OneRandomPointCross extends AdaptiveCrossover {

	public Solution crossover(Solution solution1, Solution solution2) {
							
		City[] solution1Cities = solution1.getGenome();
		City[] solution2Cities = solution2.getGenome();
		City[] childCities;
						
		int crossoverPoint = ThreadLocalRandom.current().nextInt(solution1Cities.length);
		
		childCities = new City[solution1Cities.length];
		System.arraycopy(solution1Cities, 0, childCities, 0, crossoverPoint);
		System.arraycopy(solution2Cities, crossoverPoint, childCities, crossoverPoint, solution2Cities.length-crossoverPoint);
			
		Solution childSolution = new Solution(childCities);
		
		return childSolution;
	}

}
