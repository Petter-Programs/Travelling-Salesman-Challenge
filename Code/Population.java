package pes00038.cs.stir.ac.uk.dissertation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

import pes00038.cs.stir.ac.uk.dissertation.MutationOperators.Mutation;
import pes00038.cs.stir.ac.uk.dissertation.SelectionOperators.Selection;

public class Population {
	
	private Solution[] individuals;
	
	private int totalFitness;
	private double totalInverseFitness;
	
	private Evaluator eval = new Evaluator();

	public Population(Solution[] individuals)
	{
		this.individuals = individuals;
	}
	
	public Solution[] getIndividuals()
	{
		return individuals;
	}
	
	public void calculateTotalFitness()
	{
		totalFitness=0;
		totalInverseFitness=0;
		for(Solution ind : individuals)
		{
			totalFitness+=ind.getPrice();
			totalInverseFitness+=(1.0/ind.getPrice());
		}
	}
	
	public int getTotalFitness()
	{
		return totalFitness;
	}
	
	public double getTotalInverseFitness()
	{
		return totalInverseFitness;
	}
	
	public void mutateRandomly(Mutation mut, Selection sel, double time)
	{	
		//Avoid mutating the best individual
		Solution ind = sel.select(this, 0, time);
		
		int price = ind.getPrice();
		
		mut.mutate(ind);
		
		int newPrice = eval.getTotalCost(ind);
		ind.setPrice(newPrice);
		
		updateFitnessCalcs(price, newPrice);	
	}
	
	public int getNumberOfUniqueIndividuals()
	{
		HashSet<Integer> seen = new HashSet<Integer>();
		
		for(Solution ind : individuals)
		{
			seen.add(ind.getPrice());
		}
		
		return seen.size();
	}
	
	public void mutateDuplicates(Mutation mut)
	{
		HashSet<Integer> seen = new HashSet<Integer>();
		
		for(Solution ind : individuals)
		{

			int price = ind.getPrice();
			int priceApprox = price; //(int) (Math.round(price/1.0) * 1.0);
			
			if(!seen.add(priceApprox))
			{
				mut.mutate(ind);
				int newPrice = eval.getTotalCost(ind);
				ind.setPrice(newPrice);
				
				updateFitnessCalcs(price, newPrice);
			}
		}
	}
	
	private void updateFitnessCalcs(int oldFitness, int newFitness)
	{
		totalFitness-=oldFitness;
		totalInverseFitness-=1.0/oldFitness;
		totalFitness+=newFitness;
		totalInverseFitness+=1.0/newFitness;
	}
	
	
	public void insertIndividual(Solution ind, int position)
	{
		Solution oldInd = individuals[position];
		int oldPrice = oldInd.getPrice();
		
		updateFitnessCalcs(oldPrice, ind.getPrice());
		
		individuals[position] = ind;
	}
	
	public int getSolutionIndex(Solution sol)
	{
		for(int i = 0; i<individuals.length; i++)
		{
			if(sol.isTheSameAs(individuals[i]))
			return i;
		}
		return -1;
	}
	
	public double getAverageFitness()
	{
		return totalFitness / individuals.length;
	}
	
	// Sorts the population in ascending order of cost
	public void sortPopulation() {
		Arrays.sort(individuals, new Comparator<Solution>() {
			@Override
			public int compare(Solution individual1, Solution individual2) {
				return individual1.getPrice() - individual2.getPrice();
			}
		});
	}
	

}
