package assignment1;

/**
 * combine returns the minimum of two given ages. 
 * projectInt returns age of given employee.
 * neutral returns the neutral integer for the identity function for finding 
 * the minimum of two ages, namely the maximum integer possible.
 * @author Jens Fredskov, Henrik Bendt
 *
 */

public class MinAge implements Combination {

	@Override
	public int neutral() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int combine(int x, int y) {
		return Math.min(x, y);
	}

	@Override
	public int projectInt(Employee employee) {
		return employee.getAge();
	}

}
