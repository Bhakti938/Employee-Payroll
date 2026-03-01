package com.payrollsystem.test;

import com.payrollsystem.model.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Unit tests for Employee Payroll Management System
 */
public class PayrollSystemTest {
    
    private FullTimeEmployee fullTimeEmp;
    private PartTimeEmployee partTimeEmp;
    private Contractor contractor;
    private Payroll payroll;
    private LocalDate testDate;
    
    @Before
    public void setUp() {
        // Initialize test data before each test
        testDate = LocalDate.of(2024, 1, 15);
        
        fullTimeEmp = new FullTimeEmployee(
            "FT001", 
            "John Doe", 
            "Engineering", 
            testDate, 
            75000.0
        );
        
        partTimeEmp = new PartTimeEmployee(
            "PT001", 
            "Jane Smith", 
            "Sales", 
            testDate, 
            25.50
        );
        
        contractor = new Contractor(
            "CON001", 
            "Bob Wilson", 
            "IT", 
            testDate, 
            50000.0, 
            12
        );
    }
    
    @After
    public void tearDown() {
        // Clean up after each test
        fullTimeEmp = null;
        partTimeEmp = null;
        contractor = null;
        payroll = null;
    }
    
    // ============= EMPLOYEE TESTS =============
    
    @Test
    public void testFullTimeEmployeeCreation() {
        assertNotNull("Full-time employee should be created", fullTimeEmp);
        assertEquals("Employee ID should match", "FT001", fullTimeEmp.getEmployeeId());
        assertEquals("Name should match", "John Doe", fullTimeEmp.getName());
        assertEquals("Department should match", "Engineering", fullTimeEmp.getDepartment());
        assertEquals("Base salary should match", 75000.0, fullTimeEmp.getBaseSalary(), 0.01);
        assertEquals("Initial bonus should be 0", 0.0, fullTimeEmp.getBonus(), 0.01);
        assertEquals("Initial deductions should be 0", 0.0, fullTimeEmp.getDeductions(), 0.01);
        assertEquals("Initial paid leaves should be 20", 20, fullTimeEmp.getPaidLeaves());
        assertEquals("Initial unpaid leaves should be 0", 0, fullTimeEmp.getUnpaidLeaves());
    }
    
    @Test
    public void testPartTimeEmployeeCreation() {
        assertNotNull("Part-time employee should be created", partTimeEmp);
        assertEquals("Employee ID should match", "PT001", partTimeEmp.getEmployeeId());
        assertEquals("Hourly rate should match", 25.50, partTimeEmp.getHourlyRate(), 0.01);
        assertEquals("Initial hours worked should be 0", 0, partTimeEmp.getHoursWorked());
        assertEquals("Overtime rate should be 1.5", 1.5, partTimeEmp.getOvertimeRate(), 0.01);
    }
    
    @Test
    public void testContractorCreation() {
        assertNotNull("Contractor should be created", contractor);
        assertEquals("Contract amount should match", 50000.0, contractor.getContractAmount(), 0.01);
        assertEquals("Contract duration should match", 12, contractor.getContractDuration());
        assertEquals("Initial months completed should be 0", 0, contractor.getMonthsCompleted());
        assertFalse("Contract should not be complete", contractor.isContractComplete());
    }
    
    // ============= SALARY CALCULATION TESTS =============
    
    @Test
    public void testFullTimeSalaryCalculation() {
        double monthlySalary = fullTimeEmp.getBaseSalary() / 12;
        double expectedSalary = monthlySalary; // No bonus, no deductions, no leaves
        
        double actualSalary = fullTimeEmp.calculateSalary();
        assertEquals("Monthly salary should be base/12", expectedSalary, actualSalary, 0.01);
    }
    
    @Test
    public void testFullTimeSalaryWithBonus() {
        fullTimeEmp.addBonus(1000.0);
        double monthlySalary = fullTimeEmp.getBaseSalary() / 12;
        double expectedSalary = monthlySalary + 1000.0;
        
        double actualSalary = fullTimeEmp.calculateSalary();
        assertEquals("Salary should include bonus", expectedSalary, actualSalary, 0.01);
    }
    
    @Test
    public void testFullTimeSalaryWithDeductions() {
        fullTimeEmp.addDeduction(500.0);
        double monthlySalary = fullTimeEmp.getBaseSalary() / 12;
        double expectedSalary = monthlySalary - 500.0;
        
        double actualSalary = fullTimeEmp.calculateSalary();
        assertEquals("Salary should include deductions", expectedSalary, actualSalary, 0.01);
    }
    
    @Test
    public void testFullTimeSalaryWithUnpaidLeaves() {
        fullTimeEmp.takeLeave(5, false); // 5 unpaid leaves
        double monthlySalary = fullTimeEmp.getBaseSalary() / 12;
        double leaveDeduction = (5 * monthlySalary) / 30;
        double expectedSalary = monthlySalary - leaveDeduction;
        
        double actualSalary = fullTimeEmp.calculateSalary();
        assertEquals("Salary should deduct unpaid leaves", expectedSalary, actualSalary, 0.01);
    }
    
    @Test
    public void testPartTimeSalaryRegularHours() {
        partTimeEmp.setHoursWorked(40); // Regular hours, no overtime
        double expectedSalary = 40 * 25.50 * (1 - 0.15); // With 15% tax
        
        double actualSalary = partTimeEmp.calculateSalary();
        assertEquals("Salary should calculate regular hours correctly", expectedSalary, actualSalary, 0.01);
    }
    
    @Test
    public void testPartTimeSalaryWithOvertime() {
        partTimeEmp.setHoursWorked(45); // 40 regular + 5 overtime
        double regularPay = 40 * 25.50;
        double overtimePay = 5 * 25.50 * 1.5;
        double expectedSalary = (regularPay + overtimePay) * (1 - 0.15); // With 15% tax
        
        double actualSalary = partTimeEmp.calculateSalary();
        assertEquals("Salary should include overtime", expectedSalary, actualSalary, 0.01);
    }
    
    @Test
    public void testContractorSalaryCalculation() {
        double expectedMonthly = (50000.0 / 12) * (1 - 0.10); // With 10% tax
        double actualSalary = contractor.calculateSalary();
        assertEquals("Contractor monthly salary should be contract amount/duration", 
                    expectedMonthly, actualSalary, 0.01);
    }
    
    // ============= LEAVE MANAGEMENT TESTS =============
    
    @Test
    public void testPaidLeaveDeduction() {
        int initialLeaves = fullTimeEmp.getPaidLeaves();
        fullTimeEmp.takeLeave(5, true);
        assertEquals("Paid leaves should decrease", initialLeaves - 5, fullTimeEmp.getPaidLeaves());
        assertEquals("Unpaid leaves should not change", 0, fullTimeEmp.getUnpaidLeaves());
    }
    
    @Test
    public void testUnpaidLeaveRecording() {
        fullTimeEmp.takeLeave(3, false);
        assertEquals("Unpaid leaves should increase", 3, fullTimeEmp.getUnpaidLeaves());
        assertEquals("Paid leaves should not change", 20, fullTimeEmp.getPaidLeaves());
    }
    
    @Test
    public void testInsufficientPaidLeaves() {
        fullTimeEmp.takeLeave(25, true); // Try to take more than available
        assertEquals("Should only deduct available paid leaves", 0, fullTimeEmp.getPaidLeaves());
        assertEquals("Remaining should become unpaid", 5, fullTimeEmp.getUnpaidLeaves());
    }
    
    @Test
    public void testResetLeaves() {
        fullTimeEmp.takeLeave(5, true);
        fullTimeEmp.takeLeave(3, false);
        fullTimeEmp.resetLeaves();
        assertEquals("Paid leaves should reset to 20", 20, fullTimeEmp.getPaidLeaves());
        assertEquals("Unpaid leaves should reset to 0", 0, fullTimeEmp.getUnpaidLeaves());
    }
    
    // ============= CONTRACTOR TESTS =============
    
    @Test
    public void testContractMonthCompletion() {
        for (int i = 0; i < 6; i++) {
            contractor.completeMonth();
        }
        assertEquals("Should have completed 6 months", 6, contractor.getMonthsCompleted());
        assertFalse("Contract should not be complete", contractor.isContractComplete());
        assertEquals("Remaining months should be 6", 6, contractor.getRemainingMonths());
    }
    
    @Test
    public void testContractCompletion() {
        for (int i = 0; i < 12; i++) {
            contractor.completeMonth();
        }
        assertTrue("Contract should be complete after 12 months", contractor.isContractComplete());
        assertEquals("Remaining months should be 0", 0, contractor.getRemainingMonths());
    }
    
    @Test
    public void testContractCannotExceedDuration() {
        for (int i = 0; i < 15; i++) {
            contractor.completeMonth();
        }
        assertEquals("Should not exceed contract duration", 12, contractor.getMonthsCompleted());
    }
    
    // ============= PAYROLL TESTS =============
    
    @Test
    public void testPayrollCreation() {
        LocalDate payPeriod = LocalDate.of(2024, 1, 1);
        payroll = new Payroll("PAY-FT001-202401", fullTimeEmp, payPeriod);
        
        assertNotNull("Payroll should be created", payroll);
        assertEquals("Payroll ID should match", "PAY-FT001-202401", payroll.getPayrollId());
        assertEquals("Employee should match", fullTimeEmp, payroll.getEmployee());
        assertEquals("Pay period should match", payPeriod, payroll.getPayPeriod());
        assertEquals("Initial status should be Pending", "Pending", payroll.getStatus());
    }
    
    @Test
    public void testPayrollCalculation() {
        LocalDate payPeriod = LocalDate.of(2024, 1, 1);
        payroll = new Payroll("PAY-FT001-202401", fullTimeEmp, payPeriod);
        payroll.calculatePayroll();
        
        assertTrue("Gross pay should be positive", payroll.getGrossPay() > 0);
        assertTrue("Net pay should be positive", payroll.getNetPay() > 0);
        assertTrue("Deductions should be non-negative", payroll.getDeductions() >= 0);
    }
    
    @Test
    public void testPayrollProcessing() {
        LocalDate payPeriod = LocalDate.of(2024, 1, 1);
        payroll = new Payroll("PAY-FT001-202401", fullTimeEmp, payPeriod);
        payroll.calculatePayroll();
        payroll.processPayment("Bank");
        
        assertEquals("Status should be Paid", "Paid", payroll.getStatus());
        assertEquals("Payment method should be Bank", "Bank", payroll.getPaymentMethod());
        assertNotNull("Payment date should be set", payroll.getPaymentDate());
    }
    
    @Test
    public void testFormattedPayPeriod() {
        LocalDate payPeriod = LocalDate.of(2024, 1, 1);
        payroll = new Payroll("PAY-FT001-202401", fullTimeEmp, payPeriod);
        assertEquals("Should format as Month Year", "January 2024", payroll.getFormattedPayPeriod());
    }
    
    // ============= EDGE CASE TESTS =============
    
    @Test
    public void testZeroHoursForPartTime() {
        partTimeEmp.setHoursWorked(0);
        double expectedSalary = 0.0;
        assertEquals("Salary should be 0 for 0 hours", expectedSalary, partTimeEmp.calculateSalary(), 0.01);
    }
    
    @Test
    public void testNegativeValues() {
        // Test that negative values are handled appropriately
        try {
            fullTimeEmp.addBonus(-1000.0);
            fullTimeEmp.addDeduction(-500.0);
            // Should not throw exception, but values become negative
        } catch (Exception e) {
            fail("Should handle negative values without exception");
        }
    }
    
    @Test
    public void testLargeValues() {
        FullTimeEmployee exec = new FullTimeEmployee(
            "FT999", 
            "Big Boss", 
            "Executive", 
            testDate, 
            1000000.0
        );
        exec.addBonus(100000.0);
        
        double salary = exec.calculateSalary();
        assertTrue("Should handle large salaries", salary > 0);
    }
    
    // ============= BULK OPERATION TESTS =============
    
    @Test
    public void testMultipleEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(fullTimeEmp);
        employees.add(partTimeEmp);
        employees.add(contractor);
        
        assertEquals("Should contain 3 employees", 3, employees.size());
        
        double totalPayroll = 0;
        for (Employee emp : employees) {
            totalPayroll += emp.calculateSalary();
        }
        
        assertTrue("Total payroll should be positive", totalPayroll > 0);
    }
    
    @Test
    public void testEmployeeSearch() {
        Map<String, Employee> employeeMap = new HashMap<>();
        employeeMap.put(fullTimeEmp.getEmployeeId(), fullTimeEmp);
        employeeMap.put(partTimeEmp.getEmployeeId(), partTimeEmp);
        employeeMap.put(contractor.getEmployeeId(), contractor);
        
        assertTrue("Should find FT001", employeeMap.containsKey("FT001"));
        assertTrue("Should find PT001", employeeMap.containsKey("PT001"));
        assertTrue("Should find CON001", employeeMap.containsKey("CON001"));
        assertFalse("Should not find invalid ID", employeeMap.containsKey("INVALID"));
    }
    
    // ============= TEST SUITE =============
    
    @Test
    public void runAllTests() {
        System.out.println("Running all tests for Employee Payroll System");
        System.out.println("=============================================");
        
        testFullTimeEmployeeCreation();
        testPartTimeEmployeeCreation();
        testContractorCreation();
        testFullTimeSalaryCalculation();
        testPartTimeSalaryRegularHours();
        testContractorSalaryCalculation();
        testPayrollCalculation();
        testPayrollProcessing();
        
        System.out.println("✅ All tests passed!");
    }
}