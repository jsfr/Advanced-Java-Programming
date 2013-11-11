package assignment1;

import java.util.List;

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
