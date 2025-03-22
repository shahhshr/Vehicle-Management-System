package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import com.zodiacgroup.dao.AppointmentDAO;
import com.zodiacgroup.model.Appointment;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AppointmentController {

    // Form fields
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

    private AppointmentDAO appointmentDAO;

    public AppointmentController() {
        appointmentDAO = new AppointmentDAO();
    }

    // Method to handle saving the appointment
    @FXML
    private void handleSaveAppointment() {
        try {
            // Retrieve data from the form fields
            int appointmentId = Integer.parseInt(appointmentIdField.getText());
            int vehicleId = Integer.parseInt(vehicleIdField.getText());
            String customerName = customerNameField.getText();
            LocalDate localDate = appointmentDateField.getValue();
            Date appointmentDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String serviceType = serviceTypeField.getValue();
            String status = statusField.getValue();

            // Validate input
            if (customerName.isEmpty() || serviceType == null || status == null) {
                showAlert("Input Error", "Please fill in all fields.");
                return;
            }

            // Create an Appointment object
            // Appointment appointment = new Appointment(appointmentId, vehicleId, customerName, appointmentDate, serviceType, status);

            // Save the appointment using the DAO
            // appointmentDAO.saveAppointment(appointment);

            // Show success message
            showAlert("Success", "Appointment saved successfully!");

            // Clear the form fields after saving
            clearForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Appointment ID and Vehicle ID must be valid numbers.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred while saving the appointment: " + e.getMessage());
        }
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

    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to handle the "Add" button click
    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        try {
            // Load the AddAppointmentForm.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddAppointmentForm.fxml"));
            Parent root = loader.load();

            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Add Appointment");
            stage.setScene(new Scene(root));

            // Show the form
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Add Appointment Form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to handle the "Update" button click
    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        try {
            System.out.println("Update button clicked. Loading UpdateAppointment.fxml...");

            // Load the UpdateAppointmentForm.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/UpdateAppointment.fxml"));
            Parent root = loader.load();

            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Update Appointment");
            stage.setScene(new Scene(root));

            // Show the form
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Update Appointment Form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to handle the "Delete" button click
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        try {
            System.out.println("Delete button clicked. Loading DeleteAppointment.fxml...");

            // Load the DeleteAppointment.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/DeleteAppointment.fxml"));
            Parent root = loader.load();

            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Delete Appointment");
            stage.setScene(new Scene(root));

            // Show the form
            stage.show();

            // Close the current window (optional)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Delete Appointment Form: " + e.getMessage());
            e.printStackTrace();
        }
    }
}