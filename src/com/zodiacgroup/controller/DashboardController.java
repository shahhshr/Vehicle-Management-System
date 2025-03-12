package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DashboardController {

    @FXML
    private Button btnAddCustomer, btnScheduleService, btnGenerateInvoice, btnAssignMechanic, btnViewReports;

    @FXML
    public void initialize() {
        btnAddCustomer.setOnAction(e -> System.out.println("Add Customer Clicked"));
        btnScheduleService.setOnAction(e -> System.out.println("Schedule Service Clicked"));
        btnGenerateInvoice.setOnAction(e -> System.out.println("Generate Invoice Clicked"));
        btnAssignMechanic.setOnAction(e -> System.out.println("Assign Mechanic Clicked"));
        btnViewReports.setOnAction(e -> System.out.println("View Reports Clicked"));
    }
}
