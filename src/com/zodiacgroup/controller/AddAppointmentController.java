package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddAppointmentController {

    @FXML
    private TextField appointmentIdField;

    @FXML
    private TextField vehicleIdField;

    @FXML
    private TextField customerNameField;

    @FXML
    private DatePicker appointmentDateField;

    @FXML
    private ComboBox<String> serviceTypeField;

    @FXML
    private ComboBox<String> statusField;

    // Initialize method to set up the form (optional)
    @FXML
    private void initialize() {
        // Populate ComboBoxes with default values (if needed)
        serviceTypeField.getItems().addAll("Oil Change", "Tire Rotation", "Brake Repair", "Engine Tune-Up");
        statusField.getItems().addAll("Scheduled", "In Progress", "Completed", "Cancelled");
    }

    // Method to handle saving the appointment (optional, for demonstration purposes)
    @FXML
    private void handleSaveAppointment() {
        // Retrieve data from the form fields
        String appointmentId = appointmentIdField.getText();
        String vehicleId = vehicleIdField.getText();
        String customerName = customerNameField.getText();
        String appointmentDate = appointmentDateField.getValue() != null ? appointmentDateField.getValue().toString() : "";
        String serviceType = serviceTypeField.getValue();
        String status = statusField.getValue();

        // Display the entered data (for demonstration purposes)
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Vehicle ID: " + vehicleId);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Appointment Date: " + appointmentDate);
        System.out.println("Service Type: " + serviceType);
        System.out.println("Status: " + status);

        // Clear the form fields after saving (optional)
        clearForm();
    }
    
    @FXML
    private void handleAddButtonAction() {
        try {
            // Load the AddAppointmentForm.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddAppointmentForm.fxml"));
            Parent root = loader.load();

            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Add Appointment");
            stage.setScene(new Scene(root));

            // Show the form
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading Add Appointment Form: " + e.getMessage());
            e.printStackTrace();
        }

    // Method to clear the form fields (optional)
    private void clearForm() {
        appointmentIdField.clear();
        vehicleIdField.clear();
        customerNameField.clear();
        appointmentDateField.setValue(null);
        serviceTypeField.setValue(null);
        statusField.setValue(null);
    }
}