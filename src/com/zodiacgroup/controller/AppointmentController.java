package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import com.zodiacgroup.dao.AppointmentDAO;
import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Vehicle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentController extends BaseController implements Initializable{

    
    @FXML private TableView<Appointment> AppointmentTable;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML private TableColumn<Appointment, Integer> vehicleIdColumn;
    @FXML private TableColumn<Appointment, String> customernameColumn;
    @FXML private TableColumn<Appointment, String> appointmentdateColumn;
    @FXML private TableColumn<Appointment, String> serviceTypeColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    
    @FXML private Label lblHome;
    @FXML private Label lblCustomer;
    @FXML private Label lblPayment;
    @FXML private Label lblInventory;
    @FXML private Label lblSalesRep;
    @FXML private Label lblManager;
    @FXML private Label lblViewReport;
    @FXML private Label lblLogout;

  
    @FXML private TextField appointmentIdField;
    @FXML private TextField vehicleIdField;
    @FXML private TextField customerNameField;
    @FXML private DatePicker appointmentDateField;
    @FXML private ComboBox<String> serviceTypeField;
    @FXML private ComboBox<String> statusField;

    // Buttons
    @FXML private Button refreshButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button addButton;
    @FXML private Button viewButton;
    
    @FXML
    private TextField searchField;
    @FXML
    private Rectangle searchBox;
    @FXML
    private Label searchLabel;
    @FXML
    private ImageView searchImage;
    
    private String currentRole;
    private String customerName;
    private Integer customerId;
    private Integer vehicleId;

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("Initializing AppointmentController...");
            
            // Configure table columns
            appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
            customernameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            serviceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            
            appointmentdateColumn.setCellValueFactory(cellData -> {
                Appointment appointment = cellData.getValue();
                String dateStr = appointment.getAppointmentDate() != null ? 
                    new SimpleDateFormat("yyyy-MM-dd").format(appointment.getAppointmentDate()) : 
                    "";
                System.out.println("Formatting date: " + dateStr);
                return new SimpleStringProperty(dateStr);
            });
            
           
            setCurrentPage("customer");
            initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
            if (lblCustomer != null) {
                lblCustomer.setStyle("-fx-font-weight: bold; -fx-underline: true;");
            }
         
            setupSearchField();
            
          
            refreshAppointmentTable();
        } catch (Exception e) {
            System.err.println("Initialization error:");
            e.printStackTrace();
            showAlert("Error", "Failed to initialize table: " + e.getMessage());
        }
    }

    public void setRole(String role) {
        this.currentRole = role;
        if (lblHome != null && lblCustomer != null) { // Add null checks for all labels
            configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                                   lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
            setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                          lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
        }
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
        refreshAppointmentTable();
    }

    @FXML
    private void handleRefreshButtonAction(ActionEvent event) {
        searchField.clear();
        refreshAppointmentTable();
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
        refreshAppointmentTable();
    }

    @FXML
    void refreshAppointmentList() {
        AppointmentTable.getItems().clear();
        AppointmentTable.getItems().addAll(AppointmentDAO.getAllAppointments());
    }

    @FXML
    private void handleSaveAppointment() {
        try {
           
            int vehicleId = Integer.parseInt(vehicleIdField.getText());
            String customerName = customerNameField.getText();
            LocalDate localDate = appointmentDateField.getValue();
            Date appointmentDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String serviceType = serviceTypeField.getValue();
            String status = statusField.getValue();

     
            if (customerName.isEmpty() || serviceType == null || status == null) {
                showAlert("Input Error", "Please fill in all fields.");
                return;
            }

         
            Appointment appointment = new Appointment(vehicleId, customerName, 
                                                   appointmentDate, serviceType, status);

         
            appointmentDAO.saveAppointment(appointment);

           
            showAlert("Success", "Appointment saved successfully!");

           
            clearForm();
            
     
            refreshAppointmentTable();
            
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Vehicle ID must be a valid number.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred while saving the appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        vehicleIdField.clear();
        customerNameField.clear();
        appointmentDateField.setValue(null);
        serviceTypeField.setValue(null);
        statusField.setValue(null);
    }

    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        try {
            if (customerId == null) {
                showAlert("No Customer", "Please select a customer first from the Customer view");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddAppointmentForm.fxml"));
            Parent root = loader.load();
            
            AddAppointmentController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = customerDAO.getCustomerById(customerId);
            
            if (customer != null) {
                controller.setCustomerData(customer.getCustomerId(), customer.getFullName());
            }
            
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Add New Appointment");
            
        } catch (Exception e) {
            System.err.println("Error loading Add Appointment Form: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Could not load Add Appointment form");
        }
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        Appointment selected = AppointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an appointment to update");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/UpdateAppointment.fxml"));
            Parent root = loader.load();
            
            UpdateAppointmentController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            controller.setAppointmentData(selected);
            controller.setCustomerId(customerId);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Appointment");
            
        } catch (Exception e) {
            System.err.println("Error loading Update Appointment Form: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Could not load Update Appointment form");
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Appointment selectedAppointment = AppointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert("No Selection", "Please select an appointment to delete");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/DeleteAppointment.fxml"));
            Parent root = loader.load();

            DeleteAppointmentController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            controller.setAppointmentData(selectedAppointment);
            controller.setCustomerId(customerId);
            
            Stage currentStage = (Stage) deleteButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Delete Appointment");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load delete form: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewButtonAction(ActionEvent event) {
        Appointment selectedAppointment = AppointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert("No Selection", "Please select an appointment to view");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ViewAppointment.fxml"));
            Parent root = loader.load();

            ViewAppointmentController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            controller.setAppointmentData(selectedAppointment);
            controller.setCustomerId(customerId);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("View Appointment Details");
        } catch (IOException e) {
            showAlert("Error", "Could not load appointment view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void refreshAppointmentTable() {
        try {
            List<Appointment> appointments;
            if (customerId != null) {
                appointments = appointmentDAO.getAppointmentsByCustomerId(customerId);
            } else {
                appointments = appointmentDAO.getAllAppointments();
            }
            ObservableList<Appointment> data = FXCollections.observableArrayList(appointments);
            AppointmentTable.setItems(data);
        } catch (Exception e) {
            showAlert("Error", "Failed to load appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Customer.fxml"));
            Parent root = loader.load();
            
            CustomerController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Customers");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load customer view");
        }
    }
    

    @FXML private Button serviceButton;

   
    @FXML
    private void handleServiceButtonAction(ActionEvent event) {
        Appointment selectedAppointment = AppointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert("No Selection", "Please select an appointment first");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ScheduleService.fxml"));
            Parent root = loader.load();
            
            ScheduleServiceController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            
          
            controller.setAppointmentId(selectedAppointment.getAppointmentId());
            
           
            if (customerId != null) {
                controller.setCustomerId(customerId);
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Schedule Service");
            
        } catch (IOException e) {
            showAlert("Error", "Could not load service view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupSearchField() {
      
        searchBox.setOnMouseClicked(event -> {
            searchField.requestFocus();
        });
        
        searchImage.setOnMouseClicked(event -> {
            searchField.requestFocus();
            performSearch();
        });

        
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch();
            }
        });
    }

    
 

    private void performSearch() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            refreshAppointmentTable();
            return;
        }
        
        try {
            List<Appointment> appointments = new ArrayList<>();
            
        
            try {
                int id = Integer.parseInt(searchText);
                Appointment exactMatch = appointmentDAO.getAppointmentById(id);
                if (exactMatch != null) {
                    appointments.add(exactMatch);
                }
            } catch (NumberFormatException e) {
                
            }
            
        
            if (appointments.isEmpty()) {
                
                appointments.addAll(appointmentDAO.searchAppointmentsByCustomerName(searchText));
               
                appointments.addAll(appointmentDAO.searchAppointmentsByServiceType(searchText));
                
                appointments.addAll(appointmentDAO.searchAppointmentsByStatus(searchText));
              
                appointments = new ArrayList<>(new LinkedHashSet<>(appointments));
            }
            
            if (appointments.isEmpty()) {
                showAlert("No Results", "No appointments found matching: " + searchText);
            } else {
                AppointmentTable.getItems().setAll(appointments);
            }
        } catch (Exception e) {
            showAlert("Search Error", "An error occurred during search: " + e.getMessage());
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
