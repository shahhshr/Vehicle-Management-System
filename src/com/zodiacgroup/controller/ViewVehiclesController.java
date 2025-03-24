package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.model.Vehicle;

public class ViewVehiclesController {

    @FXML
    private Label vehicleIdLabel;

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

    private VehicleDAO vehicleDAO;

    public ViewVehiclesController() {
        vehicleDAO = new VehicleDAO();
    }

    @FXML
    public void initialize() {
        // Load vehicle data when the view is initialized
        loadVehicleData();
    }

    /**
     * Loads vehicle data from the database and displays it in the UI.
     */
    private void loadVehicleData() {
        // Example: Fetch the first vehicle from the database
        // You can modify this to fetch a specific vehicle by ID or other criteria
        Vehicle vehicle = vehicleDAO.getAllVehicles().get(0);

        if (vehicle != null) {
            // Bind vehicle data to the UI components
            vehicleIdLabel.setText(String.valueOf(vehicle.getVehicleId()));

            // Access the customer ID through the Vehicle's Customer object
            if (vehicle.getCustomer() != null) {
                customerIdField.setText(String.valueOf(vehicle.getCustomer().getCustomerId())); // Assuming getCustomerId() is a method in the Customer class
            } else {
                customerIdField.setText("No customer assigned");
            }

            makeField.setText(vehicle.getMake());
            modelField.setText(vehicle.getModel());
            yearField.setText(String.valueOf(vehicle.getYear()));
            vinField.setText(vehicle.getVin());
        } else {
            // Handle case where no vehicle is found
            vehicleIdLabel.setText("No vehicle found");
            customerIdField.clear();
            makeField.clear();
            modelField.clear();
            yearField.clear();
            vinField.clear();
        }
    }


    /**
     * Handles the "Refresh" button click to reload vehicle data.
     */
    @FXML
    private void handleRefresh() {
        loadVehicleData();
    }

    /**
     * Handles the "Back" button click to navigate to the previous screen.
     */
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
}