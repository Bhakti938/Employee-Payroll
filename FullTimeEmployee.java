package com.payrollsystem.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a full-time salaried employee
 */
public class FullTimeEmployee extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double bonus;
    private double deductions;
    private int paidLeaves;
    private int unpaidLeaves;
    private double taxRate;
    private double insuranceRate;
    
    /**
     * Constructor for FullTimeEmployee
     * @param employeeId Unique employee ID
     * @param name Employee name
     * @param department Department name
     * @param hireDate Date of hiring
     * @param baseSalary Annual salary
     */
    public FullTimeEmployee(String employeeId, String name, String department,
                           LocalDate hireDate, double baseSalary) {
        super(employeeId, name, department, hireDate, baseSalary);
        this.bonus = 0;
        this.deductions = 0;
        this.paidLeaves = 20; // Default paid leaves per year
        this.unpaidLeaves = 0;
        this.taxRate = 0.20; // 20% tax rate
        this.insuranceRate = 0.05; // 5% insurance rate
    }
    
    @Override
    public double calculateSalary() {
        double monthlySalary = baseSalary / 12;
        
        // Calculate leave deductions
        double leaveDeduction = (unpaidLeaves * monthlySalary) / 30; // Assuming 30 days month
        
        // Calculate tax and insurance
        double taxAmount = monthlySalary * taxRate;
        double insuranceAmount = monthlySalary * insuranceRate;
        
        // Final salary calculation
        double grossMonthly = monthlySalary + bonus;
        double totalDeductions = deductions + taxAmount + insuranceAmount + leaveDeduction;
        
        return grossMonthly - totalDeductions;
    }
    
    /**
     * Add bonus amount
     * @param amount Bonus amount
     */
    public void addBonus(double amount) {
        this.bonus += amount;
    }
    
    /**
     * Add deduction amount
     * @param amount Deduction amount
     */
    public void addDeduction(double amount) {
        this.deductions += amount;
    }
    
    /**
     * Record leave taken
     * @param days Number of days
     * @param isPaid Whether leave is paid
     */
    public void takeLeave(int days, boolean isPaid) {
        if (isPaid && paidLeaves >= days) {
            paidLeaves -= days;
        } else {
            unpaidLeaves += days;
        }
    }
    
    /**
     * Reset leaves at the start of new year
     */
    public void resetLeaves() {
        this.paidLeaves = 20;
        this.unpaidLeaves = 0;
    }
    
    // Getters and Setters
    public double getBonus() { 
        return bonus; 
    }
    
    public void setBonus(double bonus) { 
        this.bonus = bonus; 
    }
    
    public double getDeductions() { 
        return deductions; 
    }
    
    public void setDeductions(double deductions) { 
        this.deductions = deductions; 
    }
    
    public int getPaidLeaves() { 
        return paidLeaves; 
    }
    
    public void setPaidLeaves(int paidLeaves) { 
        this.paidLeaves = paidLeaves; 
    }
    
    public int getUnpaidLeaves() { 
        return unpaidLeaves; 
    }
    
    public void setUnpaidLeaves(int unpaidLeaves) { 
        this.unpaidLeaves = unpaidLeaves; 
    }
    
    public double getTaxRate() { 
        return taxRate; 
    }
    
    public void setTaxRate(double taxRate) { 
        this.taxRate = taxRate; 
    }
    
    public double getInsuranceRate() { 
        return insuranceRate; 
    }
    
    public void setInsuranceRate(double insuranceRate) { 
        this.insuranceRate = insuranceRate; 
    }
}