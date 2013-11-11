package assignment1;

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
