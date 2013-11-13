package assignment3;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

/**
 * Generated JUnit test class to test the EmployeeDB interface. It can be used
 * to test both the client and the server implementation.
 *
 * @author bonii
 *
 */
public class EmployeeDBTest {
    private EmployeeDB employeeDB = null;
    Random r = null;

    @Before
    public void setUp() throws Exception {
        employeeDB = SimpleEmployeeDB.getInstance();
        r = new Random();
        //By setting the employeeDB
        // to either the server implementation first, you can write the server
        // implementation and test it. You dont even have to run the Jetty
        // HTTP server, you can just implement SimpleEmployeeDB and test it
        //
        // employeeDB = new EmployeeDBHTTPClient(); Once you have written the
        // client you can test the full system for the same test cases and it
        // should work, welcome to Unit testing :-)
        //
    }
    
    public List<Employee> DB(int n) {
    	List<Employee> emps = new ArrayList<Employee>();
    	for(int i = 0; i < n ; i++) {
    		Employee emp = new Employee();
    		emp.setId(i);
    		emps.add(emp);
    		employeeDB.addEmployee(emp);
    	}
    	return emps;
    }

    /**
     * Make sure that if an object has been added, it is actually there
     */
    @Test
    public void testAddEmployee() {
    	for(int i = 0; i < r.nextInt(); i++) {
    		Employee emp = new Employee();
    		employeeDB.addEmployee(emp);
    		assertTrue(employeeDB.listAllEmployees().contains(emp));
    	}
    }

    /**
     * Make sure that all employees that are added are contained in the
     * complete list of employees.
     */
    @Test
    public void testListAllEmployees() {
    	Random r = new Random();
    	for(int j = 0; j < 1000; j++) {
        	
    		List<Employee> emps = new ArrayList<Employee>();
        	
        	//Make a random number of entries
        	for(int i = 0; i < r.nextInt() ; i++) {
	    		Employee emp = new Employee();
	    		emp.setId(i);
	    		emps.add(emp);
	    		employeeDB.addEmployee(emp);
	    	};
	    	
	    	List<Employee> dbEmps = employeeDB.listAllEmployees();
	    	
	    	assertTrue(dbEmps.containsAll(emps));
    	}
    }

    /**
     * 
     */
    @Test
    public void testListEmployeesInDept() {
    	
    	List<Employee> emps = new ArrayList<Employee>();
    	
    	//What happens we we don't have any departments
    	assertEquals(new ArrayList<Employee>(),employeeDB.listEmployeesInDept(new ArrayList<Integer>()));
    	
    	int dep = r.nextInt(10);
    	Employee emp = new Employee();
    	emp.setId(1337);
    	emp.setDepartment(dep);
    	
    	assertEquals(new ArrayList<Employee>(),employeeDB.listEmployeesInDept(new ArrayList<Integer>()));
    	
    	
    	//What happens with an entry?
    	
    	//Wee I used a do..while \o/
    	int n;
    	do {
    		n = r.nextInt(10);
    	} while (n == dep);
    	
    	//not existing
    	assertEquals(new ArrayList<Employee>(),employeeDB.listEmployeesInDept(new ArrayList<Integer>(n)));
    	
    	//existing
    	assertEquals(emps, employeeDB.listEmployeesInDept(new ArrayList<Integer>(dep)));
    	
    	//Add a bunch of employees
    	for(int i = 0; i < 100; i++) {
    		emp = new Employee();
    		emp.setId(i);
    		emp.setDepartment(i % 10);
    		emps.add(emp);
    		employeeDB.addEmployee(emp);
    	}
    	
    	//What happens if the departments have duplicates
    	
    	//Pigeonhole principle ;)
    	List<Integer> deps = new ArrayList<Integer>();
    	for(int i = 0; i < 11; i++) {
    		deps.add(i % 10);
    	}
    	
    	//Employees should be listed, but only once
    	List<Employee> remoteEmps = employeeDB.listEmployeesInDept(deps);
    	for(Employee e : emps) {
    		assertTrue(remoteEmps.contains(e));
    		remoteEmps.remove(e);
    		assertFalse(remoteEmps.contains(e));
    	}
    }

    @Test
    public void testIncrementSalaryOfDepartment() {
    	
    	//Test that everything works when inputting legal stuff
    	for(int i = 0; i < 100; i++) {
    		Employee emp = new Employee();
    		emp.setId(i);
    		emp.setDepartment(i % 10);
    		emp.setSalary(0);
    		employeeDB.addEmployee(emp);
    	}

    	List<Employee> emps = employeeDB.listAllEmployees();

    	List<SalaryIncrement> sis = new ArrayList<SalaryIncrement>();

    	try {
    		employeeDB.incrementSalaryOfDepartment(sis);
    	} catch (NegativeSalaryIncrementException e) {
    		fail("Exception should not happen here");
    	} catch (DepartmentNotFoundException e) {
    		fail("Exception should not happen here");
    	}
    	
    	for(Employee emp : emps) {
    		Float salary = emp.getSalary();
    		assertEquals(0,salary.compareTo((float) 0));
    	}
    	
    	//Now give the guys a raise
    	for(int i = 0; i < 10; i++) {
    		SalaryIncrement si = new SalaryIncrement();
    		si.setDepartment(i % 10);
    		si.setIncrementBy(10);
    		sis.add(si);
    	}
    	
    	try {
    		employeeDB.incrementSalaryOfDepartment(sis);
    	} catch (NegativeSalaryIncrementException e) {
    		fail("Exception should not happen here");
    	} catch (DepartmentNotFoundException e) {
    		fail("Exception should not happen here");
    	}
    	
    	for(Employee emp : emps) {
    		Float salary = emp.getSalary();
    		assertEquals(0,salary.compareTo((float) 10));
    	}
    }
    
    @Test(expected = DepartmentNotFoundException.class) 
    public void testIncrementSalaryOfDepartmentDepartmentNotFoundException() throws Exception {
    	for(int i = 0; i < 100; i++) {
    		Employee emp = new Employee();
    		emp.setId(i);
    		emp.setDepartment(i % 9);
    		emp.setSalary(0);
    		employeeDB.addEmployee(emp);
    	}
    	
    	List<SalaryIncrement> sis = new ArrayList<SalaryIncrement>();
    	
    	for(int i = 0; i < 10; i++) {
    		SalaryIncrement si = new SalaryIncrement();
    		si.setDepartment(i % 10);
    		si.setIncrementBy(10);
    		sis.add(si);
    	}

    	employeeDB.incrementSalaryOfDepartment(sis);
    }
    
    @Test(expected = NegativeSalaryIncrementException.class)
    public void testIncrementSalaryOfDepartmentNegativeSalaryIncrementException() throws Exception {
    	for(int i = 0; i < 100; i++) {
    		Employee emp = new Employee();
    		emp.setId(i);
    		emp.setDepartment(i % 10);
    		emp.setSalary(0);
    		employeeDB.addEmployee(emp);
    	}
    	
    	List<SalaryIncrement> sis = new ArrayList<SalaryIncrement>();
    	
    	for(int i = 0; i < 10; i++) {
    		SalaryIncrement si = new SalaryIncrement();
    		si.setDepartment(i % 10);
    		si.setIncrementBy(i - 5);
    		sis.add(si);
    	}

    	employeeDB.incrementSalaryOfDepartment(sis);
    }

}
