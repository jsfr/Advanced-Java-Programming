package assignment3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleEmployeeDB implements EmployeeDB {
	private static SimpleEmployeeDB instance = null;
	
	//Map<Integer,List<Employee>> db = new HashMap<Integer, List<Employee>>();
	//List<Integer> departments = new ArrayList<Integer>();
	protected List<Employee> emps = new ArrayList<Employee>();

	private SimpleEmployeeDB() {

	}

	public synchronized static SimpleEmployeeDB getInstance() {
		if (instance == null) {
			instance = new SimpleEmployeeDB();
		}
		return instance;
	}

	@Override
	public synchronized void addEmployee(Employee emp) {
		//db.put(emp.getDepartment(), emp);
		emps.add(emp);
	}

	@Override
	public synchronized List<Employee> listAllEmployees() {
		
		return emps;
	}

	@Override
	public synchronized List<Employee> listEmployeesInDept(
			List<Integer> departmentIds) {
		
		Set<Employee> emps = new HashSet<Employee>();
		
		for(Employee emp : this.emps) {
			for(Integer i : departmentIds) {
				if(emp.getDepartment() == i) {
					emps.add(emp);	
				}
			}		
		}
		
		return new ArrayList<Employee>(emps);
	}

	@Override
	public synchronized void incrementSalaryOfDepartment(
			List<SalaryIncrement> salaryIncrements)
			throws DepartmentNotFoundException,
			NegativeSalaryIncrementException {
		
		
		
		Set<Integer> deps = new HashSet<Integer>();
		
		//Check that there is no negative salary
		for(SalaryIncrement si : salaryIncrements) {
			if(si.getIncrementBy() < 0) {
				throw new NegativeSalaryIncrementException();
			} else {
				deps.add(si.getDepartment());
			}
		}
		
		List<Employee> emps = listEmployeesInDept(new ArrayList<Integer>(deps));
		
		//Check that all departments exist
		for(Employee emp : emps) {
			deps.remove(emp.getDepartment());
		}
		if(deps.size() > 0) {
			throw new DepartmentNotFoundException();
		}
		
		//Do the actual salary increment
		for(Employee emp : emps) {
			for(SalaryIncrement si : salaryIncrements) {
				if(emp.getDepartment() == si.getDepartment()) {
					emp.setSalary(emp.getSalary() + si.getIncrementBy());
				}
			}
		}
	}

}
