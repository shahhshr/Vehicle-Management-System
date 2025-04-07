package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Service;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Vehicle;
import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.dao.AppointmentDAO;

public class ViewServiceController extends BaseController implements Initializable {
    @FXML private Text serviceIdText;
    @FXML private Text serviceDetailText;
    @FXML private Text statusText;
    @FXML private Text dateText;
    @FXML private Text costText;
    @FXML private Text mechanicNameText; // New field for mechanic name
    
    // Customer details
    @FXML private Text customerIdText;
    @FXML private Text customerNameText;
    @FXML private Text customerPhoneText;
    @FXML private Text customerEmailText;
    @FXML private Text customerAddressText;
    
    // Vehicle details
    @FXML private Text vehicleIdText;
    @FXML private Text vehicleMakeModelText;
    @FXML private Text vehicleYearText;
    @FXML private Text vehicleVinText;
    
    // Appointment details
    @FXML private Text appointmentIdText;
    @FXML private Text appointmentDateText;
    @FXML private Text appointmentServiceTypeText;
    @FXML private Text appointmentStatusText;
    
    @FXML private Button backButton;
    
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
    private Integer customerId;
    private CustomerDAO customerDAO = new CustomerDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCurrentPage("customer"); // or appropriate page name
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
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setServiceData(Service service) {
        if (service != null) {
            // Set service details
            serviceIdText.setText(String.valueOf(service.getId()));
            serviceDetailText.setText(service.getServiceDetail());
            statusText.setText(service.getStatus());
            dateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(service.getDate()));
            costText.setText(String.format("$%.2f", service.getCost()));
            String mechanicName = service.getMechanicName();
            mechanicNameText.setText(mechanicName != null && !mechanicName.isEmpty() ? 
                                   mechanicName : "Not assigned");
            
            // Get and set customer details
            Customer customer = customerDAO.getCustomerById(service.getCustomerId());
            if (customer != null) {
                customerIdText.setText(String.valueOf(customer.getCustomerId()));
                customerNameText.setText(customer.getFirstName() + " " + customer.getLastName());
                customerPhoneText.setText(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "N/A");
                customerEmailText.setText(customer.getEmail());
                customerAddressText.setText(customer.getAddress() != null ? customer.getAddress() : "N/A");
            }
            
            // Enhanced vehicle lookup
            try {
                Vehicle vehicle = null;
                
                // 1. First try direct ID lookup if available
                if (service.getVehicleId() != null) {
                    vehicle = vehicleDAO.getVehicleById(service.getVehicleId());
                }
                
                // 2. If not found by ID, try to find by customer ID and vehicle name
                if (vehicle == null && service.getVehicleName() != null && service.getCustomerId() > 0) {
                    List<Vehicle> customerVehicles = vehicleDAO.getVehiclesByCustomerId(service.getCustomerId());
                    if (customerVehicles != null) {
                        vehicle = customerVehicles.stream()
                            .filter(v -> (v.getMake() + " " + v.getModel()).equalsIgnoreCase(service.getVehicleName()))
                            .findFirst()
                            .orElse(null);
                    }
                }
                
                // 3. If still not found, try general search by name
                if (vehicle == null && service.getVehicleName() != null) {
                    List<Vehicle> vehicles = vehicleDAO.searchVehicles(service.getVehicleName());
                    if (!vehicles.isEmpty()) {
                        vehicle = vehicles.get(0);
                    }
                }
                
                // Set vehicle details if found
                if (vehicle != null) {
                    vehicleIdText.setText(String.valueOf(vehicle.getVehicleId()));
                    vehicleMakeModelText.setText(vehicle.getMake() + " " + vehicle.getModel());
                    vehicleYearText.setText(String.valueOf(vehicle.getYear()));
                    vehicleVinText.setText(vehicle.getVin());
                } else {
                    vehicleIdText.setText("N/A");
                    vehicleMakeModelText.setText(service.getVehicleName() != null ? 
                        service.getVehicleName() : "No vehicle info");
                    vehicleYearText.setText("N/A");
                    vehicleVinText.setText("N/A");
                }
            } catch (Exception e) {
                System.err.println("Error loading vehicle details:");
                e.printStackTrace();
                vehicleIdText.setText("Error");
                vehicleMakeModelText.setText("Error loading vehicle");
                vehicleYearText.setText("Error");
                vehicleVinText.setText("Error");
            }
            
            // Get and set appointment details
            List<Appointment> appointments = appointmentDAO.getAppointmentsByCustomerId(service.getCustomerId());
            if (appointments != null && !appointments.isEmpty()) {
                Appointment matchingAppointment = appointments.stream()
                    .filter(a -> a.getServiceType() != null && 
                           a.getServiceType().equalsIgnoreCase(service.getServiceDetail()))
                    .findFirst()
                    .orElse(appointments.get(0));
                    
                appointmentIdText.setText(String.valueOf(matchingAppointment.getAppointmentId()));
                appointmentDateText.setText(matchingAppointment.getAppointmentDateFormatted());
                appointmentServiceTypeText.setText(matchingAppointment.getServiceType());
                appointmentStatusText.setText(matchingAppointment.getStatus());
            } else {
                appointmentIdText.setText("N/A");
                appointmentDateText.setText("N/A");
                appointmentServiceTypeText.setText("N/A");
                appointmentStatusText.setText("N/A");
            }
        }
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
            
            Stage stage = (Stage) backButton.getScene().getWindow();
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