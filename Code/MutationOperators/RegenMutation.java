package pes00038.cs.stir.ac.uk.dissertation.MutationOperators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import pes00038.cs.stir.ac.uk.dissertation.City;
import pes00038.cs.stir.ac.uk.dissertation.Route;
import pes00038.cs.stir.ac.uk.dissertation.Solution;

public class RegenMutation extends AdaptiveMutation {
	
	public Solution mutate(Solution sol) {
		
		City[] currentCities = sol.getGenome();
		int totalCities = currentCities.length;
		
		int point = randomCityIndex(currentCities);
		
		ArrayList<String> areasInSolution = new ArrayList<String>();
		
		areasInSolution.add(currentCities[0].getArea().getName());
		
		for(int i = 1; i<totalCities; i++)
		{
			if(i<point)
			{
				areasInSolution.add(currentCities[i].getArea().getName());
				continue;
			}
			
			City previousCity = currentCities[i-1];
			
			HashMap<Integer, List<Route>> routesAvailable = previousCity.getRoutes();
									
			List<Route> routesOnDay = routesAvailable.get(i);
			
			if(routesOnDay==null)
			routesOnDay = new ArrayList<Route>();
			
			int numberOfRoutesOnDay = routesOnDay.size();
			
			if(i<totalCities-1)
			{
				List<Route> routesToNewDestinations = new ArrayList<Route>();
				
				for(int j = 0; j<numberOfRoutesOnDay; j++)
				{
					Route connection = routesOnDay.get(j);
					String connectionAreaName = connection.getTo().getArea().getName();
					if(!connection.getTo().isDeadEnd(i+1) && !areasInSolution.contains(connectionAreaName))
						routesToNewDestinations.add(connection);
				}
				
				if(routesToNewDestinations.size()>0)
				{
					Route randomRoute = null;
					
					if(routesToNewDestinations.size()>1)
					{
						double totalInverseRouteCost = 0;
						for(Route route : routesToNewDestinations)
							totalInverseRouteCost += 1.0/(Math.pow(route.getCost(), 2.75));
						
						double randomTarget = ThreadLocalRandom.current().nextDouble(totalInverseRouteCost);
						double current = 0;
						
						for(Route route : routesToNewDestinations)
						{
							current+= 1.0/(Math.pow(route.getCost(), 2.75));;
							if(current>=randomTarget)
							{
								randomRoute = route;
								break;
							}
						}	
					}	
					
					if(randomRoute == null)
					{
						int randomRouteIndex = ThreadLocalRandom.current().nextInt(routesToNewDestinations.size());
						randomRoute = routesToNewDestinations.get(randomRouteIndex);
					}

					currentCities[i] = randomRoute.getTo();
					areasInSolution.add(currentCities[i].getArea().getName());
				}
				
				else
				{				
					assert(i>1);

					boolean canContinue = false;
					int swapWith = -1;
					
					while(!canContinue)
					{
						swapWith = ThreadLocalRandom.current().nextInt(1, i);
						
						List<Route> routes = currentCities[swapWith].getRoutes().get(i);
						if(routes==null) continue;
						
						for(Route r : routes)
						{
							if(!r.getTo().isDeadEnd(i) && !areasInSolution.contains(r.getTo().getArea().getName()))
							{
//								routesInSolution.add(r);
								canContinue = true;
								break;
							}
						}
					}

					currentCities[i-1] = currentCities[swapWith];
					currentCities[swapWith] = previousCity;
					
					i--;
					continue;
				}
				
			}

			else
			{
				List<Route> routesToDestinationArea = new ArrayList<Route>();
				
				for(int j = 0; j<numberOfRoutesOnDay; j++)
				{
					Route connection = routesOnDay.get(j);
					String connectionAreaName = connection.getTo().getArea().getName();
					
					if(currentCities[0].getArea().getName().equals(connectionAreaName))
						routesToDestinationArea.add(connection);
					
				}
				
				if(routesToDestinationArea.size()>0)
				{
					int randomRouteIndex = ThreadLocalRandom.current().nextInt(routesToDestinationArea.size());
					currentCities[i] = routesToDestinationArea.get(randomRouteIndex).getTo();
				}
				else
				{	
					City[] legalDestinations = currentCities[0].getArea().getCities();

					List<String> waysToFinish = new ArrayList<String>();

					for (City c : legalDestinations)
						waysToFinish.addAll(c.getReachedOnDay(i));

					boolean foundSwap = false;
					int swapIndex = i - 1;

					int start = ThreadLocalRandom.current().nextInt(1, currentCities.length - 1);

					if (start == swapIndex)
						foundSwap = true;

					for (int j = start; j < currentCities.length-1; j++) {
						
						if(foundSwap)
							break;
						
						if (waysToFinish.contains(currentCities[j].getName())) {
							foundSwap = true;
							swapIndex = j;
						}
					}

					for (int j = 1; j < start; j++) {
						
						if(foundSwap)
							break;
						
						if (waysToFinish.contains(currentCities[j].getName())) {
							foundSwap = true;
							swapIndex = j;
						}
					}

					if (swapIndex != i - 1) {
						City temp = currentCities[i - 1];
						currentCities[i - 1] = currentCities[swapIndex];
						currentCities[swapIndex] = temp;
						i--;
					}
					
					else
					{
						int randomRouteIndex = ThreadLocalRandom.current().nextInt(legalDestinations.length);
						currentCities[i] = legalDestinations[randomRouteIndex];
					}
					
				}
				
			}
			
			
		}

//		sol.setGenome(currentCities);
		return sol;
	}

	private int randomCityIndex(City[] chooseFromCities)
	{
		int firstCity = ThreadLocalRandom.current().nextInt(1, chooseFromCities.length-1);
		int secondCity = ThreadLocalRandom.current().nextInt(1, chooseFromCities.length-1);
		
		int firstRouteCost = Integer.MAX_VALUE;
		int secondRouteCost = Integer.MAX_VALUE; 

		if(!chooseFromCities[firstCity].isDeadEnd(firstCity))
		{
			for(Route route : chooseFromCities[firstCity].getRoutes().get(firstCity))
			{
				if(route.getTo().getName().equals(chooseFromCities[firstCity+1].getName()))
				{
					firstRouteCost = route.getCost();
					break;
				}
			}
		}
		
		if(!chooseFromCities[secondCity].isDeadEnd(secondCity))
		{
			for(Route route : chooseFromCities[secondCity].getRoutes().get(secondCity))
			{
				if(route.getTo().getName().equals(chooseFromCities[secondCity+1].getName()))
				{
					secondRouteCost = route.getCost();
					break;
				}
			}
		}
		
		//Return with HIGHER cost -> higher probability of swapping higher-cost routes
		return firstRouteCost>=secondRouteCost ? firstCity : secondCity;
	}

}
