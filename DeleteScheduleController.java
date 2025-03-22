package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class DeleteScheduleController {

    @FXML
    private TextField customerIdField; // Matches the fx:id in the FXML file

    // Method to handle the "Delete" button click
    @FXML
    private void handleDeleteAppointment() {
        // Add logic to delete the schedule
        if (customerIdField.getText().isEmpty()) {
            showAlert("Error", "Please enter a valid Service ID.");
            return;
        }

        // Simulate deleting the schedule
        System.out.println("Schedule with ID " + customerIdField.getText() + " deleted!");
        showAlert("Success", "Schedule deleted successfully.");
    }

    // Method to handle the "Back" button click
    @FXML
    private void handleBackButtonAction() {
        try {
            // Load the ScheduleService.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ScheduleService.fxml"));
            Parent root = loader.load();

            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Schedule Service");
            stage.setScene(new Scene(root));

            // Show the form
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) customerIdField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the Schedule Service Form.");
            e.printStackTrace();
        }
    }

    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}