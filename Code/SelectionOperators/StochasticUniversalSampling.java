package pes00038.cs.stir.ac.uk.dissertation.SelectionOperators;

import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.Debug;
import pes00038.cs.stir.ac.uk.dissertation.Population;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class StochasticUniversalSampling implements Selection {

	//Choose a random member of the population, in a biased way towards cheaper solutions
	public Solution select(Population population, int avoidIndex, double time) {
		
		Solution[] individuals = population.getIndividuals();
		double target = ThreadLocalRandom.current().nextDouble(population.getTotalInverseFitness());
		double current = 0;
		
		for(int i = 0; i<individuals.length; i++)
		{
			Solution ind = individuals[i];
			current+=(1.0/ind.getPrice());
			
			if(current>=target)
			{
				if(i==avoidIndex)
				{
					if(i>0)
						i--;
					else
						i++;
					
					ind = individuals[i];
				}
				
				Debug.writeDebug("SUS: Chose individual no. " + i);
				return ind;
			}
		}
		
		Debug.printAndWrite("Stochastic universal sampling somehow exited loop");
		return individuals[individuals.length-1];

	}

	
	
}
