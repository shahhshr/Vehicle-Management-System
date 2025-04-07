package com.zodiacgroup.controller;

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

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.MechanicDAO;
import com.zodiacgroup.model.Mechanic;

public class UpdateMechanicController extends BaseController implements Initializable {
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
    private Button saveButton, cancelButton, backButton;
    
   

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
    private void handleSave(ActionEvent event) {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || 
            phoneNumberField.getText().isEmpty() || specializationField.getText().isEmpty()) {
            showAlert("Validation Error", "First Name, Last Name, Phone Number and Specialization are required");
            return;
        }

        try {
            int mechanicId = Integer.parseInt(mechanicIdField.getText());
            int experienceYears = Integer.parseInt(experienceYearsField.getText());

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Are you sure you want to update this mechanic?");
            confirmAlert.setContentText("This will modify the mechanic details in the database.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Changed from static call to instance call
                Mechanic mechanic = mechanicDAO.getMechanicById(mechanicId);

                if (mechanic != null) {
                    mechanic.setFirstName(firstNameField.getText());
                    mechanic.setLastName(lastNameField.getText());
                    mechanic.setPhoneNumber(phoneNumberField.getText());
                    mechanic.setEmail(emailField.getText());
                    mechanic.setSpecialization(specializationField.getText());
                    mechanic.setExperienceYears(experienceYears);

                    mechanicDAO.updateMechanic(mechanic);

                    showAlert("Success", "Mechanic updated successfully!");
                    navigateToMechanicList();
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Mechanic ID or Experience Years format");
        } catch (Exception e) {
            showAlert("Error", "Failed to update mechanic: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        mechanicIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        emailField.clear();
        specializationField.clear();
        experienceYearsField.clear();
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToMechanicList();
    }

    
    private void navigateToMechanicList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Mechanic.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) saveButton.getScene().getWindow();

            stage.setScene(new Scene(root, 1200, 800)); // Added consistent window size
            stage.setTitle("Mechanic Management");

            MechanicController controller = loader.getController();
            controller.setRole(currentRole); // Ensure role is maintained
            controller.setMainApp(mainApp);
            controller.refreshMechanicList();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load mechanic list view");
        }
    }

    public void setMechanicData(Mechanic mechanic) {
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}