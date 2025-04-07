package com.zodiacgroup.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "SERVICE")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SERVICEID")
    private int id;
    
    @Column(name = "CUSTOMERID")
    private int customerId;
    
    @Column(name = "CUSTOMERNAME")
    private String customerName;
    
    @Column(name = "VEHICLENAME")
    private String vehicleName;
    
    @Column(name = "SERVICEDETAIL")
    private String serviceDetail;
    
    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "SERVICEDATE") 
    private Date date;
    
    @Column(name = "COST")
    private double cost;
    
    @Column(name = "VEHICLEID")
    private Integer vehicleId;  
    
    @Column(name = "MechanicName")
    private String mechanicName;

    // Add getter and setter
    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }
    
   

    // Constructors
    public Service() {}

    public Service(int id, int customerId, String customerName, String vehicleName, 
            String serviceDetail, String status, Date date, double cost, String mechanicName) {
  this.id = id;
  this.customerId = customerId;
  this.customerName = customerName;
  this.vehicleName = vehicleName;
  this.serviceDetail = serviceDetail;
  this.status = status;
  this.date = date;
  this.cost = cost;
  this.mechanicName = mechanicName;
}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getMechanicName() {
        return mechanicName;
    }

    public void setMechanicName(String mechanicName) {
        this.mechanicName = mechanicName;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", vehicleName='" + vehicleName + '\'' +
                ", serviceDetail='" + serviceDetail + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", cost=" + cost +
                 ", mechanicName='" + mechanicName + '\'' +
                '}';
    }
}