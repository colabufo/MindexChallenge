package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private static final String JOHN_LENNON_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";

    private String employeeUrl;
    private String reportingStructureUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee/{employeeId}";
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{employeeId}";
    }

    @Test
    public void testReadReportingStructure() {
        Employee johnLennon = restTemplate.getForEntity(employeeUrl, Employee.class, JOHN_LENNON_ID).getBody();
        assertNotNull(johnLennon);
        ReportingStructure reportingStructure = reportingStructureService.read(JOHN_LENNON_ID);
        assertNotNull(reportingStructure);
        assertEquals(JOHN_LENNON_ID, reportingStructure.getEmployee().getEmployeeId());
        assertEquals(4, reportingStructure.getNumberOfReports());
    }

    @Test
    public void testReadReportingStructureViaRestEndpoint() {
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, JOHN_LENNON_ID).getBody();
        assertNotNull(reportingStructure);
        assertNotNull(reportingStructure.getEmployee());
        assertEquals(JOHN_LENNON_ID, reportingStructure.getEmployee().getEmployeeId());
        assertEquals(4, reportingStructure.getNumberOfReports());
    }
}
