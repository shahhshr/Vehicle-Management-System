package com.zodiacgroup.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.MechanicDAO;
import com.zodiacgroup.model.Mechanic;

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

public class DeleteMechanicController extends BaseController implements Initializable {
    @FXML
    private TextField mechanicIdField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField specializationField;
    @FXML
    private TextField experienceYearsField;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;

    private Mechanic currentMechanic;
    private MechanicDAO mechanicDAO = new MechanicDAO();

    @FXML
	private Label lblHome, lblCustomer, lblSalesRep, lblManager, lblLogout, lblInventory, lblPayment,
			lblViewReport;
    
    
    private String currentRole;

 // Update the initialize method
 @Override
 public void initialize(URL location, ResourceBundle resources) {
	 setCurrentPage("mechanic");
     initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        


     if (lblManager != null) {
     	lblManager.setStyle("-fx-font-weight: bold; -fx-underline: true;");
     }
     
 }

 // Add setRole method
 public void setRole(String role) {
     this.currentRole = role;
     configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
             lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
     setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
             lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
 }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToMechanicList();
    }

    public void setMechanicData(Mechanic mechanic) {
        this.currentMechanic = mechanic;
        if (mechanic != null) {
            mechanicIdField.setText(String.valueOf(mechanic.getMechanicId()));
            firstNameField.setText(mechanic.getFirstName());
            lastNameField.setText(mechanic.getLastName());
            phoneNumberField.setText(mechanic.getPhoneNumber());
            emailField.setText(mechanic.getEmail());
            specializationField.setText(mechanic.getSpecialization());
            experienceYearsField.setText(String.valueOf(mechanic.getExperienceYears()));
        }
    }

    @FXML
    private void handleDeleteMechanic(ActionEvent event) {
        if (currentMechanic == null) {
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this mechanic?");
        confirmAlert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                mechanicDAO.deleteMechanic(currentMechanic.getMechanicId());
                showAlert("Success", "Mechanic deleted successfully!");
                navigateToMechanicList();
            } catch (Exception e) {
                showAlert("Error", "Failed to delete mechanic: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        navigateToMechanicList();
    }

    private void navigateToMechanicList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Mechanic.fxml"));
            Parent root = loader.load();

            // Refresh the mechanic list
            MechanicController controller = loader.getController();
            controller.refreshMechanicList();
            controller.setRole(currentRole); // Ensure role is maintained
            controller.setMainApp(mainApp);

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Mechanic Management");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load mechanic list view");
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