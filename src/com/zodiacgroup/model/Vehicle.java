package com.zodiacgroup.model;

import javax.persistence.*;

@Entity
@Table(name = "VEHICLES")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VEHICLEID")
    private int vehicleId;
    
    @Column(name = "MAKE", nullable = false)
    private String make;
    
    @Column(name = "MODEL", nullable = false)
    private String model;
    
    @Column(name = "YEAR", nullable = false)
    private int year;
    
    @Column(name = "VIN", unique = true, nullable = false)
    private String vin;
    
    @ManyToOne
    @JoinColumn(name = "CustomerID", nullable = false)
    private Customer customer;

    @Override
    public String toString() {
        return getMake() + " " + getModel() + " (" + getVehicleId() + ")";
    }
    
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}