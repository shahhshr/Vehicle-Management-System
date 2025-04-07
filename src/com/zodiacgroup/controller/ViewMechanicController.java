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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.zodiacgroup.dao.MechanicDAO;
import com.zodiacgroup.model.Mechanic;

public class ViewMechanicController extends BaseController implements Initializable {
    @FXML
    private Text mechanicIdText;
    @FXML
    private Text firstNameText;
    @FXML
    private Text lastNameText;
    @FXML
    private Text phoneText;
    @FXML
    private Text emailText;
    @FXML
    private Text specializationText;
    @FXML
    private Text experienceText;
    @FXML
    private Button cancelButton;
  
    @FXML
    private Label mechanicNameLabel;

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

    public void setMechanicData(Mechanic mechanic) {
        if (mechanic != null) {
            mechanicIdText.setText(String.valueOf(mechanic.getMechanicId()));
            firstNameText.setText(mechanic.getFirstName());
            lastNameText.setText(mechanic.getLastName());
            phoneText.setText(mechanic.getPhoneNumber());
            emailText.setText(mechanic.getEmail());
            specializationText.setText(mechanic.getSpecialization());
            experienceText.setText(String.valueOf(mechanic.getExperienceYears()));
            
            mechanicNameLabel.setText(mechanic.getFirstName() + " " + mechanic.getLastName());
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToMechanicList();
    }

    private void navigateToMechanicList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Mechanic.fxml"));
            Parent root = loader.load();

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