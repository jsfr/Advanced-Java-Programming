package assignment1;

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