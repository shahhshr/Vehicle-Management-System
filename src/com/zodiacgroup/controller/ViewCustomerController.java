package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.AppointmentDAO;
import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Service;
import com.zodiacgroup.model.Vehicle;

public class ViewCustomerController extends BaseController implements Initializable {
    @FXML private Text customerIdText;
    @FXML private Text firstNameText;
    @FXML private Text lastNameText;
    @FXML private Text phoneText;
    @FXML private Text emailText;
    @FXML private Text addressText;
    @FXML private VBox vehiclesContainer;
    private String currentRole;

    @FXML
    private Label lblHome, lblCustomer, lblVehicles, lblAppointment, lblService, lblInventory, lblPayment,
            lblViewReport, lblSalesRep, lblManager, lblLogout;
    
    private CustomerDAO customerDAO = new CustomerDAO();
    private Customer currentCustomer;
    
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
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);

        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
    }
    
    public void setCustomerData(Customer customer) {
        this.currentCustomer = customer;
        
        if (customer != null) {
            customerIdText.setText(String.valueOf(customer.getCustomerId()));
            firstNameText.setText(customer.getFirstName());
            lastNameText.setText(customer.getLastName());
            phoneText.setText(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "N/A");
            emailText.setText(customer.getEmail());
            addressText.setText(customer.getAddress() != null ? customer.getAddress() : "N/A");
            
            vehiclesContainer.getChildren().clear();
            
            if (customer.getVehicles() != null && !customer.getVehicles().isEmpty()) {
                for (Vehicle vehicle : customer.getVehicles()) {
                    VehicleDAO vehicleDAO = new VehicleDAO();
                    Vehicle fullVehicle = vehicleDAO.getVehicleById(vehicle.getVehicleId());
                    addVehicleSection(fullVehicle);
                }
            } else {
                Label noVehiclesLabel = new Label("No vehicles registered for this customer");
                vehiclesContainer.getChildren().add(noVehiclesLabel);
            }
        }
    }
    
    private void addVehicleSection(Vehicle vehicle) {
        VBox vehicleBox = new VBox(10);
        vehicleBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");
        
        Label vehicleTitle = new Label(vehicle.getMake() + " " + vehicle.getModel() + " (" + vehicle.getYear() + ")");
        vehicleTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        vehicleBox.getChildren().add(vehicleTitle);
        
        GridPane vehicleGrid = new GridPane();
        vehicleGrid.setHgap(10);
        vehicleGrid.setVgap(5);
        vehicleGrid.setPadding(new Insets(5, 0, 5, 10));
        
        vehicleGrid.add(new Label("VIN:"), 0, 0);
        vehicleGrid.add(new Label(vehicle.getVin()), 1, 0);
        
        vehicleBox.getChildren().add(vehicleGrid);
        
        ServiceDAO serviceDAO = new ServiceDAO();
        List<Service> services = serviceDAO.getServicesByVehicleId(vehicle.getVehicleId());
        
        if (!services.isEmpty()) {
            VBox servicesBox = new VBox(5);
            servicesBox.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-radius: 5;");
            
            Label servicesLabel = new Label("Services:");
            servicesLabel.setStyle("-fx-font-weight: bold; -fx-underline: true;");
            servicesBox.getChildren().add(servicesLabel);
            
            GridPane servicesGrid = new GridPane();
            servicesGrid.setHgap(10);
            servicesGrid.setVgap(5);
            servicesGrid.setPadding(new Insets(5, 0, 5, 10));
            
            servicesGrid.add(new Label("Service ID"), 0, 0);
            servicesGrid.add(new Label("Detail"), 1, 0);
            servicesGrid.add(new Label("Date"), 2, 0);
            servicesGrid.add(new Label("Cost"), 3, 0);
            servicesGrid.add(new Label("Status"), 4, 0);
            servicesGrid.add(new Label("Mechanic"), 5, 0);
            
            int row = 1;
            for (Service service : services) {
                servicesGrid.add(new Label(String.valueOf(service.getId())), 0, row);
                servicesGrid.add(new Label(service.getServiceDetail()), 1, row);
                servicesGrid.add(new Label(new SimpleDateFormat("yyyy-MM-dd").format(service.getDate())), 2, row);
                servicesGrid.add(new Label(String.format("$%.2f", service.getCost())), 3, row);
                servicesGrid.add(new Label(service.getStatus()), 4, row);
                servicesGrid.add(new Label(service.getMechanicName() != null ? service.getMechanicName() : "N/A"), 5, row);
                row++;
            }
            
            servicesBox.getChildren().add(servicesGrid);
            vehicleBox.getChildren().add(servicesBox);
        }
        
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointments = appointmentDAO.getAppointmentsByVehicleId(vehicle.getVehicleId());
        
        if (!appointments.isEmpty()) {
            VBox appointmentsBox = new VBox(5);
            appointmentsBox.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-radius: 5;");
            
            Label appointmentsLabel = new Label("Appointments:");
            appointmentsLabel.setStyle("-fx-font-weight: bold; -fx-underline: true;");
            appointmentsBox.getChildren().add(appointmentsLabel);
            
            GridPane appointmentsGrid = new GridPane();
            appointmentsGrid.setHgap(10);
            appointmentsGrid.setVgap(5);
            appointmentsGrid.setPadding(new Insets(5));
            appointmentsGrid.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #aaa;" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 3px;" +
                "-fx-padding: 5px;"
            );
            
            Label idHeader = new Label("Appt ID");
            idHeader.setStyle("-fx-font-weight: bold;");
            appointmentsGrid.add(idHeader, 0, 0);
            
            Label dateHeader = new Label("Date");
            dateHeader.setStyle("-fx-font-weight: bold;");
            appointmentsGrid.add(dateHeader, 1, 0);
            
            Label typeHeader = new Label("Service Type");
            typeHeader.setStyle("-fx-font-weight: bold;");
            appointmentsGrid.add(typeHeader, 2, 0);
            
            Label statusHeader = new Label("Status");
            statusHeader.setStyle("-fx-font-weight: bold;");
            appointmentsGrid.add(statusHeader, 3, 0);
            
            int row = 1;
            for (Appointment appointment : appointments) {
                appointmentsGrid.add(new Label(String.valueOf(appointment.getAppointmentId())), 0, row);
                appointmentsGrid.add(new Label(appointment.getAppointmentDateFormatted()), 1, row);
                appointmentsGrid.add(new Label(appointment.getServiceType()), 2, row);
                appointmentsGrid.add(new Label(appointment.getStatus()), 3, row);
                row++;
            }
            
            appointmentsBox.getChildren().add(appointmentsGrid);
            vehicleBox.getChildren().add(appointmentsBox);
        }
        
        vehiclesContainer.getChildren().add(vehicleBox);
    }
    
    @FXML
    private void handleBackButtonAction() {
        navigateToCustomerList();
    }
    
    private void navigateToCustomerList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/customer.fxml"));
            Parent root = loader.load();
            
            CustomerController controller = loader.getController();
            controller.refreshCustomerList();
            controller.setRole(currentRole);
            controller.setMainApp(mainApp); 
            
            Stage stage = (Stage) vehiclesContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Management");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load customer list view");
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