package assignment1;

import java.util.List;

/**
 * Maps the given mutation on the given list of employees.
 * Runs sequential.
 * @author Jens Fredskov, Henrik Bendt
 *
 */

public class MapSequential implements Map {

	@Override
	public void map(Mutation m, List<Employee> l) {
		for (Employee e : l) {
			m.mutate(e);
		}
	}

}
