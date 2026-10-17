package com.mindex.challenge.data;

import java.time.LocalDate;

public class Compensation {
    private double salary;
    private LocalDate effectiveDate;
    
    public Compensation(double salary, LocalDate effectiveDate) {
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public double getSalary() {
        return salary;
    }
}