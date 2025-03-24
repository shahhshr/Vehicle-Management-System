package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.model.Customer;

public class EditCustomerController {

    @FXML
    private TextField customerIdField;

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

    private CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    private void handleSave() {
        // Get the customer ID
        int customerId = Integer.parseInt(customerIdField.getText());

        // Fetch the customer from the database
        Customer customer = customerDAO.getCustomerById(customerId);

        if (customer != null) {
            // Update the customer details
            customer.setFirstName(firstNameField.getText());
            customer.setLastName(lastNameField.getText());
            customer.setPhoneNumber(phoneNumberField.getText());
            customer.setEmail(emailField.getText());
            customer.setAddress(addressField.getText());

            // Save the updated customer to the database
            customerDAO.updateCustomer(customer);
        }
    }

    @FXML
    private void handleDelete() {
        // Get the customer ID
        int customerId = Integer.parseInt(customerIdField.getText());

        // Delete the customer from the database
        customerDAO.deleteCustomer(customerId);

        // Clear the form fields
        clearFields();
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        customerIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        emailField.clear();
        addressField.clear();
    }
    
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