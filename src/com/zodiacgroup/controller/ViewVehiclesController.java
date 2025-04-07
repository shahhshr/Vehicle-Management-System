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
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.AppointmentDAO;
import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Service;
import com.zodiacgroup.model.Vehicle;

public class ViewVehiclesController extends BaseController implements Initializable {

    @FXML private Label idLabel;
    @FXML private Label makeLabel;
    @FXML private Label modelLabel;
    @FXML private Label yearLabel;
    @FXML private Label vinLabel;
    
    @FXML private Label customerIdLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;
    
    @FXML private VBox appointmentsContainer;
    @FXML private VBox servicesContainer;
    
    @FXML private Button cancelButton;
    
    @FXML
    private Label lblHome, lblCustomer, lblSalesRep, lblManager, lblLogout, lblInventory, lblPayment, lblViewReport;
    private String currentRole;

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private Customer customer;
    private Vehicle currentVehicle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCurrentPage("customer");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
        if (lblCustomer != null) {
            lblCustomer.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        
        if (currentRole != null) {
            setRole(currentRole);
        }
    }

    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);

        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public void setVehicleData(Vehicle vehicle) {
        if (vehicle == null) {
            showAlert("Error", "No vehicle data provided");
            return;
        }
        
        this.currentVehicle = vehicle;
        updateVehicleDetails();
        updateCustomerDetails();
        loadAppointments();
        loadServices();
    }
    
    private void updateVehicleDetails() {
        idLabel.setText(String.valueOf(currentVehicle.getVehicleId()));
        makeLabel.setText(currentVehicle.getMake());
        modelLabel.setText(currentVehicle.getModel());
        yearLabel.setText(String.valueOf(currentVehicle.getYear()));
        vinLabel.setText(currentVehicle.getVin());
    }
    
    private void updateCustomerDetails() {
        Customer cust = currentVehicle.getCustomer();
        if (cust == null) {
            setDefaultCustomerDetails();
            return;
        }
        
        customerIdLabel.setText(String.valueOf(cust.getCustomerId()));
        firstNameLabel.setText(cust.getFirstName());
        lastNameLabel.setText(cust.getLastName());
        phoneLabel.setText(cust.getPhoneNumber() != null ? cust.getPhoneNumber() : "N/A");
        emailLabel.setText(cust.getEmail());
        addressLabel.setText(cust.getAddress() != null ? cust.getAddress() : "N/A");
    }
    
    private void setDefaultCustomerDetails() {
        customerIdLabel.setText("N/A");
        firstNameLabel.setText("N/A");
        lastNameLabel.setText("N/A");
        phoneLabel.setText("N/A");
        emailLabel.setText("N/A");
        addressLabel.setText("N/A");
    }
    
    private void loadAppointments() {
        try {
            if (appointmentsContainer == null) {
                System.err.println("Appointments container not initialized!");
                return;
            }
            
            appointmentsContainer.getChildren().clear();
            
            List<Appointment> appointments = appointmentDAO.getAppointmentsByVehicleId(currentVehicle.getVehicleId());
            if (!appointments.isEmpty()) {
                VBox appointmentsBox = new VBox(5);
                appointmentsBox.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-radius: 5;");
                
                Label appointmentsLabel = new Label("Appointments:");
                appointmentsLabel.setStyle("-fx-font-weight: bold; -fx-underline: true; -fx-font-family: 'Arial'; -fx-font-size: 14px;");
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
                appointmentsContainer.getChildren().add(appointmentsBox);
            }
        } catch (Exception e) {
            System.err.println("Error loading appointments: " + e.getMessage());
            e.printStackTrace();
            Label errorLabel = new Label("Error loading appointments");
            appointmentsContainer.getChildren().add(errorLabel);
        }
    }
    
    private void loadServices() {
        try {
            if (servicesContainer == null) {
                System.err.println("Services container not initialized!");
                return;
            }
            
            servicesContainer.getChildren().clear();
            
            List<Service> services = serviceDAO.getServicesByVehicleId(currentVehicle.getVehicleId());
            if (!services.isEmpty()) {
                VBox servicesBox = new VBox(5);
                servicesBox.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-radius: 5;");
                
                Label servicesLabel = new Label("Services:");
                servicesLabel.setStyle("-fx-font-weight: bold; -fx-underline: true;");
                servicesBox.getChildren().add(servicesLabel);
                
                GridPane servicesGrid = new GridPane();
                servicesGrid.setHgap(10);
                servicesGrid.setVgap(5);
                servicesGrid.setPadding(new Insets(5));
                servicesGrid.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #aaa;" +
                    "-fx-border-width: 1px;" +
                    "-fx-border-radius: 3px;" +
                    "-fx-padding: 5px;"
                );
                
                Label idHeader = new Label("Service ID");
                idHeader.setStyle("-fx-font-weight: bold;");
                servicesGrid.add(idHeader, 0, 0);
                
                Label detailHeader = new Label("Detail");
                detailHeader.setStyle("-fx-font-weight: bold;");
                servicesGrid.add(detailHeader, 1, 0);
                
                Label dateHeader = new Label("Date");
                dateHeader.setStyle("-fx-font-weight: bold;");
                servicesGrid.add(dateHeader, 2, 0);
                
                Label costHeader = new Label("Cost");
                costHeader.setStyle("-fx-font-weight: bold;");
                servicesGrid.add(costHeader, 3, 0);
                
                Label statusHeader = new Label("Status");
                statusHeader.setStyle("-fx-font-weight: bold;");
                servicesGrid.add(statusHeader, 4, 0);
                
                Label mechanicHeader = new Label("Mechanic");
                mechanicHeader.setStyle("-fx-font-weight: bold;");
                servicesGrid.add(mechanicHeader, 5, 0);
                
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
                servicesContainer.getChildren().add(servicesBox);
            }
        } catch (Exception e) {
            System.err.println("Error loading services: " + e.getMessage());
            e.printStackTrace();
            Label errorLabel = new Label("Error loading services");
            servicesContainer.getChildren().add(errorLabel);
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/vehicles.fxml"));
            Parent root = loader.load();
            
            VehiclesController controller = loader.getController();
            if (customer != null) {
                controller.setCustomer(customer);
            }
            controller.setRole(currentRole);
            controller.setMainApp(mainApp); 
            
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Vehicles Management");
        } catch (IOException e) {
            System.err.println("Error navigating back: " + e.getMessage());
            showAlert("Navigation Error", "Could not return to vehicles view");
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}