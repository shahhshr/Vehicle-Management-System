package com.zodiacgroup.controller;

import com.zodiacgroup.dao.PaymentDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import java.awt.Desktop;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PaymentController extends BaseController implements Initializable {

    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, Integer> invoiceIdColumn;
    @FXML private TableColumn<Payment, String> customerNameColumn;
    @FXML private TableColumn<Payment, Integer> serviceIdColumn;
    @FXML private TableColumn<Payment, Double> amountColumn;
    @FXML private TableColumn<Payment, Date> paymentDateColumn;
    @FXML private TableColumn<Payment, String> paymentMethodColumn;
    @FXML private TableColumn<Payment, String> statusColumn;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button addButton;
    
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
   
    private PaymentDAO paymentDAO = new PaymentDAO();
    private ObservableList<Payment> paymentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCurrentPage("payment");
    	
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
        
    	if (lblPayment != null) {
            lblPayment.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
    
    	        	    invoiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceId")); 
    	    customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName")); 
    	    serviceIdColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId")); 
    	    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount")); 
    	    paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate")); 
    	    paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod")); 
    	    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status")); 
    	    
        
    	    refreshPaymentTable();
            
            
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty()) {
                    refreshPaymentTable();
                } else {
                    searchPayments(newValue);
                }
            });
        }
    
    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        
    }

    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
        
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
    
        
        setupRoleBasedAccess();
    }
    
    private void setupRoleBasedAccess() {
        if (currentRole != null) {
           
            if ("Mechanic".equals(currentRole)) {
                
                paymentTable.setEditable(false);
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                addButton.setDisable(true);
            }
        }
    }
    
    public void refreshPaymentTable() {
        paymentList.clear();
        List<Payment> payments = paymentDAO.getAllPayments();
        System.out.println("Loaded " + payments.size() + " payments from database"); 
        paymentList.addAll(payments);
        paymentTable.setItems(paymentList);
        
        
        if (!payments.isEmpty()) {
            System.out.println("First payment: " + payments.get(0).toString());
        }
    }
    
    private void searchPayments(String searchTerm) {
        paymentList.clear();
        paymentList.addAll(paymentDAO.searchPayments(searchTerm));
        paymentTable.setItems(paymentList);
    }
    
    @FXML
    private void handleRefreshButtonAction() {
        refreshPaymentTable();
    }
    
 

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        Payment selectedPayment = paymentTable.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            showAlert("Error", "Please select a payment to update", Alert.AlertType.ERROR);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/UpdatePayment.fxml"));
            Parent root = loader.load();

            UpdatePaymentController controller = loader.getController();
            controller.setPaymentData(selectedPayment);
            controller.setMainApp(mainApp);  
            controller.setRole(currentRole);

            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Payment");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load update payment form", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Payment selectedPayment = paymentTable.getSelectionModel().getSelectedItem();
        if (selectedPayment != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Are you sure you want to delete this payment?");
            
            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                try {
                    paymentDAO.deletePayment(selectedPayment); 
                    showAlert("Success", "Payment deleted successfully", Alert.AlertType.INFORMATION);
                    refreshPaymentTable();
                } catch (Exception e) {
                    showAlert("Error", "Failed to delete payment", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("Error", "Please select a payment to delete", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleProcessInvoiceAction() {
        Payment selectedPayment = paymentTable.getSelectionModel().getSelectedItem();
        if (selectedPayment != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/Process-inovoice.fxml"));
                Parent root = loader.load();
                
                ProcessInvoiceController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setRole(currentRole);
                controller.setPayment(selectedPayment);
                
                Stage stage = new Stage();
                stage.setTitle("Process Invoice");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                
                refreshPaymentTable();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load process invoice form: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Please select an invoice to process", Alert.AlertType.ERROR);
        }
    }
    
    
    
    
    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/addPayment.fxml"));
            Parent root = loader.load();

            AddPaymentController controller = loader.getController();
            controller.setMainApp(mainApp);  
            controller.setRole(currentRole);

            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Payment");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load update payment form", Alert.AlertType.ERROR);
        }
    }
   
    
    @FXML
    private void handleGeneratePDFAction() {
        Payment selectedPayment = paymentTable.getSelectionModel().getSelectedItem();
        if (selectedPayment != null) {
            try {
                generateInvoicePDF(selectedPayment);
                showAlert("Success", "PDF invoice generated successfully", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to generate PDF: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Please select an invoice to generate PDF", Alert.AlertType.ERROR);
        }
    }

    private void generateInvoicePDF(Payment payment) throws Exception {
        
        Document document = new Document();
        
        
        String fileName = "Invoice_" + payment.getInvoiceId() + ".pdf";
        
        try {
            
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            
            
            addInvoiceHeader(document);
            addInvoiceTitle(document);
            addInvoiceDetails(document, payment);
            
            document.close();
            
            
            File file = new File(fileName);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } catch (DocumentException | IOException e) {
            throw new Exception("Error generating PDF: " + e.getMessage());
        }
    }

    private void addInvoiceHeader(Document document) throws DocumentException {
        
        Paragraph header = new Paragraph();
        header.add(new Chunk("Zodiac Group", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24)));
        header.setAlignment(Element.ALIGN_LEFT);
        document.add(header);
        document.add(new Paragraph(" ")); 
    }

    private void addInvoiceTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("INVOICE", 
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, Font.BOLD, BaseColor.BLUE));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); 
    }

    private void addInvoiceDetails(Document document, Payment payment) throws DocumentException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        
        
        addTableRow(table, "Invoice Number:", String.valueOf(payment.getInvoiceId()));
        addTableRow(table, "Invoice Date:", dateFormat.format(payment.getPaymentDate()));
        addTableRow(table, "Customer Name:", payment.getCustomerName());
        addTableRow(table, "Service ID:", String.valueOf(payment.getServiceId()));
        addTableRow(table, "Amount:", String.format("$%.2f", payment.getAmount()));
        addTableRow(table, "Payment Method:", payment.getPaymentMethod());
        addTableRow(table, "Status:", payment.getStatus());
        
        document.add(table);
    }

    private void addTableRow(PdfPTable table, String label, String value) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, 
            FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        PdfPCell cell2 = new PdfPCell(new Phrase(value));
        table.addCell(cell1);
        table.addCell(cell2);
    }
    
    
    
    @FXML
    private void handleGetEmailAction() {
        Payment selectedPayment = paymentTable.getSelectionModel().getSelectedItem();
        if (selectedPayment != null) {
            
            showAlert("Info", "Email retrieval would be implemented here", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Please select an invoice to get email", Alert.AlertType.ERROR);
        }
    }
    

    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}