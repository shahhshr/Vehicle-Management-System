package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.zodiacgroup.dao.AppointmentDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Appointment;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateAppointmentController extends BaseController implements Initializable {

    @FXML private TextField appointmentIdField;
    @FXML private TextField vehicleIdField;
    @FXML private TextField customerNameField;
    @FXML private DatePicker appointmentDateField;
    @FXML private ComboBox<String> serviceTypeField;
    @FXML private ComboBox<String> statusField;
    @FXML private Button cancelButton;
    
    // Side menu labels
    @FXML private Label lblHome;
    @FXML private Label lblCustomer;
    @FXML private Label lblPayment;
    @FXML private Label lblInventory;
    @FXML private Label lblSalesRep;
    @FXML private Label lblManager;
    @FXML private Label lblViewReport;
    @FXML private Label lblLogout;

    private String currentRole;
    private Appointment appointment;
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private Integer customerId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	setCurrentPage("customer");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
		if (lblCustomer != null) {
            lblCustomer.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        initializeComboBoxes();
    }

    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                               lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                      lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
    }
 


    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    private void initializeComboBoxes() {
        serviceTypeField.getItems().addAll(
        		"Oil Change",
                "Tire Rotation",
                "Brake Service",
                "Engine Diagnostic",
                "Transmission Service",
                "Battery Replacement",
                "Air Conditioning Service",
                "Wheel Alignment",
                "Exhaust System Repair",
                "General Inspection"
        );
        
        statusField.getItems().addAll(
            "Scheduled", "Completed", "Cancelled", "No-show"
        );
    }

    public void setAppointmentData(Appointment appointment) {
        this.appointment = appointment;
        
        appointmentIdField.setText(String.valueOf(appointment.getAppointmentId()));
        vehicleIdField.setText(String.valueOf(appointment.getVehicleId()));
        customerNameField.setText(appointment.getCustomerName());
        
        setAppointmentDate(appointment.getAppointmentDate());
        
        serviceTypeField.setValue(appointment.getServiceType());
        statusField.setValue(appointment.getStatus());
    }

    private void setAppointmentDate(Date appointmentDate) {
        LocalDate localDate;
        
        if (appointmentDate instanceof java.sql.Date) {
            localDate = ((java.sql.Date) appointmentDate).toLocalDate();
        } else {
            localDate = appointmentDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        
        appointmentDateField.setValue(localDate);
    }

    @FXML
    private void handleUpdateAppointment(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        if (confirmUpdate()) {
            updateAppointmentDetails();
            appointmentDAO.updateAppointment(appointment);
            showAlert("Success", "Appointment updated successfully!");
            navigateToAppointmentList();
        }
    }

    private boolean validateInputs() {
        return !customerNameField.getText().isEmpty() 
                && serviceTypeField.getValue() != null 
                && statusField.getValue() != null 
                && appointmentDateField.getValue() != null;
    }

    private boolean confirmUpdate() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Update");
        confirmAlert.setHeaderText("Are you sure you want to update this appointment?");
        confirmAlert.setContentText("This will modify the appointment details in the database.");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void updateAppointmentDetails() {
        appointment.setVehicleId(Integer.parseInt(vehicleIdField.getText()));
        appointment.setCustomerName(customerNameField.getText());
        
        LocalDate localDate = appointmentDateField.getValue();
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        appointment.setAppointmentDate(new java.sql.Date(instant.toEpochMilli()));
        
        appointment.setServiceType(serviceTypeField.getValue());
        appointment.setStatus(statusField.getValue());
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        navigateToAppointmentList();
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Appointment.fxml"));
            Parent root = loader.load();
            
            AppointmentController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            
            if (customerId != null) {
                controller.setCustomerId(customerId);
            }
            controller.refreshAppointmentTable();
            
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Could not load appointment list view");
            e.printStackTrace();
        }
    }

    private void navigateToAppointmentList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Appointment.fxml"));
            Parent root = loader.load();
            
            AppointmentController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            controller.setCustomerId(customerId);
            controller.refreshAppointmentTable();
            
            Stage stage = (Stage) appointmentIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Could not load appointment list view");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}