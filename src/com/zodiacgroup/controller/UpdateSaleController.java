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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.zodiacgroup.dao.LoginDAO;
import com.zodiacgroup.dao.SaleDAO;
import com.zodiacgroup.model.Login;
import com.zodiacgroup.model.SaleRepresentative;

public class UpdateSaleController extends BaseController implements Initializable {
    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ComboBox<String> positionField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    
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
	private String currentRole;

    private SaleDAO saleDAO = new SaleDAO();
    private LoginDAO loginDAO = new LoginDAO();
    private SaleRepresentative currentSaleRep;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
    // Phone number validation pattern (allows numbers, +, -, and spaces)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9\\- ]+$"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
		setCurrentPage("salesRep");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        

		if (lblSalesRep != null) {
			lblSalesRep.setStyle("-fx-font-weight: bold; -fx-underline: true;");
		}
		positionField.getItems().addAll(
		        "Sales Associate",
		        "Senior Sales Associate",
		        "Sales Manager",
		        "Regional Sales Manager",
		        "VP of Sales"
		    );
	}
	
    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);

        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
    }
	public void setSaleRepresentativeToEdit(SaleRepresentative saleRep) {
	    this.currentSaleRep = saleRep;
	    if (saleRep != null) {
	        employeeIdField.setText(String.valueOf(saleRep.getEmployeeId()));
	        // Split employee name into first and last names if needed
	        String[] names = saleRep.getEmployeeName().split(" ", 2);
	        firstNameField.setText(names.length > 0 ? names[0] : "");
	        lastNameField.setText(names.length > 1 ? names[1] : "");
	        positionField.setValue(saleRep.getPosition());
	        emailField.setText(saleRep.getEmail());
	        phoneNumberField.setText(saleRep.getPhoneNumber());
	        usernameField.setText(saleRep.getUsername());
	        passwordField.setText(saleRep.getPassword());
	    }
	}

    @FXML
    private void handleSave(ActionEvent event) {
        // Validate required fields
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || 
            positionField.getValue() == null || emailField.getText().isEmpty() || 
            phoneNumberField.getText().isEmpty() || usernameField.getText().isEmpty() ||
            passwordField.getText().isEmpty()) {
            showAlert("Validation Error", "All fields are required");
            return;
        }
        
        // Validate email format
        if (!isValidEmail(emailField.getText())) {
            showAlert("Error", "Please enter a valid email address!");
            emailField.requestFocus();
            return;
        }
        
        // Validate phone number format
        if (!isValidPhoneNumber(phoneNumberField.getText())) {
            showAlert("Error", "Please enter a valid phone number (only numbers, +, -, and spaces allowed)!");
            phoneNumberField.requestFocus();
            return;
        }
        
        // Validate username uniqueness
        Login existingLogin = loginDAO.getLoginByUsername(usernameField.getText());
        if (existingLogin != null && (currentSaleRep == null || 
            !existingLogin.getUsername().equals(currentSaleRep.getUsername()))) {
            showAlert("Error", "Username already exists! Please choose a different username.");
            usernameField.requestFocus();
            return;
        }

        try {
            int employeeId = Integer.parseInt(employeeIdField.getText());
            String fullName = firstNameField.getText() + " " + lastNameField.getText();

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Are you sure you want to update this sales representative?");
            confirmAlert.setContentText("This will modify the sales representative details in the database.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Update sales representative
                SaleRepresentative updatedSaleRep = new SaleRepresentative();
                updatedSaleRep.setEmployeeId(employeeId);
                updatedSaleRep.setEmployeeName(fullName);
                updatedSaleRep.setPosition(positionField.getValue());
                updatedSaleRep.setEmail(emailField.getText());
                updatedSaleRep.setPhoneNumber(phoneNumberField.getText());
                updatedSaleRep.setUsername(usernameField.getText());
                updatedSaleRep.setPassword(passwordField.getText());

                saleDAO.updateSaleRepresentative(updatedSaleRep);

                // Update login credentials
                Login login = new Login();
                login.setUsername(usernameField.getText());
                login.setPassword(passwordField.getText());
                login.setRole("SALES_REP");
                
                if (currentSaleRep != null && currentSaleRep.getUsername() != null) {
                    // Update existing login
                    Login existing = loginDAO.getLoginByUsername(currentSaleRep.getUsername());
                    if (existing != null) {
                        login.setId(existing.getId());
                        loginDAO.updateLogin(fullName, login);
                    } else {
                        loginDAO.createLogin(login);
                    }
                } else {
                    // Create new login
                    loginDAO.createLogin(login);
                }

                showAlert("Success", "Sales representative updated successfully!");
                navigateToSaleList();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Employee ID format");
        } catch (Exception e) {
            showAlert("Error", "Failed to update sales representative: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    // Phone number validation method
    private boolean isValidPhoneNumber(String phone) {
        // Basic validation - at least 6 digits and matches the pattern
        return PHONE_PATTERN.matcher(phone).matches() && 
               phone.replaceAll("[^0-9]", "").length() >= 6;
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        employeeIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        positionField.setValue(null);
        emailField.clear();
        phoneNumberField.clear();
        usernameField.clear();
        passwordField.clear();
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToSaleList();
    }

    private void navigateToSaleList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Sales.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sales Representatives Management");

            // Refresh the table
            SaleController controller = loader.getController();
            controller.setRole(currentRole); // Ensure role is maintained
            controller.setMainApp(mainApp); 
            controller.refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load sales representatives list view");
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