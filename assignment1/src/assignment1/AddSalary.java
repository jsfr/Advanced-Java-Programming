package assignment1;

/**
 * Sums the two given salaries. 
 * projectInt returns salary of given employee
 * neutral returns the neutral integer for the identity function for adding 
 * two salaries, namely 0.
 * @author Jens Fredskov, Henrik Bendt
 *
 */

public class AddSalary implements Combination {

	@Override
	public int neutral() {
		return 0;
	}

	@Override
	public int combine(int x, int y) {
		return x + y;
	}

	@Override
	public int projectInt(Employee employee) {
		return employee.getSalary();
	}
}
