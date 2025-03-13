package com.zodiacgroup.model;

import javax.persistence.*;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Primary key
    private int id;

    @Column(name = "service_id", unique = true, nullable = false)
    private String serviceId; // Unique service identifier

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false) // Foreign key reference to Customer
    private Customer customer;

    @Column(name = "vehicle_name", nullable = false)
    private String vehicleName;

    @Column(name = "service_detail", nullable = false)
    private String serviceDetail;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "time", nullable = false)
    private String time;

    @Column(name = "mechanic", nullable = false)
    private String mechanic;

    // Constructors
    public Service() {}

    public Service(String serviceId, Customer customer, String vehicleName, String serviceDetail, String date, String time, String mechanic) {
        this.serviceId = serviceId;
        this.customer = customer;
        this.vehicleName = vehicleName;
        this.serviceDetail = serviceDetail;
        this.date = date;
        this.time = time;
        this.mechanic = mechanic;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    public String getServiceDetail() {
        return serviceDetail;
    }

    public void setServiceDetail(String serviceDetail) {
        this.serviceDetail = serviceDetail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMechanic() {
        return mechanic;
    }

    public void setMechanic(String mechanic) {
        this.mechanic = mechanic;
    }
}
