package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Vehicle;

public class AddVehiclesController extends BaseController implements Initializable{

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
    private Integer customerId;

    private VehicleDAO vehicleDAO = new VehicleDAO();
    
    private Customer customer; 
    
    
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
    }

    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);

        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerId = customer.getCustomerId();
        if (customerIdField != null) {
            customerIdField.setText("ID: " + customer.getCustomerId() + ",     Name: " + 
                    customer.getFirstName() + " " + customer.getLastName());
        }
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
        customerIdField.setText(String.valueOf(customerId));
        customerIdField.setEditable(false); 
    }
    
  

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        try {
            Vehicle vehicle = new Vehicle();
            
            Customer customer = new Customer();
            customer.setCustomerId(customerId);
            vehicle.setCustomer(customer);
            
            vehicle.setMake(makeField.getText().trim());
            vehicle.setModel(modelField.getText().trim());
            vehicle.setYear(Integer.parseInt(yearField.getText().trim()));
            vehicle.setVin(vinField.getText().trim());

            vehicleDAO.addVehicle(vehicle);
            
            showAlert("Success", "Vehicle added successfully!");
            handleBackButtonAction(event);
        } catch (Exception e) {
            showAlert("Error", "Failed to add vehicle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        if (makeField.getText().isEmpty() || modelField.getText().isEmpty() || 
            yearField.getText().isEmpty() || vinField.getText().isEmpty()) {
            showAlert("Error", "All fields are required!");
            return false;
        }
        
        try {
            int year = Integer.parseInt(yearField.getText());
            if (year < 1900 || year > java.time.Year.now().getValue() + 1) {
                showAlert("Error", "Please enter a valid year between 1900 and current year+1");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Year must be a number");
            return false;
        }
        
        if (vinField.getText().length() != 17) {
            showAlert("Error", "VIN must be exactly 17 characters");
            return false;
        }
        
        return true;
    }

    private Customer getCustomerById(int customerId) {
        CustomerDAO customerDAO = new CustomerDAO();
        return customerDAO.getCustomerById(customerId); 
    }



    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        vehicleIdField.clear();
        customerIdField.clear();
        makeField.clear();
        modelField.clear();
        yearField.clear();
        vinField.clear();
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}