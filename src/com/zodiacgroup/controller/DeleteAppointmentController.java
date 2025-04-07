package com.zodiacgroup.controller;

import com.zodiacgroup.dao.AppointmentDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Appointment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeleteAppointmentController extends BaseController implements Initializable {

    @FXML private TextField appointmentIdField;
    @FXML private TextField vehicleIdField;
    @FXML private TextField customerNameField;
    @FXML private TextField appointmentDateField;
    @FXML private TextField serviceTypeField;
    @FXML private TextField statusField;
    @FXML private Button cancelButton;

    // Side menu labels
    @FXML private Label lblHome;
    @FXML private Label lblCustomer;
    @FXML private Label lblPayment;
    @FXML private Label lblInventory;
    @FXML private Label lblSalesRep;
    @FXML private Label lblManager;
    @FXML private Label lblViewReport;
    @FXML private Label lblLogout;

    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private Appointment currentAppointment;
    private String currentRole;
    private Integer customerId;
   
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

    public void setAppointmentData(Appointment appointment) {
        this.currentAppointment = appointment;
        if (appointment != null) {
            appointmentIdField.setText(String.valueOf(appointment.getAppointmentId()));
            vehicleIdField.setText(String.valueOf(appointment.getVehicleId()));
            customerNameField.setText(appointment.getCustomerName());
            appointmentDateField.setText(appointment.getAppointmentDate().toString());
            serviceTypeField.setText(appointment.getServiceType());
            statusField.setText(appointment.getStatus());
        }
    }


    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    

    @FXML
    private void handleDeleteAppointment() {
        if (confirmDelete()) {
            boolean deleted = appointmentDAO.deleteAppointment(currentAppointment.getAppointmentId());
            if (deleted) {
                showAlert("Success", "Appointment deleted successfully!");
                navigateToAppointmentList();
            }
        }
    }

    private boolean confirmDelete() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Appointment");
        confirmation.setContentText("Are you sure you want to delete this appointment?");
        
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Appointment.fxml"));
            Parent root = loader.load();
            
            AppointmentController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            
            if (customerId != null) {
                controller.setCustomerId(customerId);
            }
            controller.refreshAppointmentTable();
            
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Could not load appointment list view");
            e.printStackTrace();
        }
    }

    private void navigateToAppointmentList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Appointment.fxml"));
            Parent root = loader.load();
            
            AppointmentController controller = loader.getController();
            controller.setRole(currentRole); // Ensure role is maintained
            controller.setMainApp(mainApp); 
            controller.setCustomerId(customerId);
            controller.refreshAppointmentTable();
            
            Stage stage = (Stage) appointmentIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Could not load appointment list view");
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