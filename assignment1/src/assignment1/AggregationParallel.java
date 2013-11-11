package assignment1;

import java.util.List;

/**
 * Aggregation in parallel. Creates threads for each call to combinator via 
 * aggregationRunnable.
 * Returns result of aggregation over list of employees with the given 
 * combination.
 * @author Jens Fredskov, Henrik Bendt
 *
 */

public class AggregationParallel implements Aggregation {

	@Override
	public int aggregate(Combination c, List<Employee> employees) {
		if (employees.isEmpty()) {
			return 0;
		} else {
			MutableInt x = new MutableInt(0);
			Thread t = new Thread(new AggregationRunnable(c, employees, x));
			t.start();
			try {
				t.join();
				return x.getInt();
			} catch (InterruptedException e) {
				return 0;
			}
		}
	}
}
