package com.payrollsystem.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a part-time hourly employee
 */
public class PartTimeEmployee extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double hourlyRate;
    private int hoursWorked;
    private double overtimeRate;
    private int regularHoursPerWeek;
    private double taxRate;
    
    /**
     * Constructor for PartTimeEmployee
     * @param employeeId Unique employee ID
     * @param name Employee name
     * @param department Department name
     * @param hireDate Date of hiring
     * @param hourlyRate Hourly pay rate
     */
    public PartTimeEmployee(String employeeId, String name, String department,
                           LocalDate hireDate, double hourlyRate) {
        super(employeeId, name, department, hireDate, 0);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = 0;
        this.overtimeRate = 1.5; // 1.5x for overtime
        this.regularHoursPerWeek = 40;
        this.taxRate = 0.15; // 15% tax rate for part-time
    }
    
    @Override
    public double calculateSalary() {
        int regularHours = Math.min(hoursWorked, regularHoursPerWeek);
        int overtimeHours = Math.max(hoursWorked - regularHoursPerWeek, 0);
        
        double regularPay = regularHours * hourlyRate;
        double overtimePay = overtimeHours * hourlyRate * overtimeRate;
        
        double grossPay = regularPay + overtimePay;
        double taxAmount = grossPay * taxRate;
        
        return grossPay - taxAmount;
    }
    
    /**
     * Add hours worked
     * @param hours Hours to add
     */
    public void addHours(int hours) {
        this.hoursWorked += hours;
    }
    
    /**
     * Reset hours at end of pay period
     */
    public void resetHours() {
        this.hoursWorked = 0;
    }
    
    // Getters and Setters
    public double getHourlyRate() { 
        return hourlyRate; 
    }
    
    public void setHourlyRate(double hourlyRate) { 
        this.hourlyRate = hourlyRate; 
    }
    
    public int getHoursWorked() { 
        return hoursWorked; 
    }
    
    public void setHoursWorked(int hoursWorked) { 
        this.hoursWorked = hoursWorked; 
    }
    
    public double getOvertimeRate() { 
        return overtimeRate; 
    }
    
    public void setOvertimeRate(double overtimeRate) { 
        this.overtimeRate = overtimeRate; 
    }
    
    public int getRegularHoursPerWeek() { 
        return regularHoursPerWeek; 
    }
    
    public void setRegularHoursPerWeek(int regularHoursPerWeek) { 
        this.regularHoursPerWeek = regularHoursPerWeek; 
    }
    
    public double getTaxRate() { 
        return taxRate; 
    }
    
    public void setTaxRate(double taxRate) { 
        this.taxRate = taxRate; 
    }
}