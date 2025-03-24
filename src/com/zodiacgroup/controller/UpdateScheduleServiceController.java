package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class UpdateScheduleServiceController {

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField vehicleField;

    @FXML
    private TextField serviceDetailField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField timeField;

    @FXML
    private ComboBox<String> mechanicComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize() {
        // Populate ComboBox with mechanic names
        mechanicComboBox.getItems().addAll(
            "John Doe", "Jane Smith", "Robert Brown", "Emily Johnson"
        );
    }

    @FXML
    private void handleSaveButtonAction() {
        // Logic to save the updated service appointment
        System.out.println("Service Appointment Updated");
    }

    @FXML
    private void handleCancelButtonAction() {
        // Logic to cancel the update
        System.out.println("Update Cancelled");
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
}