package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String employeeId) {
        Employee employee = findEmployee(employeeId);
        ReportingStructure reportingStructure = new ReportingStructure(employee, countReports(employee));
        return reportingStructure;
    }

    @Override
    public int countReports(Employee employee) {
        List<Employee> reports = employee.getDirectReports();
        if (reports == null) {
            return 0;
        }
        int numberOfReports = reports.size();
        for (Employee report : reports) {
            numberOfReports += countReports(findEmployee(report.getEmployeeId()));
        }
        return numberOfReports;
    }

    private Employee findEmployee(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }
        return employee;
    }
}
