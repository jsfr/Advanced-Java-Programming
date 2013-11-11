package assignment1;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the assosiativ and neutral property of the combination methods 
 * AddSalary and MinAge.
 * Tests are over a random generated list, where salary is between 3000 and 5000
 * and age is between 20 and 60.
 * Tests are iterated 1000 times each.
 * @author Jens Fredskov, Henrik Bendt
 *
 */

public class CombinationTest {
	private MinAge minage;
	private AddSalary addsal;

	private Employee genEmployee() {
		Random rng = new java.util.Random();
		String name = "John Doe";
		int age = rng.nextInt(41) + 20;
		int salary = rng.nextInt(2001) + 3000;
		return new Employee(name, age, salary);
	}
	
	@Before
	public void setUp() {
		this.minage = new MinAge();
		this.addsal = new AddSalary();
	}
	
	@Test
	public void testAddSalaryNeutral() {
		for (int x = 0; x < 1000; x++) {
			Employee employee1 = this.genEmployee();
			int employee2 = addsal.neutral();
			int min1 = addsal.combine(addsal.projectInt(employee1), employee2);
			int min2 = addsal.combine(employee2, addsal.projectInt(employee1));
			assertEquals(min1, min2);
		}
	}

	@Test
	public void testAddSalaryCombine() {
		for (int x = 0; x < 1000; x++) {
			Employee employee1 = this.genEmployee();
			Employee employee2 = this.genEmployee();
			int min1 = addsal.combine(addsal.projectInt(employee1), addsal.projectInt(employee2));
			int min2 = addsal.combine(addsal.projectInt(employee2), addsal.projectInt(employee1));
			assertEquals(min1, min2);
		}
	}
	
	@Test
	public void testMinAgeNeutral() {
		for (int x = 0; x < 1000; x++) {
			Employee employee1 = this.genEmployee();
			int employee2 = minage.neutral();
			int min1 = minage.combine(minage.projectInt(employee1), employee2);
			int min2 = minage.combine(employee2, minage.projectInt(employee1));
			assertEquals(min1, min2);
		}
	}

	@Test
	public void testMinAgeCombine() {
		for (int x = 0; x < 1000; x++) {
			Employee employee1 = this.genEmployee();
			Employee employee2 = this.genEmployee();
			int min1 = minage.combine(minage.projectInt(employee1), minage.projectInt(employee2));
			int min2 = minage.combine(minage.projectInt(employee2), minage.projectInt(employee1));
			assertEquals(min1, min2);
		}
	}
}
