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
import java.util.Optional;
import java.util.ResourceBundle;


public class EditInventoryController extends BaseController implements Initializable {
    @FXML private TextField inventoryIdField;
    @FXML private TextField itemNameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField stockQuantityField;
    @FXML private TextField unitPriceField;
    @FXML private TextField categoryField;
    @FXML private TextField minStockField;
    @FXML private TextField supplierField;
    @FXML private Button saveButton, deleteButton, cancelButton;
    
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

    public void setInventoryData(Inventory item) {
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
    private void handleSave(ActionEvent event) {
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
            } catch (NumberFormatException e) {
                showAlert("Validation Error", "Stock and Price must be valid numbers");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Are you sure you want to update this inventory item?");
            confirmAlert.setContentText("This will modify the item details in the database.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int inventoryId = Integer.parseInt(inventoryIdField.getText());
                Inventory item = inventoryDAO.getInventoryItemById(inventoryId);

                if (item != null) {
                    item.setItemName(itemNameField.getText().trim());
                    item.setDescription(descriptionField.getText().trim());
                    item.setStockQuantity(stockQuantity);
                    item.setUnitPrice(unitPrice);
                    item.setCategory(categoryField.getText().trim());
                    item.setMinimumStockThreshold(minStock);
                    item.setSupplier(supplierField.getText().trim());

                    inventoryDAO.updateInventoryItem(item);
                    showAlert("Success", "Inventory item updated successfully!");
                    navigateToInventoryList();
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to update inventory item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            int inventoryId = Integer.parseInt(inventoryIdField.getText());

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Are you sure you want to delete this inventory item?");
            confirmAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                inventoryDAO.deleteInventoryItem(inventoryId);
                showAlert("Success", "Inventory item deleted successfully!");
                navigateToInventoryList();
            }
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

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inventory Management");

            InventoryController controller = loader.getController();
            controller.refreshInventoryTable();
            controller.setRole(currentRole); 
            controller.setMainApp(mainApp);
        } catch (IOException e) {
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