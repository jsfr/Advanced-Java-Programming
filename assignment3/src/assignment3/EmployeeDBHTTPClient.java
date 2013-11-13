package assignment3;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

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
 * @param <E>
 *
 */
public class EmployeeDBHTTPClient<E> implements EmployeeDBClient, EmployeeDB {
    private HttpClient client = null;
    private static final String SPLIT_DEPT = ";";
    private static final String filePath = "/home/jens/Documents/DIKU/year4/AJP/assignments/assignment3/src/assignment3/departmentservermapping.properties";
    private Map<Integer, String> departmentServerURLMap;
    private ContentExchange exchange;
    private XStream xmlStream;

    public EmployeeDBHTTPClient() throws Exception {
        initMappings();
        xmlStream = new XStream(new StaxDriver());
        // You need to initiate HTTPClient here
        client = new HttpClient();
        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.setMaxConnectionsPerAddress(300);
        client.setThreadPool(new QueuedThreadPool(20));
        client.setTimeout(300000);
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
        exchange = new ContentExchange();
        try {
            exchange.setMethod("GET");
            // exchange.setURL(this.getServerURLForDepartment(emp.getDepartment()));
            // exchange.setRequestURI("/addEmployee");
            // exchange.addRequestHeader("id", String.valueOf(emp.getId()));
            // exchange.addRequestHeader("name", emp.getName());
            // exchange.addRequestHeader("department",
            // String.valueOf(emp.getDepartment()));
            // exchange.addRequestHeader("salary",
            // String.valueOf(emp.getSalary()));
            String url = this.getServerURLForDepartment(emp.getDepartment())
                    + "addEmployee?" + "id=" + String.valueOf(emp.getId())
                    + "&name=" + emp.getName() + "&department="
                    + String.valueOf(emp.getDepartment()) + "&salary="
                    + String.valueOf(emp.getSalary());
            exchange.setURL(url);
            client.send(exchange);

            int exchangeState = exchange.waitForDone();
            // System.out.println(String.valueOf(exchangeState));
            if (exchangeState == HttpExchange.STATUS_COMPLETED) {
                int httpStatus = exchange.getResponseStatus();

                switch (httpStatus) {
                case HttpServletResponse.SC_OK:
                    System.out.println("Status_ok");
                    break;
                default:
                    System.out.println("Request not found.");
                    return;
                }
            } else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
                System.out.println("Error occured in connection.");
            } else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
                System.out.println("Request timed out.");
            }
        } catch (DepartmentNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println("IO exception.");
        } catch (InterruptedException e) {
            System.out
                    .println("Could not recieve list of employee. Interrupted exception.");
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
            HashSet<String> hs = new HashSet<String>();
            hs.addAll(departmentServerURLMap.values());
            for (String url : hs) {
                exchange = new ContentExchange();
                exchange.setMethod("GET");
                exchange.setURL(url + "listAllEmployees");
                client.send(exchange);

                // Wait for result
                int exchangeState = exchange.waitForDone();
                // System.out.println(String.valueOf(exchangeState));
                if (exchangeState == HttpExchange.STATUS_COMPLETED) {
                    int httpStatus = exchange.getResponseStatus();
                    switch (httpStatus) {
                    case HttpServletResponse.SC_OK:
                        System.out.println("Status_ok");
                        String content = exchange.getResponseContent();
                        emps.addAll((List<Employee>) xmlStream.fromXML(content));
                        break;
                    default:
                        System.out.println("Request not found.");
                        return null;
                    }
                } else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
                    System.out.println("Error occured in connection.");
                    return null;
                } else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
                    System.out.println("Request timed out.");
                    return null;
                }
            }
            return emps;
        } catch (IOException e) {
            System.out
                    .println("Could not recieve list of employee. IO exception.");
        } catch (InterruptedException e) {
            System.out
                    .println("Could not recieve list of employee. Interrupted exception.");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Employee> listEmployeesInDept(List<Integer> departmentIds) {
        List<Employee> emps = new ArrayList<Employee>();
        try {
            for (String url : departmentServerURLMap.values()) { // Broadcast to
                                                                 // all serers!
                exchange = new ContentExchange();
                exchange.setMethod("POST");
                exchange.setURL(url);
                exchange.setRequestURI("/listEmployeesInDept");
                String xmlString = xmlStream.toXML(departmentIds);
                Buffer postData = new ByteArrayBuffer(xmlString);
                exchange.setRequestContent(postData);
                client.send(exchange);

                // Wait for result
                int exchangeState = exchange.waitForDone();
                // System.out.println(String.valueOf(exchangeState));
                if (exchangeState == HttpExchange.STATUS_COMPLETED) {
                    int httpStatus = exchange.getResponseStatus();
                    switch (httpStatus) {
                    case HttpServletResponse.SC_OK:
                        System.out.println("Status_ok");
                        String content = exchange.getResponseContent();
                        emps.addAll((List<Employee>) xmlStream.fromXML(content));
                        break;
                    default:
                        System.out.println("Request not found.");
                        return new ArrayList<Employee>();
                    }
                } else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
                    System.out.println("Error occured in connection.");
                    return new ArrayList<Employee>();
                } else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
                    System.out.println("Request timed out.");
                    return new ArrayList<Employee>();
                }
            }
        } catch (IOException e) {
            System.out
                    .println("Could not recieve list of employee. IO exception.");
        } catch (InterruptedException e) {
            System.out
                    .println("Could not recieve list of employee. Interrupted exception.");
        }
        return new ArrayList<Employee>();
    }

    /**
     * This action is not atomic, that is, it is not guaranteed that if an error
     * occurs, no salary is incremented.
     */
    @Override
    public void incrementSalaryOfDepartment(
            List<SalaryIncrement> salaryIncrements)
            throws DepartmentNotFoundException,
            NegativeSalaryIncrementException {
        try {
            Map<String, List<SalaryIncrement>> salMap = new HashMap<String, List<SalaryIncrement>>();
            for (SalaryIncrement s : salaryIncrements) {
                String url = this.getServerURLForDepartment(s.getDepartment());
                if (salMap.get(url) == null) {
                    salMap.put(url, new ArrayList<SalaryIncrement>());
                }
                salMap.get(url).add(s);
            }
            for (String s : salMap.keySet()) {
                exchange = new ContentExchange();
                exchange.setMethod("POST");
                String xmlString = xmlStream.toXML(salMap.get(s));
                exchange.setURL(s);
                exchange.setRequestURI("/incrementSalaryOfDepartment");
                Buffer postData = new ByteArrayBuffer(xmlString);
                exchange.setRequestContent(postData);
                client.send(exchange);

                // Wait for result
                int exchangeState = exchange.waitForDone();
                // System.out.println(String.valueOf(exchangeState));
                if (exchangeState == HttpExchange.STATUS_COMPLETED) {
                    int httpStatus = exchange.getResponseStatus();
                    switch (httpStatus) {
                    case HttpServletResponse.SC_OK:
                        System.out.println("Status_ok");
                        break;
                    case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                        System.out.println("Could not add employee.");
                        throw new NegativeSalaryIncrementException();
                    default:
                        System.out.println("Request not found.");
                        return;
                    }
                } else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
                    System.out.println("Error occured in connection.");
                    return;
                } else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
                    System.out.println("Request timed out.");
                    return;
                }
            }
        } catch (IOException e) {
            System.out
                    .println("Could not recieve list of employee. IO exception.");
        } catch (InterruptedException e) {
            System.out
                    .println("Could not recieve list of employee. Interrupted exception.");
        } catch (DepartmentNotFoundException e) {
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
