package com.zodiacgroup.main;

import java.io.IOException;

import com.zodiacgroup.controller.*;
import com.zodiacgroup.model.Customer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;
    private static MainApp instance;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        instance = this;
        showLoginView();
    }

    public static MainApp getInstance() {
        return instance;
    }
    
    public void showLoginView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/login.fxml"));
        Parent root = loader.load();
        
        LoginController controller = loader.getController();
        controller.setMainApp(this);
        
        Scene scene = new Scene(root, 637.5, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Zodiac Group - Login");
        primaryStage.setWidth(637.5);
        primaryStage.setHeight(450);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
 // In MainApp.java
 // In MainApp.java
    public void showDashboard(String role) {
        showDashboard(role, "");
    }

    public void showDashboard(String role, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/dashboard.fxml"));
            Parent root = loader.load();
            
            DashboardController controller = loader.getController();
            controller.setRole(role);
            if (username != null && !username.isEmpty()) {
                controller.setUsername(username);
            }
            controller.setMainApp(this); // Set the mainApp reference
            
            primaryStage.setScene(new Scene(root));
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Zodiac Group - Dashboard");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

 // In MainApp.java
    public void showCustomerView(String role) {
        showCustomerView(role, ""); // Call the version with empty username
    }

    public void showCustomerView(String role, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/customer.fxml"));
            Parent root = loader.load();
            
            CustomerController controller = loader.getController();
            controller.setRole(role);
            
            controller.setMainApp(this);
            
            primaryStage.setScene(new Scene(root));
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        primaryStage.setTitle("Zodiac Group - Customers");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    
    public void showPaymentView(String role) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Payment.fxml"));
        Parent root = loader.load();
        
        PaymentController controller = loader.getController();
        controller.setRole(role);
        controller.setMainApp(this);
        
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(root);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
        }
        
        primaryStage.setTitle("Zodiac Group - Payment & Invoice");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public void showInventoryView(String role) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/InventoryItems.fxml"));
        Parent root = loader.load();
        
        InventoryController controller = loader.getController();
        controller.setRole(role);
        controller.setMainApp(this);
        
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(root);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
        }
        
        primaryStage.setTitle("Zodiac Group - Inventory");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public void showSalesRepView(String role) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Sales.fxml"));
        Parent root = loader.load();
        
        SaleController controller = loader.getController();
        controller.setRole(role);
        controller.setMainApp(this);
        
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(root);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
        }
        
        primaryStage.setTitle("Zodiac Group - Sales Representative");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public void showMechanicView(String role) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Mechanic.fxml"));
        Parent root = loader.load();
        
        MechanicController controller = loader.getController();
        controller.setRole(role);
        controller.setMainApp(this);
        
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(root);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
        }
        
        primaryStage.setTitle("Zodiac Group - Mechanic");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public void showReportView(String role) throws Exception {
        System.out.println("Entering showReportView with role: " + role); // Debug
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Report.fxml"));
        Parent root = loader.load();
        
        ReportController controller = loader.getController();
        controller.setRole(role);
        controller.setMainApp(this);
        
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(root);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
        }
        
        primaryStage.setTitle("Zodiac Group - Report");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public void showAppointmentView(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Appointment.fxml"));
            Parent root = loader.load();
            
            AppointmentController controller = loader.getController();
            controller.setMainApp(this);  // This is critical
            controller.setRole(role);     // Set the current role
            
            Scene scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(root, 1200, 800);
                primaryStage.setScene(scene);
            } else {
                scene.setRoot(root);
            }
            
            primaryStage.setTitle("Zodiac Group - Appointments");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void showVehiclesView(String role, Customer customer) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/vehicles.fxml"));
        Parent root = loader.load();
        
        VehiclesController controller = loader.getController();
        controller.setMainApp(this);  // This is critical
        controller.setRole(role);     // Set the role
        if (customer != null) {
            controller.setCustomer(customer);
        }
        
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(root);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
        }
        
        primaryStage.setTitle("Zodiac Group - Vehicles");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    
    
    public static void main(String[] args) {
        launch(args);
    }
}