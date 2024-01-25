package pes00038.cs.stir.ac.uk.dissertation;

import pes00038.cs.stir.ac.uk.dissertation.CrossoverOperators.*;
import pes00038.cs.stir.ac.uk.dissertation.MutationOperators.*;
import pes00038.cs.stir.ac.uk.dissertation.SelectionOperators.*;

public class Main {
	
private static boolean automaticMode;
	
	public static void main(String[] args)
	{
		if(args.length>0)
		{
			if(args.length==3 && args[2].equalsIgnoreCase("auto"))
			{				
				automaticMode = true;
				Debug.setAutomaticMode(true);
			}
			else if(args.length!=2)
			{
				System.out.println("Usage: [TSP].jar [debugDirectory] [dataSetPath] [opt: auto]");
				return;
			}
			
			String debugPath = args[0];
			String dataSetPath = args[1];
			
			Debug.setDebugPath(debugPath);
			Loader.setDataSetPath(dataSetPath);
		}
		
		Debug.startDebugger();
		
		Debug.printAndWrite("Loading cities");
		
		if(Loader.loadFromFile()) 
		{
			City startingCity = Loader.getStartingCity();
			int areasToVisit = Loader.getAreasToVisit();
						
			if(automaticMode)
			{
				int secondsToRun = Settings.TIME_TO_RUN;
				int populationSize = Settings.POPULATION_SIZE;								
				Crossover currentCrossover = Settings.CROSSOVER_TO_USE;
				Selection currentSelection = Settings.SELECTION_TO_USE;
				Mutation currentMutation = Settings.MUTATION_TO_USE;
				
				int minimumMutations = Settings.MINIMUM_MUTATIONS;
				int maximumMutations = Settings.MAXIMUM_MUTATIONS;
				
				Debug.writeAutomaticFeedback("Dataset,Mutation,Crossover,Selection,Iteration,Legal,Price");
				
					for(int i = 0; i<Settings.AUTOMATIC_MODE_RUNS; i++)
					{	
					
						if(currentMutation.getClass().getSimpleName().equals(AdaptiveMutationSet.class.getSimpleName()))
							currentMutation = new AdaptiveMutationSet();
								
						GeneticAlgorithm GA = new GeneticAlgorithm(populationSize, currentSelection, currentMutation, currentCrossover);
						GA.setNumberOfMutations(minimumMutations, maximumMutations);
						GA.runGeneticAlgorithm(areasToVisit, startingCity, secondsToRun);
						Solution best = GA.getBestIndividual();
						String feedback = Loader.getDataSetName() + "," + currentMutation.getClass().getSimpleName() + "," + currentCrossover.getClass().getSimpleName() + "," + currentSelection.getClass().getSimpleName() + "," + i + "," + best.isValid() + "," + best.getPrice();
						Debug.writeAutomaticFeedback(feedback);
					}	
			}
			
			else
			{
			
				GeneticAlgorithm GA = new GeneticAlgorithm(Settings.POPULATION_SIZE, Settings.SELECTION_TO_USE, Settings.MUTATION_TO_USE, Settings.CROSSOVER_TO_USE);				
				GA.setNumberOfMutations(Settings.MINIMUM_MUTATIONS, Settings.MAXIMUM_MUTATIONS);
				GA.runGeneticAlgorithm(areasToVisit, startingCity, Settings.TIME_TO_RUN); 

				Solution bestIndividual = GA.getBestIndividual();
				
				Debug.printAndWrite("Best solution: " + bestIndividual + ", cost: " + bestIndividual.getPrice());
				
				Evaluator evaluator = new Evaluator();
				
				Debug.printAndWrite("Manual check for fitness: " + evaluator.getTotalCost(bestIndividual));
				Debug.printAndWrite("Is valid? " + evaluator.isValidSolution(bestIndividual));
				
				Debug.printAndWrite("History: " + GA.getCostHistory());
				
			}
			
			Debug.closeDebugger();
			
		}

	}

}
