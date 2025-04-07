package com.zodiacgroup.controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import com.zodiacgroup.controller.UpdateMechanicController;
import com.zodiacgroup.dao.MechanicDAO;
import com.zodiacgroup.model.Mechanic;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class MechanicController extends BaseController implements Initializable {
    @FXML
    private TableColumn<Mechanic, Integer> mechanicIdColumn;
    @FXML
    private TableColumn<Mechanic, String> mechanicNameColumn;
    @FXML
    private TableColumn<Mechanic, String> phoneColumn;
    @FXML
    private TableColumn<Mechanic, String> skillsColumn;
    @FXML
    private TableView<Mechanic> mechanicTable;
    @FXML
    private MechanicDAO mechanicDAO;
    @FXML
    private TextField searchField;
    @FXML
    private Button addButton, deleteButton, updateButton, viewButton, refreshButton;
   
    private ObservableList<Mechanic> mechanicList = FXCollections.observableArrayList();

    @FXML
    private Label lblHome, lblCustomer, lblSalesRep, lblManager, lblLogout, lblInventory, lblPayment, lblViewReport;
    private String currentRole;

    public MechanicController() {
        mechanicDAO = new MechanicDAO();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	setCurrentPage("mechanic");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        


        if (lblManager != null) {
        	lblManager.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        // Initialize table columns
        mechanicIdColumn.setCellValueFactory(new PropertyValueFactory<>("mechanicId"));
        mechanicNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        skillsColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        // Load initial data
        refreshMechanicList();
        
     // Set up search functionality
        setupSearch();
    }

    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
        
    }

    @FXML
    private void handleOpenAddMechanic(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/AddMechanic.fxml"));
            Parent root = loader.load();
            
            AddMechanicController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            
            Stage currentStage = (Stage) addButton.getScene().getWindow();
            currentStage.setScene(new Scene(root, 1200, 800));
            currentStage.setTitle("Add Mechanic");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load add mechanic form: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        Mechanic selectedMechanic = mechanicTable.getSelectionModel().getSelectedItem();
        if (selectedMechanic == null) {
            showAlert("No Selection", "Please select a mechanic to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/UpdateMechanic.fxml"));
            Parent root = loader.load();

            UpdateMechanicController controller = loader.getController();
            controller.setMechanicData(selectedMechanic);
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);

            Stage currentStage = (Stage) updateButton.getScene().getWindow();
            currentStage.setScene(new Scene(root, 1200, 800));
            currentStage.setTitle("Edit Mechanic");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load edit form: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Mechanic selectedMechanic = mechanicTable.getSelectionModel().getSelectedItem();
        if (selectedMechanic == null) {
            showAlert("No Selection", "Please select a mechanic to delete");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/DeleteMechanic.fxml"));
            Parent root = loader.load();

            DeleteMechanicController controller = loader.getController();
            controller.setMechanicData(selectedMechanic);
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);

            Stage currentStage = (Stage) deleteButton.getScene().getWindow();
            currentStage.setScene(new Scene(root, 1200, 800));
            currentStage.setTitle("Delete Mechanic");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load delete form: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewButtonAction(ActionEvent event) {
        Mechanic selectedMechanic = mechanicTable.getSelectionModel().getSelectedItem();
        if (selectedMechanic == null) {
            showAlert("No Selection", "Please select a mechanic to view");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/ViewMechanic.fxml"));
            Parent root = loader.load();

            ViewMechanicController controller = loader.getController();
            controller.setMechanicData(selectedMechanic);
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);

            Stage currentStage = (Stage) viewButton.getScene().getWindow();
            currentStage.setScene(new Scene(root, 1200, 800));
            currentStage.setTitle("View Mechanic Details");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load view form: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefreshButtonAction(ActionEvent event) {
        refreshMechanicList();
    }

    public void refreshMechanicList() {
        List<Mechanic> mechanics = mechanicDAO.getAllMechanics();
        mechanicTable.getItems().setAll(mechanics);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                refreshMechanicList();
            } else {
                searchMechanics(newValue.trim());
            }
        });
    }

    private void searchMechanics(String searchTerm) {
        List<Mechanic> mechanics = mechanicDAO.searchMechanics(searchTerm);
        mechanicTable.getItems().setAll(mechanics);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}