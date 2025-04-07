package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Vehicle;

public class EditVehiclesController extends BaseController implements Initializable {

    @FXML private TextField vehicleIdField;
    @FXML private TextField customerIdField;
    @FXML private TextField makeField;
    @FXML private TextField modelField;
    @FXML private TextField yearField;
    @FXML private TextField vinField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    

    private VehicleDAO vehicleDAO = new VehicleDAO();
    private Vehicle currentVehicle;
    private Customer customer;
    private int customerId; 

       
       @FXML
       private Label lblHome, lblCustomer, lblSalesRep, lblManager, lblLogout, lblInventory, lblPayment, lblViewReport;
       private String currentRole;

       @Override
       public void initialize(URL location, ResourceBundle resources) {
    	   setCurrentPage("customer");
           initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
           if (lblCustomer != null) {
        	   lblCustomer.setStyle("-fx-font-weight: bold; -fx-underline: true;");
           }
           if (currentRole != null) {
               setRole(currentRole);
           }
       }

       public void setRole(String role) {
           this.currentRole = role;
           configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                   lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);

           setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                   lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
       }

    public void setVehicleData(Vehicle vehicle) {
        this.currentVehicle = vehicle;
        if (vehicle != null) {
            this.customer = vehicle.getCustomer(); 
            this.customerId = customer.getCustomerId();
            vehicleIdField.setText(String.valueOf(vehicle.getVehicleId()));
            customerIdField.setText(customer.getFirstName() + " " + customer.getLastName());
            makeField.setText(vehicle.getMake());
            modelField.setText(vehicle.getModel());
            yearField.setText(String.valueOf(vehicle.getYear()));
            vinField.setText(vehicle.getVin());
        }
    }

    

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        if (makeField.getText().isEmpty() || modelField.getText().isEmpty() || 
            yearField.getText().isEmpty() || vinField.getText().isEmpty()) {
            showAlert("Validation Error", "All fields are required");
            return;
        }

        try {
            int year = Integer.parseInt(yearField.getText());
            
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Are you sure you want to update this vehicle?");
            confirmAlert.setContentText("This will modify the vehicle details in the database.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                currentVehicle.setMake(makeField.getText());
                currentVehicle.setModel(modelField.getText());
                currentVehicle.setYear(year);
                currentVehicle.setVin(vinField.getText());

                vehicleDAO.updateVehicle(currentVehicle);

                showAlert("Success", "Vehicle updated successfully!");
                navigateBackToVehicles();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Year must be a valid number");
        } catch (Exception e) {
            showAlert("Error", "Failed to update vehicle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        if (currentVehicle == null) return;

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this vehicle?");
        confirmAlert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                vehicleDAO.deleteVehicle(currentVehicle.getVehicleId());
                showAlert("Success", "Vehicle deleted successfully!");
                navigateBackToVehicles();
            } catch (Exception e) {
                showAlert("Error", "Failed to delete vehicle: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        navigateBackToVehicles();
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/vehicles.fxml"));
            Parent root = loader.load();
            
            VehiclesController controller = loader.getController();
            if (customer != null) {
                controller.setCustomer(customer);
            }
            controller.setRole(currentRole); 
            controller.setMainApp(mainApp); 
            
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Vehicles Management");
        } catch (IOException e) {
            System.err.println("Error navigating back: " + e.getMessage());
            showAlert("Navigation Error", "Could not return to vehicles view");
        }
    }

    private void navigateBackToVehicles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/vehicles.fxml"));
            Parent root = loader.load();

            VehiclesController controller = loader.getController();
            controller.setRole(currentRole); 
            controller.setMainApp(mainApp); 
            controller.setCustomer(customer); 
            controller.refreshVehicleTable();

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(customer.getFirstName() + "'s Vehicles");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load vehicle list view");
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