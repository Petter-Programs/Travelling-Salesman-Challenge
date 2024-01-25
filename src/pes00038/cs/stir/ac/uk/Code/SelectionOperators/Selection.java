package pes00038.cs.stir.ac.uk.dissertation.SelectionOperators;

import pes00038.cs.stir.ac.uk.dissertation.Solution;
import pes00038.cs.stir.ac.uk.dissertation.Population;

public interface Selection {

	public Solution select(Population population, int avoidIndex, double time);

}
