package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Customer;

public class EditCustomerController extends BaseController implements Initializable {
	@FXML
	private TextField customerIdField;
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private TextField emailField;
	@FXML
	private TextArea addressField;
	@FXML
	private Button cancelButton, deleteButton, saveButton;
	private String currentRole;

	@FXML
	private Label lblHome, lblCustomer, lblVehicles, lblAppointment, lblService, lblInventory, lblPayment,
			lblViewReport, lblSalesRep, lblManager, lblLogout ;


	private CustomerDAO customerDAO = new CustomerDAO();

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
    

	@FXML
	private void handleSave(ActionEvent event) {
		if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || emailField.getText().isEmpty()) {
			showAlert("Validation Error", "First Name, Last Name and Email are required");
			return;
		}

		try {
			int customerId = Integer.parseInt(customerIdField.getText());

			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			confirmAlert.setTitle("Confirm Update");
			confirmAlert.setHeaderText("Are you sure you want to update this customer?");
			confirmAlert.setContentText("This will modify the customer details in the database.");

			Optional<ButtonType> result = confirmAlert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				Customer customer = customerDAO.getCustomerById(customerId);

				if (customer != null) {
					customer.setFirstName(firstNameField.getText());
					customer.setLastName(lastNameField.getText());
					customer.setPhoneNumber(phoneNumberField.getText());
					customer.setEmail(emailField.getText());
					customer.setAddress(addressField.getText());

					customerDAO.updateCustomer(customer);

					showAlert("Success", "Customer updated successfully!");

					navigateToCustomerList();
				}
			}
		} catch (NumberFormatException e) {
			showAlert("Error", "Invalid Customer ID format");
		} catch (Exception e) {
			showAlert("Error", "Failed to update customer: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	private void handleDelete(ActionEvent event) {
		try {
			int customerId = Integer.parseInt(customerIdField.getText());

			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			confirmAlert.setTitle("Confirm Deletion");
			confirmAlert.setHeaderText("Are you sure you want to delete this customer?");
			confirmAlert.setContentText("This action cannot be undone.");

			Optional<ButtonType> result = confirmAlert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				customerDAO.deleteCustomer(customerId);
				showAlert("Success", "Customer deleted successfully!");

				navigateToCustomerList();
			}
		} catch (Exception e) {
			showAlert("Error", "Failed to delete customer: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	private void handleCancelButtonAction(ActionEvent event) {
		clearFields();
	}

	private void clearFields() {
		customerIdField.clear();
		firstNameField.clear();
		lastNameField.clear();
		phoneNumberField.clear();
		emailField.clear();
		addressField.clear();
	}

	@FXML
	private void handleBackButtonAction(ActionEvent event) {
		navigateToCustomerList();
	}

	private void navigateToCustomerList() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/customer.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) saveButton.getScene().getWindow(); 

			stage.setScene(new Scene(root));
			stage.setTitle("Customer Management");

			CustomerController controller = loader.getController();
			controller.setRole(currentRole); 
            controller.setMainApp(mainApp); 
			controller.refreshCustomerList();
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Could not load customer list view");
		}
	}

	public void setCustomerData(Customer customer) {
		if (customer != null) {
			customerIdField.setText(String.valueOf(customer.getCustomerId()));
			firstNameField.setText(customer.getFirstName());
			lastNameField.setText(customer.getLastName());
			phoneNumberField.setText(customer.getPhoneNumber());
			emailField.setText(customer.getEmail());
			addressField.setText(customer.getAddress());
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