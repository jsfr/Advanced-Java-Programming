package assignment1;

import java.util.List;

public class AggregationSequential implements Aggregation {

	@Override
	public int aggregate(Combination c, List<Employee> employees) {
		int result = 0;
		for (Employee e : employees) {
			result = c.combine(c.projectInt(e), result);
		}
		return result;
	}

}
