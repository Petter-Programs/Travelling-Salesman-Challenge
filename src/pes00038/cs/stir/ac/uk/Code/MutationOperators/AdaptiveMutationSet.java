package pes00038.cs.stir.ac.uk.dissertation.MutationOperators;

import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.City;
import pes00038.cs.stir.ac.uk.dissertation.Evaluator;
import pes00038.cs.stir.ac.uk.dissertation.Loader;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class AdaptiveMutationSet implements Mutation {
	
	private int MINIMUM_EXPLORATION = 50;
	private int MAXIMUM_EXPLORATION = 100;
	
	private int explorationProbability = 100;
	private int iteration = 0;
	
	private int successCount = 0;
	
	private int recentSuccessRunsToConsider = 50;
	
	private int no_improvement = 0;
	
	private AdaptiveMutation[] mutationSet = { new AirportMutation(), new LegalSwapMutation(),
			new TournamentSwapMutation(),  new DifferentRouteMutation(), new RegenMutation()};

	private AdaptiveMutation[] mutationSetNoAirports = { new LegalSwapMutation(),
			new TournamentSwapMutation(), new DifferentRouteMutation(), new RegenMutation()};
	
	public AdaptiveMutationSet()
	{
		if(Loader.oneAirportPerArea())
			mutationSet = mutationSetNoAirports;
	}
		
	public Solution mutate(Solution sol) {
				
		Evaluator eval = new Evaluator();
		
		int originalCost = sol.getPrice();
		
		City[] cloneSol = sol.getGenome().clone();
		
		AdaptiveMutation currentMut = chooseMutation();
		
		currentMut.usedAtIteration(iteration);
		iteration++;
		
		currentMut.mutate(sol);
		
		int newCost = eval.getTotalCost(sol);
		
		if(originalCost>newCost)
		{
			no_improvement = 0;
			decreaseExplorationProbability();
			int magnitude = originalCost-newCost;
			currentMut.thisMutationSucceeded(magnitude, recentSuccessRunsToConsider);
			
			if(successCount >= recentSuccessRunsToConsider)
			{
				for(AdaptiveMutation mut : mutationSet)
				{
					if(!mut.getClass().getSimpleName().equals(currentMut.getClass().getSimpleName()))
						mut.otherMutationSucceded();
				}
			}
			else
				successCount++;
		}
		else
		{			
			no_improvement++;
			
			if(newCost>=Evaluator.ILLEGAL_ROUTE_PENALTY)
			sol.setGenome(cloneSol);
		}
		
		if(no_improvement%50 == 0)
			increaseExplorationProbability();
		
		return sol;

	}
	

	private AdaptiveMutation chooseMutation()
	{
		boolean exploit = ThreadLocalRandom.current().nextInt(100) > explorationProbability;
		
		if(exploit)
		{
			double expectedRates = 0;
			for(AdaptiveMutation amut : mutationSet)
			{
				double expectedImprovement = amut.getMagnitudesCount() * amut.getExpectedMagnitude();
				expectedRates += expectedImprovement;
			}
			if(expectedRates>0)
			{
				double randomRate = ThreadLocalRandom.current().nextDouble(expectedRates);
				
				for(AdaptiveMutation amut : mutationSet)
				{
					randomRate += amut.getMagnitudesCount() * amut.getExpectedMagnitude();
					if(randomRate>=expectedRates)
					{
						return amut;
					}
				}
			}
		}
		
		int randomMut = ThreadLocalRandom.current().nextInt(mutationSet.length);
		return mutationSet[randomMut];
	}
	
	public void setMutationSet(AdaptiveMutation[] mutations)
	{
		mutationSet = mutations;
	}
	
	public void setMinimumExplorationProbability(int probability)
	{
		MINIMUM_EXPLORATION = probability;
	}
	
	private void increaseExplorationProbability()
	{
		explorationProbability = explorationProbability+5>=MAXIMUM_EXPLORATION ? MAXIMUM_EXPLORATION : explorationProbability+5;
	}
	
	private void decreaseExplorationProbability()
	{
		explorationProbability = explorationProbability/2>=MINIMUM_EXPLORATION ? explorationProbability/2 : MINIMUM_EXPLORATION;
	}
	
	public String getUsageDebug()
	{
		String debugString = "";
		for(AdaptiveMutation mut : mutationSet)
		{
			debugString += "$" + mut.getClass().getSimpleName();
						
			debugString+=mut.getUsedAtIterations();
		}
		
		return debugString;
	}
	
}
