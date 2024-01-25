package pes00038.cs.stir.ac.uk.dissertation.MutationOperators;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AdaptiveMutation implements Mutation {

	private int recentSuccessCount;
	
	private Queue<Integer> magnitudesAdded = new LinkedList<Integer>();
	private double sumOfAllMagnitudes;
	
	private String iterationDebug = "";
	
	public int getMagnitudesCount()
	{
		return magnitudesAdded.size();
	}
	
	public double getExpectedMagnitude()
	{
		if(magnitudesAdded.size()>0)
		return sumOfAllMagnitudes/magnitudesAdded.size();
		return 0;
	}
	
	public void usedAtIteration(int iteration)
	{
		iterationDebug+=","+iteration;
	}
	
	public String getUsedAtIterations()
	{
		return iterationDebug;
	}
	

	public void thisMutationSucceeded(int magnitude, int maxCount)
	{
		if(magnitudesAdded.size()>maxCount)
		{
			sumOfAllMagnitudes-=magnitudesAdded.remove();
		}
		magnitudesAdded.add(magnitude);
		recentSuccessCount++;
	}
	
	public void otherMutationSucceded()
	{
		if(recentSuccessCount>0)
			recentSuccessCount--;
		
		if(recentSuccessCount<magnitudesAdded.size())
			removeMagnitudeValue();

	}
	
	public void removeMagnitudeValue()
	{
		sumOfAllMagnitudes-=magnitudesAdded.remove();	
	}
	
}
