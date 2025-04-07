package com.zodiacgroup.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.SaleDAO;
import com.zodiacgroup.model.SaleRepresentative;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DeleteSaleController extends BaseController implements Initializable {
    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField employeeNameField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button backButton;

    private SaleRepresentative currentSaleRep;
    private SaleDAO saleDAO = new SaleDAO();

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
    }
		public void setRole(String role) {
	        this.currentRole = role;
	        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
	                lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);

	        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
	                lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
	    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToSaleList();
    }

    public void setSaleRepresentativeData(SaleRepresentative saleRep) {
        this.currentSaleRep = saleRep;
        if (saleRep != null) {
            employeeIdField.setText(String.valueOf(saleRep.getEmployeeId()));
            employeeNameField.setText(saleRep.getEmployeeName());
            positionField.setText(saleRep.getPosition());
            phoneNumberField.setText(saleRep.getPhoneNumber());
            emailField.setText(saleRep.getEmail());
            usernameField.setText(saleRep.getUsername());
        }
    }

    @FXML
    private void handleDeleteSale(ActionEvent event) {
        if (currentSaleRep == null) {
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this sales representative?");
        confirmAlert.setContentText("This will permanently remove the representative and their login credentials.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                saleDAO.deleteSaleRepresentative(currentSaleRep.getEmployeeId());
                showAlert("Success", "Sales representative and their login credentials deleted successfully!");
                navigateToSaleList();
            } catch (Exception e) {
                showAlert("Error", "Failed to delete sales representative: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}