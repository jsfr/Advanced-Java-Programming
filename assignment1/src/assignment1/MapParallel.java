package assignment1;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps the given mutation on the given list of employees.
 * All mutations are run in parallel.
 * Joins (waits) on all threads (to finish) before returning.
 * Runs each thread in MapRunnable.
 * @author Jens Fredskov, Henrik Bendt
 *
 */

public class MapParallel implements Map {
	
	@Override
	public void map(Mutation m, List<Employee> l) {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for(Employee e : l) {
			Thread t = new Thread(new MapRunnable(m, e));
			threads.add(t);
			t.start();
		}
		for(Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e1) {
				return;
			}
		}
	}
}