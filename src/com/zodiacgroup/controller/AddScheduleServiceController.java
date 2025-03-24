package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddScheduleServiceController implements Initializable {

    @FXML
    private TextField customerIDField, customerNameField, vehicleField, serviceDetailField, timeField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> mechanicComboBox;

    @FXML
    private Button saveButton, cancelButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate ComboBox with mechanic names
        mechanicComboBox.getItems().addAll(
            "John Doe", "Jane Smith", "Robert Brown", "Emily Johnson"
        );
    }

    @FXML
    private void saveService(ActionEvent event) {
        // Example validation
        if (customerIDField.getText().isEmpty() || customerNameField.getText().isEmpty() ||
            vehicleField.getText().isEmpty() || serviceDetailField.getText().isEmpty() ||
            datePicker.getValue() == null || timeField.getText().isEmpty() || mechanicComboBox.getValue() == null) {
            
            showAlert("Error", "Please fill in all fields before saving.");
            return;
        }
        
        // Logic to save the service appointment
        System.out.println("Service Appointment Saved");
        showAlert("Success", "Service appointment saved successfully.");
    }

    @FXML
    private void cancelService(ActionEvent event) {
        // Clearing fields
        customerIDField.clear();
        customerNameField.clear();
        vehicleField.clear();
        serviceDetailField.clear();
        timeField.clear();
        datePicker.setValue(null);
        mechanicComboBox.setValue(null);
    }

    // Method to handle the "Back" button click
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
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
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Schedule Service Form: " + e.getMessage());
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