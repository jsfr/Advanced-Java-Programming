package assignment1;

import java.util.List;

public class AggregationParallel implements Aggregation {

	@Override
	public int aggregate(Combination c, List<Employee> employees) {
		if (employees.isEmpty()) {
			return 0;
		} else {
			Integer x = new Integer(0);
			Thread t = new Thread(new AggregationRunnable(c, employees, x));
			t.start();
			try {
				t.join();
				return x.intValue();
			} catch (InterruptedException e) {
				return 0;
			}
		}
	}
}
