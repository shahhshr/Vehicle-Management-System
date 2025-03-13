package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import com.zodiacgroup.dao.AppointmentDAO;
import com.zodiacgroup.model.Appointment;

public class AppointmentController {
	 @FXML
	 private TextField AppointmentIdField;
	   
	@FXML
    private TextField customerIdField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField vehicleNameField;

    private AppointmentDAO appointmentDAO;

    public AppointmentController() {
        appointmentDAO = new AppointmentDAO();
    }

  
}
