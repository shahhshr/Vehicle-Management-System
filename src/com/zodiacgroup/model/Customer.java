package com.zodiacgroup.model;

import javax.persistence.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.List;

@Entity
@Table(name = "CUSTOMERS")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMERID")
    private int customerId;
    
    @Column(name = "FIRSTNAME", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "LASTNAME", nullable = false, length = 50)
    private String lastName;
    
    @Column(name = "EMAIL", unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(name = "PHONE", length = 15) 
    private String phoneNumber;
    
    @Column(name = "ADDRESS", length = 255)
    private String address;

    public Customer() {}
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public StringProperty fullNameProperty() {
        return new SimpleStringProperty(getFullName());
    }

    @Override
    public String toString() {
        return "Customer [id=" + customerId + ", name=" + firstName + " " + lastName + "]";
    }
    
    public boolean matchesSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return true;
        }
        
        String lowerSearch = searchTerm.toLowerCase();
        return String.valueOf(customerId).contains(searchTerm) ||
               firstName.toLowerCase().contains(lowerSearch) ||
               lastName.toLowerCase().contains(lowerSearch) ||
               (firstName + " " + lastName).toLowerCase().contains(lowerSearch) ||
               (email != null && email.toLowerCase().contains(lowerSearch)) ||
               (phoneNumber != null && phoneNumber.contains(searchTerm));
    }
}