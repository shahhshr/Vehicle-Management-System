package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class ScheduleServiceController {

    @FXML
    private Button addButton;

    // Method to handle the "Add" button click
    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        try {
            // Load the AddScheduleService.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddScheduleService.fxml"));
            Parent root = loader.load();

            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Add Schedule Service");
            stage.setScene(new Scene(root));

            // Show the form
            stage.show();

            // Close the current window (optional)
            Stage currentStage = (Stage) addButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Add Schedule Service Form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private Button updateButton;

    // Method to handle the "Update" button click
    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        try {
            // Load the UpdateScheduleService.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/UpdateScheduleService.fxml"));
            Parent root = loader.load();

            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Update Schedule Service");
            stage.setScene(new Scene(root));

            // Show the form
            stage.show();

            // Close the current window (optional)
            Stage currentStage = (Stage) updateButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Update Schedule Service Form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private Button deleteButton;

    // Method to handle the "Delete" button click
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        try {
            // Load the DeleteSchedule.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/DeleteSchedule.fxml"));
            Parent root = loader.load();

            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Delete Schedule");
            stage.setScene(new Scene(root));

            // Show the form
            stage.show();

            // Close the current window (optional)
            Stage currentStage = (Stage) deleteButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Delete Schedule Form: " + e.getMessage());
            e.printStackTrace();
        }
    }
}