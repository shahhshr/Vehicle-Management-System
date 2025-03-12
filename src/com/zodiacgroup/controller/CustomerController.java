package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.model.Customer;

public class CustomerController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    private CustomerDAO customerDAO;

    public CustomerController() {
        customerDAO = new CustomerDAO();
    }

    
}