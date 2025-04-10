package com.zodiacgroup.controller;

import com.zodiacgroup.dao.SaleDAO;
import com.zodiacgroup.model.SaleRepresentative;

import javafx.application.Platform;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SaleController extends BaseController implements Initializable {
    @FXML private TableView<SaleRepresentative> saleTable;
    @FXML private TableColumn<SaleRepresentative, Integer> employeeIdColumn;
    @FXML private TableColumn<SaleRepresentative, String> employeeNameColumn;
    @FXML private TableColumn<SaleRepresentative, String> positionColumn;
    @FXML private TableColumn<SaleRepresentative, String> emailColumn;
    @FXML private TableColumn<SaleRepresentative, String> phoneColumn;
    @FXML private TableColumn<SaleRepresentative, String> usernameColumn;
    @FXML private TableColumn<SaleRepresentative, String> passwordColumn;
    @FXML private TextField searchField;
    @FXML private Button refreshButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button addButton;
    @FXML private Button viewButton;
    @FXML private ImageView refreshImageView;
    @FXML private Rectangle searchBox;
    @FXML private Label searchLabel;
    @FXML private ImageView searchImage;
    
  
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
    
    private SaleDAO saleDAO = new SaleDAO();
    private ObservableList<SaleRepresentative> saleRepList = FXCollections.observableArrayList();
    private String currentRole;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
		setCurrentPage("salesRep");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        

		if (lblSalesRep != null) {
			lblSalesRep.setStyle("-fx-font-weight: bold; -fx-underline: true;");
		}
		setupTableColumns();
        loadSaleRepresentatives();
        setupSearchFunctionality();
		}
		
    
			public void setRole(String role) {
		        this.currentRole = role;
		        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
		                lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);

		        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
		                lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
		    }
    
    private void setupTableColumns() {
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
    }
    
    public void loadSaleRepresentatives() {
        new Thread(() -> {
            List<SaleRepresentative> representatives = saleDAO.getAllSaleRepresentatives();
            Platform.runLater(() -> {
                saleRepList.clear();
                saleRepList.addAll(representatives);
                saleTable.setItems(saleRepList);
            });
        }).start();
    }
    
    private void setupSearchFunctionality() {
        searchBox.setOnMouseClicked(event -> {
            searchField.requestFocus();
        });

        searchImage.setOnMouseClicked(event -> {
            searchField.requestFocus();
            performSearch();
        });

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch();
            }
        });
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                saleTable.setItems(saleRepList);
            } else {
                performSearch();
            }
        });
    }
    
    private void performSearch() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadSaleRepresentatives();
            return;
        }

        ObservableList<SaleRepresentative> filteredList = FXCollections.observableArrayList();
        for (SaleRepresentative saleRep : saleRepList) {
            if (saleRep.matchesSearchTerm(searchText)) {
                filteredList.add(saleRep);
            }
        }
        saleTable.setItems(filteredList);
    }
    
    @FXML
    private void handleRefreshButtonAction() {
        loadSaleRepresentatives();
    }
    
    @FXML
    private void handleRefreshImageClick(MouseEvent event) {
        loadSaleRepresentatives();
    }
    
    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        SaleRepresentative selected = saleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/UpdateSale.fxml"));
                Parent root = loader.load();
                
                UpdateSaleController controller = loader.getController();
                controller.setSaleRepresentativeToEdit(selected);
                controller.setMainApp(mainApp);
                controller.setRole(currentRole);
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Edit Sales Representative");
            } catch (IOException e) {
                showAlert("Error", "Failed to open update view: " + e.getMessage());
            }
        } else {
            showAlert("Warning", "Please select a sales representative to update");
        }
    }
    
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        SaleRepresentative selected = saleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/DeleteSale.fxml"));
                Parent root = loader.load();

                DeleteSaleController controller = loader.getController();
                controller.setSaleRepresentativeData(selected);
                controller.setMainApp(mainApp);
                controller.setRole(currentRole);

                Stage currentStage = (Stage) deleteButton.getScene().getWindow();
                currentStage.setScene(new Scene(root));
                currentStage.setTitle("Delete Sales Representative");
            } catch (IOException e) {
                showAlert("Error", "Could not load delete form: " + e.getMessage());
            }
        } else {
            showAlert("Warning", "Please select a sales representative to delete");
        }
    }
    
    @FXML
    private void handleOpenAddSale(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddSale.fxml"));
            Parent root = loader.load();
            
            AddSaleController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Sales Representative");
        } catch (IOException e) {
            showAlert("Error", "Failed to open add sales representative view: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleViewButtonAction(ActionEvent event) {
        SaleRepresentative selected = saleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ViewSale.fxml"));
                Parent root = loader.load();
                
                ViewSaleController controller = loader.getController();
                controller.setSaleRepresentativeData(selected);
                controller.setMainApp(mainApp);
                controller.setRole(currentRole);
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Sales Representative Details");
            } catch (IOException e) {
                showAlert("Error", "Failed to open sales representative details view: " + e.getMessage());
            }
        } else {
            showAlert("Warning", "Please select a sales representative to view");
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }
    
    public void refreshTable() {
        loadSaleRepresentatives();
    }
}