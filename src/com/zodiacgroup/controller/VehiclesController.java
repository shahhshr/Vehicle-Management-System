package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import com.zodiacgroup.dao.VehicleDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Vehicle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VehiclesController extends BaseController implements Initializable {
    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, Integer> vehicleIdColumn;
    @FXML
    private TableColumn<Vehicle, String> makeColumn;
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    @FXML
    private TableColumn<Vehicle, Integer> yearColumn;
    @FXML
    private TableColumn<Vehicle, String> vinColumn;
    @FXML
    private TableColumn<Vehicle, String> customerColumn;
    @FXML
    private Button addButton, deleteButton, updateButton, viewButton, cancelButton;
    @FXML
    private ImageView refreshImageView;
    @FXML 
    private TextField searchField;
    @FXML 
    private Rectangle searchBox;
    @FXML 
    private Label searchLabel;
    @FXML 
    private ImageView searchImage;
    private Customer customer;
    @FXML
    private Integer customerId;
    private VehicleDAO vehicleDAO;
    private ObservableList<Vehicle> vehicleList;
   
    
    
    

    public VehiclesController() {
        vehicleDAO = new VehicleDAO();
        vehicleList = FXCollections.observableArrayList();
    }
    
    @FXML
   	private Label lblHome, lblCustomer, lblSalesRep, lblManager, lblLogout, lblInventory, lblPayment,
   			lblViewReport;
       private String currentRole;
       
       
   

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
           vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
           makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
           modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
           yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
           vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));
           customerColumn.setCellValueFactory(cellData -> {
               if (cellData.getValue().getCustomer() != null) {
                   return new SimpleStringProperty(
                       cellData.getValue().getCustomer().getFirstName() + " " + 
                       cellData.getValue().getCustomer().getLastName());
               }
               return new SimpleStringProperty("");
           });

           refreshVehicleTable();
           
           setupSearchField();
           
           
       }

       public void setRole(String role) {
           this.currentRole = role;
           if (lblHome != null && lblCustomer != null) { 
               configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                       lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
               
               setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                       lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
           }
       }
     
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
        refreshVehicleTable(); 
    }
    
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/customer.fxml"));
            Parent root = loader.load();
            
            CustomerController controller = loader.getController();
            controller.setRole(currentRole); 
            controller.setMainApp(mainApp); 
            
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Customers Management");
        } catch (IOException e) {
            System.err.println("Error navigating back: " + e.getMessage());
            showAlert("Navigation Error", "Could not return to customers view");
        }
    }
    
    @FXML
    private void handleClearSearch(MouseEvent event) {
        searchField.clear();
        refreshVehicleTable();
    }

	

    @FXML
    private void handleViewButtonAction(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("No Selection", "Please select a vehicle to view");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ViewVehicles.fxml"));
            Parent root = loader.load();

            ViewVehiclesController controller = loader.getController();
            controller.setVehicleData(selectedVehicle);
            controller.setMainApp(mainApp);
			controller.setRole(currentRole);
            controller.setCustomer(customer); 

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("View Vehicle Details");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load view form: " + e.getMessage());
        }
    }
    
    
    @FXML
    private Label customerNameLabel;

    public void setCustomerName(String name) {
        customerNameLabel.setText(name + "'s Vehicles");
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerId = customer != null ? customer.getCustomerId() : null;
        if (customerNameLabel != null && customer != null) {
            customerNameLabel.setText(customer.getFirstName() + "'s Vehicles");
        }
        refreshVehicleTable();
    }
    
    

    @FXML
    private void handleOpenAddVehicle(ActionEvent event) {
        if (customer == null) {
            showAlert("Error", "No customer selected");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddVehicles.fxml"));
            Parent root = loader.load();
            
            AddVehiclesController controller = loader.getController();
            controller.setMainApp(mainApp);
			controller.setRole(currentRole);
            controller.setCustomer(customer); 
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Vehicle Details");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load form: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("No Selection", "Please select a vehicle to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/EditVehicles.fxml"));
            Parent root = loader.load();

            EditVehiclesController controller = loader.getController();
            if (selectedVehicle.getCustomer() == null && this.customer != null) {
                selectedVehicle.setCustomer(this.customer);
            }
            controller.setVehicleData(selectedVehicle);
            controller.setMainApp(mainApp);
			controller.setRole(currentRole);

            Stage currentStage = (Stage) updateButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Edit Vehicle");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load edit form: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("No Selection", "Please select a vehicle to delete");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/DeleteVehicles.fxml"));
            Parent root = loader.load();

            DeleteVehicleController controller = loader.getController();
            controller.setVehicleData(selectedVehicle);
            controller.setMainApp(mainApp);
			controller.setRole(currentRole);

            Stage currentStage = (Stage) deleteButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Delete Vehicle");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load delete form: " + e.getMessage());
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
            refreshVehicleTable();
            return;
        }
        
        try {
            List<Vehicle> vehicles = new ArrayList<>();
            
            try {
                int id = Integer.parseInt(searchText);
                Vehicle exactMatch = vehicleDAO.getVehicleById(id);
                if (exactMatch != null) {
                    vehicles.add(exactMatch);
                }
            } catch (NumberFormatException e) {
            }
            
            if (vehicles.isEmpty()) {
                vehicles = vehicleDAO.searchVehicles(searchText);
            }
            
            vehicleTable.getItems().setAll(vehicles);
        } catch (Exception e) {
            showAlert("Search Error", "An error occurred during search: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleAppointmentsButtonAction(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("No Selection", "Please select a vehicle first");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/appointment.fxml"));
            Parent root = loader.load();
            
            AppointmentController controller = loader.getController();
            controller.setCustomerId(selectedVehicle.getCustomer().getCustomerId());
            controller.setVehicleId(selectedVehicle.getVehicleId()); 
            controller.setRole(currentRole); 
            controller.setMainApp(mainApp);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Appointments for " + selectedVehicle.getMake() + " " + selectedVehicle.getModel());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load appointments view");
        }
    }
    
    @FXML
    private void handleServicesButtonAction(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("No Selection", "Please select a vehicle first");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/service.fxml"));
            Parent root = loader.load();
            
            ScheduleServiceController controller = loader.getController();
            controller.setCustomerId(selectedVehicle.getCustomer().getCustomerId());
            controller.setRole(currentRole); 
            controller.setMainApp(mainApp);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Services for " + selectedVehicle.getMake() + " " + selectedVehicle.getModel());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load services view");
        }
    }
    
    

    @FXML
    private void handleRefreshImageClick(MouseEvent event) {
        refreshVehicleTable();
    }

    @FXML
    private void handleRefreshButtonAction(ActionEvent event) {
        refreshVehicleTable();
    }

    public void refreshVehicleTable() {
        if (customerId != null) {
            List<Vehicle> vehicles = vehicleDAO.getVehiclesByCustomerId(customerId);
            vehicleTable.getItems().setAll(vehicles);
        } else {
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            vehicleTable.getItems().setAll(vehicles);
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