package pes00038.cs.stir.ac.uk.dissertation.SelectionOperators;

import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.Population;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class NTournamentSelection implements Selection {

	private final int N = 4;
	
	//Choose N random members of a population and return the more fit one
	public Solution select(Population population, int avoidIndex, double time) {
		
		Solution[] individuals = population.getIndividuals();
		
		int leastCostIndex = 0;
		int leastCost = Integer.MAX_VALUE;
		
		for(int i = 0; i<N; i++)
		{
			int randomIndividual;
			
			do
				randomIndividual = ThreadLocalRandom.current().nextInt(individuals.length);
			while(randomIndividual==avoidIndex);
			
			if(individuals[randomIndividual].getPrice()<leastCost)
			{
				leastCostIndex = randomIndividual;
				leastCost = individuals[randomIndividual].getPrice();
			}
		}
		
		return individuals[leastCostIndex];
	}

	
	
}
