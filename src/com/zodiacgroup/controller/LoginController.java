package com.zodiacgroup.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.zodiacgroup.dao.LoginDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Login;

public class LoginController extends BaseController {
    @FXML private RadioButton adminRadio;
    @FXML private RadioButton salesRepRadio;
    @FXML private TextField loginnameField;
    @FXML private PasswordField passwordField;
    
    private ToggleGroup roleToggleGroup;
    private LoginDAO loginDAO = new LoginDAO();
    private MainApp mainApp;

    @FXML
    public void initialize() {
        roleToggleGroup = new ToggleGroup();
        adminRadio.setToggleGroup(roleToggleGroup);
        salesRepRadio.setToggleGroup(roleToggleGroup);
        adminRadio.setSelected(true);
        adminRadio.setUserData("ADMIN");
        salesRepRadio.setUserData("SALES_REP");
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() {
        String username = loginnameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty");
            return;
        }

        try {
            Login login = loginDAO.getLoginByUsername(username);
            
            if (login == null && !username.equals(username.toLowerCase())) {
                login = loginDAO.getLoginByUsername(username.toLowerCase());
            }
            
            if (login == null) {
                showAlert("Error", "User not found");
                return;
            }
            
            if (!login.getPassword().equals(password)) {
                showAlert("Error", "Invalid password");
                return;
            }
            
            String selectedRole = (String) roleToggleGroup.getSelectedToggle().getUserData();
            
            if (!login.getRole().equalsIgnoreCase(selectedRole)) {
                showAlert("Error", "You don't have permissions for this role");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/dashboard.fxml"));
            Parent root = loader.load();
            
            DashboardController dashboardController = loader.getController();
            dashboardController.setRole(login.getRole().toUpperCase());
            dashboardController.setUsername(login.getUsername());
            dashboardController.setMainApp(mainApp); 
            
            Stage stage = (Stage) loginnameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setWidth(1200);
            stage.setHeight(800);
            stage.centerOnScreen();
            
        } catch (Exception e) {
            showAlert("Database Error", "Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void navigateToDashboard(String role, String username) {
        try {
            mainApp.showDashboard(role, username);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}