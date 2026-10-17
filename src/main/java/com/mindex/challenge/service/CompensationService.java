package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

import java.time.LocalDate;

public interface CompensationService {
    Compensation create(String employeeId, double salary, LocalDate effectiveDate);
    Compensation read(String employeeId);
}
