package assignment3;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * EmployeeDBHTTPClient implements the client side methods of EmployeeDB
 * interface using HTTP protocol. The methods must send HTTP requests to the
 * EmployeeDBHTTPServer
 *
 * @author 
 *
 */
public class EmployeeDBHTTPClient implements EmployeeDBClient, EmployeeDB {
    private HttpClient client = null;
    private static final String SPLIT_DEPT = ";";
    private static final String filePath = "/home/heben2/git-repos/Advanced-Java-Programming/assignment3/src/departmentservermapping.properties";
    private Map<Integer, String> departmentServerURLMap;
    private ContentExchange exchange;
    private XStream xmlStream;

    public EmployeeDBHTTPClient() throws Exception {
        initMappings();
        exchange = new ContentExchange();
        xmlStream = new XStream(new StaxDriver());
        // You need to initiate HTTPClient here
        client = new HttpClient();
		client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		client.setMaxConnectionsPerAddress(300);
		client.setThreadPool(new QueuedThreadPool(20));
		client.setTimeout(30000);
		client.start();
    }

    public void initMappings() throws IOException, IllegalArgumentException {
        Properties props = new Properties();
        departmentServerURLMap = new ConcurrentHashMap<Integer, String>(); 

        props.load(new FileInputStream(filePath));
        for (String serverURL : props.stringPropertyNames()) {
            String departmentString = props.getProperty(serverURL);
            if (!serverURL.startsWith("http://")) {
                serverURL = new String("http://" + serverURL);
            }
            if (!serverURL.endsWith("/")) {
                serverURL = new String(serverURL + "/");
            }

            String[] departments = departmentString.split(SPLIT_DEPT);
            for (String department : departments) {
                int departmentValue = Integer.parseInt(department);
                if (departmentServerURLMap.containsKey(Integer
                        .valueOf(departmentValue))) {
                    throw new IllegalArgumentException("Duplicate key found");
                }
                departmentServerURLMap.put(Integer.valueOf(departmentValue),
                        serverURL);
            }
        }
    }

    @Override
    public void addEmployee(Employee emp) {
    	try {
    		exchange.setMethod("GET");
    		String xmlString = xmlStream.toXML(emp);
    		exchange.setURL(this.getServerURLForDepartment(emp.getDepartment()));
    		exchange.setRequestHeader("addEmployee",xmlString);  //no clue if this works. Fuck Jetty and its documentation
    		client.send(exchange);
    	} catch (DepartmentNotFoundException e){
    		System.out.println(e);
    	} catch (IOException e) {
			System.out.println("Could not add employee. IO exception.");
		}
    }

    /**
     * @return list of employees or null.
     */
    @SuppressWarnings("unchecked")
	@Override
    public List<Employee> listAllEmployees() {
    	List<Employee> emps = new ArrayList<Employee>();
    	try {
    		exchange.setMethod("GET");
    		for(String url : departmentServerURLMap.values()) {
	    		exchange.setURL(url);
	    		exchange.setRequestHeader("listAllEmployees","");  //no clue if this works. Fuck Jetty and its documentation
	    		client.send(exchange);
	    		
	    		//Wait for result
	    		int exchangeState = exchange.waitForDone();
	    		if (exchangeState == HttpExchange.STATUS_COMPLETED) {
	    			String content = exchange.getResponseContent();
	    			emps.addAll((List<Employee>) xmlStream.fromXML(content));
	    		} else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
	    			System.out.println("Error occured. Could not get all employees. Continuing.");
	    		} else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
	    			System.out.println("Request timed out. Could not get all employees. Continuing.");
	    		}
    		}
    		return emps;
    	} catch (IOException e) {
			System.out.println("Could not recieve list of employee. IO exception.");
			return null;
		} catch (InterruptedException e) {
			System.out.println("Could not recieve list of employee. Interrupted exception.");
			return null;
		}
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<Employee> listEmployeesInDept(List<Integer> departmentIds) {
    	List<Employee> emps = new ArrayList<Employee>();
    	try {
    		exchange.setMethod("POST");
    		for(int dpId : departmentIds){
    			exchange.setURL(this.getServerURLForDepartment(dpId));
    			exchange.setRequestURI("listEmployeesInDept");
    			Buffer postData = new ByteArrayBuffer(String.valueOf(dpId));
    			exchange.setRequestContent(postData);
    			client.send(exchange);
	    		
	    		//Wait for result
	    		int exchangeState = exchange.waitForDone();
	    		if (exchangeState == HttpExchange.STATUS_COMPLETED) {
	    			String content = exchange.getResponseContent();
	    			emps.addAll((List<Employee>) xmlStream.fromXML(content));
	    		} else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
	    			System.out.println("Error occured. Could not get all employees. Continuing.");
	    		} else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
	    			System.out.println("Request timed out. Could not get all employees. Continuing.");
	    		}
    		}
    		
    		client.send(exchange);
    	} catch (IOException e) {
			System.out.println("Could not recieve list of employee. IO exception.");
			return null;
		} catch (InterruptedException e) {
			System.out.println("Could not recieve list of employee. Interrupted exception.");
			return null;
		} catch (DepartmentNotFoundException e){
    		System.out.println(e);
    		return null;
		}
    	
    	
        return null;
    }

    /**
     * This action is not atomic, that is, it is not guaranteed that if an 
     * error occurs, no salary is incremented.
     */
    @Override
    public void incrementSalaryOfDepartment(
    		List<SalaryIncrement> salaryIncrements)
            throws DepartmentNotFoundException,
            NegativeSalaryIncrementException {
    	try {
    		exchange.setMethod("POST");
	        for(SalaryIncrement s : salaryIncrements){
	        	String xmlString = xmlStream.toXML(s);
	        	exchange.setURL(this.getServerURLForDepartment(s.getDepartment()));
    			exchange.setRequestURI("incrementSalaryOfDepartment");
    			Buffer postData = new ByteArrayBuffer(xmlString);
    			exchange.setRequestContent(postData);
    			client.send(exchange);
	    		
	    		//Wait for result
	    		int exchangeState = exchange.waitForDone();
	    		if (exchangeState == HttpExchange.STATUS_COMPLETED) {
	    			//Do nothing
	    		} else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
	    			System.out.println("Error occured. Could not get all employees. Continuing.");
	    		} else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
	    			System.out.println("Request timed out. Could not get all employees. Continuing.");
	    		}
	        }
    	} catch (IOException e) {
			System.out.println("Could not recieve list of employee. IO exception.");
		} catch (InterruptedException e) {
			System.out.println("Could not recieve list of employee. Interrupted exception.");
		} catch (DepartmentNotFoundException e){
    		System.out.println(e);
		}
    	
    }

    /**
     * Returns the server URL (starting with http:// and ending with /) to
     * contact for a department
     */
    public String getServerURLForDepartment(int departmentId)
            throws DepartmentNotFoundException {
        if (!departmentServerURLMap.containsKey(departmentId)) {
            throw new DepartmentNotFoundException("department " + departmentId
                    + " does not exist in mapping");
        }
        return departmentServerURLMap.get(departmentId);
    }

}
