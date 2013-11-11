package assignment1;

import java.util.ArrayList;
import java.util.List;

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