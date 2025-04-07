package com.zodiacgroup.controller;

import com.zodiacgroup.dao.DashboardDAO;
import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.model.Inventory;
import com.zodiacgroup.model.Notification;
import com.zodiacgroup.main.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController extends BaseController implements Initializable {
    @FXML private Label lblHome;
    @FXML private Label lblCustomer;
    @FXML private Label lblPayment;
    @FXML private Label lblInventory;
    @FXML private Label lblSalesRep;
    @FXML private Label lblManager;
    @FXML private Label lblViewReport;
    @FXML private Label lblLogout;
    @FXML private TabPane tabPane;
    @FXML private VBox appointmentsContainer;
    @FXML private VBox notificationsContainer;
    @FXML private Label lblPendingAppointments;
    @FXML private Label lblLowStockItems;
    @FXML private Tab quickActionsTab;
    @FXML private Tab notificationsTab;
    
    private String currentRole;
    private String currentUsername;
    @FXML private Label lblUsername;
    
    private DashboardDAO dashboardDAO = new DashboardDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	setCurrentPage("home");
    	
    	if (tabPane == null) {
            System.err.println("TabPane is not properly injected from FXML");
        }
    	
    	if (lblHome != null) {
    		lblHome.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
    }

    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole();
        loadDashboardContent();
    }
    
    public void setUsername(String username) {
        if (lblUsername != null) {
            lblUsername.setText(username.toUpperCase());
        }
    }
    
    public String getUsername() {
        return this.lblUsername != null ? this.lblUsername.getText() : "";
    }

    private void configureMenuBasedOnRole() {
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                               lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
        
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                      lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
        
       
        
        if (tabPane != null) {
            if (!"ADMIN".equals(currentRole)) {
                tabPane.getTabs().removeAll(quickActionsTab, notificationsTab);
            }
        } else {
            System.err.println("Error: tabPane is null - check FXML injection");
        }
    }

    private void loadDashboardContent() {
        try {
            loadUpcomingAppointments();
            if ("ADMIN".equals(currentRole)) {
            	loadNotifications();
            }
        } catch (Exception e) {
            System.err.println("Error loading dashboard content: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadUpcomingAppointments() {
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date endDate = calendar.getTime();
        
        try {
            List<Appointment> appointments = dashboardDAO.getUpcomingAppointments(startDate, endDate);
            appointmentsContainer.getChildren().clear();
            
            int count = Math.min(appointments.size(), 5);
            for (int i = 0; i < count; i++) {
                Appointment appointment = appointments.get(i);
                AnchorPane appointmentCard = createAppointmentCard(appointment);
                appointmentsContainer.getChildren().add(appointmentCard);
            }
        } catch (Exception e) {
            System.err.println("Error loading appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private AnchorPane createAppointmentCard(Appointment appointment) {
        AnchorPane card = new AnchorPane();
        card.setPrefHeight(100.0);
        card.setPrefWidth(380.0);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #4D8BA0;");
        
        Label nameLabel = new Label("Name: " + appointment.getCustomerName());
        nameLabel.setLayoutX(26.0);
        nameLabel.setLayoutY(5.0);
        
        Label serviceLabel = new Label("Service: " + appointment.getServiceType());
        serviceLabel.setLayoutX(26.0);
        serviceLabel.setLayoutY(23.0);
        
        Label dateLabel = new Label("Date: " + appointment.getAppointmentDate());
        dateLabel.setLayoutX(26.0);
        dateLabel.setLayoutY(41.0);
        
        Label statusLabel = new Label("Status: " + appointment.getStatus());
        statusLabel.setLayoutX(26.0);
        statusLabel.setLayoutY(59.0);
        
        card.getChildren().addAll(nameLabel, serviceLabel, dateLabel, statusLabel);
        return card;
    }
        
        private void loadSalesRepAppointments() {
            Calendar calendar = Calendar.getInstance();
            Date startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            Date endDate = calendar.getTime();
            
            List<Appointment> appointments = dashboardDAO.getUpcomingAppointments(startDate, endDate);
            appointmentsContainer.getChildren().clear();
            
            for (Appointment appointment : appointments) {
                AnchorPane appointmentCard = createSimpleAppointmentCard(appointment);
                appointmentsContainer.getChildren().add(appointmentCard);
            }
        }
        
        private AnchorPane createSimpleAppointmentCard(Appointment appointment) {
            AnchorPane card = new AnchorPane();
            card.setPrefHeight(60.0);
            card.setPrefWidth(380.0);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #4D8BA0;");
            
            Label nameLabel = new Label("Customer: " + appointment.getCustomerName());
            nameLabel.setLayoutX(10.0);
            nameLabel.setLayoutY(5.0);
            
            Label dateLabel = new Label("Date: " + appointment.getAppointmentDate());
            dateLabel.setLayoutX(10.0);
            dateLabel.setLayoutY(25.0);
            
            card.getChildren().addAll(nameLabel, dateLabel);
            return card;
        }

        private void loadNotifications() {
            try {
                System.out.println("Loading low stock items...");
                List<Inventory> lowStockItems = dashboardDAO.getLowStockItems(5);
                System.out.println("Found " + lowStockItems.size() + " low stock items");
                
                notificationsContainer.getChildren().clear();
                
                if (lowStockItems.isEmpty()) {
                    Label noItemsLabel = new Label("No low stock items found");
                    notificationsContainer.getChildren().add(noItemsLabel);
                    return;
                }
                
                for (Inventory item : lowStockItems) {
                    AnchorPane itemCard = createLowStockItemCard(item);
                    notificationsContainer.getChildren().add(itemCard);
                }
            } catch (Exception e) {
                System.err.println("Error loading low stock items: " + e.getMessage());
                e.printStackTrace();
                Label errorLabel = new Label("Could not load low stock items");
                notificationsContainer.getChildren().add(errorLabel);
            }
        }

        private AnchorPane createLowStockItemCard(Inventory item) {
            AnchorPane card = new AnchorPane();
            card.setPrefHeight(80.0);
            card.setPrefWidth(350.0);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #FFA000;");
            
            Label nameLabel = new Label(item.getItemName());
            nameLabel.setLayoutX(10.0);
            nameLabel.setLayoutY(10.0);
            nameLabel.setStyle("-fx-font-weight: bold;");
            
            Label stockLabel = new Label(
                "Stock: " + item.getStockQuantity() + 
                " (Min: " + item.getMinimumStockThreshold() + ")"
            );
            stockLabel.setLayoutX(10.0);
            stockLabel.setLayoutY(30.0);
            
            Label categoryLabel = new Label("Category: " + item.getCategory());
            categoryLabel.setLayoutX(10.0);
            categoryLabel.setLayoutY(50.0);
            categoryLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");
            
            card.getChildren().addAll(nameLabel, stockLabel, categoryLabel);
            return card;
        }

        private AnchorPane createNotificationCard(Notification notification) {
            AnchorPane card = new AnchorPane();
            card.setPrefHeight(60.0);
            card.setPrefWidth(350.0);
            
            if ("stock".equals(notification.getType())) {
                card.setStyle("-fx-background-color: #FFF3E0; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #FFA000;");
            } else {
                card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #4D8BA0;");
            }
            
            Label messageLabel = new Label(notification.getMessage());
            messageLabel.setLayoutX(10.0);
            messageLabel.setLayoutY(10.0);
            
            Label timeLabel = new Label(notification.getTimestamp().toString());
            timeLabel.setLayoutX(10.0);
            timeLabel.setLayoutY(30.0);
            timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
            
            card.getChildren().addAll(messageLabel, timeLabel);
            return card;
        }

        private void loadQuickActionStats() {
            long pendingAppointments = dashboardDAO.getPendingAppointmentsCount();
            long lowStockItems = dashboardDAO.getLowStockItemsCount();
            
            lblPendingAppointments.setText("Pending Appointments: " + pendingAppointments);
            lblLowStockItems.setText("Low Stock Items: " + lowStockItems);
        }


        @FXML
        private void handleAddCustomer() {
            try {
            	mainApp.showCustomerView(currentRole, currentUsername);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Navigation Error", "You don't have permission for this action");
            }
        }

        @FXML
        private void handleScheduleService() {
            if (currentRole.equals("ADMIN") || currentRole.equals("SALES_REP")) {
                try {
                    mainApp.showAppointmentView(currentRole);
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Navigation Error", "Failed to open Appointment view");
                }
            } else {
                showAlert("Permission Denied", "You don't have permission to schedule services");
            }
        }


        @FXML
        private void handleGenerateInvoice() {
            try {
                mainApp.showPaymentView(currentRole);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Navigation Error", "Failed to open Payment/Invoice view");
            }
        }

        @FXML
        private void handleAssignMechanic() {
            try {
                mainApp.showMechanicView(currentRole);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Navigation Error", "Failed to open Mechanic view");
            }
        }

        @FXML
        private void handleViewReports() {
            try {
                mainApp.showReportView(currentRole);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Navigation Error", "Failed to open Reports view");
            }
        }
    
    
    public void initData(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    private void setupQuickActionHandlers() {
        
    }
    
    
    
    
    @FXML
    private void handleLogout() {
        try {
            Stage currentStage = (Stage) lblLogout.getScene().getWindow();
            
            currentStage.setWidth(637.5);
            currentStage.setHeight(450);
            
            mainApp.showLoginView();
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
            showAlert("Logout Error", "Failed to return to login screen");
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
