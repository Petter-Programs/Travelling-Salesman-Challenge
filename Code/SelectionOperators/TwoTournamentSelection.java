package pes00038.cs.stir.ac.uk.dissertation.SelectionOperators;

import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.Population;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class TwoTournamentSelection implements Selection {

	//Choose two random members of a population and return the more fit one
	public Solution select(Population population, int avoidIndex, double time) {
		
		Solution[] individuals = population.getIndividuals();
		
		int first;
		int second;
		
		do
			first = ThreadLocalRandom.current().nextInt(individuals.length);
		while(first==avoidIndex);
		
		do
			second = ThreadLocalRandom.current().nextInt(individuals.length);
		while(second==avoidIndex);
		
		
		if(individuals[first].getPrice()<individuals[second].getPrice())
			return individuals[first];
		return individuals[second];
	}

	
	
}
