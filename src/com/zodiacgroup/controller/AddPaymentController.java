package com.zodiacgroup.controller;

import com.zodiacgroup.dao.CustomerDAO;
import com.zodiacgroup.dao.PaymentDAO;
import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Payment;
import com.zodiacgroup.model.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddPaymentController extends BaseController implements Initializable {

    @FXML private TextField customerIdField;
    @FXML private TextField customerNameField;
    @FXML private ComboBox<Integer> serviceIdComboBox;
    @FXML private TextField amountField;
    @FXML private DatePicker paymentDatePicker;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button fetchCustomerButton;
    @FXML private Button fetchServiceButton;
    
    // Side menu labels
    @FXML private Label lblHome;
    @FXML private Label lblCustomer;
    @FXML private Label lblPayment;
    @FXML private Label lblInventory;
    @FXML private Label lblSalesRep;
    @FXML private Label lblManager;
    @FXML private Label lblViewReport;
    @FXML private Label lblLogout;

    private String currentRole;
    private MainApp mainApp;

    private PaymentDAO paymentDAO = new PaymentDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private ServiceDAO serviceDAO = new ServiceDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCurrentPage("payment");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
       if (lblPayment != null) {
    	   lblPayment.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        
        initializeComboBoxes();
        loadCustomerIds();
    }

    private void initializeComboBoxes() {
        paymentMethodComboBox.getItems().addAll("Credit Card", "Debit Card", "Cash", "Bank Transfer", "PayPal");
        statusComboBox.getItems().addAll("Pending", "Completed", "Failed", "Refunded");
        paymentDatePicker.setValue(LocalDate.now());
        statusComboBox.setValue("Pending");
    }

    public void setRole(String role) {
        this.currentRole = role;
        initializeNavigation();
    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        initializeNavigation();
    }
    
    public void initializeNavigation() {
        if (mainApp != null && currentRole != null) {
            configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                    lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
            setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                    lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
        }
    }
    
    private void loadCustomerIds() {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            ObservableList<Integer> customerIds = FXCollections.observableArrayList();
            
            for (Customer customer : customers) {
                customerIds.add(customer.getCustomerId());
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load customers: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFetchCustomerAction() {
        try {
            int customerId = Integer.parseInt(customerIdField.getText());
            Customer customer = customerDAO.getCustomerById(customerId);
            
            if (customer != null) {
                customerNameField.setText(customer.getFirstName() + " " + customer.getLastName());
                loadServicesForCustomer(customerId);
            } else {
                showAlert("Error", "Customer not found with ID: " + customerId, Alert.AlertType.ERROR);
                clearCustomerFields();
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid numeric Customer ID", Alert.AlertType.ERROR);
            clearCustomerFields();
        } catch (Exception e) {
            showAlert("Database Error", "Failed to fetch customer: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
            clearCustomerFields();
        }
    }

    private void loadServicesForCustomer(int customerId) {
        try {
            List<Service> services = serviceDAO.getServicesByCustomerId(customerId);
            ObservableList<Integer> serviceIds = FXCollections.observableArrayList();
            
            for (Service service : services) {
                serviceIds.add(service.getId());
            }
            
            serviceIdComboBox.setItems(serviceIds);
            
            if (serviceIds.isEmpty()) {
                showAlert("Information", "No services found for this customer", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load services: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFetchServiceAction() {
        try {
            Integer serviceId = serviceIdComboBox.getValue();
            if (serviceId != null) {
                Service service = serviceDAO.getServiceById(serviceId);
                if (service != null) {
                    amountField.setText(String.format("%.2f", service.getCost()));
                } else {
                    showAlert("Error", "Service not found with ID: " + serviceId, Alert.AlertType.ERROR);
                    clearServiceFields();
                }
            } else {
                showAlert("Error", "Please select a service", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to fetch service details: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
            clearServiceFields();
        }
    }

    @FXML
    private void handleSaveButtonAction() {
        try {

            if (customerIdField.getText().isEmpty() || customerNameField.getText().isEmpty() || 
                serviceIdComboBox.getValue() == null || amountField.getText().isEmpty() ||
                paymentMethodComboBox.getValue() == null || statusComboBox.getValue() == null) {
                showAlert("Validation Error", "Please fill in all required fields", Alert.AlertType.ERROR);
                return;
            }


            Payment payment = new Payment();
            payment.setCustomerId(Integer.parseInt(customerIdField.getText()));
            payment.setCustomerName(customerNameField.getText());
            payment.setServiceId(serviceIdComboBox.getValue());
            payment.setAmount(Double.parseDouble(amountField.getText()));
            payment.setPaymentDate(java.sql.Date.valueOf(paymentDatePicker.getValue()));
            payment.setPaymentMethod(paymentMethodComboBox.getValue());
            payment.setStatus(statusComboBox.getValue());


            boolean saved = paymentDAO.saveOrUpdatePayment(payment);
            
            if (saved) {
                showAlert("Success", "Payment saved successfully", Alert.AlertType.INFORMATION);
                navigateToPaymentList();
            } else {
                showAlert("Error", "Failed to save payment", Alert.AlertType.ERROR);
            }
            
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for Customer ID and Amount", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Database Error", "Failed to save payment: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToPaymentList();
    }
    
    private void navigateToPaymentList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Payment.fxml"));
            Parent root = loader.load();
            
            PaymentController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setRole(currentRole);
            controller.refreshPaymentTable();
            
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Payment Management");
        } catch (IOException e) {
            showAlert("Error", "Could not load payment list view", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void clearCustomerFields() {
        customerNameField.clear();
        serviceIdComboBox.getItems().clear();
        clearServiceFields();
    }

    private void clearServiceFields() {
        amountField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}