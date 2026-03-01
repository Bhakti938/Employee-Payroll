package com.payrollsystem.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a payroll record for an employee
 */
public class Payroll implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String payrollId;
    private Employee employee;
    private LocalDate payPeriod;
    private double grossPay;
    private double deductions;
    private double netPay;
    private String paymentMethod;
    private LocalDate paymentDate;
    private String status;
    private String notes;
    
    /**
     * Constructor for Payroll
     * @param payrollId Unique payroll ID
     * @param employee Employee object
     * @param payPeriod Pay period date
     */
    public Payroll(String payrollId, Employee employee, LocalDate payPeriod) {
        this.payrollId = payrollId;
        this.employee = employee;
        this.payPeriod = payPeriod;
        this.deductions = 0;
        this.status = "Pending";
        this.paymentDate = null;
        this.notes = "";
    }
    
    /**
     * Calculate payroll amounts
     */
    public void calculatePayroll() {
        this.grossPay = employee.calculateSalary();
        
        // Calculate standard deductions
        calculateDeductions();
        
        // Calculate net pay
        this.netPay = grossPay - deductions;
        
        // Add bonus for full-time employees
        if (employee instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) employee;
            this.netPay += ft.getBonus();
        }
    }
    
    /**
     * Calculate all deductions
     */
    private void calculateDeductions() {
        // Standard deductions (can be customized based on employee type)
        if (employee instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) employee;
            this.deductions = ft.getDeductions();
        } else {
            // Default deductions (tax, etc. are already calculated in employee's calculateSalary)
            this.deductions = 0;
        }
    }
    
    /**
     * Process payment
     * @param method Payment method (Bank/Cash/Check)
     */
    public void processPayment(String method) {
        this.paymentMethod = method;
        this.paymentDate = LocalDate.now();
        this.status = "Paid";
    }
    
    /**
     * Get pay period as string
     * @return formatted pay period
     */
    public String getFormattedPayPeriod() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return payPeriod.format(formatter);
    }
    
    // Getters and Setters
    public String getPayrollId() { 
        return payrollId; 
    }
    
    public void setPayrollId(String payrollId) { 
        this.payrollId = payrollId; 
    }
    
    public Employee getEmployee() { 
        return employee; 
    }
    
    public void setEmployee(Employee employee) { 
        this.employee = employee; 
    }
    
    public LocalDate getPayPeriod() { 
        return payPeriod; 
    }
    
    public void setPayPeriod(LocalDate payPeriod) { 
        this.payPeriod = payPeriod; 
    }
    
    public double getGrossPay() { 
        return grossPay; 
    }
    
    public void setGrossPay(double grossPay) { 
        this.grossPay = grossPay; 
    }
    
    public double getDeductions() { 
        return deductions; 
    }
    
    public void setDeductions(double deductions) { 
        this.deductions = deductions; 
    }
    
    public double getNetPay() { 
        return netPay; 
    }
    
    public void setNetPay(double netPay) { 
        this.netPay = netPay; 
    }
    
    public String getPaymentMethod() { 
        return paymentMethod; 
    }
    
    public void setPaymentMethod(String paymentMethod) { 
        this.paymentMethod = paymentMethod; 
    }
    
    public LocalDate getPaymentDate() { 
        return paymentDate; 
    }
    
    public void setPaymentDate(LocalDate paymentDate) { 
        this.paymentDate = paymentDate; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public String getNotes() { 
        return notes; 
    }
    
    public void setNotes(String notes) { 
        this.notes = notes; 
    }
    
    @Override
    public String toString() {
        return String.format(
            "Payroll ID: %s\nEmployee: %s\nPeriod: %s\nGross Pay: $%.2f\nDeductions: $%.2f\nNet Pay: $%.2f\nStatus: %s\nPayment Date: %s",
            payrollId, 
            employee.getName(), 
            getFormattedPayPeriod(), 
            grossPay, 
            deductions, 
            netPay, 
            status,
            paymentDate != null ? paymentDate.toString() : "Not paid"
        );
    }
}