package assignment1;

/**
 * Computes the mutation on the given employee.
 * Used by MapParallel.
 * @author Jens Fredskov, Henrik Bendt
 *
 */

public class MapRunnable implements Runnable {

	private Employee employee;
	private Mutation mutation;
	
	public MapRunnable(Mutation m, Employee e) {
		this.mutation = m;
		this.employee = e;
	}
	
	@Override
	public void run() {
		this.mutation.mutate(this.employee);
	}		
}