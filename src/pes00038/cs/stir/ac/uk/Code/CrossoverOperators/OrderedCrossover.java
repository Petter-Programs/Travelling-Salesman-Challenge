package pes00038.cs.stir.ac.uk.dissertation.CrossoverOperators;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import Unused.AdaptiveCrossover;
import pes00038.cs.stir.ac.uk.dissertation.City;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class OrderedCrossover extends AdaptiveCrossover {

	public Solution crossover(Solution solution1, Solution solution2) {

		// Assumes all areas are in parent solutions
		
		City[] solution1Cities = solution1.getGenome();
		City[] solution2Cities = solution2.getGenome();
		City[] childCities = new City[solution1Cities.length];
		int size = solution1Cities.length;
		
		if(solution1.getPrice()==solution2.getPrice() && solution1.isTheSameAs(solution2))
			return new Solution(solution1.getGenome().clone());
		
		int startPoint = ThreadLocalRandom.current().nextInt(size);
		int endPoint = ThreadLocalRandom.current().nextInt(size-startPoint)+startPoint;
		
		System.arraycopy(solution1Cities, startPoint, childCities, startPoint, endPoint-startPoint + 1);
		
		ArrayList<String> areasAlreadyAdded = new ArrayList<String>();
		
	    int solution2Index = 0;
	    
	    for (int i = 0; i < size; i++) {
	    
	        if (i < startPoint || i > endPoint) {
	        	
	        	while(areasAlreadyAdded.contains(solution2Cities[solution2Index].getArea().getName()))
	        		solution2Index++;
	        	
	        	childCities[i] = solution2Cities[solution2Index];
	        }
	        else if (i>0)
	        	areasAlreadyAdded.add(solution1Cities[i].getArea().getName());
	    	
	    }

		Solution childSolution = new Solution(childCities);
		
		return childSolution;
	}
	
}
