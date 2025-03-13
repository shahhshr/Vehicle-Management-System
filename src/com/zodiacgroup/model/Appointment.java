package com.zodiacgroup.model;

import javax.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // The primary key
    private int id;

    @Column(name = "appointment_id", unique = true, nullable = false)
    private String appointmentId;  

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)  // Foreign key reference to Customer
    private Customer customer;

    @Column(name = "vehicle_name", nullable = false)
    private String vehicleName;

    // Constructors
    public Appointment() {}

    public Appointment(String appointmentId, Customer customer, String vehicleName) {
        this.appointmentId = appointmentId;
        this.customer = customer;
        this.vehicleName = vehicleName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
}
