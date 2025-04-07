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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.MechanicDAO;
import com.zodiacgroup.model.Mechanic;

public class AddMechanicController extends BaseController implements Initializable {

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
    private Button saveButton, cancelButton;


    private MechanicDAO mechanicDAO = new MechanicDAO();

    @FXML
	private Label lblHome, lblCustomer, lblSalesRep, lblManager, lblLogout, lblInventory, lblPayment,
			lblViewReport;
    private String currentRole;

 @Override
 public void initialize(URL location, ResourceBundle resources) {
	 
	 setCurrentPage("mechanic");
     initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        


     if (lblManager != null) {
     	lblManager.setStyle("-fx-font-weight: bold; -fx-underline: true;");
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
    private void handleSaveButtonAction(ActionEvent event) {
        try {
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() 
                || phoneNumberField.getText().isEmpty() || specializationField.getText().isEmpty()
                || experienceYearsField.getText().isEmpty()) {
                showAlert("Validation Error", "First Name, Last Name, Phone Number, Specialization and Experience Years are required fields");
                return;
            }

            if (!emailField.getText().isEmpty() && !emailField.getText().matches(".+@.+\\..+")) {
                showAlert("Validation Error", "Please enter a valid email address");
                return;
            }

            int experienceYears;
            try {
                experienceYears = Integer.parseInt(experienceYearsField.getText());
            } catch (NumberFormatException e) {
                showAlert("Validation Error", "Experience Years must be a number");
                return;
            }

            Mechanic mechanic = new Mechanic();
            mechanic.setFirstName(firstNameField.getText().trim());
            mechanic.setLastName(lastNameField.getText().trim());
            mechanic.setPhoneNumber(phoneNumberField.getText().trim());
            mechanic.setEmail(emailField.getText().trim().toLowerCase());
            mechanic.setSpecialization(specializationField.getText().trim());
            mechanic.setExperienceYears(experienceYears);

            mechanicDAO.addMechanic(mechanic);

            showAlert("Success", "Mechanic saved successfully");
            clearFields();
            handleBackButtonAction(event);
        } catch (RuntimeException e) {
            showAlert("Error", "Failed to save mechanic: " + e.getCause().getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Error", "Failed to save mechanic: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToMechanicList();
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        emailField.clear();
        specializationField.clear();
        experienceYearsField.clear();
    }

    private void navigateToMechanicList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Mechanic.fxml"));
            Parent root = loader.load();

            MechanicController controller = loader.getController();
            controller.refreshMechanicList();
            controller.setRole(currentRole); 
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