package com.zodiacgroup.controller;

import com.zodiacgroup.dao.AppointmentDAO;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.model.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddAppointmentController extends BaseController implements Initializable {

    @FXML private TextField customerIdField;
    @FXML private TextField vehicleIdField;
    @FXML private TextField customerNameField;
    @FXML private DatePicker appointmentDateField;
    @FXML private ComboBox<String> serviceTypeField;
    @FXML private ComboBox<String> statusField;
    @FXML private Button cancelButton;
    @FXML private ComboBox<Vehicle> vehicleComboBox;
    
 
    @FXML private Label lblHome;
    @FXML private Label lblCustomer;
    @FXML private Label lblPayment;
    @FXML private Label lblInventory;
    @FXML private Label lblSalesRep;
    @FXML private Label lblManager;
    @FXML private Label lblViewReport;
    @FXML private Label lblLogout;

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private Integer customerId;
    private Integer vehicleId;
    private String customerName;
    private String currentRole;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        serviceTypeField.getItems().addAll(
            "Oil Change", 
            "Tire Rotation", 
            "Brake Service",
            "Engine Diagnostic",
            "Transmission Service"
        );
        
        statusField.getItems().addAll(
            "Scheduled",
            "Confirmed",
            "In Progress",
            "Completed",
            "Cancelled"
        );
        
        
        appointmentDateField.setValue(LocalDate.now());
        
   
        if (customerId != null) {
            loadCustomerVehicles();
        }
        
       
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

    

    private void loadCustomerVehicles() {
        try {
            VehicleDAO vehicleDAO = new VehicleDAO();
            List<Vehicle> vehicles = vehicleDAO.getVehiclesByCustomerId(customerId);
            
            vehicleComboBox.getItems().clear();
            vehicleComboBox.getItems().addAll(vehicles);
            
           
            vehicleComboBox.setCellFactory(param -> new ListCell<Vehicle>() {
                @Override
                protected void updateItem(Vehicle vehicle, boolean empty) {
                    super.updateItem(vehicle, empty);
                    if (empty || vehicle == null) {
                        setText(null);
                    } else {
                        setText(vehicle.getMake() + " " + vehicle.getModel() + " (" + vehicle.getVehicleId() + ")");
                    }
                }
            });
            
            vehicleComboBox.setConverter(new StringConverter<Vehicle>() {
                @Override
                public String toString(Vehicle vehicle) {
                    if (vehicle == null) {
                        return null;
                    }
                    return vehicle.getMake() + " " + vehicle.getModel() + " (" + vehicle.getVehicleId() + ")";
                }

                @Override
                public Vehicle fromString(String string) {
                    return null; // Not needed for display
                }
            });
            
        } catch (Exception e) {
            showAlert("Error", "Could not load vehicle data");
            e.printStackTrace();
        }
    }
    
    

    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public void setCustomerData(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
        customerIdField.setText(String.valueOf(customerId));
        customerNameField.setText(customerName);
        customerIdField.setEditable(false);
        customerNameField.setEditable(false);
        
        loadCustomerVehicles();
    }
    
    public void setVehicleData(int vehicleId, String vehicleDetails) {
        this.vehicleId = vehicleId;
        vehicleIdField.setText(String.valueOf(vehicleId));
        vehicleIdField.setEditable(false);
    }

    @FXML
    private void handleSaveAppointment(ActionEvent event) {
        try {
            if (!validateInput()) {
                return;
            }
            
            Vehicle selectedVehicle = vehicleComboBox.getValue();
            if (selectedVehicle == null) {
                showAlert("Validation Error", "Please select a vehicle");
                return;
            }
            
            Appointment appointment = new Appointment();
            appointment.setCustomerId(customerId);
            appointment.setVehicleId(selectedVehicle.getVehicleId());
            appointment.setCustomerName(customerName);
            appointment.setAppointmentDate(
                Date.from(appointmentDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
            appointment.setServiceType(serviceTypeField.getValue());
            appointment.setStatus(statusField.getValue());
            
            boolean saved = appointmentDAO.saveAppointment(appointment);
            
            if (saved) {
                showAlert("Success", "Appointment saved successfully!");
                navigateToAppointmentList();
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        
        
        if (customerIdField.getText().isEmpty()) {
            errors.append("- Customer ID is required\n");
        }
       
        if (vehicleComboBox.getValue() == null) {
            errors.append("- Please select a vehicle\n");
        }
        
     
        if (customerNameField.getText().isEmpty()) {
            errors.append("- Customer name is required\n");
        }
       
        if (appointmentDateField.getValue() == null) {
            errors.append("- Appointment date is required\n");
        } else if (appointmentDateField.getValue().isBefore(LocalDate.now())) {
            errors.append("- Appointment date cannot be in the past\n");
        }
        
       
        if (serviceTypeField.getValue() == null) {
            errors.append("- Service type is required\n");
        }
        
      
        if (statusField.getValue() == null) {
            errors.append("- Status is required\n");
        }
        
        if (errors.length() > 0) {
            showAlert("Validation Error", "Please correct the following:\n\n" + errors);
            return false;
        }
        
        return true;
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        clearForm();
    }

    private void clearForm() {
        customerIdField.clear();
        vehicleIdField.clear();
        customerNameField.clear();
        appointmentDateField.setValue(LocalDate.now());
        serviceTypeField.setValue(null);
        statusField.setValue(null);
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Appointment.fxml"));
            Parent root = loader.load();
            
            AppointmentController controller = loader.getController();
            controller.setRole(currentRole); // Ensure role is maintained
            controller.setMainApp(mainApp); 
            // Only set customerId if it's not null
            if (customerId != null) {
                controller.setCustomerId(customerId);
            }
            controller.setRole(currentRole); // Ensure role is maintained
            controller.setMainApp(mainApp);
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
            
            Stage stage = (Stage) customerIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not return to appointment list");
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
