package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationEmployeeUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationEmployeeUrl = "http://localhost:" + port + "/compensation/{employeeId}";
    }

    @Test
    public void testCreateReturnsCompensation() {
        Employee employee = createEmployee();
        LocalDate effectiveDate = LocalDate.of(2026, 1, 1);

        ResponseEntity<Compensation> created = postCompensation(employee, 125000, effectiveDate);

        assertEquals(HttpStatus.OK, created.getStatusCode());
        assertNotNull(created.getBody());
        assertEquals(125000.0, created.getBody().getSalary(), 0.0);
        assertEquals(effectiveDate, created.getBody().getEffectiveDate());
    }

    @Test
    public void testCreateAddsCompensationToEmployee() {
        Employee employee = createEmployee();

        postCompensation(employee, 125000, LocalDate.of(2026, 1, 1));

        Employee updatedEmployee = employeeService.read(employee.getEmployeeId());
        assertNotNull(updatedEmployee.getCompensations());
        assertEquals(1, updatedEmployee.getCompensations().size());
        assertEquals(125000.0, updatedEmployee.getCompensations().get(0).getSalary(), 0.0);
    }

    @Test
    public void testReadReturnsMostRecentCompensation() {
        Employee employee = createEmployee();
        LocalDate firstEffectiveDate = LocalDate.of(2026, 1, 1);
        LocalDate latestEffectiveDate = LocalDate.of(2026, 6, 1);
        postCompensation(employee, 100000, firstEffectiveDate);
        postCompensation(employee, 125000, latestEffectiveDate);

        Compensation read = restTemplate.getForObject(compensationEmployeeUrl, Compensation.class, employee.getEmployeeId());
        assertNotNull(read);
        assertEquals(125000.0, read.getSalary(), 0.0);
        assertEquals(latestEffectiveDate, read.getEffectiveDate());
    }

    private Employee createEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Test");
        employee.setLastName("Employee");
        return employeeService.create(employee);
    }

    private ResponseEntity<Compensation> postCompensation(Employee employee, double salary, LocalDate effectiveDate) {
        return restTemplate.postForEntity(
                compensationUrl + "?employee={employee}&salary={salary}&effectiveDate={effectiveDate}",
                null,
                Compensation.class,
                employee.getEmployeeId(),
                salary,
                effectiveDate);
    }
}
