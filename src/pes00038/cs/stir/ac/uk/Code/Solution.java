package pes00038.cs.stir.ac.uk.dissertation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Solution {
	
	private City[] genes;
	private final City startFrom;
	
	private int price;

	private HashSet<Area> areasToVisit;
	
		
	public Solution(int areasToVisit, City startFrom) {
		genes = new City[areasToVisit + 1];
		genes[0] = startFrom;
		this.startFrom = startFrom;
		initializeRandomSolution();
	}

	public Solution(HashSet<Area> areasToVisit, City startFrom) {
		this.startFrom = startFrom;
		this.areasToVisit = areasToVisit;
		genes = new City[areasToVisit.size() + 1];
		initializeCompletelyRandomSolution();
	}

	public Solution(City[] baseGenome) {
		genes = baseGenome;
		this.startFrom = genes[0];
	}
	
	public void setGenome(City[] baseGenome)
	{
		genes = baseGenome;
	}

	public City[] getGenome() {
		return genes;
	}

	public City getStartCity() {
		return startFrom;
	}
	
	private void initializeCompletelyRandomSolution() {
		genes[0] = startFrom;

		City[] legalDestinations = startFrom.getArea().getCities();
		int randomIndex = ThreadLocalRandom.current().nextInt(legalDestinations.length);
		genes[genes.length - 1] = legalDestinations[randomIndex];

		ArrayList<String> areasInSolution = new ArrayList<String>();
		areasInSolution.add(startFrom.getArea().getName());

		ArrayList<Area> areasNotInSolution = new ArrayList<Area>();

		for (Area area : areasToVisit) {
			if (!area.getName().equals(startFrom.getArea().getName()))
				areasNotInSolution.add(area);
		}

		for (int i = 1; i < genes.length - 1; i++) {

			int randomAreaIndex = ThreadLocalRandom.current().nextInt(areasNotInSolution.size());
			Area randomArea = areasNotInSolution.get(randomAreaIndex);
			City[] citiesInArea = randomArea.getCities();

			int randomCityIndex = ThreadLocalRandom.current().nextInt(citiesInArea.length);
			City randomCity = citiesInArea[randomCityIndex];
			
			while(randomCity.isDeadEnd(i))
			{
				randomCityIndex = ThreadLocalRandom.current().nextInt(citiesInArea.length);
				randomCity = citiesInArea[randomCityIndex];
			}
			
			genes[i] = citiesInArea[randomCityIndex];
		}

	}
	
	private boolean initializeRandomSolution() {
		ArrayList<String> areasInSolution = new ArrayList<String>();
		
		areasInSolution.add(genes[0].getArea().getName());

		for (int i = 1; i < genes.length; i++) {
			City previousCity = genes[i - 1];

			HashMap<Integer, List<Route>> routesAvailable = previousCity.getRoutes();

			List<Route> routesOnDay = routesAvailable.get(i);

			assert (routesOnDay != null);

			int numberOfRoutesOnDay = routesOnDay.size();

			if (i < genes.length - 1) {
				List<Route> routesToNewDestinations = new ArrayList<Route>();

				for (int j = 0; j < numberOfRoutesOnDay; j++) {
					Route connection = routesOnDay.get(j);
					String connectionAreaName = connection.getTo().getArea().getName();
					if (!connection.getTo().isDeadEnd(i + 1) && !areasInSolution.contains(connectionAreaName))
					{
						routesToNewDestinations.add(connection);
					}
				}
				
				if (routesToNewDestinations.size() > 0) {
					
					int randomRouteIndex = ThreadLocalRandom.current().nextInt(routesToNewDestinations.size());
					Route randomRoute = routesToNewDestinations.get(randomRouteIndex);	
					genes[i] = randomRoute.getTo();
					areasInSolution.add(genes[i].getArea().getName());
				}

				else {
						
					Debug.writeDebug("No good options, shuffling!");

					assert (i > 1);

					boolean canContinue = false;
					int swapWith = -1;

					while (!canContinue) {
						swapWith = ThreadLocalRandom.current().nextInt(1, i);

						List<Route> routes = genes[swapWith].getRoutes().get(i);

						for (Route r : routes) {
							
							if (!r.getTo().isDeadEnd(i) && !areasInSolution.contains(r.getTo().getArea().getName())) {
								
								canContinue = true;
								break;
							}
						}

					}

					genes[i - 1] = genes[swapWith];
					genes[swapWith] = previousCity;

					i--;
				}

			}

			else {
				List<Route> routesToDestinationArea = new ArrayList<Route>();

				for (int j = 0; j < numberOfRoutesOnDay; j++) {
					Route connection = routesOnDay.get(j);
					String connectionAreaName = connection.getTo().getArea().getName();

					if (startFrom.getArea().getName().equals(connectionAreaName))
						routesToDestinationArea.add(connection);
				}
				if (routesToDestinationArea.size() > 0) {
					int randomRouteIndex = ThreadLocalRandom.current().nextInt(routesToDestinationArea.size());
					genes[i] = routesToDestinationArea.get(randomRouteIndex).getTo();
				} else {
					
					City[] legalDestinations = startFrom.getArea().getCities();

					List<String> waysToFinish = new ArrayList<String>();

					for (City c : legalDestinations)
						waysToFinish.addAll(c.getReachedOnDay(i));

					boolean foundSwap = false;
					int swapIndex = i - 1;

					int start = ThreadLocalRandom.current().nextInt(1, genes.length - 1);

					if (start == swapIndex)
						foundSwap = true;

					for (int j = start; j < genes.length-1; j++) {
						
						if(foundSwap)
							break;
						
						if (waysToFinish.contains(genes[j].getName())) {
							foundSwap = true;
							swapIndex = j;
						}
					}

					for (int j = 1; j < start; j++) {
						
						if(foundSwap)
							break;
						
						if (waysToFinish.contains(genes[j].getName())) {
							foundSwap = true;
							swapIndex = j;
						}
					}

					if (swapIndex != i - 1) {
						City temp = genes[i - 1];
						genes[i - 1] = genes[swapIndex];
						genes[swapIndex] = temp;
						i--;
					}
					
					else
					{
						int randomRouteIndex = ThreadLocalRandom.current().nextInt(legalDestinations.length);
						genes[i] = legalDestinations[randomRouteIndex];
					}
					
				}

			}			
		}
		return true;
	}
	
	public boolean isValid()
	{
		return price<Evaluator.ILLEGAL_ROUTE_PENALTY; 
	}
	
	public int getPrice()
	{
		return price;
	}
	
	public void setPrice(int newPrice) 
	{
		price = newPrice;
	}
	
	public boolean isTheSameAs(Solution sol)
	{	
		for(int i = 0; i<genes.length; i++)
		{
			if(!sol.genes[i].getName().equals(genes[i].getName()))
				return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		String solutionString = "";

		for (City city : genes) {
			solutionString += city.getName() + "->";
		}
		solutionString += "FIN";

		return solutionString;

	}

}
