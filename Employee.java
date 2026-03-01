package com.payrollsystem.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Base abstract class for all employee types
 */
public abstract class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String employeeId;
    protected String name;
    protected String department;
    protected LocalDate hireDate;
    protected double baseSalary;
    protected String email;
    protected String phoneNumber;
    
    /**
     * Constructor for Employee
     * @param employeeId Unique employee ID
     * @param name Employee name
     * @param department Department name
     * @param hireDate Date of hiring
     * @param baseSalary Base salary amount
     */
    public Employee(String employeeId, String name, String department, 
                   LocalDate hireDate, double baseSalary) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.hireDate = hireDate;
        this.baseSalary = baseSalary;
    }
    
    /**
     * Abstract method to calculate salary - to be implemented by subclasses
     * @return calculated salary amount
     */
    public abstract double calculateSalary();
    
    // Getters and Setters
    public String getEmployeeId() { 
        return employeeId; 
    }
    
    public void setEmployeeId(String employeeId) { 
        this.employeeId = employeeId; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getDepartment() { 
        return department; 
    }
    
    public void setDepartment(String department) { 
        this.department = department; 
    }
    
    public LocalDate getHireDate() { 
        return hireDate; 
    }
    
    public double getBaseSalary() { 
        return baseSalary; 
    }
    
    public void setBaseSalary(double baseSalary) { 
        this.baseSalary = baseSalary; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Department: %s, Base Salary: $%.2f", 
                           employeeId, name, department, baseSalary);
    }
}