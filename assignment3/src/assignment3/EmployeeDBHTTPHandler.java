package assignment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * EmployeeDBHTTPHandler is invoked when an HTTP request is received by the
 * EmployeeDBHTTPServer
 *
 * @author bonii
 *
 */
public class EmployeeDBHTTPHandler extends AbstractHandler {

    /**
     * Although this method is thread-safe, what it invokes is not thread-safe
     */
    @SuppressWarnings("unchecked")
    public void handle(String target, Request baseRequest,
            HttpServletRequest req, HttpServletResponse res)
                    throws IOException, ServletException {

        res.setContentType("text/html;charset=utf-8");
        res.setStatus(HttpServletResponse.SC_OK);
        String uri = req.getRequestURI().trim().toLowerCase();
        System.out.println(uri);
        XStream xmlStream = new XStream(new StaxDriver());
        String method = req.getMethod().toLowerCase();
        if (method.equals("get")) {
            switch (uri) {
            case "/addemployee":
                Employee emp = new Employee();
                emp.setName(req.getParameter("name"));
                emp.setId(Integer.parseInt(req.getParameter("id")));
                emp.setDepartment(Integer.parseInt(req.getParameter("department")));
                emp.setSalary(Float.parseFloat(req.getParameter("salary")));
                SimpleEmployeeDB.getInstance().addEmployee(emp);
                break;
            case "/listallemployees":
                List<Employee> emps = SimpleEmployeeDB.getInstance().listAllEmployees();
                res.getWriter().println(xmlStream.toXML(emps));
                break;
            }
        } else {
            int len = req.getContentLength();
            BufferedReader reqReader = req.getReader();
            char[] cbuf = new char[len];
            reqReader.read(cbuf);
            reqReader.close();
            String content = new String(cbuf);

            switch (uri) {
            case "/listemployeesindept":
                ArrayList<Integer> deps = (ArrayList<Integer>) xmlStream.fromXML(content);
                List<Employee> emps1 = SimpleEmployeeDB.getInstance().listEmployeesInDept(deps);
                res.getWriter().println(xmlStream.toXML(emps1));
                break;
            case "/incrementsalaryofdepartment":
                ArrayList<SalaryIncrement> incs = (ArrayList<SalaryIncrement>) xmlStream.fromXML(content);
                try {
                    SimpleEmployeeDB.getInstance().incrementSalaryOfDepartment(incs);
                } catch (DepartmentNotFoundException e) {
                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    ;
                } catch (NegativeSalaryIncrementException e) {
                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                break;
            default:
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                break;
            }
        }
        baseRequest.setHandled(true);
    }
}
