package pes00038.cs.stir.ac.uk.dissertation;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.CrossoverOperators.*;
import pes00038.cs.stir.ac.uk.dissertation.MutationOperators.*;
import pes00038.cs.stir.ac.uk.dissertation.SelectionOperators.*;

public class GeneticAlgorithm {

	Crossover[] crossover;
	Selection selection;
	Mutation mutation;

	ArrayList<Integer> costHistory;
	Solution bestIndividual;
	
	Evaluator evaluator = new Evaluator();
	
	Population population;

	private int populationSize = 50;
	
	private int minimumNumberOfMutations = 1;
	private int maximumNumberOfMutations = 1;
	
	public GeneticAlgorithm(int popSize, Selection selection, Mutation mutation, Crossover... crossover)
	{
		this(selection, mutation, crossover);
		populationSize = popSize;
	}
	
	public GeneticAlgorithm(Selection selection, Mutation mutation, Crossover... crossover) {
		this.crossover = crossover;
		this.selection = selection;
		this.mutation = mutation;
	}
	
	public void runGeneticAlgorithm(int areasToVisit, City startFromCity, int secondsToRun) {
		long startTime = System.currentTimeMillis();
		long runTime = secondsToRun * 1000;
		long endTime = startTime + runTime;
		int iterations = 0;

		Solution[] individuals = new Solution[populationSize];
		costHistory = new ArrayList<Integer>();
		
		for (int i = 0; i < populationSize; i++) {
						
			Solution newSolution = new Solution(areasToVisit, startFromCity);		
//			Solution newSolution = new Solution(Loader.getAreasToVisit(), startFromCity);

			int solPrice = evaluator.getTotalCost(newSolution);

			newSolution.setPrice(solPrice);
			
			individuals[i] = newSolution;			
		}

		Debug.writeDebug("Generated population in... " + (System.currentTimeMillis() - startTime) + "ms");
		
		population = new Population(individuals);
		
		population.sortPopulation();
		
		String diversityDebugInitial = "Initial diversity: ";
		
		for(Solution sol : population.getIndividuals())
		{
			diversityDebugInitial += sol.getPrice() + " ";
		}
		Debug.printAndWrite(diversityDebugInitial);
		
		costHistory.add(individuals[0].getPrice());
		
		bestIndividual = individuals[0];
		
		Debug.printAndWrite("Best initial solution was " + bestIndividual.getPrice());

		population.calculateTotalFitness();
				
		while (System.currentTimeMillis() < endTime) {
			
			int numberOfMutationsLeft = ThreadLocalRandom.current().nextInt(minimumNumberOfMutations, maximumNumberOfMutations+1);
			
			double timePassed = (double) (System.currentTimeMillis()-startTime)/runTime;
			double timeLeft = 1.0 - timePassed;
					
			Solution parent1 = selection.select(population, -1, timeLeft);
			Solution parent2 = selection.select(population, population.getSolutionIndex(parent1), timeLeft);
			
			Solution childSolution = crossover[0].crossover(parent1, parent2);
			
			int childSolutionPrice = evaluator.getTotalCost(childSolution);
			childSolution.setPrice(childSolutionPrice);
			
			if(Settings.MUTATE_CHILD && numberOfMutationsLeft>0)
			{
				mutation.mutate(childSolution);
				childSolutionPrice = evaluator.getTotalCost(childSolution);
				childSolution.setPrice(childSolutionPrice);
				numberOfMutationsLeft--;
			}

			int location = individuals.length-1;
			population.insertIndividual(childSolution, location);
			
			while(numberOfMutationsLeft>0)
			{
				population.mutateRandomly(mutation, selection, timeLeft);
				numberOfMutationsLeft--;
			}
						
			population.sortPopulation();

			bestIndividual = individuals[0];
			
			int bestIndividualFitness = bestIndividual.getPrice();
			if (costHistory.get(costHistory.size() - 1) != bestIndividualFitness)
			{
				Debug.printAndWrite("New best solution at iteration " + iterations + "! Now: " + bestIndividualFitness);
				costHistory.add(bestIndividualFitness);
			}
			iterations++;
		}
		
		String diversityDebug = "Final diversity: ";
		
		for(Solution ind : population.getIndividuals())
		{
			diversityDebug += ind.getPrice() + " ";
		}
		
		Debug.printAndWrite(diversityDebug);
				
		Debug.printAndWrite("Ran for " + iterations + " generations");

	}
	
	public void setNumberOfMutations(int minimumNumber, int maximumNumber)
	{
		minimumNumberOfMutations = minimumNumber;
		maximumNumberOfMutations = maximumNumber;
	}
	
	public Solution getBestIndividual() {
		return bestIndividual;
	}

	public ArrayList<Integer> getCostHistory() {
		return costHistory;
	}
}
