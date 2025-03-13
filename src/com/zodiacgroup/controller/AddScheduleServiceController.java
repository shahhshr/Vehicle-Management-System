package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.model.Service;

public class AddScheduleServiceController {
    @FXML
    private TextField serviceIdField;

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField vehicleNameField;

    @FXML
    private TextField serviceDetailField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField timeField;

    @FXML
    private TextField mechanicField;

    private ServiceDAO serviceDAO;

    public AddScheduleServiceController() {
        serviceDAO = new ServiceDAO();
    }

    
}
