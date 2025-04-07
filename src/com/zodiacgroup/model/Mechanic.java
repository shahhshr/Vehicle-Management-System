package com.zodiacgroup.model;

import javax.persistence.*;

@Entity
@Table(name = "MECHANICS")
public class Mechanic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MECHANICID")
    private int mechanicId;
    
    @Column(name = "FIRSTNAME", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "LASTNAME", nullable = false, length = 50)
    private String lastName;
    
    @Column(name = "PHONENUMBER", nullable = false, length = 15)
    private String phoneNumber;
    
    @Column(name = "EMAIL", length = 100)
    private String email;
    
    @Column(name = "SPECIALIZATION", nullable = false, length = 100)
    private String specialization;
    
    @Column(name = "EXPERIENCEYEARS", nullable = false)
    private int experienceYears;

    public Mechanic() {}

    // Getters and setters
    public int getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(int mechanicId) {
        this.mechanicId = mechanicId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    @Override
    public String toString() {
        return "Mechanic [id=" + mechanicId + ", name=" + firstName + " " + lastName + "]";
    }
}