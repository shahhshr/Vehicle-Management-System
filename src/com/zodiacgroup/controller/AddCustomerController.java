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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Customer;

public class AddCustomerController extends BaseController implements Initializable {

	@FXML
	private TextField inputCustomerFirstNameField;
	@FXML
	private TextField inputCustomerLastNameField;
	@FXML
	private TextField inputCustomerPhoneNumberField;
	@FXML
	private TextField inputCustomerEmailIdField;
	@FXML
	private TextArea inputCustomerAddressField;
	@FXML
	private Button saveButton, cancelButton;

	private CustomerDAO customerDAO = new CustomerDAO();

	@FXML private Label lblUsername;
	private String currentRole;
	private String currentUsername;

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
	
	public void setUsername(String username) {
	    this.currentUsername = username;
	    if (lblUsername != null) {
	        lblUsername.setText(username.toUpperCase());
	    }
	}

	public String getUsername() {
	    return this.currentUsername;
	}

	
	@FXML
	private void handleSaveButtonAction(ActionEvent event) {
		try {
			if (inputCustomerFirstNameField.getText().isEmpty() || inputCustomerLastNameField.getText().isEmpty()
					|| inputCustomerEmailIdField.getText().isEmpty()) {
				showAlert("Validation Error", "First Name, Last Name and Email are required fields");
				return;
			}

			if (!inputCustomerEmailIdField.getText().matches(".+@.+\\..+")) {
				showAlert("Validation Error", "Please enter a valid email address");
				return;
			}

			Customer customer = new Customer();
			
			customer.setFirstName(inputCustomerFirstNameField.getText().trim());
			customer.setLastName(inputCustomerLastNameField.getText().trim());
			customer.setPhoneNumber(inputCustomerPhoneNumberField.getText().trim());
			customer.setEmail(inputCustomerEmailIdField.getText().trim().toLowerCase());
			customer.setAddress(inputCustomerAddressField.getText().trim());

			customerDAO.addCustomer(customer);

			showAlert("Success", "Customer saved successfully");
			clearFields();
			handleBackButtonAction(event);
		} catch (RuntimeException e) {
			showAlert("Error", "Failed to save customer: " + e.getCause().getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			showAlert("Error", "Failed to save customer: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	private void handleCancelButtonAction() {
		clearFields();
	}

	private void clearFields() {
		inputCustomerFirstNameField.clear();
		inputCustomerLastNameField.clear();
		inputCustomerPhoneNumberField.clear();
		inputCustomerEmailIdField.clear();
		inputCustomerAddressField.clear();
	}

	@FXML
	private void handleBackButtonAction(ActionEvent event) {
		try {
			mainApp.showCustomerView(currentRole,currentUsername);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void navigateToCustomerList() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/customer.fxml"));
			Parent root = loader.load();

			CustomerController controller = loader.getController();
			
			controller.setRole(currentRole); 
            controller.setMainApp(mainApp); 
            controller.refreshCustomerList();

			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Customer Management");
		} catch (IOException e) {
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