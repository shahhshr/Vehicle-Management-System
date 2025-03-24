package com.zodiacgroup.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // The primary key
    private int id;

    @Column(name = "appointment_id", unique = true, nullable = false)
    private String appointmentId;  // Field for appointment ID

    @Column(name = "vehicle_id", nullable = false)
    private int vehicleId;  // Field for vehicle ID

    @Column(name = "customer_name", nullable = false)
    private String customerName;  // Field for customer name

    @Column(name = "appointment_date", nullable = false)
    private Date appointmentDate;  // Field for appointment date

    @Column(name = "service_type", nullable = false)
    private String serviceType;  // Field for service type

    @Column(name = "status", nullable = false)
    private String status;  // Field for status

    // Constructors
    public Appointment() {}

    public Appointment(String appointmentId, int vehicleId, String customerName, Date appointmentDate, String serviceType, String status) {
        this.appointmentId = appointmentId;
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.appointmentDate = appointmentDate;
        this.serviceType = serviceType;
        this.status = status;
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

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}