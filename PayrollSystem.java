package com.payrollsystem.main;

import com.payrollsystem.model.*;
import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Main payroll system class with user interface
 */
public class PayrollSystem {
    private List<Employee> employees;
    private List<Payroll> payrollRecords;
    private Scanner scanner;
    private static final String EMPLOYEE_FILE = "employees.dat";
    private static final String PAYROLL_FILE = "payroll.dat";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Constructor - initializes the system and loads data
     */
    public PayrollSystem() {
        this.employees = new ArrayList<>();
        this.payrollRecords = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        loadData();
    }
    
    /**
     * Save data to files
     */
    private void saveData() {
        try {
            // Save employees
            try (ObjectOutputStream empOut = new ObjectOutputStream(
                    new FileOutputStream(EMPLOYEE_FILE))) {
                empOut.writeObject(employees);
            }
            
            // Save payroll records
            try (ObjectOutputStream payOut = new ObjectOutputStream(
                    new FileOutputStream(PAYROLL_FILE))) {
                payOut.writeObject(payrollRecords);
            }
            
            System.out.println("✅ Data saved successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }
    
    /**
     * Load data from files
     */
    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            // Load employees
            File empFile = new File(EMPLOYEE_FILE);
            if (empFile.exists()) {
                try (ObjectInputStream empIn = new ObjectInputStream(
                        new FileInputStream(EMPLOYEE_FILE))) {
                    employees = (ArrayList<Employee>) empIn.readObject();
                }
                System.out.println("✅ Loaded " + employees.size() + " employees");
            }
            
            // Load payroll records
            File payFile = new File(PAYROLL_FILE);
            if (payFile.exists()) {
                try (ObjectInputStream payIn = new ObjectInputStream(
                        new FileInputStream(PAYROLL_FILE))) {
                    payrollRecords = (ArrayList<Payroll>) payIn.readObject();
                }
                System.out.println("✅ Loaded " + payrollRecords.size() + " payroll records");
            }
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ℹ️ No existing data found. Starting fresh.");
        }
    }
    
    /**
     * Add new employee
     */
    public void addEmployee() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        ADD NEW EMPLOYEE           ║");
        System.out.println("╚════════════════════════════════════╝");
        
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine().trim();
        
        // Check if employee ID already exists
        if (findEmployeeById(id) != null) {
            System.out.println("❌ Employee ID already exists!");
            return;
        }
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Department: ");
        String dept = scanner.nextLine().trim();
        
        LocalDate hireDate = null;
        while (hireDate == null) {
            System.out.print("Enter Hire Date (YYYY-MM-DD): ");
            try {
                hireDate = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format! Please use YYYY-MM-DD");
            }
        }
        
        System.out.println("\nSelect Employee Type:");
        System.out.println("1. Full-Time (Salaried)");
        System.out.println("2. Part-Time (Hourly)");
        System.out.println("3. Contractor (Contract-based)");
        System.out.print("Choice (1-3): ");
        
        int type;
        try {
            type = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input!");
            return;
        }
        
        switch (type) {
            case 1:
                addFullTimeEmployee(id, name, dept, hireDate);
                break;
            case 2:
                addPartTimeEmployee(id, name, dept, hireDate);
                break;
            case 3:
                addContractor(id, name, dept, hireDate);
                break;
            default:
                System.out.println("❌ Invalid choice!");
                return;
        }
        
        saveData();
    }
    
    /**
     * Add full-time employee
     */
    private void addFullTimeEmployee(String id, String name, String dept, LocalDate hireDate) {
        System.out.print("Enter Annual Salary: $");
        double salary;
        try {
            salary = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid salary!");
            return;
        }
        
        FullTimeEmployee ft = new FullTimeEmployee(id, name, dept, hireDate, salary);
        
        System.out.print("Enter Email (optional): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            ft.setEmail(email);
        }
        
        System.out.print("Enter Phone (optional): ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) {
            ft.setPhoneNumber(phone);
        }
        
        employees.add(ft);
        System.out.println("✅ Full-time employee added successfully!");
    }
    
    /**
     * Add part-time employee
     */
    private void addPartTimeEmployee(String id, String name, String dept, LocalDate hireDate) {
        System.out.print("Enter Hourly Rate: $");
        double rate;
        try {
            rate = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid rate!");
            return;
        }
        
        PartTimeEmployee pt = new PartTimeEmployee(id, name, dept, hireDate, rate);
        
        System.out.print("Enter Email (optional): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            pt.setEmail(email);
        }
        
        System.out.print("Enter Phone (optional): ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) {
            pt.setPhoneNumber(phone);
        }
        
        employees.add(pt);
        System.out.println("✅ Part-time employee added successfully!");
    }
    
    /**
     * Add contractor
     */
    private void addContractor(String id, String name, String dept, LocalDate hireDate) {
        System.out.print("Enter Contract Amount: $");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount!");
            return;
        }
        
        System.out.print("Enter Contract Duration (months): ");
        int duration;
        try {
            duration = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid duration!");
            return;
        }
        
        Contractor c = new Contractor(id, name, dept, hireDate, amount, duration);
        
        System.out.print("Enter Company Name (optional): ");
        String company = scanner.nextLine().trim();
        if (!company.isEmpty()) {
            c.setCompanyName(company);
        }
        
        System.out.print("Enter Email (optional): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            c.setEmail(email);
        }
        
        employees.add(c);
        System.out.println("✅ Contractor added successfully!");
    }
    
    /**
     * View all employees
     */
    public void viewAllEmployees() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        ALL EMPLOYEES              ║");
        System.out.println("╚════════════════════════════════════╝");
        
        if (employees.isEmpty()) {
            System.out.println("📭 No employees found.");
            return;
        }
        
        System.out.println("\nTotal Employees: " + employees.size());
        System.out.println("──────────────────────────────────────");
        
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            System.out.printf("%d. %s%n", i + 1, emp);
            
            if (emp instanceof FullTimeEmployee) {
                FullTimeEmployee ft = (FullTimeEmployee) emp;
                System.out.println("   📌 Type: Full-Time");
                System.out.println("   💰 Monthly Salary: $" + String.format("%.2f", ft.getBaseSalary() / 12));
                System.out.println("   🏖️  Paid Leaves: " + ft.getPaidLeaves());
            } else if (emp instanceof PartTimeEmployee) {
                PartTimeEmployee pt = (PartTimeEmployee) emp;
                System.out.println("   📌 Type: Part-Time");
                System.out.println("   ⏱️  Hourly Rate: $" + String.format("%.2f", pt.getHourlyRate()));
                System.out.println("   📊 Hours Worked: " + pt.getHoursWorked());
            } else if (emp instanceof Contractor) {
                Contractor c = (Contractor) emp;
                System.out.println("   📌 Type: Contractor");
                System.out.println("   📝 Contract: $" + String.format("%.2f", c.getContractAmount()) + 
                                 " for " + c.getContractDuration() + " months");
                System.out.println("   📈 Progress: " + c.getMonthsCompleted() + "/" + c.getContractDuration() + " months");
            }
            
            if (emp.getEmail() != null) {
                System.out.println("   📧 Email: " + emp.getEmail());
            }
            if (emp.getPhoneNumber() != null) {
                System.out.println("   📞 Phone: " + emp.getPhoneNumber());
            }
            System.out.println("──────────────────────────────────────");
        }
    }
    
    /**
     * Find employee by ID
     */
    private Employee findEmployeeById(String id) {
        for (Employee emp : employees) {
            if (emp.getEmployeeId().equalsIgnoreCase(id)) {
                return emp;
            }
        }
        return null;
    }
    
    /**
     * Process payroll for an employee
     */
    public void processPayroll() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        PROCESS PAYROLL            ║");
        System.out.println("╚════════════════════════════════════╝");
        
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine().trim();
        
        Employee emp = findEmployeeById(id);
        if (emp == null) {
            System.out.println("❌ Employee not found!");
            return;
        }
        
        // For part-time employees, get hours worked
        if (emp instanceof PartTimeEmployee) {
            System.out.print("Enter hours worked this period: ");
            try {
                int hours = Integer.parseInt(scanner.nextLine().trim());
                ((PartTimeEmployee) emp).setHoursWorked(hours);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid hours!");
                return;
            }
        }
        
        System.out.print("Enter Pay Period (YYYY-MM): ");
        String periodStr = scanner.nextLine().trim();
        LocalDate payPeriod;
        try {
            payPeriod = YearMonth.parse(periodStr).atDay(1);
        } catch (Exception e) {
            System.out.println("❌ Invalid period format! Use YYYY-MM");
            return;
        }
        
        // Check if payroll already processed for this period
        for (Payroll p : payrollRecords) {
            if (p.getEmployee().getEmployeeId().equalsIgnoreCase(id) && 
                p.getPayPeriod().equals(payPeriod)) {
                System.out.println("❌ Payroll already processed for this period!");
                return;
            }
        }
        
        String payrollId = "PAY-" + id + "-" + periodStr.replace("-", "");
        Payroll payroll = new Payroll(payrollId, emp, payPeriod);
        payroll.calculatePayroll();
        
        System.out.println("\n📋 Payroll Summary:");
        System.out.println("═══════════════════════════════════════");
        System.out.println(payroll);
        System.out.println("═══════════════════════════════════════");
        
        System.out.print("\nProcess payment? (Y/N): ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equalsIgnoreCase("Y")) {
            System.out.print("Enter Payment Method (Bank/Cash/Check): ");
            String method = scanner.nextLine().trim();
            payroll.processPayment(method);
            
            System.out.print("Add notes (optional): ");
            String notes = scanner.nextLine().trim();
            if (!notes.isEmpty()) {
                payroll.setNotes(notes);
            }
            
            payrollRecords.add(payroll);
            
            // Reset hours for part-time employees
            if (emp instanceof PartTimeEmployee) {
                ((PartTimeEmployee) emp).resetHours();
            }
            
            System.out.println("✅ Payment processed successfully!");
            saveData();
        } else {
            System.out.println("❌ Payment cancelled.");
        }
    }
    
    /**
     * View payroll history
     */
    public void viewPayrollHistory() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        PAYROLL HISTORY            ║");
        System.out.println("╚════════════════════════════════════╝");
        
        if (payrollRecords.isEmpty()) {
            System.out.println("📭 No payroll records found.");
            return;
        }
        
        System.out.print("Enter Employee ID (or 'all' for all employees): ");
        String id = scanner.nextLine().trim();
        
        boolean found = false;
        double totalPaid = 0;
        int count = 0;
        
        for (Payroll payroll : payrollRecords) {
            if (id.equalsIgnoreCase("all") || 
                payroll.getEmployee().getEmployeeId().equalsIgnoreCase(id)) {
                System.out.println("\n" + payroll);
                if (payroll.getStatus().equals("Paid")) {
                    totalPaid += payroll.getNetPay();
                    count++;
                }
                System.out.println("──────────────────────────────────────");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("❌ No records found for this employee.");
        } else if (count > 0) {
            System.out.printf("Total Paid: $%.2f across %d payments%n", totalPaid, count);
        }
    }
    
    /**
     * Generate salary report
     */
    public void generateSalaryReport() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        SALARY REPORT              ║");
        System.out.println("╚════════════════════════════════════╝");
        
        System.out.print("Enter month (YYYY-MM): ");
        String monthStr = scanner.nextLine().trim();
        LocalDate month;
        try {
            month = YearMonth.parse(monthStr).atDay(1);
        } catch (Exception e) {
            System.out.println("❌ Invalid month format! Use YYYY-MM");
            return;
        }
        
        double totalGross = 0;
        double totalNet = 0;
        double totalDeductions = 0;
        int count = 0;
        
        System.out.println("\n📊 Employee Salary Details for " + monthStr + ":");
        System.out.println("═══════════════════════════════════════════════════");
        System.out.printf("%-20s %-12s %-12s %-12s%n", "Employee", "Gross Pay", "Deductions", "Net Pay");
        System.out.println("───────────────────────────────────────────────────");
        
        for (Payroll payroll : payrollRecords) {
            if (payroll.getPayPeriod().equals(month) && payroll.getStatus().equals("Paid")) {
                System.out.printf("%-20s $%-11.2f $%-11.2f $%-11.2f%n",
                    payroll.getEmployee().getName().length() > 20 ? 
                        payroll.getEmployee().getName().substring(0, 17) + "..." : 
                        payroll.getEmployee().getName(),
                    payroll.getGrossPay(),
                    payroll.getDeductions(),
                    payroll.getNetPay()
                );
                
                totalGross += payroll.getGrossPay();
                totalNet += payroll.getNetPay();
                totalDeductions += payroll.getDeductions();
                count++;
            }
        }
        
        if (count > 0) {
            System.out.println("═══════════════════════════════════════════════════");
            System.out.printf("Summary for %s:%n", monthStr);
            System.out.printf("Total Employees Processed: %d%n", count);
            System.out.printf("Total Gross Pay: $%.2f%n", totalGross);
            System.out.printf("Total Deductions: $%.2f%n", totalDeductions);
            System.out.printf("Total Net Pay: $%.2f%n", totalNet);
            System.out.printf("Average Net Pay: $%.2f%n", totalNet / count);
        } else {
            System.out.println("📭 No paid payroll records found for this month.");
        }
    }
    
    /**
     * Update employee information
     */
    public void updateEmployee() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║     UPDATE EMPLOYEE INFORMATION   ║");
        System.out.println("╚════════════════════════════════════╝");
        
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine().trim();
        
        Employee emp = findEmployeeById(id);
        if (emp == null) {
            System.out.println("❌ Employee not found!");
            return;
        }
        
        System.out.println("\nCurrent Information:");
        System.out.println(emp);
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Department");
        System.out.println("3. Base Salary/Hourly Rate");
        System.out.println("4. Contact Information");
        System.out.println("5. Leave/Bonus (Full-time only)");
        System.out.println("6. Cancel");
        System.out.print("Choice (1