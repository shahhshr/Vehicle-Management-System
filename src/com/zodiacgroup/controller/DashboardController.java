package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class DashboardController {

    @FXML
    private Label lblHome, lblCustomer, lblVehicles, lblAppointment, lblService, lblInventory, lblPayment, lblViewReport;

    @FXML
    public void initialize() {
        // Add event handlers for navigation labels
        lblAppointment.setOnMouseClicked(event -> navigateToAppointment(event));
        lblService.setOnMouseClicked(event -> navigateToScheduleService(event));
        lblVehicles.setOnMouseClicked(event -> navigateToVehicles(event));
        lblCustomer.setOnMouseClicked(event -> navigateToCustomer(event));
        lblInventory.setOnMouseClicked(event -> navigateToInventory(event));
        lblPayment.setOnMouseClicked(event -> navigateToPaymentInvoice(event));
        lblViewReport.setOnMouseClicked(event -> navigateToViewReport(event));
    }

    private void navigateToAppointment(javafx.scene.input.MouseEvent event) {
        try {
            // Load the Appointment.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Appointment.fxml"));
            Parent root = loader.load();

            // Create a new stage for the Appointment page
            Stage stage = new Stage();
            stage.setTitle("Appointment");
            stage.setScene(new Scene(root));

            // Show the Appointment page
            stage.show();

            // Close the current dashboard window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Appointment page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void navigateToScheduleService(javafx.scene.input.MouseEvent event) {
        try {
            // Load the ScheduleService.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ScheduleService.fxml"));
            Parent root = loader.load();

            // Create a new stage for the ScheduleService page
            Stage stage = new Stage();
            stage.setTitle("Schedule Service");
            stage.setScene(new Scene(root));

            // Show the ScheduleService page
            stage.show();

            // Close the current dashboard window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading ScheduleService page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void navigateToVehicles(javafx.scene.input.MouseEvent event) {
        try {
            // Load the Vehicles.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Vehicles.fxml"));
            Parent root = loader.load();

            // Create a new stage for the Vehicles page
            Stage stage = new Stage();
            stage.setTitle("Vehicles");
            stage.setScene(new Scene(root));

            // Show the Vehicles page
            stage.show();

            // Close the current dashboard window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Vehicles page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void navigateToCustomer(javafx.scene.input.MouseEvent event) {
        try {
            // Load the Customer.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Customer.fxml"));
            Parent root = loader.load();

            // Create a new stage for the Customer page
            Stage stage = new Stage();
            stage.setTitle("Customer");
            stage.setScene(new Scene(root));

            // Show the Customer page
            stage.show();

            // Close the current dashboard window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Customer page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void navigateToInventory(javafx.scene.input.MouseEvent event) {
        try {
            // Load the Inventory.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/InventoryItems.fxml"));
            Parent root = loader.load();

            // Create a new stage for the Inventory page
            Stage stage = new Stage();
            stage.setTitle("Inventory");
            stage.setScene(new Scene(root));

            // Show the Inventory page
            stage.show();

            // Close the current dashboard window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Inventory page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void navigateToPaymentInvoice(javafx.scene.input.MouseEvent event) {
        try {
            // Load the PaymentInvoice.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/PaymentInvoice.fxml"));
            Parent root = loader.load();

            // Create a new stage for the PaymentInvoice page
            Stage stage = new Stage();
            stage.setTitle("Payment Invoice");
            stage.setScene(new Scene(root));

            // Show the PaymentInvoice page
            stage.show();

            // Close the current dashboard window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading PaymentInvoice page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void navigateToViewReport(javafx.scene.input.MouseEvent event) {
        try {
            // Load the ViewReport.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ViewReport.fxml"));
            Parent root = loader.load();

            // Create a new stage for the ViewReport page
            Stage stage = new Stage();
            stage.setTitle("View Report");
            stage.setScene(new Scene(root));

            // Show the ViewReport page
            stage.show();

            // Close the current dashboard window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading ViewReport page: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Load the Login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/login.fxml"));
            Parent root = loader.load();

            // Create a new stage for the Login page
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));

            // Show the Login page
            stage.show();

            // Close the current dashboard window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading Login page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}