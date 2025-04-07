package com.zodiacgroup.controller;

import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.dao.MechanicDAO;
import com.zodiacgroup.model.Mechanic;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Service;
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

public class AddScheduleServiceController extends BaseController implements Initializable {

    @FXML private TextField customerIDField;
    @FXML private TextField customerNameField;
    @FXML private ComboBox<Vehicle> vehicleComboBox;
    @FXML 
    private ComboBox<String> serviceDetailComboBox;
    @FXML private TextField costField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<Mechanic> mechanicComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    
    @FXML private Label lblHome;
    @FXML private Label lblCustomer;
    @FXML private Label lblPayment;
    @FXML private Label lblInventory;
    @FXML private Label lblSalesRep;
    @FXML private Label lblManager;
    @FXML private Label lblViewReport;
    @FXML private Label lblLogout;

    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final MechanicDAO mechanicDAO = new MechanicDAO();
    private Integer customerId;
    private String customerName;
    private String currentRole;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        statusComboBox.getItems().addAll(
            "Pending",
            "In Progress",
            "Completed"
        );
        
        
        serviceDetailComboBox.getItems().addAll(
            "Oil Change",
            "Tire Rotation",
            "Brake Service",
            "Engine Diagnostic",
            "Transmission Service",
            "Battery Replacement",
            "Air Conditioning Service",
            "Wheel Alignment",
            "Exhaust System Repair",
            "General Inspection"
        );

   
        datePicker.setValue(LocalDate.now());
        
        
        loadMechanics();

        
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
    
    private void loadMechanics() {
        try {
            List<Mechanic> mechanics = mechanicDAO.getAllMechanics();
            
            mechanicComboBox.getItems().clear();
            mechanicComboBox.getItems().addAll(mechanics);
            
            mechanicComboBox.setCellFactory(param -> new ListCell<Mechanic>() {
                @Override
                protected void updateItem(Mechanic mechanic, boolean empty) {
                    super.updateItem(mechanic, empty);
                    if (empty || mechanic == null) {
                        setText(null);
                    } else {
                        setText(mechanic.getFirstName() + " " + mechanic.getLastName());
                    }
                }
            });
            
            mechanicComboBox.setConverter(new StringConverter<Mechanic>() {
                @Override
                public String toString(Mechanic mechanic) {
                    if (mechanic == null) {
                        return null;
                    }
                    return mechanic.getFirstName() + " " + mechanic.getLastName();
                }

                @Override
                public Mechanic fromString(String string) {
                    return null;
                }
            });
            
        } catch (Exception e) {
            showAlert("Error", "Could not load mechanic data");
            e.printStackTrace();
        }
    }


    public void setCustomerData(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
        customerIDField.setText(String.valueOf(customerId));
        customerNameField.setText(customerName); // This should come from Customer table
        customerIDField.setEditable(false);
        customerNameField.setEditable(false);
        
        loadCustomerVehicles();
    }
    private void loadCustomerVehicles() {
        try {
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
                    return null;
                }
            });
            
        } catch (Exception e) {
            showAlert("Error", "Could not load vehicle data");
            e.printStackTrace();
        }
    }

   
    @FXML
    private void saveService(ActionEvent event) {
        try {
            if (!validateInput()) {
                return;
            }
            
            Vehicle selectedVehicle = vehicleComboBox.getValue();
            if (selectedVehicle == null) {
                showAlert("Validation Error", "Please select a vehicle");
                return;
            }
            Mechanic selectedMechanic = mechanicComboBox.getValue();
            if (selectedMechanic == null) {
                showAlert("Validation Error", "Please select a mechanic");
                return;
            }
            
            Service service = new Service();
            service.setCustomerId(customerId);
            service.setCustomerName(customerName);
            service.setVehicleName(selectedVehicle.getMake() + " " + selectedVehicle.getModel());
            service.setServiceDetail(serviceDetailComboBox.getValue()); // Changed from text field to combo box
            service.setStatus(statusComboBox.getValue());
            service.setDate(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            service.setMechanicName(selectedMechanic.getFirstName() + " " + selectedMechanic.getLastName());
            
            if (!costField.getText().isEmpty()) {
                try {
                    service.setCost(Double.parseDouble(costField.getText()));
                } catch (NumberFormatException e) {
                    showAlert("Validation Error", "Cost must be a valid number");
                    return;
                }
            }
            
            boolean saved = serviceDAO.saveService(service);
            
            if (saved) {
                showAlert("Success", "Service saved successfully!");
                navigateToServiceList();
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        
        if (customerIDField.getText().isEmpty()) {
            errors.append("- Customer ID is required\n");
        }
        
        if (vehicleComboBox.getValue() == null) {
            errors.append("- Please select a vehicle\n");
        }
        if (mechanicComboBox.getValue() == null) {
            errors.append("- Please select a mechanic\n");
        }

        if (customerNameField.getText().isEmpty()) {
            errors.append("- Customer name is required\n");
        }
        
        if (serviceDetailComboBox.getValue() == null) { // Changed from text field to combo box
            errors.append("- Service detail is required\n");
        }
        
        if (datePicker.getValue() == null) {
            errors.append("- Date is required\n");
        } else if (datePicker.getValue().isBefore(LocalDate.now())) {
            errors.append("- Date cannot be in the past\n");
        }
        
        if (statusComboBox.getValue() == null) {
            errors.append("- Status is required\n");
        }
        
        if (errors.length() > 0) {
            showAlert("Validation Error", "Please correct the following:\n\n" + errors);
            return false;
        }
        
        return true;
    }

    @FXML
    private void cancelService(ActionEvent event) {
        clearForm();
    }
    private void clearForm() {
        customerIDField.clear();
        customerNameField.clear();
        vehicleComboBox.setValue(null);
        serviceDetailComboBox.setValue(null); // Changed from clear() to setValue(null)
        costField.clear();
        datePicker.setValue(LocalDate.now());
        statusComboBox.setValue(null);
    }
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
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
    
    private void navigateToServiceList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ScheduleService.fxml"));
            Parent root = loader.load();
            
            ScheduleServiceController controller = loader.getController();
            controller.setRole(currentRole);
            controller.setMainApp(mainApp);
            controller.setCustomerId(customerId);
            controller.refreshServiceTable();
            
            Stage stage = (Stage) customerIDField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not return to service list");
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