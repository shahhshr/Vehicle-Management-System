package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.model.Customer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController extends BaseController implements Initializable {
	@FXML
	private TableColumn<Customer, Integer> customerIdColumn;
	@FXML
	private TableColumn<Customer, String> customerNameColumn;
	@FXML
	private TableColumn<Customer, String> emailColumn;
	@FXML
	private TableColumn<Customer, String> phoneColumn;
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField addressField;
	@FXML
	private TableView<Customer> customerTable;
	@FXML
	private CustomerDAO customerDAO;
	@FXML
	private Button addButton, deleteButton, updateButton;
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
	private String currentRole;

	public CustomerController() {
		customerDAO = new CustomerDAO();
	}

	private ObservableList<Customer> customerList = FXCollections.observableArrayList();

	@FXML
	private Label lblHome;
	@FXML
	private Label lblCustomer;
	@FXML
	private Label lblPayment;
	@FXML
	private Label lblInventory;
	@FXML
	private Label lblSalesRep;
	@FXML
	private Label lblManager;
	@FXML
	private Label lblViewReport;
	@FXML
	private Label lblLogout;

	public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
				lblLogout, currentRole);

		setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
				lblLogout, currentRole);
    }

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setCurrentPage("customer");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
		if (lblCustomer != null) {
            lblCustomer.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }

		customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
		customerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

		loadCustomerData();

		setupSearchField();

	}

	private void loadCustomerData() {
		List<Customer> customers = customerDAO.getAllCustomers();
		customerTable.getItems().setAll(customers);
	}

	@FXML
	private void handleViewButtonAction(ActionEvent event) {
		Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
		if (selectedCustomer == null) {
			showAlert("No Selection", "Please select a customer to view");
			return;
		}

		try {
			Customer customerWithVehicles = customerDAO.getCustomerWithVehicles(selectedCustomer.getCustomerId());

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ViewCustomer.fxml"));
			Parent root = loader.load();

			ViewCustomerController controller = loader.getController();
			controller.setCustomerData(customerWithVehicles);
			controller.setMainApp(mainApp);
			controller.setRole(currentRole);

			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(new Scene(root));
			currentStage.setTitle("View Customer Details");
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Could not load view form: " + e.getMessage());
		}
	}

	@FXML
	private void handleOpenAddCustomer(ActionEvent event) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddCustomer.fxml"));
	        Parent root = loader.load();
	        
	        AddCustomerController controller = loader.getController();
	        controller.setMainApp(mainApp);  
	        controller.setRole(currentRole); 
	        
	        Stage currentStage = (Stage) addButton.getScene().getWindow();
	        currentStage.setScene(new Scene(root, 1200, 800));
	        currentStage.setTitle("Add Customer");
	    } catch (IOException e) {
	        e.printStackTrace();
	        showAlert("Error", "Could not load add customer form");
	    }
	}

	@FXML
	private void handleUpdateButtonAction(ActionEvent event) {
		Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
		if (selectedCustomer == null) {
			showAlert("No Selection", "Please select a customer to edit");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/editCustomer.fxml"));
			Parent root = loader.load();

			EditCustomerController controller = loader.getController();
			controller.setCustomerData(selectedCustomer);
			controller.setMainApp(mainApp);
			controller.setRole(currentRole);

			Stage currentStage = (Stage) updateButton.getScene().getWindow();
			currentStage.setScene(new Scene(root));
			currentStage.setTitle("Edit Customer");
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Could not load edit form: " + e.getMessage());
		}
	}

	@FXML
	private void handleDeleteButtonAction(ActionEvent event) {
		Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
		
		if (selectedCustomer == null) {
			showAlert("No Selection", "Please select a customer to delete");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/deleteCustomer.fxml"));
			Parent root = loader.load();

			DeleteCustomerController controller = loader.getController();
			controller.setCustomerData(selectedCustomer);
			controller.setMainApp(mainApp);
			controller.setRole(currentRole);

			Stage currentStage = (Stage) deleteButton.getScene().getWindow();
			currentStage.setScene(new Scene(root));
			currentStage.setTitle("Delete Customer");
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
			loadCustomerData();
			return;
		}

		try {
			List<Customer> customers = new ArrayList<>();

			try {
				int id = Integer.parseInt(searchText);
				Customer exactMatch = customerDAO.getCustomerById(id);
				if (exactMatch != null) {
					customers.add(exactMatch);
				}
				customers.addAll(customerDAO.searchCustomersById(searchText));
			} catch (NumberFormatException e) {
			}

			if (customers.isEmpty()) {
				String[] nameParts = searchText.split("\\s+", 2);

				if (nameParts.length == 2) {
					customers = customerDAO.searchCustomersByName(nameParts[0], nameParts[1]);
				} else {
					customers = customerDAO.searchCustomersByFirstName(searchText);
					if (customers.isEmpty()) {
						customers = customerDAO.searchCustomersByLastName(searchText);
					}
				}
			}

			customerTable.getItems().setAll(customers);
		} catch (Exception e) {
			showAlert("Search Error", "An error occurred during search: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	private void handleRefreshImageClick(MouseEvent event) {
		refreshTable();
	}

	@FXML
	private void handleRefreshButtonAction(ActionEvent event) {
		refreshTable();
	}

	private void refreshTable() {
		customerTable.getItems().setAll(customerDAO.getAllCustomers());
	}

	@FXML
	void refreshCustomerList() {
		customerTable.getItems().clear();
		customerTable.getItems().addAll(customerDAO.getAllCustomers());
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	private void handleVehiclesButtonAction(ActionEvent event) {
	    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
	    if (selectedCustomer == null) {
	        showAlert("No Selection", "Please select a customer first");
	        return;
	    }

	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/vehicles.fxml"));
	        Parent root = loader.load();

	        VehiclesController controller = loader.getController();
	        controller.setMainApp(mainApp);      
	        controller.setRole(currentRole);     
	        controller.setCustomer(selectedCustomer);

	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setScene(new Scene(root));
	        stage.setTitle(selectedCustomer.getFirstName() + "'s Vehicles");
	    } catch (IOException e) {
	        e.printStackTrace();
	        showAlert("Error", "Could not load vehicles view");
	    }
	}

	@FXML
	private void handleAppointmentsButtonAction(ActionEvent event) {
	    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
	    if (selectedCustomer == null) {
	        showAlert("No Selection", "Please select a customer first");
	        return;
	    }

	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/appointment.fxml"));
	        Parent root = loader.load();

	        AppointmentController controller = loader.getController();
	        controller.setMainApp(mainApp);
	        controller.setRole(currentRole);
	        controller.setCustomerId(selectedCustomer.getCustomerId());
	        
	        controller.refreshAppointmentTable();

	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setScene(new Scene(root));
	        stage.setTitle("Appointments for " + selectedCustomer.getFullName());
	    } catch (IOException e) {
	        e.printStackTrace(); 
	        showAlert("Error", "Could not load appointments view: " + e.getMessage());
	    }
	}
	
	@FXML
	private void handleServicesButtonAction(ActionEvent event) {
	    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
	    if (selectedCustomer == null) {
	        showAlert("No Selection", "Please select a customer first");
	        return;
	    }

	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ScheduleService.fxml"));
	        Parent root = loader.load();

	        ScheduleServiceController controller = loader.getController();
	        controller.setMainApp(mainApp);
	        controller.setRole(currentRole);
	        controller.setCustomerId(selectedCustomer.getCustomerId()); 
	        
	        controller.refreshServiceTable();

	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setScene(new Scene(root));
	        stage.setTitle("Services for " + selectedCustomer.getFullName());
	    } catch (IOException e) {
	        e.printStackTrace();
	        showAlert("Error", "Could not load services view: " + e.getMessage());
	    }
	}
	
}