package com.zodiacgroup.model;

import javax.persistence.*;

@Entity
@Table(name = "SALES_REPRESENTATIVES")
public class SaleRepresentative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID")
    private int employeeId;
    
    @Column(name = "EMPLOYEE_NAME", nullable = false, length = 100)
    private String employeeName;
    
    @Column(name = "POSITION", length = 50)
    private String position;
    
    @Column(name = "EMAIL", length = 100)
    private String email;
    
    @Column(name = "PHONE_NUMBER", length = 20)
    private String phoneNumber;
    
    @Column(name = "USERNAME", length = 50)
    private String username;
    
    @Column(name = "PASSWORD", length = 255)
    private String password;

    public SaleRepresentative() {}

    // Getters and setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return "SaleRepresentative [id=" + employeeId + ", name=" + employeeName + "]";
    }
    
    public boolean matchesSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return true;
        }
        
        String lowerSearch = searchTerm.toLowerCase();
        return String.valueOf(employeeId).contains(searchTerm) ||
               employeeName.toLowerCase().contains(lowerSearch) ||
               position.toLowerCase().contains(lowerSearch) ||
               (email != null && email.toLowerCase().contains(lowerSearch)) ||
               (phoneNumber != null && phoneNumber.contains(searchTerm)) ||
               (username != null && username.toLowerCase().contains(lowerSearch));
    }
}