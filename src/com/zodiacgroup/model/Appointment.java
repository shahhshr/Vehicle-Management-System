package com.zodiacgroup.model;

import javax.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "APPOINTMENTS")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPOINTMENTID")
    private int appointmentId;
    
    @Column(name = "CUSTOMERID", nullable = false)
    private int customerId;

    @Column(name = "VEHICLEID", nullable = false)
    private int vehicleId;

    @Column(name = "CUSTOMERNAME", nullable = false)
    private String customerName;

    @Column(name = "APPOINTMENTDATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;

    @Column(name = "SERVICETYPE", nullable = false)
    private String serviceType;

    @Column(name = "STATUS", nullable = false)
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    // Constructors
    public Appointment() {
    }

    public Appointment(int vehicleId, String customerName, Date appointmentDate, 
                      String serviceType, String status) {
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.appointmentDate = appointmentDate;
        this.serviceType = serviceType;
        this.status = status;
    }

    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", vehicleId=" + vehicleId +
                ", customerName='" + customerName + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", serviceType='" + serviceType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    
    public String getAppointmentDateFormatted() {
        if (appointmentDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(appointmentDate);
        }
        return "";
    }
}