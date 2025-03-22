package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing AppointmentController...");

        // Check if serviceTypeField is null
        if (serviceTypeField == null) {
            System.err.println("Error: serviceTypeField is null!");
        } else {
            // Populate the Service Type ComboBox
            serviceTypeField.getItems().clear();
            serviceTypeField.getItems().addAll("Oil Change", "Tire Rotation", "Brake Repair", "Engine Tune-Up");

            // Debugging: Print the items in the console
            System.out.println("Service Type Items: " + serviceTypeField.getItems());
        }

        // Check if statusField is null
        if (statusField == null) {
            System.err.println("Error: statusField is null!");
        } else {
            // Populate the Status ComboBox
            statusField.getItems().clear();
            statusField.getItems().addAll("Scheduled", "In Progress", "Completed", "Cancelled");

            // Debugging: Print the items in the console
            System.out.println("Status Items: " + statusField.getItems());
        }
    }

    // Method to enable Drag and Drop functionality for ComboBox
    private void enableDragAndDrop(ComboBox<String> comboBox) {
        comboBox.setOnDragDetected(event -> {
            if (comboBox.getValue() != null) {
                Dragboard db = comboBox.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(comboBox.getValue());
                db.setContent(content);
            }
            event.consume();
        });

        comboBox.setOnDragOver(event -> {
            if (event.getGestureSource() != comboBox && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        comboBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                comboBox.setValue(db.getString());
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    // Method to handle saving the appointment
    @FXML
    private void handleSaveAppointment() {
        if (validateFields()) {
            // Retrieve data from the form fields
            String appointmentId = appointmentIdField.getText();
            String vehicleId = vehicleIdField.getText();
            String customerName = customerNameField.getText();
            String appointmentDate = appointmentDateField.getValue() != null ? appointmentDateField.getValue().toString() : "";
            String serviceType = serviceTypeField.getValue();
            String status = statusField.getValue();

            // Simulate saving the data (you can replace this with actual database storage)
            System.out.println("Appointment Saved:");
            System.out.println("Appointment ID: " + appointmentId);
            System.out.println("Vehicle ID: " + vehicleId);
            System.out.println("Customer Name: " + customerName);
            System.out.println("Appointment Date: " + appointmentDate);
            System.out.println("Service Type: " + serviceType);
            System.out.println("Status: " + status);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment saved successfully!");

            // Clear the form fields after saving
            clearForm();
        }
    }

    // Method to validate required fields
    private boolean validateFields() {
        if (appointmentIdField.getText().isEmpty() || vehicleIdField.getText().isEmpty() ||
            customerNameField.getText().isEmpty() || appointmentDateField.getValue() == null ||
            serviceTypeField.getValue() == null || statusField.getValue() == null) {
            
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required!");
            return false;
        }
        return true;
    }

    // Method to show alert messages
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to clear the form fields
    private void clearForm() {
        appointmentIdField.clear();
        vehicleIdField.clear();
        customerNameField.clear();
        appointmentDateField.setValue(null);
        serviceTypeField.setValue(null);
        statusField.setValue(null);
    }

    // Method to load another FXML form
    @FXML
    private void handleAddButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddAppointmentForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Appointment");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Add Appointment Form.");
            e.printStackTrace();
        }
    }

    // Method to handle the "Back" button click
    @FXML
    private void handleBackButtonAction() {
        try {
            // Load the Appointment.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Appointment.fxml"));
            Parent root = loader.load();

            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Appointment");
            stage.setScene(new Scene(root));

            // Show the form
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) appointmentIdField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Appointment Form.");
            e.printStackTrace();
        }
    }
}