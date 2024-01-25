package pes00038.cs.stir.ac.uk.dissertation.SelectionOperators;

import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.Population;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class SimulatedAnnealingSelection implements Selection {
	
	public Solution select(Population population, int avoidIndex, double time) {
				
		Solution[] individuals = population.getIndividuals();
		int individualsToConsider = (int) (individuals.length * time);
		
		if(individualsToConsider<2)
			individualsToConsider = 2;
		
		int first;
		int second;
		
		do
			first = ThreadLocalRandom.current().nextInt(individualsToConsider);
		while(first==avoidIndex);
		
		do
			second = ThreadLocalRandom.current().nextInt(individualsToConsider);
		while(second==avoidIndex);
		
		
		if(individuals[first].getPrice()<individuals[second].getPrice())
			return individuals[first];
		return individuals[second];
	}

	
	
}
