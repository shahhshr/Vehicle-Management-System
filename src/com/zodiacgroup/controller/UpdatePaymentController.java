package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.zodiacgroup.dao.PaymentDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Payment;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdatePaymentController extends BaseController implements Initializable {

    @FXML private TextField invoiceIdField;
    @FXML private TextField customerIdField;
    @FXML private TextField customerNameField;
    @FXML private TextField serviceIdField;
    @FXML private TextField amountField;
    @FXML private DatePicker paymentDatePicker;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button cancelButton;
    
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
    private Payment payment;
    private final PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	setCurrentPage("payment");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
        if (lblPayment != null) {
        	lblPayment.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        initializeComboBoxes();
    }

    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, 
                               lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, 
                      lblSalesRep, lblManager, lblViewReport, lblLogout, currentRole);
    }
    
    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void initializeComboBoxes() {
        paymentMethodComboBox.getItems().addAll(
            "Cash", "Credit Card", "Debit Card", "Bank Transfer", "Check"
        );
        
        statusComboBox.getItems().addAll(
            "Pending", "Completed", "Failed", "Refunded"
        );
    }

    public void setPaymentData(Payment payment) {
        this.payment = payment;
        
        invoiceIdField.setText(String.valueOf(payment.getInvoiceId()));
        customerIdField.setText(String.valueOf(payment.getCustomerId()));
        customerNameField.setText(payment.getCustomerName());
        serviceIdField.setText(String.valueOf(payment.getServiceId()));
        amountField.setText(String.valueOf(payment.getAmount()));
        
        if (payment.getPaymentDate() != null) {
            try {
            	LocalDate localDate = payment.getPaymentDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
paymentDatePicker.setValue(localDate);
            } catch (Exception e) {
                paymentDatePicker.setValue(LocalDate.now());
                System.err.println("Error converting payment date: " + e.getMessage());
            }
        } else {
            paymentDatePicker.setValue(LocalDate.now());
        }
        
        paymentMethodComboBox.setValue(payment.getPaymentMethod());
        statusComboBox.setValue(payment.getStatus());
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        if (confirmUpdate()) {
            updatePaymentDetails();
            boolean success = paymentDAO.saveOrUpdatePayment(payment);
            if (success) {
                showAlert("Success", "Payment updated successfully!");
                navigateToPaymentList();
            } else {
                showAlert("Error", "Failed to update payment in database");
            }
        }
    }

    private boolean validateInputs() {
        if (customerNameField.getText().isEmpty()) {
            showAlert("Error", "Customer name cannot be empty");
            return false;
        }
        
        try {
            Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Amount must be a valid number");
            return false;
        }
        
        if (paymentMethodComboBox.getValue() == null) {
            showAlert("Error", "Please select a payment method");
            return false;
        }
        
        if (statusComboBox.getValue() == null) {
            showAlert("Error", "Please select a status");
            return false;
        }
        
        if (paymentDatePicker.getValue() == null) {
            showAlert("Error", "Please select a payment date");
            return false;
        }
        
        return true;
    }

    private boolean confirmUpdate() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Update");
        confirmAlert.setHeaderText("Are you sure you want to update this payment?");
        confirmAlert.setContentText("This will modify the payment details in the database.");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void updatePaymentDetails() {
        payment.setCustomerId(Integer.parseInt(customerIdField.getText()));
        payment.setCustomerName(customerNameField.getText());
        payment.setServiceId(Integer.parseInt(serviceIdField.getText()));
        payment.setAmount(Double.parseDouble(amountField.getText()));
        
        LocalDate localDate = paymentDatePicker.getValue();
        payment.setPaymentDate(Date.valueOf(localDate));
        
        payment.setPaymentMethod(paymentMethodComboBox.getValue());
        payment.setStatus(statusComboBox.getValue());
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
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
            showAlert("Error", "Could not load payment list view");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToPaymentList();
    }


}