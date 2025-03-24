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

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Vehicle;

public class AddVehiclesController {

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
    private Button saveButton, cancelButton;

    private VehicleDAO vehicleDAO = new VehicleDAO();

    // Handles the "Save" button click
    
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        try {
            // Retrieve customerId from the input
            int customerId = Integer.parseInt(customerIdField.getText());

            // Fetch the customer from the database (assuming you have a CustomerDAO or a method to get the customer)
            Customer customer = getCustomerById(customerId);
            if (customer == null) {
                showAlert("Error", "Customer not found.");
                return;
            }

            // Create a new Vehicle object and set the values
            Vehicle vehicle = new Vehicle();
            vehicle.setCustomer(customer); // Set the Customer object, not the ID
            vehicle.setMake(makeField.getText());
            vehicle.setModel(modelField.getText());
            vehicle.setYear(Integer.parseInt(yearField.getText()));
            vehicle.setVin(vinField.getText());

            // Add the vehicle to the database
            vehicleDAO.addVehicle(vehicle);

            // Clear the form fields
            clearFields();

            // Close the current window
            Stage currentStage = (Stage) saveButton.getScene().getWindow();
            currentStage.close();
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input. Please check the fields and try again.");
        }
    }


    private Customer getCustomerById(int customerId) {
        // Replace this with actual logic to fetch a Customer by ID from the database
        CustomerDAO customerDAO = new CustomerDAO();
        return customerDAO.getCustomerById(customerId); // Assuming this method exists in your DAO
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