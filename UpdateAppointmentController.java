package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateAppointmentController {

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

    // Method to handle the "Update" button click
    @FXML
    private void handleUpdateAppointment() {
        // Add logic to update the appointment
        System.out.println("Appointment updated!");
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
            showAlert("Error", "Failed to load the Appointment Form.");
            e.printStackTrace();
        }
    }

    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}