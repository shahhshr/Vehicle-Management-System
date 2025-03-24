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

import com.zodiacgroup.dao.VehicleDAO;

public class DeleteVehicleController {

    @FXML
    private TextField vehicleIdField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    private VehicleDAO vehicleDAO = new VehicleDAO();

    // Handles the "Delete" button click
    @FXML
    private void handleDeleteVehicle() {
        String vehicleIdText = vehicleIdField.getText().trim();

        if (vehicleIdText.isEmpty()) {
            showAlert("Error", "Please enter a Vehicle ID to delete.");
            return;
        }

        try {
            int vehicleId = Integer.parseInt(vehicleIdText);
            vehicleDAO.deleteVehicle(vehicleId); // Delete the vehicle from the database
            showAlert("Success", "Vehicle ID " + vehicleId + " deleted successfully!");
            vehicleIdField.clear(); // Clear the field after deletion
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Vehicle ID. Please enter a valid number.");
        }
    }

    // Handles the "Cancel" button click
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        vehicleIdField.clear();
    }

    // Handles the "Back" button click
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Load the vehicles.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/vehicles.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();

            // Replace the current scene with the vehicles list
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Vehicle List"); // Optionally set a new title
        } catch (IOException e) {
            e.printStackTrace();
        }
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