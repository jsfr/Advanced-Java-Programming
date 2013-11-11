package assignment1;

import java.util.List;

/**
 * Runnable class used by aggregationParallel for threads. 
 * Use divide and conquer where the list is divided into 2 sublists 
 * which are given to 2 new threads. These are combined via the combination.
 * Initialized with the combinator, list of employees and a mutable integer, 
 * where the result is saved.
 * @author Jens Fredskov, Henrik Bendt
 * 
 */

public class AggregationRunnable implements Runnable {

	private Combination combination;
	private List<Employee> list;
	public MutableInt result;

	public AggregationRunnable(Combination c, List<Employee> e, MutableInt i) {
		this.combination = c;
		this.list = e;
		this.result = i;
	}
	
	@Override
	public void run() {
		int length = list.size();
		if (length > 2) {
			MutableInt x = new MutableInt(0);
			MutableInt y = new MutableInt(0);
			Thread t1 = new Thread(new AggregationRunnable(combination, list.subList(0, length/2), x));
			Thread t2 = new Thread(new AggregationRunnable(combination, list.subList(length/2, length), y));
			t1.start();
			t2.start();
			try {
				t1.join();
				t2.join();
			} catch (InterruptedException e) {
				return;
			}
			this.result.setInt(this.combination.combine(x.getInt(), y.getInt()));
		} else if (length == 2) {
			this.result.setInt(this.combination.combine(combination.projectInt(list.get(0)), combination.projectInt(list.get(1))));
		} else if (length == 1) {
			this.result.setInt(this.combination.combine(combination.projectInt(list.get(0)), combination.neutral()));
		}
	}
}
