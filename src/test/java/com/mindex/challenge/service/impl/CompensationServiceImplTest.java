package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    private String compensationUrl;
    private String employeeUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        employeeUrl = "http://localhost:" + port + "/employee";
    }

    @Test
    public void testValidCreateRead() {
        Employee emp1 = new Employee();
        emp1.setFirstName("John");
        emp1.setLastName("Doe");
        emp1.setDepartment("IT");
        emp1.setPosition("Sr. Manager");

        // post employee to database
        Employee created1 = restTemplate.postForEntity(employeeUrl, emp1, Employee.class).getBody();

        //Null check
        assertNotNull(created1.getEmployeeId());

        Compensation comp1 = new Compensation();
        comp1.setEmployeeId(created1.getEmployeeId());
        comp1.setSalary(10000000);
        comp1.setEffectiveDate("2025-08-30");

        //post to database
        Compensation createdComp = restTemplate.postForEntity(compensationUrl, comp1, Compensation.class).getBody();
        //TEST CREATE
        assertNotNull(createdComp);

        //TEST READ
        Compensation comp = compensationService.read(created1.getEmployeeId());
        assertEquals(created1.getEmployeeId(), comp.getEmployeeId());
        assertEquals(10000000, comp.getSalary(), 0.0);
        assertEquals("2025-08-30", comp.getEffectiveDate());
    }

    @Test
    public void testInvalid() {
        Compensation comp1 = new Compensation();
        comp1.setEmployeeId(null);
        comp1.setSalary(10000000);
        comp1.setEffectiveDate("2025-08-30");

        //post to database
        Compensation createdComp = restTemplate.postForEntity(compensationUrl, comp1, Compensation.class).getBody();

        //This should be null, because there was no employee associated with the compensation object.
        assertNull(createdComp.getEmployeeId());
    }

    @Test
    public void testInvalid2(){
        //In this case, there was no compensation added to the database to begin with. Should throw an exception.
        assertThrows(Exception.class, () -> {
            compensationService.read(null);
        });
    }
}
