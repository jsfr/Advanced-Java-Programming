package assignment1;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the correctness of combination methods MapSequential, MapParallel and 
 * MapChunked.
 * Does not test parallelism.
 * Tests are assertions between the manual calculated results and the returned 
 * results of the three maps over the hard-coded list dataSet with mutation 
 * IncreaseSalary.
 * @author Jens Fredskov, Henrik Bendt
 *
 */

public class MapTest {
	private MapSequential mapseq;
	private MapParallel mappar;
	private MapChunked mapchk;

	@Before
	public void setUp() throws Exception {
		mapseq = new MapSequential();
		mappar = new MapParallel();
		mapchk = new MapChunked();
	}
	
	private List<Employee> dataSet() {
		return Arrays.asList(new Employee("Knuth", 60, 10000),
		        new Employee("Dennis Ritchie", 70, 250),
		        new Employee("Stalin", 20, 10),
		        new Employee("Nega Stalin", 250, 1000000),
		        new Employee("Bob(cat)", 50, 100),
		        new Employee("Russian Jurij", 30, 1),
		        new Employee("Jewbacca", 40, 7893),
		        new Employee("Barack Hussein Obama", 50, 750000),
		        new Employee("Cpt. William Kidd", 52, 500000),
		        new Employee("Famous person", 26, 751000));
	}
	
	private void testMap(Map map) throws InterruptedException {
		List<Employee> employees = this.dataSet();
		IncreaseSalary incsal = new IncreaseSalary();
		map.map(incsal, employees);
		int result = 0;
		for(Employee e : employees) {
			result += e.getSalary();
		}
		assertEquals(3019520, result);
	}

	@Test
	public void testMapSequential() throws InterruptedException {
		testMap(mapseq);
	}
	
	@Test
	public void testMapParallel() throws InterruptedException {
		testMap(mappar);
	}
	
	@Test
	public void testMapChunked() throws InterruptedException {
		testMap(mapchk);
	}
}