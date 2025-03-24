package com.zodiacgroup.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DeleteCustomerController {

    @FXML
    private TextField appointmentIdField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    // Handles the "Delete" button click
    @FXML
    private void handleDeleteCustomer() {
        String appointmentId = appointmentIdField.getText().trim();
        
        if (appointmentId.isEmpty()) {
            showAlert("Error", "Please enter an appointment ID to delete.");
            return;
        }

        // Simulate deleting the appointment (you can replace this with database logic)
        showAlert("Success", "Appointment ID " + appointmentId + " deleted successfully!");
        appointmentIdField.clear(); // Clear the field after deletion
    }

    // Handles the "Cancel" button click
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Load the customer.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/customer.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();

            // Replace the current scene with the customer list
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Customer List"); // Optionally set a new title
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        appointmentIdField.clear();
    }

    // Utility method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
