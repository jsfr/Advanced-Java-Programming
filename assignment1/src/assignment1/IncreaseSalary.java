package assignment1;

/**
 * Increments salary of the given employee, if the employee is older than 40,
 * by half the employees age.
 * @author Jens Fredskov, Henrik Bendt
 *
 */
public class IncreaseSalary implements Mutation {

	@Override
	public void mutate(Employee employee) {
		int age = employee.getAge();
		if (age > 40) {
			employee.setSalary(employee.getSalary() + age/2);
		}
	}
	
}