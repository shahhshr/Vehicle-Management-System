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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Service;
import com.zodiacgroup.model.Vehicle;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import java.text.SimpleDateFormat;

public class ViewAppointmentController extends BaseController implements Initializable {
    @FXML private Text appointmentIdText;
    @FXML private Text appointmentDateText;
    @FXML private Text serviceTypeText;
    @FXML private Text statusText;
    
    // Customer details
    @FXML private Text customerIdText;
    @FXML private Text customerNameText;
    @FXML private Text customerPhoneText;
    @FXML private Text customerEmailText;
    
    // Vehicle details
    @FXML private Text vehicleIdText;
    @FXML private Text vehicleMakeModelText;
    @FXML private Text vehicleYearText;
    @FXML private Text vehicleVinText;
    
    @FXML private VBox servicesContainer;
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
    private Appointment currentAppointment;
    private CustomerDAO customerDAO = new CustomerDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();
    private ServiceDAO serviceDAO = new ServiceDAO();
    
    private Integer customerId; 
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

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
            // Set appointment details
            appointmentIdText.setText(String.valueOf(appointment.getAppointmentId()));
            appointmentDateText.setText(appointment.getAppointmentDateFormatted());
            serviceTypeText.setText(appointment.getServiceType());
            statusText.setText(appointment.getStatus());
            
            // Get and set customer details
            Customer customer = customerDAO.getCustomerById(appointment.getCustomerId());
            if (customer != null) {
                customerIdText.setText(String.valueOf(customer.getCustomerId()));
                customerNameText.setText(customer.getFirstName() + " " + customer.getLastName());
                customerPhoneText.setText(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "N/A");
                customerEmailText.setText(customer.getEmail());
            }
            
            // Get and set vehicle details
            Vehicle vehicle = vehicleDAO.getVehicleById(appointment.getVehicleId());
            if (vehicle != null) {
                vehicleIdText.setText(String.valueOf(vehicle.getVehicleId()));
                vehicleMakeModelText.setText(vehicle.getMake() + " " + vehicle.getModel());
                vehicleYearText.setText(String.valueOf(vehicle.getYear()));
                vehicleVinText.setText(vehicle.getVin());
            }
            
            // Get and display services for this vehicle
            List<Service> services = serviceDAO.getServicesByVehicleId(appointment.getVehicleId());
            displayServices(services);
        }
    }
    
    private void displayServices(List<Service> services) {
        servicesContainer.getChildren().clear();
        
        if (services == null || services.isEmpty()) {
            Label noServicesLabel = new Label("No services recorded for this vehicle");
            noServicesLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");
            servicesContainer.getChildren().add(noServicesLabel);
            return;
        }
        
        // Add a title for the services section
        Label servicesTitle = new Label("Vehicle Service History:");
        servicesTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-padding: 0 0 10 0;");
        servicesContainer.getChildren().add(servicesTitle);
        
        for (Service service : services) {
            VBox serviceBox = new VBox(10);
            serviceBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 15;");
            
            // Service header with ID and date
            HBox headerBox = new HBox(10);
            Label serviceIdLabel = new Label("Service #" + service.getId());
            serviceIdLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            
            Label dateLabel = new Label(new SimpleDateFormat("yyyy-MM-dd").format(service.getDate()));
            dateLabel.setStyle("-fx-text-fill: #555;");
            
            headerBox.getChildren().addAll(serviceIdLabel, dateLabel);
            serviceBox.getChildren().add(headerBox);
            
            // Service details grid
            GridPane serviceGrid = new GridPane();
            serviceGrid.setHgap(15);
            serviceGrid.setVgap(8);
            serviceGrid.setPadding(new Insets(5, 0, 5, 10));
            
            // Add details to the grid
            serviceGrid.add(new Label("Details:"), 0, 0);
            serviceGrid.add(new Label(service.getServiceDetail()), 1, 0);
            
            serviceGrid.add(new Label("Status:"), 0, 1);
            serviceGrid.add(new Label(service.getStatus()), 1, 1); // Removed styling
            
            serviceGrid.add(new Label("Cost:"), 0, 2);
            serviceGrid.add(new Label(String.format("$%.2f", service.getCost())), 1, 2);
            
            serviceGrid.add(new Label("Mechanic:"), 0, 3);
            serviceGrid.add(new Label(service.getMechanicName() != null ? service.getMechanicName() : "Not assigned"), 1, 3);
            
            serviceBox.getChildren().add(serviceGrid);
            servicesContainer.getChildren().add(serviceBox);
            
            // Add some spacing between service boxes
            servicesContainer.getChildren().add(new Pane());
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Appointment.fxml"));
            Parent root = loader.load();
            
            AppointmentController controller = loader.getController();
            controller.setRole(currentRole);
            controller.setMainApp(mainApp);
            
            // Pass the customerId back to the AppointmentController
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}