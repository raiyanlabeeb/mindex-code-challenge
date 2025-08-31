package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    private String employeeUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
    }

    @Test
    public void testValidRead() {
        Employee emp1 = new Employee();
        emp1.setFirstName("John");
        emp1.setLastName("Doe");
        emp1.setDepartment("IT");
        emp1.setPosition("Sr. Manager");

        Employee emp2 = new Employee();
        emp2.setFirstName("Jane");
        emp2.setLastName("Doe");
        emp2.setDepartment("IT");
        emp2.setPosition("Intern");

        Employee emp3 = new Employee();
        emp3.setFirstName("Jack");
        emp3.setLastName("Doe");
        emp3.setDepartment("IT");
        emp3.setPosition("Lead Developer");

        emp1.setDirectReports(Arrays.asList(emp2, emp3));

        // post employees to database
        Employee created1 = restTemplate.postForEntity(employeeUrl, emp1, Employee.class).getBody();
        Employee created2 = restTemplate.postForEntity(employeeUrl, emp2, Employee.class).getBody();
        Employee created3 = restTemplate.postForEntity(employeeUrl, emp3, Employee.class).getBody();

        //Null check
        assertNotNull(created1.getEmployeeId());
        assertNotNull(created2.getEmployeeId());
        assertNotNull(created3.getEmployeeId());

        ReportingStructure rs = reportingStructureService.read(created1.getEmployeeId());
        assertNotNull(rs);

        assertEquals(2, rs.getNumberOfReports());
    }

    @Test
    public void testInvalidRead() {
        assertThrows(Exception.class, () -> {
            reportingStructureService.read(null);
        });
    }
}
