package pes00038.cs.stir.ac.uk.dissertation.CrossoverOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import Unused.AdaptiveCrossover;
import pes00038.cs.stir.ac.uk.dissertation.Area;
import pes00038.cs.stir.ac.uk.dissertation.City;
import pes00038.cs.stir.ac.uk.dissertation.Loader;
import pes00038.cs.stir.ac.uk.dissertation.Route;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class OneRepairCross extends AdaptiveCrossover {

	public Solution crossover(Solution solution1, Solution solution2) {
						
		City[] solution1Cities = solution1.getGenome();
		City[] solution2Cities = solution2.getGenome();
		City[] childCities = new City[solution1Cities.length];
		int size = solution1Cities.length;
		
		if(solution1.getPrice()==solution2.getPrice() && solution1.isTheSameAs(solution2))
			return new Solution(solution1.getGenome().clone());
				
		ArrayList<Integer> possibleCrossoverPoints = new ArrayList<Integer>();
		
		// Do not choose a point too close to the edges; causes cloning
		for(int i = size/5; i<size-(size/5); i++)
		{
			List<String> reachedOnDay = solution1Cities[i+1].getReachedOnDay(i+1);
			if(reachedOnDay==null)
				continue;
			
			if(reachedOnDay.contains(solution2.getGenome()[i].getName()))
			{
				if(!solution1Cities[i+1].getName().equals(solution2Cities[i+1].getName()))
				possibleCrossoverPoints.add(i);
			}
		}
		
		int crossoverPoint;
		
		if(possibleCrossoverPoints.size()>0)
		{
			int crossoverPointSize = possibleCrossoverPoints.size();
			int crossoverPointIndex = ThreadLocalRandom.current().nextInt(crossoverPointSize);
			crossoverPoint = possibleCrossoverPoints.get(crossoverPointIndex);
		}
		else
		{
			crossoverPoint = ThreadLocalRandom.current().nextInt(size/5, size-(size/5));
//			crossoverPoint = size/2;
		}
		
		System.arraycopy(solution1Cities, 0, childCities, 0, crossoverPoint);

		ArrayList<String> areasAlreadyAdded = new ArrayList<String>();
		ArrayList<String> unusedAreas = new ArrayList<String>();
		
		for(Area area : Loader.getAllAreas())
			unusedAreas.add(area.getName());
		
	    for (int i = 0; i < size; i++) {
	    
	    	String currentParent1AreaName = solution1Cities[i].getArea().getName();
	    	String currentParent2AreaName = solution2Cities[i].getArea().getName();
	    	
	        if (i >= crossoverPoint) {
	        	
	        	if(!areasAlreadyAdded.contains(currentParent2AreaName))
	        	{
	        		 childCities[i] = solution2Cities[i];
	        		 areasAlreadyAdded.add(currentParent2AreaName);
	        		 
	        		 if(i>0)
	        		 unusedAreas.remove(currentParent2AreaName);
	        	}
	        }
	        else if (i>0)
	        {
	        	areasAlreadyAdded.add(currentParent1AreaName);	        
	        	unusedAreas.remove(currentParent1AreaName);
	        }
	    }
	    
	    
	    //Repair Part
	    
	    int parent1Pointer = 0;
	    int parent2Pointer = 0;
	    
	    mainLoop: for (int i = crossoverPoint; i < size; i++) {
	    	
	    	if(childCities[i] == null)
	    	{
		    	City childCity = null;
		    	
		    	City previousCity = childCities[i-1];
		    	
		    	HashMap<Integer,List<Route>> routesFromPrev = previousCity.getRoutes();
//		    	
		    	//Attempt to find a valid route
		    	
		    	if(routesFromPrev != null)
		    	{
		    		List<Route> routesOnDay = routesFromPrev.get(i-1);
		    		
		    		if(routesOnDay != null)
		    		{
		    			ArrayList<Route> possibleRoutes = new ArrayList<Route>(routesOnDay);
		    			Collections.shuffle(possibleRoutes);
		    			
		    			for(int j = 0; j<possibleRoutes.size(); j++)
		    			{
		    				Route randomRoute = possibleRoutes.get(j);
		    				
		    				if(unusedAreas.contains(randomRoute.getTo().getArea().getName()))
		    				{
			    				childCities[i] = randomRoute.getTo();
		    					unusedAreas.remove(randomRoute.getTo().getArea().getName());
		    					continue mainLoop;
		    				}
		    			}
		    		}
		    	}
		    	
///
		    	
		    	
		    	//Else just plug the hole
		    	
    			while(parent1Pointer < size && !unusedAreas.contains(solution1Cities[parent1Pointer].getArea().getName()))
    				parent1Pointer++;
    			
    			if(parent1Pointer < size)
    				childCity = solution1Cities[parent1Pointer];
    			
    			while(childCity == null && parent2Pointer < size && !unusedAreas.contains(solution2Cities[parent2Pointer].getArea().getName()))
    				parent2Pointer++;
    			
    			if(parent2Pointer >= size)
    			{
    	    		City randomCity = null;
    	    		int randomAreaIndex = -1;
    	    		
    	    		while(randomCity == null || randomCity.isDeadEnd(i))
    	    		{
    		    		randomAreaIndex = ThreadLocalRandom.current().nextInt(unusedAreas.size());
    		    		Area randomArea = Loader.getAreaFromName(unusedAreas.get(randomAreaIndex));
    		    		
    		    		int randomCityIndex = ThreadLocalRandom.current().nextInt(randomArea.getCities().length);
    		    		randomCity = randomArea.getCities()[randomCityIndex];
    	    		}
    	    		
    	    		childCity = randomCity;
    			}
    			
    			if(childCity == null)
    				childCity = solution2Cities[parent2Pointer];
    			
    			childCities[i] = childCity;
    			unusedAreas.remove(childCity.getArea().getName());
	    	}
	    }

		Solution childSolution = new Solution(childCities);
		return childSolution;
	}

}
