package assignment1;

/**
 * Changes the name of the given employee to all lower case.
 * @author Jens Fredskov, Henrik Bendt
 *
 */

public class LowerCaseName implements Mutation {

	@Override
	public void mutate(Employee employee) {
		employee.setName(employee.getName().toLowerCase());
	}
}
