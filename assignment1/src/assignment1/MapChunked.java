package assignment1;

import java.util.List;

public class MapChunked implements Map {
	
	@Override
	public void map(Mutation m, List<Employee> l) {
		MapParallel mappar = new MapParallel();
		int length = l.size();
		for(int k = 0; k < length; k += 3) {
			if (k+3 <= length) {
				mappar.map(m, l.subList(k, k+3));
			}
			else {
				mappar.map(m, l.subList(k, length));
			}
		}
	}
}