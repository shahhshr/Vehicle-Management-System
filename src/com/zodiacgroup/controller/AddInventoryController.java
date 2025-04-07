package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import com.zodiacgroup.dao.InventoryDAO;
import com.zodiacgroup.model.Inventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.exception.ConstraintViolationException;

public class AddInventoryController extends BaseController implements Initializable {
    @FXML private TextField itemNameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField stockQuantityField;
    @FXML private TextField unitPriceField;
    @FXML private TextField categoryField;
    @FXML private TextField minStockField;
    @FXML private TextField supplierField;
    @FXML private Button saveButton, cancelButton;
    private InventoryDAO inventoryDAO = new InventoryDAO();

    
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

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        try {
            if (itemNameField.getText().isEmpty() || 
                stockQuantityField.getText().isEmpty() || 
                unitPriceField.getText().isEmpty() || 
                categoryField.getText().isEmpty()) {
                showAlert("Validation Error", "Item Name, Stock, Unit Price and Category are required fields");
                return;
            }

            int stockQuantity, minStock;
            double unitPrice;
            try {
                stockQuantity = Integer.parseInt(stockQuantityField.getText());
                unitPrice = Double.parseDouble(unitPriceField.getText());
                minStock = minStockField.getText().isEmpty() ? 0 : Integer.parseInt(minStockField.getText());
                
                if (stockQuantity < 0 || unitPrice < 0 || minStock < 0) {
                    showAlert("Validation Error", "Stock, Price and Minimum Stock must be positive numbers");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Validation Error", "Stock and Price must be valid numbers");
                return;
            }

            Inventory item = new Inventory();
            item.setItemName(itemNameField.getText().trim());
            item.setDescription(descriptionField.getText().trim());
            item.setStockQuantity(stockQuantity);
            item.setUnitPrice(unitPrice);
            item.setCategory(categoryField.getText().trim());
            item.setMinimumStockThreshold(minStock);
            item.setSupplier(supplierField.getText().trim());

            inventoryDAO.addInventoryItem(item);

            showAlert("Success", "Inventory item saved successfully");
            clearFields();
            navigateToInventoryList();
        } catch (ConstraintViolationException e) {
            showAlert("Database Error", "This item already exists or violates database constraints");
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if (e.getCause() != null) {
                errorMessage = e.getCause().getMessage();
            }
            showAlert("Error", "Failed to save inventory item: " + errorMessage);
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Error", "Failed to save inventory item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        clearFields();
    }

    private void clearFields() {
        itemNameField.clear();
        descriptionField.clear();
        stockQuantityField.clear();
        unitPriceField.clear();
        categoryField.clear();
        minStockField.clear();
        supplierField.clear();
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
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
        } catch (IOException e) {
            showAlert("Error", "Could not load inventory list view: " + e.getMessage());
            e.printStackTrace();
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