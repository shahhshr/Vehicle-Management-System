package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class DeleteAppointmentController {

    @FXML
    private TextField appointmentIdField;

    // Method to handle the "Delete" button click
    @FXML
    private void handleDeleteAppointment() {
        // Add logic to delete the appointment
        System.out.println("Appointment deleted!");
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