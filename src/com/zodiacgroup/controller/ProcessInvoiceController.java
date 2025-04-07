package com.zodiacgroup.controller;

import com.zodiacgroup.dao.AppointmentDAO;
import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.dao.PaymentDAO;
import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ProcessInvoiceController extends BaseController implements Initializable {

    @FXML private Label invoiceNumberLabel;
    @FXML private Label invoiceDateLabel;
    @FXML private Label paymentMethodLabel;
    @FXML private Label paymentStatusLabel;
    @FXML private Label serviceIdLabel;
    @FXML private Label serviceDateLabel;
    @FXML private Label customerIdLabel;
    @FXML private Label customerNameLabel;
    @FXML private Label customerEmailLabel;
    @FXML private Label customerPhoneLabel;
    @FXML private Label customerAddressLabel;
    @FXML private Label vehicleIdLabel;
    @FXML private Label vehicleMakeModelLabel;
    @FXML private Label vehicleYearLabel;
    @FXML private Label vehicleVinLabel;
    @FXML private Label appointmentIdLabel;
    @FXML private Label appointmentDateLabel;
    @FXML private Label appointmentServiceTypeLabel;
    @FXML private Label appointmentStatusLabel;
    @FXML private Label serviceDetailLabel;
    @FXML private Label serviceStatusLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Button processButton;
    @FXML private Button backButton;
    
    @FXML private Label lblHome, lblCustomer, lblInventory, lblPayment, lblSalesRep, lblManager, lblViewReport, lblLogout;
    
    private String currentRole;
    private Payment currentPayment;
    private MainApp mainApp;
    private PaymentDAO paymentDAO = new PaymentDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();
    private ServiceDAO serviceDAO = new ServiceDAO();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	setCurrentPage("payment");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
        if (lblPayment != null) {
        	lblPayment.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        // Verify FXML injection
        if (processButton == null) {
            System.err.println("Warning: processButton was not injected from FXML");
        }
        if (backButton == null) {
            System.err.println("Warning: backButton was not injected from FXML");
        }
    }

    private void populateInvoiceDetails() {
        if (currentPayment != null) {
            setLabelText(invoiceNumberLabel, String.valueOf(currentPayment.getInvoiceId()));
            setLabelText(invoiceDateLabel, dateFormat.format(currentPayment.getPaymentDate()));
            setLabelText(paymentMethodLabel, currentPayment.getPaymentMethod());
            setLabelText(paymentStatusLabel, currentPayment.getStatus());
            setLabelText(serviceIdLabel, String.valueOf(currentPayment.getServiceId()));
            setLabelText(totalAmountLabel, String.format("$%.2f", currentPayment.getAmount()));
            

            if (processButton != null) {
                processButton.setDisable("Processed".equals(currentPayment.getStatus()));
            }
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
        
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
    }

    public void setPayment(Payment payment) {
        this.currentPayment = payment;
        populateInvoiceDetails();
        loadRelatedData();
    }

    

    private void loadRelatedData() {
        if (currentPayment == null) return;
        
        try {

            Customer customer = customerDAO.getCustomerById(currentPayment.getCustomerId());
            if (customer != null) {
                setLabelText(customerIdLabel, String.valueOf(customer.getCustomerId()));
                setLabelText(customerNameLabel, customer.getFirstName() + " " + customer.getLastName());
                setLabelText(customerEmailLabel, customer.getEmail());
                setLabelText(customerPhoneLabel, customer.getPhoneNumber());
                setLabelText(customerAddressLabel, customer.getAddress());
                

                List<Vehicle> vehicles = vehicleDAO.getVehiclesByCustomerId(customer.getCustomerId());
                if (!vehicles.isEmpty()) {
                    Vehicle vehicle = vehicles.get(0); 
                    setLabelText(vehicleIdLabel, String.valueOf(vehicle.getVehicleId()));
                    setLabelText(vehicleMakeModelLabel, vehicle.getMake() + " " + vehicle.getModel());
                    setLabelText(vehicleYearLabel, String.valueOf(vehicle.getYear()));
                    setLabelText(vehicleVinLabel, vehicle.getVin());
                }
            }
            

            Service service = serviceDAO.getServiceById(currentPayment.getServiceId());
            if (service != null) {
                setLabelText(serviceDateLabel, dateFormat.format(service.getDate()));
                setLabelText(serviceDetailLabel, service.getServiceDetail());
                setLabelText(serviceStatusLabel, service.getStatus());
                

                List<Vehicle> vehicles = vehicleDAO.getVehiclesByCustomerId(currentPayment.getCustomerId());
                if (!vehicles.isEmpty()) {
                    List<Appointment> appointments = appointmentDAO.getAppointmentsByVehicleId(vehicles.get(0).getVehicleId());
                    if (!appointments.isEmpty()) {

                        Appointment matchingAppointment = findMatchingAppointment(appointments, service.getDate());
                        if (matchingAppointment != null) {
                            setLabelText(appointmentIdLabel, String.valueOf(matchingAppointment.getAppointmentId()));
                            setLabelText(appointmentDateLabel, dateFormat.format(matchingAppointment.getAppointmentDate()));
                            setLabelText(appointmentServiceTypeLabel, matchingAppointment.getServiceType());
                            setLabelText(appointmentStatusLabel, matchingAppointment.getStatus());
                        }
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load related data: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private Appointment findMatchingAppointment(List<Appointment> appointments, Date serviceDate) {

        for (Appointment appt : appointments) {
            if (appt.getAppointmentDate() != null && 
                appt.getAppointmentDate().equals(serviceDate)) {
                return appt;
            }
        }

        return appointments.stream()
            .max(Comparator.comparing(Appointment::getAppointmentDate))
            .orElse(null);
    }

    private void setLabelText(Labeled label, String text) {
        if (label != null) {
            label.setText(text != null ? text : "N/A");
        }
    }

    @FXML
    private void handleProcessAction() {
        if (currentPayment == null) return;
        
        if ("Processed".equals(currentPayment.getStatus())) {
            showAlert("Info", "This invoice is already processed", Alert.AlertType.INFORMATION);
            return;
        }
        

        currentPayment.setStatus("Processed");
        
        boolean success = paymentDAO.saveOrUpdatePayment(currentPayment);
        if (success) {
            showAlert("Success", "Invoice has been processed successfully", Alert.AlertType.INFORMATION);
            if (processButton != null) {
                processButton.setDisable(true);
            }
            populateInvoiceDetails();
        } else {
            showAlert("Error", "Failed to process invoice", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleBackButtonAction() {
        if (mainApp != null) {
            try {
                mainApp.showPaymentView(currentRole);
            } catch (Exception e) {
                showAlert("Error", "Failed to return to payment view: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}