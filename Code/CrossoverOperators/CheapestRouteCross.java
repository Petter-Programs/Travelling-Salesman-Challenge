package pes00038.cs.stir.ac.uk.dissertation.CrossoverOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.Area;
import pes00038.cs.stir.ac.uk.dissertation.City;
import pes00038.cs.stir.ac.uk.dissertation.Loader;
import pes00038.cs.stir.ac.uk.dissertation.Route;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class CheapestRouteCross implements Crossover {

	public Solution crossover(Solution solution1, Solution solution2) {
		
		City[] solution1Cities = solution1.getGenome();
		City[] solution2Cities = solution2.getGenome();
		
		City[] childCities = new City[solution1.getGenome().length];
		int size = solution1Cities.length;
		
		if(solution1.getPrice()==solution2.getPrice() && solution1.isTheSameAs(solution2))
		return new Solution(solution1.getGenome().clone());
		
		ArrayList<String> areasAlreadyAdded = new ArrayList<String>();
		ArrayList<String> unusedAreas = new ArrayList<String>();
		
		for(Area area : Loader.getAllAreas())
			unusedAreas.add(area.getName());
		
		//Starting city
		childCities[0] = solution1Cities[0];
		
		//Ending city from random parent
		if(ThreadLocalRandom.current().nextInt(1)==0)
			childCities[size-1] = solution1Cities[size-1];
		else
			childCities[size-1] = solution2Cities[size-1];
		
		areasAlreadyAdded.add(childCities[0].getArea().getName());
		unusedAreas.remove(childCities[0].getArea().getName());
		
	    int parent1RepairPointer = 0;
	    int parent2RepairPointer = 0;
		
	    for(int i = 0; i < size; i++)
	    {
//	    	String debugString = "";
//	    	for(int k = 0; k<childCities.length; k++)
//	    	{
//	    		if(childCities[k]==null)
//	    			debugString += " (NULL)";
//	    			
//	    			else
//	    				debugString += childCities[k].getName();
//	    	}
//	    	Debug.printAndWrite(debugString);
	    	
	    	int j = size-i-1;
	    	
	    	//Move repair pointers
	    	if(childCities[i] == null || childCities[j] == null)
	    	{		    	
    			while(parent1RepairPointer < size && areasAlreadyAdded.contains(solution1Cities[parent1RepairPointer].getArea().getName()))
    				parent1RepairPointer++;

    			while(parent1RepairPointer >= size && parent2RepairPointer < size && areasAlreadyAdded.contains(solution2Cities[parent2RepairPointer].getArea().getName()))
    				parent2RepairPointer++;
	    	}
	    	
	    	///REPAIR LEFT SIDE PART
	    	if(childCities[i] == null)
	    	{
		    	City childCity = null;
		    	
    			if(parent1RepairPointer < size)
    			{
    				childCity = solution1Cities[parent1RepairPointer];
    				parent1RepairPointer++;
    			}
    			
    			else if(parent2RepairPointer< size)
    			{
    				childCity = solution2Cities[parent2RepairPointer];
    				parent2RepairPointer++;
    			}
    			
    			if(childCity == null)
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
    			    			
    			childCities[i] = childCity;
    			areasAlreadyAdded.add(childCity.getArea().getName());
    			unusedAreas.remove(childCity.getArea().getName());
	    	}
	    	
	    	///REPAIR RIGHT SIDE PART
	    	if(childCities[j] == null)
	    	{
		    	City childCity = null;
		    	
    			if(parent1RepairPointer < size)
    				childCity = solution1Cities[parent1RepairPointer];
    			
    			else if(parent2RepairPointer< size)
    				childCity = solution2Cities[parent2RepairPointer];
    			
    			if(childCity == null)
    			{
    	    		City randomCity = null;
    	    		int randomAreaIndex = -1;
    	    		
    	    		while(randomCity == null || randomCity.isDeadEnd(j))
    	    		{
    		    		randomAreaIndex = ThreadLocalRandom.current().nextInt(unusedAreas.size());
    		    		Area randomArea = Loader.getAreaFromName(unusedAreas.get(randomAreaIndex));
    		    		
    		    		int randomCityIndex = ThreadLocalRandom.current().nextInt(randomArea.getCities().length);
    		    		randomCity = randomArea.getCities()[randomCityIndex];
    	    		}
    	    		
    	    		childCity = randomCity;
    			}
    			    			
    			childCities[j] = childCity;
    			areasAlreadyAdded.add(childCity.getArea().getName());
    			unusedAreas.remove(childCity.getArea().getName());
	    	}
	    	
	    	///END REPAIR PART
	    	
	    	if(i==j)
	    		break;
	    	
	    	///Add a city to the left
	    	City parent1Next = solution1Cities[i+1];
	    	City parent2Next = solution2Cities[i+1];
	    	
	    	String parent1NextCityString = parent1Next.getName();
	    	String parent2NextCityString = parent2Next.getName();
	    	
	    	String parent1NextAreaString = parent1Next.getArea().getName();
	    	String parent2NextAreaString = parent2Next.getArea().getName();
	    	
	    	boolean randomlyAdd = ThreadLocalRandom.current().nextInt(size)<size/10;
	    	
	    	if(parent1NextCityString.equals(parent2NextCityString) && !randomlyAdd)
	    	{
	    		childCities[i+1] = parent1Next;
	    		areasAlreadyAdded.add(parent1NextAreaString);
	    		unusedAreas.remove(parent1NextAreaString);
	    	}
	    	else if((areasAlreadyAdded.contains(parent1NextAreaString) && areasAlreadyAdded.contains(parent2NextAreaString) 
	    			&& i<size-1) || randomlyAdd)
	    	{
	    		List<Route> rs = childCities[i].getRoutes().get(i+1);
	    		
	    		if(rs == null)
	    			continue;
	    		
	    		ArrayList<Route> shuffledRoutes = new ArrayList<Route>(rs);
	    		Collections.shuffle(shuffledRoutes);
	    		
	    		for(Route r : shuffledRoutes)
	    		{
	    			if(!areasAlreadyAdded.contains(r.getTo().getArea().getName())
	    					&& !r.getTo().getArea().getName().equals(childCities[0].getArea().getName()))
	    			{
		    			childCities[i+1] = r.getTo();
		    			areasAlreadyAdded.add(r.getTo().getArea().getName());
		    			unusedAreas.remove(r.getTo().getArea().getName());
		    			break;
	    			}
	    		}    		
	    	}
	    	else if(i<size-1 && areasAlreadyAdded.contains(parent1NextAreaString))
	    	{
	    		childCities[i+1] = parent2Next;
	    		areasAlreadyAdded.add(parent2NextAreaString);
	    		unusedAreas.remove(parent2NextAreaString);
	    	}
	    	else if(i<size-1 && areasAlreadyAdded.contains(parent2NextAreaString))
	    	{
	    		childCities[i+1] = parent1Next;
	    		areasAlreadyAdded.add(parent1NextAreaString);
	    		unusedAreas.remove(parent1NextAreaString);
	    	}
	    	else
	    	{
	    		List<Route> routes = childCities[i].getRoutes().get(i+1);
	    		
	    		if(routes==null)
	    			continue;
	    		
	    		int costToParent1City = Integer.MAX_VALUE;
	    		int costToParent2City = Integer.MAX_VALUE;
	    		
	    		for(Route r : routes)
	    		{
	    			if(r.getTo().getName().equals(parent1NextCityString))
	    				costToParent1City = r.getCost();
	    			
	    			else if(r.getTo().getName().equals(parent2NextCityString))
	    				costToParent2City = r.getCost();
	    				
	    				if(costToParent1City < Integer.MAX_VALUE && costToParent2City < Integer.MAX_VALUE)
	    					break;
	    		}
	    		
	    		if(costToParent1City == Integer.MAX_VALUE && costToParent2City == Integer.MAX_VALUE)
	    		{
		    		List<Route> rs = childCities[i].getRoutes().get(i+1);
		    		
		    		if(rs == null)
		    			continue;
		    		
		    		ArrayList<Route> shuffledRoutes = new ArrayList<Route>(rs);
		    		Collections.shuffle(shuffledRoutes);
		    		
		    		for(Route r : shuffledRoutes)
		    		{
		    			if(!areasAlreadyAdded.contains(r.getTo().getArea().getName())
		    					&& !r.getTo().getArea().getName().equals(childCities[0].getArea().getName()))
		    			{
			    			childCities[i+1] = r.getTo();
			    			areasAlreadyAdded.add(r.getTo().getArea().getName());
			    			unusedAreas.remove(r.getTo().getArea().getName());
			    			break;
		    			}
		    		}
	    		}
	    		
	    		else
	    		{
	    			if(costToParent2City==Integer.MAX_VALUE)
	    			{
		    			childCities[i+1] = parent1Next;
		    			areasAlreadyAdded.add(parent1NextAreaString);
		    			unusedAreas.remove(parent1NextAreaString);
	    			}
	    			else if(costToParent1City==Integer.MAX_VALUE)
	    			{
		    			childCities[i+1] = parent2Next;
		    			areasAlreadyAdded.add(parent2NextAreaString);
		    			unusedAreas.remove(parent2NextAreaString);
	    			}
	    			else
	    			{
	    				double sumOfDifferences = 1.0/(costToParent1City+costToParent2City);
	    				double randomTarget = ThreadLocalRandom.current().nextDouble(sumOfDifferences);
	    				
	    				if((1.0/costToParent1City)>randomTarget)
	    				{
			    			childCities[i+1] = parent1Next;
			    			areasAlreadyAdded.add(parent1NextAreaString);
			    			unusedAreas.remove(parent1NextAreaString);
	    				}
	    				else
	    				{
			    			childCities[i+1] = parent2Next;
			    			areasAlreadyAdded.add(parent2NextAreaString);
			    			unusedAreas.remove(parent2NextAreaString);
	    				}
	    			}
	    		}
	    	}
	    	
	    	//Add a city to the right
	    	City wantToGoTo = childCities[j];
	    	
	    	City solution1Prev = solution1Cities[j-1];
	    	City solution2Prev = solution2Cities[j-1];
	    	
	    	List<String> possibleCities = wantToGoTo.getReachedOnDay(j+1);
	    	
	    	boolean canGoToOne = true;
	    	boolean canGoToTwo = true;
	    	
	    	if(!possibleCities.contains(solution1Prev.getName())
	    			|| areasAlreadyAdded.contains(solution1Prev.getArea().getName()))
	    		canGoToOne = false;
	    	
	    	if(!possibleCities.contains(solution2Prev.getName())
	    			|| areasAlreadyAdded.contains(solution2Prev.getArea().getName()))
	    		canGoToTwo = false;
	    	
	    	boolean randomlyAdd1 = ThreadLocalRandom.current().nextInt(size)<size/10;
	    	
	    	if(canGoToOne && canGoToTwo && !randomlyAdd1)
	    	{
	    		List<Route> routesFromParent1 = solution1Prev.getRoutes().get(j);
	    		List<Route> routesFromParent2 = solution2Prev.getRoutes().get(j);

	    		int costToParent1City = Integer.MAX_VALUE;
	    		int costToParent2City = Integer.MAX_VALUE;
	    		
	    		for(Route r : routesFromParent1)
	    		{
	    			if(r.getTo().getName().equals(wantToGoTo.getName()))
	    			{
	    				costToParent1City = r.getCost();
	    				break;
	    				
	    			}
	    		}
	    		
	    		for(Route r : routesFromParent2)
	    		{
	    			if(r.getTo().getName().equals(wantToGoTo.getName()))
	    			{
	    				costToParent2City = r.getCost();
	    				break;
	    				
	    			}
	    		}
	    		
				double sumOfDifferences = 1.0/(costToParent1City+costToParent2City);
				double randomTarget = ThreadLocalRandom.current().nextDouble(sumOfDifferences);
				
				if((1.0/costToParent1City)>randomTarget)
				{
	    			childCities[j-1] = solution1Prev;
	    			areasAlreadyAdded.add(solution1Prev.getArea().getName());
	    			unusedAreas.remove(solution1Prev.getArea().getName());
				}
				else
				{
	    			childCities[j-1] = solution2Prev;
	    			areasAlreadyAdded.add(solution1Prev.getArea().getName());
	    			unusedAreas.remove(solution1Prev.getArea().getName());
				}
	    	}
	    	else if(canGoToOne)
	    	{
    			childCities[j-1] = solution1Prev;
    			areasAlreadyAdded.add(solution1Prev.getArea().getName());
    			unusedAreas.remove(solution1Prev.getArea().getName());
	    	}
	    	else if(canGoToTwo)
	    	{
    			childCities[j-1] = solution2Prev;
    			areasAlreadyAdded.add(solution1Prev.getArea().getName());
    			unusedAreas.remove(solution1Prev.getArea().getName());
	    	}
	    	else
	    	{
	    		ArrayList<String> shuffledPossibleCities = new ArrayList<String>(possibleCities);
	    		Collections.shuffle(shuffledPossibleCities);
	    		
	    		for(String possibleCity : shuffledPossibleCities)
	    		{
	    			City city = Loader.getCityFromName(possibleCity);
	    			Area area = city.getArea();
	    			if(unusedAreas.contains(area.getName()))
	    			{
	        			childCities[j-1] = city;
	        			areasAlreadyAdded.add(area.getName());
	        			unusedAreas.remove(area.getName());
	        			break;
	    			}
	    		}
	    	}
	    }
	    
	    return new Solution(childCities);    
	}

}
