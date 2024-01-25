package pes00038.cs.stir.ac.uk.dissertation.SelectionOperators;

import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.Population;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class RandomSelection implements Selection {

	//Choose a random member of the population
	public Solution select(Population population, int avoidIndex, double time) {
		
		Solution[] individuals = population.getIndividuals();
		
		int choice;
		
		do
			choice = ThreadLocalRandom.current().nextInt(individuals.length);

		while(choice == avoidIndex);
		
		return individuals[choice];
	}

	
	
}
