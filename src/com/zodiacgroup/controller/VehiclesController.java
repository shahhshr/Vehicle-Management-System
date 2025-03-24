package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.model.Vehicle;

public class VehiclesController {

    @FXML
    private TableView<Vehicle> vehicleTable;

    @FXML
    private Button addButton, deleteButton, updateButton;

    private VehicleDAO vehicleDAO;

    public VehiclesController() {
        vehicleDAO = new VehicleDAO();
    }

    @FXML
    public void initialize() {
        // Load all vehicles into the table when the view is initialized
        refreshVehicleTable();
    }

    @FXML
    private void handleOpenAddVehicle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddVehicles.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) addButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Add Vehicle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/EditVehicles.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) updateButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Edit Vehicle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/DeleteVehicles.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) deleteButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Delete Vehicle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshVehicleTable() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        if (vehicles != null) {
            vehicleTable.getItems().clear();
            vehicleTable.getItems().addAll(vehicles);
        } else {
            System.err.println("No vehicles found or an error occurred.");
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