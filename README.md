### The Kiwi.com Traveling Salesman Challenge 
In 2018, travel agency website Kiwi.com launched the second edition of its 'Traveling Salesman Challenge'
(TSC), in which programmers were invited to compete to find the cheapest possible route to visit a set of
provided areas. As the name suggests, the challenge is a variation of the classic TSP which directly
relates to the website's own travel search engine 'NOMAD' where users are able to pick a set of areas
they would like to visit under a set time period and be provided with the cheapest possible airports and
routes to take which the system was able to find.

Although the competition itself has long since ended, the still publicly available datasets provide an
excellent opportunity for experimentation. This is in part because the way the challenge is set up is by
design highly transferrable to real life and includes some realistic constraints which are not present in the
original TSP. Additionally, benchmark performances from the competition and existing solutions can be
used for comparison to tell whether or not the algorithm is effective; due to the NP-hard nature of the
problem, it is often unknown whether current solutions are the true best possible solutions. The TSC
provides 14 datasets, which all vary slightly in terms of their structure (number of airports per area,
sparseness of routes etc.). The datasets are divided into two parts; the publicly available "development
datasets" used by contestants to design their algorithms, and the "evaluation datasets", hidden to
contestants, which were used by the contest organizers to further evaluate the algorithms.


### Explanation of Rules
The goal of the TSC is to identify an ordering of cities which minimizes the cost of travel between a set of
areas and returns to the starting area. The solution identified must be feasible, meaning that there exists
a route for every adjacent city on the day of the route, where the day is represented by the city's index
(relative to other cities) starting from 1. For example, for a solution ABC to be feasible, the route AB must
exists on day 1, and the route BC must exist on day 2.

The TSC distinguishes between cities and areas; each area can contain multiple cities, in which only one
city must be visited. In the example solution ABC, this means that city C must belong to the same area as
city A to be valid. Although the TSC does not contain information about airports, the term is sometimes
used interchangeably with cities in the context of the TSC. A route is simply information about two cities,
the day in which the route exists, and the price/cost of the route. Price information is given as an integer
with no unit attached.

The problem dataset provided by Kiwi.com contains information about how many areas should be
visited, which cities are part of what areas, and the legal routes available between cities on specific days,
including the cost of taking the specific route. A few additional rules were also specified on the (no
longer available, but archived) contest website:
* "The trip must start from the city we give you"
* "In each area, you must visit exactly one city (but you can choose which one)"
* "You have to move between areas every day"
* "The trip must continue from the same city in which you arrived"
* "The entire trip should end in the area where it began"

Notice the distinction between areas and cities; a valid solution can start in one airport of an area, and
end in another airport as long as it belongs to the same area. However, a solution would be invalid if it
required the switching of an airport to continue the journey.

### Directions for Use of Code
Use the Settings.java class to adjust parameters as desired

To compile, import code into an IDE such as Eclipse and export into a runnable Jar file, using the Main.java class as the main class.

To run using a command line such as Terminal:
java -jar [Your compiled file].jar

You can specify debug directory and dataset path as arguments (both required if one is used):
java -jar [Your compiled file].jar [path to folder for debug] [path to dataset file to use]

To run in automatic mode (for a number of times set in Settings.java)
java -jar [Your compiled file].jar [path to folder for debug] [path to dataset file to use] auto

Datasets used can be downloaded from
https://code.kiwi.com/articles/travelling-salesman-challenge-2-0-wrap-up/

