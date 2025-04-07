package com.zodiacgroup.controller;

import com.zodiacgroup.dao.LoginDAO;
import com.zodiacgroup.dao.SaleDAO;
import com.zodiacgroup.model.Login;
import com.zodiacgroup.model.SaleRepresentative;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddSaleController extends BaseController implements Initializable {
    @FXML private TextField inputEmployeeFirstNameField;
    @FXML private TextField inputEmployeeLastNameField;
    @FXML private ComboBox<String> inputEmployeePositionField;
    @FXML private TextField inputEmployeeEmailField;
    @FXML private TextField inputEmployeePhoneField;
    @FXML private TextField inputEmployeeUsernameField;
    @FXML private PasswordField inputEmployeePasswordField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    
    private SaleDAO saleDAO = new SaleDAO();
    private LoginDAO loginDAO = new LoginDAO();
    private SaleRepresentative saleRepToEdit;
    
  
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
 
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9\\- ]+$"
    );
    
   
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]+$"
    );
    
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
		setCurrentPage("salesRep");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        

		if (lblSalesRep != null) {
			lblSalesRep.setStyle("-fx-font-weight: bold; -fx-underline: true;");
		}
        inputEmployeePositionField.getItems().addAll(
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
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        String firstName = inputEmployeeFirstNameField.getText().trim();
        String lastName = inputEmployeeLastNameField.getText().trim();
        String position = inputEmployeePositionField.getValue();
        String email = inputEmployeeEmailField.getText().trim();
        String phone = inputEmployeePhoneField.getText().trim();
        String username = inputEmployeeUsernameField.getText().trim();
        String password = inputEmployeePasswordField.getText().trim();
        
       
        if (firstName.isEmpty() || lastName.isEmpty() || position == null || position.isEmpty() || 
            email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }
       
        if (!isValidEmail(email)) {
            showAlert("Error", "Please enter a valid email address!");
            inputEmployeeEmailField.requestFocus();
            return;
        }
        
     
        if (!isValidPhoneNumber(phone)) {
            showAlert("Error", "Please enter a valid phone number (only numbers, +, -, and spaces allowed)!");
            inputEmployeePhoneField.requestFocus();
            return;
        }
        
      
        if (!isValidUsername(username)) {
            showAlert("Error", "Username can only contain letters, numbers, and underscores!");
            inputEmployeeUsernameField.requestFocus();
            return;
        }
        
    
        if (loginDAO.getLoginByUsername(username) != null) {
            showAlert("Error", "Username already exists! Please choose a different username.");
            inputEmployeeUsernameField.requestFocus();
            return;
        }
        
        String fullName = firstName + " " + lastName;
        SaleRepresentative saleRep = new SaleRepresentative();
        saleRep.setEmployeeName(fullName);
        saleRep.setPosition(position);
        saleRep.setEmail(email);
        saleRep.setPhoneNumber(phone);
        saleRep.setUsername(username);
        saleRep.setPassword(password);
        
        try {
            
            Login login = new Login();
            login.setUsername(username);
            login.setPassword(password);
            login.setRole("SALES_REP");
            
            if (saleRepToEdit == null) {
            
                saleDAO.addSaleRepresentative(saleRep);
                loginDAO.createLogin(login);
                showAlert("Success", "Sales representative added successfully!");
            } else {
            
                saleRep.setEmployeeId(saleRepToEdit.getEmployeeId());
                saleDAO.updateSaleRepresentative(saleRep);
                
                
                if (!username.equals(saleRepToEdit.getUsername())) {
                    loginDAO.updateLogin(saleRepToEdit.getUsername(), login);
                } else if (!password.equals(saleRepToEdit.getPassword())) {
                    login.setUsername(username);
                    login.setPassword(password);
                    loginDAO.updateLogin(username, login);
                }
                
                showAlert("Success", "Sales representative updated successfully!");
            }
            navigateToSaleList();
        } catch (Exception e) {
            showAlert("Error", "Failed to save sales representative: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
   
    private boolean isValidPhoneNumber(String phone) {
        return PHONE_PATTERN.matcher(phone).matches() && 
               phone.replaceAll("[^0-9]", "").length() >= 6;
    }
    
   
    private boolean isValidUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }
    
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToSaleList();
    }

    private void navigateToSaleList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Sales.fxml"));
            Parent root = loader.load();

            SaleController controller = loader.getController();
            controller.setRole(currentRole);
            controller.setMainApp(mainApp); 
            controller.refreshTable();

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sales Representatives");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load sales representatives list view");
        }
    }
    
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        navigateToSaleList();
    }
    
    public void setSaleRepresentativeToEdit(SaleRepresentative saleRep) {
        this.saleRepToEdit = saleRep;
        if (saleRep != null) {
            String[] nameParts = saleRep.getEmployeeName().split(" ", 2);
            inputEmployeeFirstNameField.setText(nameParts.length > 0 ? nameParts[0] : "");
            inputEmployeeLastNameField.setText(nameParts.length > 1 ? nameParts[1] : "");
            inputEmployeePositionField.setValue(saleRep.getPosition());
            inputEmployeeEmailField.setText(saleRep.getEmail());
            inputEmployeePhoneField.setText(saleRep.getPhoneNumber());
            inputEmployeeUsernameField.setText(saleRep.getUsername());
            inputEmployeePasswordField.setText(saleRep.getPassword());
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