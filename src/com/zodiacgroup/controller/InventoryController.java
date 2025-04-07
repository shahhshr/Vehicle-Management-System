package com.zodiacgroup.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import com.zodiacgroup.dao.InventoryDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Inventory;
import com.zodiacgroup.util.HibernateUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InventoryController extends BaseController implements Initializable {
    @FXML private TableView<Inventory> inventoryTable;
    @FXML private TableColumn<Inventory, Integer> inventoryIdColumn;
    @FXML private TableColumn<Inventory, String> itemNameColumn;
    @FXML private TableColumn<Inventory, String> categoryColumn;
    @FXML private TableColumn<Inventory, Integer> stockColumn;
    @FXML private TableColumn<Inventory, Double> priceColumn;
    @FXML private TableColumn<Inventory, String> supplierColumn;
    
    @FXML private Button addButton, deleteButton, updateButton, viewButton, lowStockButton;
    @FXML private ImageView refreshImageView;
    @FXML private TextField searchField;
    @FXML private Rectangle searchBox;
    @FXML private ImageView searchImage;
    
    private InventoryDAO inventoryDAO = new InventoryDAO();
    private ObservableList<Inventory> inventoryList = FXCollections.observableArrayList();

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
        
        setupTableColumns(); 
        refreshInventoryTable(); 
        setupSearchField();
    }
    
    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
        
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
    }
    
    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    private void setupTableColumns() {
        inventoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
    }

    @FXML
    private void handleOpenAddInventory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddInventoryItems.fxml"));
            Parent root = loader.load();
            AddInventoryController controller = loader.getController();
            controller.setMainApp(mainApp);  
	        controller.setRole(currentRole);
            
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Inventory Item");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load add inventory form");
        }
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        Inventory selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select an item to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/EditInventoryItems.fxml"));
            Parent root = loader.load();

            EditInventoryController controller = loader.getController();
            controller.setInventoryData(selectedItem);
            controller.setMainApp(mainApp);  
	        controller.setRole(currentRole);

            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Inventory Item");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load edit form");
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Inventory selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select an item to delete");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/DeleteInventoryItems.fxml"));
            Parent root = loader.load();

            DeleteInventoryController controller = loader.getController();
            controller.setInventoryData(selectedItem);
            controller.setMainApp(mainApp);  
	        controller.setRole(currentRole);

            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Delete Inventory Item");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load delete form");
        }
    }

    @FXML
    private void handleViewButtonAction(ActionEvent event) {
        Inventory selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select an item to view");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ViewInventoryItems.fxml"));
            Parent root = loader.load();

            ViewInventoryController controller = loader.getController();
            controller.setInventoryData(selectedItem);
            controller.setMainApp(mainApp);  
	        controller.setRole(currentRole);

            Stage stage = (Stage) viewButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("View Inventory Item");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load view form");
        }
    }

    @FXML
    private void handleLowStockButtonAction(ActionEvent event) {
        List<Inventory> lowStockItems = inventoryDAO.searchLowStockItems();
        inventoryTable.getItems().setAll(lowStockItems);
    }

    private void setupSearchField() {
        searchBox.setOnMouseClicked(event -> searchField.requestFocus());
        searchImage.setOnMouseClicked(event -> {
            searchField.requestFocus();
            performSearch();
        });
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            refreshInventoryTable();
            return;
        }
        
        try {
            List<Inventory> items = new ArrayList<>();
            
            try {
                int numericValue = Integer.parseInt(searchText);
                
                Inventory exactMatch = inventoryDAO.getInventoryItemById(numericValue);
                if (exactMatch != null) {
                    items.add(exactMatch);
                }
                
                items.addAll(inventoryDAO.searchInventoryById(searchText));
                
                if (items.isEmpty()) {
                    String hql = "FROM Inventory WHERE stockQuantity = :quantity";
                    items = HibernateUtil.getSessionFactory().openSession()
                        .createQuery(hql, Inventory.class)
                        .setParameter("quantity", numericValue)
                        .list();
                }
            } catch (NumberFormatException e) {
            }
            
            if (items.isEmpty()) {
                items = inventoryDAO.searchInventoryByName(searchText);
                
                if (items.isEmpty()) {
                    items = inventoryDAO.searchInventoryByCategory(searchText);
                }
                
                if (items.isEmpty()) {
                    items = inventoryDAO.searchInventoryBySupplier(searchText);
                }
            }
            
            inventoryTable.getItems().setAll(items);
            
        } catch (Exception e) {
            showAlert("Search Error", "An error occurred during search");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefreshButtonAction(ActionEvent event) {
        refreshInventoryTable();
    }

    @FXML
    private void handleRefreshImageClick(MouseEvent event) {
        refreshInventoryTable();
    }

    public void refreshInventoryTable() {
        List<Inventory> items = inventoryDAO.getAllInventoryItems();
        inventoryTable.getItems().setAll(items);
    }
    
    private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}