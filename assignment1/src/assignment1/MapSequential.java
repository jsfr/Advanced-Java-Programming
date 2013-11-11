package assignment1;

import java.util.List;

public class MapSequential implements Map {

	@Override
	public void map(Mutation m, List<Employee> l) {
		for (Employee e : l) {
			m.mutate(e);
		}
	}

}
