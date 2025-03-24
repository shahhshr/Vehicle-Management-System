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

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.model.Customer;

public class ViewCustomerController {

    @FXML
    private Label customerIdLabel;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;
    
    @FXML
    private Button cancelButton;

    private CustomerDAO customerDAO;

    public ViewCustomerController() {
        customerDAO = new CustomerDAO();
    }

    @FXML
    public void initialize() {
        // Load customer data when the view is initialized
        loadCustomerData();
    }

    /**
     * Loads customer data from the database and displays it in the UI.
     */
    private void loadCustomerData() {
        // Example: Fetch the first customer from the database
        // You can modify this to fetch a specific customer by ID or other criteria
        Customer customer = customerDAO.getAllCustomers().get(0);

        if (customer != null) {
            // Bind customer data to the UI components
            customerIdLabel.setText(String.valueOf(customer.getCustomerId()));
            firstNameField.setText(customer.getFirstName());
            lastNameField.setText(customer.getLastName());
            phoneNumberField.setText(customer.getPhoneNumber());
            emailField.setText(customer.getEmail());
            addressField.setText(customer.getAddress());
        } else {
            // Handle case where no customer is found
            customerIdLabel.setText("No customer found");
            firstNameField.clear();
            lastNameField.clear();
            phoneNumberField.clear();
            emailField.clear();
            addressField.clear();
        }
    }

    /**
     * Handles the "Refresh" button click to reload customer data.
     */
    @FXML
    private void handleRefresh() {
        loadCustomerData();
    }

    /**
     * Handles the "Back" button click to navigate to the previous screen.
     */
 // Method to handle the "Back" button click
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Load the customer.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/customer.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();

            // Replace the current scene with the customer list
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Customer List"); // Optionally set a new title
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}