package assignment1;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AggregationTest {
	private AggregationSequential aggregationseq;
	private AggregationParallel aggregationpar;

	@Before
	public void setUp() throws Exception {
		aggregationseq = new AggregationSequential();
		aggregationpar = new AggregationParallel();
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

	private void testAggregation(Aggregation aggregation) throws InterruptedException {
		List<Employee> employees = this.dataSet();
		AddSalary addsal = new AddSalary();
		int result = aggregation.aggregate(addsal, employees);
		assertEquals(3019254, result);
	}

	@Test
	public void testAggregationSequential() throws InterruptedException {
		testAggregation(aggregationseq);
	}
	
	@Test
	public void testAggregationParallel() throws InterruptedException {
		testAggregation(aggregationpar);
	}
}
