package com.zodiacgroup.model;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PAYMENTS")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVOICEID")
    private int invoiceId;
    
    @Column(name = "CUSTOMERID", nullable = false)
    private int customerId;
    
    @Column(name = "CUSTOMERNAME", nullable = false)
    private String customerName;
    
    @Column(name = "SERVICEID", nullable = false)
    private int serviceId;
    
    @Column(name = "AMOUNT", nullable = false)
    private double amount;
    
    @Column(name = "PAYMENTDATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    
    @Column(name = "PAYMENTMETHOD", nullable = false)
    private String paymentMethod;
    
    @Column(name = "STATUS", nullable = false)
    private String status;
    
    
    
    public Payment() {}
    
    public Payment(int customerId, String customerName, int serviceId, 
                  double amount, Date paymentDate, String paymentMethod, String status) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.serviceId = serviceId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }
    


    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
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

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}