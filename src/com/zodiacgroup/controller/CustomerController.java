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

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.model.Customer;

public class CustomerController {
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
    private TableView<Customer> customerTable;
    @FXML
    private CustomerDAO customerDAO;
    @FXML
    private Button addButton, deleteButton, updateButton;

    public CustomerController() {
        customerDAO = new CustomerDAO();
    }

    @FXML
    private void handleOpenAddCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddCustomer.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) addButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Add Customer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleUpdateButtonAction() {
    	try {
            // Load the deleteCustomer.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/editCustomer.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage currentStage = (Stage) updateButton.getScene().getWindow();

            // Replace the current scene with the delete customer page
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Update Customer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        try {
            // Load the deleteCustomer.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/deleteCustomer.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage currentStage = (Stage) deleteButton.getScene().getWindow();

            // Replace the current scene with the delete customer page
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Delete Customer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Refresh customer list after adding a new one
    @FXML
    private void refreshCustomerList() {
        // Clear the table and reload data from the database
        customerTable.getItems().clear();
        customerTable.getItems().addAll(customerDAO.getAllCustomers());
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}