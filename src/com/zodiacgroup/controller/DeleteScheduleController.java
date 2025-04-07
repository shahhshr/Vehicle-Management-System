package com.zodiacgroup.controller;

import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Service;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeleteScheduleController extends BaseController implements Initializable {

    @FXML private TextField serviceIdField;
    @FXML private TextField customerNameField;
    @FXML private TextField vehicleNameField;
    @FXML private ComboBox<String> serviceDetailField;
    @FXML private ComboBox<String> statusField;
    @FXML private DatePicker serviceDateField;
    @FXML private TextField costField;
    @FXML private Button deleteButton;
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

    private ServiceDAO serviceDAO = new ServiceDAO();
    private Service currentService;
    private String currentRole;
    private Integer customerId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	setCurrentPage("customer");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
        if (lblCustomer != null) {
            lblCustomer.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        // Initialize ComboBox items
        serviceDetailField.getItems().addAll(
            "Oil Change",
            "Tire Rotation",
            "Brake Service",
            "Engine Diagnostic",
            "Transmission Service"
        );
        
        statusField.getItems().addAll(
            "Pending",
            "In Progress",
            "Completed"
        );
        
        // Make fields non-editable
        serviceDetailField.setEditable(false);
        statusField.setEditable(false);
        serviceDateField.setEditable(false);
    }

    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                               lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                      lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
    }

    public void setServiceData(Service service) {
        this.currentService = service;
        if (service != null) {
            serviceIdField.setText(String.valueOf(service.getId()));
            customerNameField.setText(service.getCustomerName());
            vehicleNameField.setText(service.getVehicleName());
            serviceDetailField.setValue(service.getServiceDetail());
            statusField.setValue(service.getStatus());
            
            if (service.getDate() != null) {
                try {
                    // Convert java.util.Date to LocalDate
                    Date date = service.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(sdf.format(date));
                    serviceDateField.setValue(localDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            costField.setText(service.getCost() != 0.0 ? String.valueOf(service.getCost()) : "");
        }
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @FXML
    private void handleDeleteService(ActionEvent event) {
        if (currentService != null) {
            if (confirmDelete()) {
                boolean deleted = serviceDAO.deleteService(currentService.getId());
                if (deleted) {
                    showAlert("Success", "Service deleted successfully!");
                    navigateToServiceList();
                } else {
                    showAlert("Error", "Failed to delete service");
                }
            }
        } else {
            showAlert("Error", "No service selected to delete");
        }
    }

    private boolean confirmDelete() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Service");
        confirmation.setContentText("Are you sure you want to delete this service?");
        
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToServiceList();
    }

    private void navigateToServiceList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ScheduleService.fxml"));
            Parent root = loader.load();
            
            ScheduleServiceController controller = loader.getController();
            controller.setRole(currentRole);
            controller.setMainApp(mainApp); 
            if (customerId != null) {
                controller.setCustomerId(customerId);
            }
            controller.refreshServiceTable();
            
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Could not load service list view");
            e.printStackTrace();
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