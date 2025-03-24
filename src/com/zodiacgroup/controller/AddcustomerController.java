package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.io.IOException;

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.model.Customer;

public class AddCustomerController {

	@FXML
    private TextField inputCustomerFirstNameField;
    @FXML
    private TextField inputCustomerLastNameField;
    @FXML
    private TextField inputCustomerPhoneNumberField;
    @FXML
    private TextField inputCustomerEmailIdField;
    @FXML
    private TextArea inputCustomerAddressField; // Change this to TextArea
    @FXML
    private Button saveButton, cancelButton;




    private CustomerDAO customerDAO = new CustomerDAO();

    

    @FXML
    private void handleCancelButtonAction() {
        // Clear the form fields
        clearFields();
    }

    private void clearFields() {
    	inputCustomerFirstNameField.clear();
    	inputCustomerLastNameField.clear();
    	inputCustomerPhoneNumberField.clear();
    	inputCustomerEmailIdField.clear();
    	inputCustomerAddressField.clear();
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
    
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        // Create a new Customer object
        Customer customer = new Customer();
        customer.setFirstName(inputCustomerFirstNameField.getText());
        customer.setLastName(inputCustomerLastNameField.getText());
        customer.setPhoneNumber(inputCustomerPhoneNumberField.getText());
        customer.setEmail(inputCustomerEmailIdField.getText());
        customer.setAddress(inputCustomerAddressField.getText());

        // Add the customer to the database
        customerDAO.addCustomer(customer);

        // Clear the form fields
        clearFields();

        // Close the current window
        Stage currentStage = (Stage) saveButton.getScene().getWindow();
        currentStage.close();
    }
}