package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.SaleDAO;
import com.zodiacgroup.model.SaleRepresentative;

public class ViewSaleController extends BaseController implements Initializable {
    @FXML private Text employeeIdText;
    @FXML private Text employeeNameText;
    @FXML private Text positionText;
    @FXML private Text emailText;
    @FXML private Text phoneText;
    @FXML private Text usernameText;
    @FXML private Text passwordText;
    @FXML private Label salesRepNameLabel;
    @FXML private Button cancelButton;
    
    private SaleDAO saleDAO = new SaleDAO();
    private SaleRepresentative currentSaleRep;
    
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

    public void setSaleRepresentativeData(SaleRepresentative saleRep) {
        this.currentSaleRep = saleRep;
        
        if (saleRep != null) {
            employeeIdText.setText(String.valueOf(saleRep.getEmployeeId()));
            employeeNameText.setText(saleRep.getEmployeeName());
            positionText.setText(saleRep.getPosition());
            emailText.setText(saleRep.getEmail());
            phoneText.setText(saleRep.getPhoneNumber());
            usernameText.setText(saleRep.getUsername());
            passwordText.setText(saleRep.getPassword()); // In production, you might want to mask this
            salesRepNameLabel.setText(saleRep.getEmployeeName());
        }
    }
    
    @FXML
    private void handleBackButtonAction() {
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Sales.fxml"));
            Parent root = loader.load();
            
            SaleController controller = loader.getController();
            controller.setRole(currentRole);
            controller.setMainApp(mainApp); 
            controller.loadSaleRepresentatives();
            
            stage.setScene(new Scene(root));
            stage.setTitle("Sales Representatives");
        } catch (Exception e) {
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