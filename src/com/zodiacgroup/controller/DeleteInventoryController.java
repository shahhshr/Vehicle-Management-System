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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.InventoryDAO;
import com.zodiacgroup.model.Inventory;

public class DeleteInventoryController extends BaseController implements Initializable {
	 @FXML private TextField inventoryIdField;
	    @FXML private TextField itemNameField;
	    @FXML private TextArea descriptionField;
	    @FXML private TextField stockQuantityField;
	    @FXML private TextField unitPriceField;
	    @FXML private TextField categoryField;
	    @FXML private TextField minStockField;
	    @FXML private TextField supplierField;
	    @FXML private Button deleteButton, cancelButton;
    
    private InventoryDAO inventoryDAO = new InventoryDAO();
    private Inventory currentItem;
    
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
        this.currentItem = item;
        if (item != null) {
        	inventoryIdField.setText(String.valueOf(item.getInventoryId()));
            itemNameField.setText(item.getItemName());
            descriptionField.setText(item.getDescription());
            stockQuantityField.setText(String.valueOf(item.getStockQuantity()));
            unitPriceField.setText(String.valueOf(item.getUnitPrice()));
            categoryField.setText(item.getCategory());
            minStockField.setText(String.valueOf(item.getMinimumStockThreshold()));
            supplierField.setText(item.getSupplier());
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        if (currentItem == null) return;
        
        try {
            inventoryDAO.deleteInventoryItem(currentItem.getInventoryId());
            showAlert("Success", "Inventory item deleted successfully");
            navigateToInventoryList();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete inventory item: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
	private void handleBackButtonAction(ActionEvent event) {
		navigateToInventoryList();
	}

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
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
            
            Stage stage = (Stage) cancelButton.getScene().getWindow();
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