package com.payrollsystem.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a contract-based employee
 */
public class Contractor extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double contractAmount;
    private int contractDuration; // in months
    private int monthsCompleted;
    private double taxRate;
    private String companyName;
    private String contractType;
    
    /**
     * Constructor for Contractor
     * @param employeeId Unique employee ID
     * @param name Employee name
     * @param department Department name
     * @param hireDate Date of hiring
     * @param contractAmount Total contract amount
     * @param contractDuration Contract duration in months
     */
    public Contractor(String employeeId, String name, String department,
                     LocalDate hireDate, double contractAmount, int contractDuration) {
        super(employeeId, name, department, hireDate, 0);
        this.contractAmount = contractAmount;
        this.contractDuration = contractDuration;
        this.monthsCompleted = 0;
        this.taxRate = 0.10; // 10% tax rate for contractors
        this.contractType = "Fixed Price";
    }
    
    @Override
    public double calculateSalary() {
        double monthlyPayment = contractAmount / contractDuration;
        double taxAmount = monthlyPayment * taxRate;
        return monthlyPayment - taxAmount;
    }
    
    /**
     * Complete a month of the contract
     */
    public void completeMonth() {
        if (monthsCompleted < contractDuration) {
            monthsCompleted++;
        }
    }
    
    /**
     * Check if contract is complete
     * @return true if contract is complete
     */
    public boolean isContractComplete() {
        return monthsCompleted >= contractDuration;
    }
    
    /**
     * Get remaining months in contract
     * @return remaining months
     */
    public int getRemainingMonths() {
        return contractDuration - monthsCompleted;
    }
    
    // Getters and Setters
    public double getContractAmount() { 
        return contractAmount; 
    }
    
    public void setContractAmount(double contractAmount) { 
        this.contractAmount = contractAmount; 
    }
    
    public int getContractDuration() { 
        return contractDuration; 
    }
    
    public void setContractDuration(int contractDuration) { 
        this.contractDuration = contractDuration; 
    }
    
    public int getMonthsCompleted() { 
        return monthsCompleted; 
    }
    
    public void setMonthsCompleted(int monthsCompleted) { 
        this.monthsCompleted = monthsCompleted; 
    }
    
    public double getTaxRate() { 
        return taxRate; 
    }
    
    public void setTaxRate(double taxRate) { 
        this.taxRate = taxRate; 
    }
    
    public String getCompanyName() { 
        return companyName; 
    }
    
    public void setCompanyName(String companyName) { 
        this.companyName = companyName; 
    }
    
    public String getContractType() { 
        return contractType; 
    }
    
    public void setContractType(String contractType) { 
        this.contractType = contractType; 
    }
}