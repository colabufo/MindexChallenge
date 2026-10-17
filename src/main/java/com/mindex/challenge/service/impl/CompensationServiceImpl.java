package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CompensationServiceImpl implements CompensationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Compensation create(String employeeId, double salary, LocalDate effectiveDate) {
        Employee storedEmployee = employeeRepository.findByEmployeeId(employeeId);
        if (storedEmployee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        Compensation compensation = new Compensation(salary, effectiveDate);
        storedEmployee.setNewCompensation(compensation);
        employeeRepository.save(storedEmployee);
        return compensation;
    }

    @Override
    public Compensation read(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        Compensation compensation = employee.getMostRecentCompensation();
        if (compensation == null) {
            throw new RuntimeException("No compensation found for employeeId: " + employeeId);
        }
        return compensation;
    }
}
