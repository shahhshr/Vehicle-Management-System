package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import com.zodiacgroup.model.Inventory;

public class ViewInventoryController extends BaseController implements Initializable {
    @FXML private Text inventoryIdText;
    @FXML private Text itemNameText;
    @FXML private Text descriptionText;
    @FXML private Text stockText;
    @FXML private Text priceText;
    @FXML private Text categoryText;
    @FXML private Text minStockText;
    @FXML private Text supplierText;
    @FXML private VBox lowStockWarning;
    
    @FXML private TextField inventoryIdField;
    @FXML private TextField itemNameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField stockQuantityField;
    @FXML private TextField unitPriceField;
    @FXML private TextField categoryField;
    @FXML private TextField minStockField;
    @FXML private TextField supplierField;
    
    @FXML
    private Label lblHome, lblCustomer, lblInventory, lblPayment, lblSalesRep, lblManager, lblViewReport, lblLogout;
    
    private String currentRole;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCurrentPage("inventory");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
        
        if (lblInventory != null) {
        	lblInventory.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
    }
    
    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
        
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
    }
    
    public void setInventoryData(Inventory item) {
        if (item != null) {
            inventoryIdText.setText(String.valueOf(item.getInventoryId()));
            itemNameText.setText(item.getItemName());
            descriptionText.setText(item.getDescription() != null ? item.getDescription() : "N/A");
            stockText.setText(String.valueOf(item.getStockQuantity()));
            priceText.setText(String.format("$%.2f", item.getUnitPrice()));
            categoryText.setText(item.getCategory());
            minStockText.setText(String.valueOf(item.getMinimumStockThreshold()));
            supplierText.setText(item.getSupplier() != null ? item.getSupplier() : "N/A");
            
            if (item.isLowStock()) {
                lowStockWarning.setVisible(true);
            } else {
                lowStockWarning.setVisible(false);
            }
        }
    }
    
    @FXML
    private void handleBackButtonAction() {
        navigateToInventoryList();
    }
    
    private void navigateToInventoryList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/InventoryItems.fxml"));
            Parent root = loader.load();
            
            InventoryController controller = loader.getController();
            controller.refreshInventoryTable();
            controller.setRole(currentRole);  
            controller.setMainApp(mainApp);
            
            Stage stage = (Stage) lowStockWarning.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inventory Management");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load inventory list view");
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