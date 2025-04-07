package com.zodiacgroup.controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Service;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateScheduleServiceController extends BaseController implements Initializable {

    @FXML private TextField serviceIdField;
    @FXML private TextField customerNameField;
    @FXML private TextField vehicleNameField;
    @FXML private DatePicker serviceDateField;
    @FXML private ComboBox<String> serviceDetailField;
    @FXML private ComboBox<String> statusField;
    @FXML private ComboBox<String> mechanicNameField;
    @FXML private TextField costField;
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

    private String currentRole;
    private Service service;
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private Integer customerId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	setCurrentPage("customer");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
        if (lblCustomer != null) {
            lblCustomer.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        initializeComboBoxes();
        
     // Load mechanic names
        List<String> mechanicNames = serviceDAO.getAllMechanicNames();
        mechanicNameField.getItems().addAll(mechanicNames);
    }

    
    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                               lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                      lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
    }
    

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    private void initializeComboBoxes() {
        serviceDetailField.getItems().addAll(serviceDAO.getServiceTypes());
        statusField.getItems().addAll("Pending", "In Progress", "Completed");
    }
    
    

    public void setServiceData(Service service) {
        this.service = service;
        
        serviceIdField.setText(String.valueOf(service.getId()));
        customerNameField.setText(service.getCustomerName());
        vehicleNameField.setText(service.getVehicleName());
        
        setServiceDate(service.getDate());
        
        serviceDetailField.setValue(service.getServiceDetail());
        statusField.setValue(service.getStatus());
        costField.setText(String.valueOf(service.getCost()));
        mechanicNameField.setValue(service.getMechanicName());
    }

    private void setServiceDate(Date serviceDate) {
        if (serviceDate != null) {
            LocalDate localDate;
            if (serviceDate instanceof java.sql.Date) {
                localDate = ((java.sql.Date) serviceDate).toLocalDate();
            } else {
                localDate = serviceDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            }
            serviceDateField.setValue(localDate);
        }
    }

    @FXML
    private void handleUpdateService(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        if (confirmUpdate()) {
            updateServiceDetails();
            if (serviceDAO.updateService(service)) {
                showAlert("Success", "Service updated successfully!");
                navigateToServiceList();
            } else {
                showAlert("Error", "Failed to update service");
            }
        }
    }

    private boolean validateInputs() {
        return !customerNameField.getText().isEmpty() 
                && !vehicleNameField.getText().isEmpty()
                && serviceDetailField.getValue() != null 
                && statusField.getValue() != null 
                && serviceDateField.getValue() != null
        && mechanicNameField.getValue() != null;
    }

    private boolean confirmUpdate() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Update");
        confirmAlert.setHeaderText("Are you sure you want to update this service?");
        confirmAlert.setContentText("This will modify the service details in the database.");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void updateServiceDetails() {
        service.setCustomerName(customerNameField.getText());
        service.setVehicleName(vehicleNameField.getText());
        
        LocalDate localDate = serviceDateField.getValue();
        if (localDate != null) {
            service.setDate(java.sql.Date.valueOf(localDate));
        }
        
        service.setServiceDetail(serviceDetailField.getValue());
        service.setStatus(statusField.getValue());
        service.setMechanicName(mechanicNameField.getValue());
        
        try {
            service.setCost(Double.parseDouble(costField.getText()));
        } catch (NumberFormatException e) {
            service.setCost(0.0);
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        navigateToServiceList();
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
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            
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