package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.model.Vehicle;

public class EditVehiclesController {

    @FXML
    private TextField vehicleIdField;

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField makeField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField vinField;

    @FXML
    private Button cancelButton;

    private VehicleDAO vehicleDAO = new VehicleDAO();

    

    // Handles the "Delete" button click
    @FXML
    private void handleDelete() {
        try {
            int vehicleId = Integer.parseInt(vehicleIdField.getText());
            vehicleDAO.deleteVehicle(vehicleId); // Delete the vehicle from the database
            showAlert("Success", "Vehicle deleted successfully!");
            clearFields(); // Clear the form fields
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Vehicle ID. Please enter a valid number.");
        }
    }

    // Handles the "Cancel" button click
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        clearFields();
    }

    // Clears all the form fields
    private void clearFields() {
        vehicleIdField.clear();
        customerIdField.clear();
        makeField.clear();
        modelField.clear();
        yearField.clear();
        vinField.clear();
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