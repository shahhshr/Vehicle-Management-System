package com.zodiacgroup.controller;

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ScheduleServiceController extends BaseController implements Initializable {

    @FXML private TableView<Service> serviceTable;
    @FXML private TableColumn<Service, Integer> serviceIdColumn;
    @FXML private TableColumn<Service, String> customerNameColumn;
    @FXML private TableColumn<Service, String> vehicleNameColumn;
    @FXML private TableColumn<Service, String> serviceDetailColumn;
    @FXML private TableColumn<Service, String> statusColumn;
    @FXML private TableColumn<Service, String> dateColumn;
    @FXML private TableColumn<Service, Double> costColumn;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    // Side menu labels
    @FXML private Label lblHome;
    @FXML private Label lblCustomer;
    @FXML private Label lblPayment;
    @FXML private Label lblInventory;
    @FXML private Label lblSalesRep;
    @FXML private Label lblManager;
    @FXML private Label lblViewReport;
    @FXML private Label lblLogout;
    @FXML private TextField searchField;
    @FXML private Rectangle searchBox;
    @FXML private ImageView searchImage;

    private final ServiceDAO serviceDAO = new ServiceDAO();
    private Integer customerId;
    private String currentRole;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the table columns
        serviceIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        vehicleNameColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));
        serviceDetailColumn.setCellValueFactory(new PropertyValueFactory<>("serviceDetail"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        
        // Set current page and initialize menu
        setCurrentPage("customer");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
		if (lblCustomer != null) {
            lblCustomer.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        setupSearchField();
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
        refreshServiceTable();
    }

    private Integer appointmentId;

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
        // You can use this ID to pre-populate fields or filter services
        refreshServiceTable();
    }
    
    private void setupSearchField() {
        // Make the search box and image clickable to focus the field
        searchBox.setOnMouseClicked(event -> {
            searchField.requestFocus();
        });
        
        searchImage.setOnMouseClicked(event -> {
            searchField.requestFocus();
            performSearch();
        });

        // Handle Enter key in search field
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            refreshServiceTable();
            return;
        }
        
        try {
            List<Service> services;
            
            if (customerId != null) {
                // Search within customer's services
                services = serviceDAO.searchServicesByCustomerId(customerId, searchText);
            } else {
                // General search
                services = serviceDAO.searchServices(searchText);
            }
            
            if (services.isEmpty()) {
                showAlert("No Results", "No services found matching your search criteria.");
            } else {
                serviceTable.getItems().setAll(services);
            }
        } catch (Exception e) {
            showAlert("Search Error", "An error occurred during search: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleClearSearch(MouseEvent event) {
        searchField.clear();
        refreshServiceTable();
    }
    
    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddScheduleService.fxml"));
            Parent root = loader.load();
            
            AddScheduleServiceController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            
            if (customerId != null) {
                // Fetch customer details from database
                CustomerDAO customerDAO = new CustomerDAO();
                Customer customer = customerDAO.getCustomerById(customerId);
                
                if (customer != null) {
                    // Set both customer ID and full name
                    controller.setCustomerData(
                        customerId, 
                        customer.getFirstName() + " " + customer.getLastName()
                    );
                } else {
                    showAlert("Warning", "Customer not found with ID: " + customerId);
                    controller.setCustomerData(customerId, "Unknown Customer");
                }
            }
            
            Stage stage = (Stage) serviceTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load add service form");
        }
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        Service selectedService = serviceTable.getSelectionModel().getSelectedItem();
        if (selectedService == null) {
            showAlert("No Selection", "Please select a service to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/UpdateScheduleService.fxml"));
            Parent root = loader.load();

            UpdateScheduleServiceController controller = loader.getController();
            controller.setServiceData(selectedService);
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            controller.setCustomerId(customerId);

            Stage currentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Update Service");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load update form: " + e.getMessage());
        }
    }
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Service selectedService = serviceTable.getSelectionModel().getSelectedItem();
        if (selectedService != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/DeleteSchedule.fxml"));
                Parent root = loader.load();
                
                DeleteScheduleController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setRole(currentRole);
                controller.setServiceData(selectedService);
                
                if (customerId != null) {
                    controller.setCustomerId(customerId);
                }
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                showAlert("Error", "Could not load delete service view");
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "Please select a service to delete");
        }
    }
 // In ScheduleServiceController.java, add this method:
    @FXML
    private void handleViewButtonAction(ActionEvent event) {
        Service selectedService = serviceTable.getSelectionModel().getSelectedItem();
        if (selectedService == null) {
            showAlert("No Selection", "Please select a service to view");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ViewService.fxml"));
            Parent root = loader.load();

            ViewServiceController controller = loader.getController();
            controller.setServiceData(selectedService);
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            controller.setCustomerId(customerId);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Service Details");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load service view");
        }
    }

    @FXML
    private void handleRefreshButtonAction(ActionEvent event) {
        refreshServiceTable();
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

    public void refreshServiceTable() {
        try {
            List<Service> services;
            if (customerId != null) {
                services = serviceDAO.getServicesByCustomerId(customerId);
            } else {
                services = serviceDAO.getAllServices();
            }
            serviceTable.getItems().setAll(services);
        } catch (Exception e) {
            showAlert("Error", "Failed to load services: " + e.getMessage());
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