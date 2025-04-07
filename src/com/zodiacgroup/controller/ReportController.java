package com.zodiacgroup.controller;

import com.zodiacgroup.dao.ServiceDAO;
import com.zodiacgroup.main.MainApp;
import com.zodiacgroup.model.Service;
import com.zodiacgroup.util.DateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ReportController extends BaseController implements Initializable {
    
	@FXML
    private Label lblHome, lblCustomer, lblInventory, lblPayment, lblSalesRep, lblManager, lblViewReport, lblLogout;
    
    @FXML
    private TableView<Service> serviceTable;
    @FXML
    private TableColumn<Service, Integer> colId;
    @FXML
    private TableColumn<Service, String> colCustomerName;
    @FXML
    private TableColumn<Service, String> colVehicleName;
    @FXML
    private TableColumn<Service, String> colServiceDetail;
    @FXML
    private TableColumn<Service, String> colStatus;
    @FXML
    private TableColumn<Service, Date> colDate;
    @FXML
    private TableColumn<Service, Double> colCost;
    
    @FXML
    private ComboBox<String> statusFilter;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField searchField;
    
    private ObservableList<Service> serviceList;
    private FilteredList<Service> filteredData;
    private ServiceDAO serviceDAO;
    private String currentRole;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	setCurrentPage("report");
        initializeCurrentPageStyle(lblHome, lblCustomer, lblManager, lblSalesRep, lblInventory, lblPayment, lblViewReport);        
        
        if (lblViewReport != null) {
        	lblViewReport.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        
        serviceDAO = new ServiceDAO();
        initializeTable();
        setupFilters();
        loadServices();
    }
    
    public void setRole(String role) {
        this.currentRole = role;
        configureMenuBasedOnRole(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
        
        setupNavigation(lblHome, lblCustomer, lblPayment, lblInventory, lblSalesRep, lblManager, lblViewReport,
                lblLogout, currentRole);
    }
    
    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colVehicleName.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));
        colServiceDetail.setCellValueFactory(new PropertyValueFactory<>("serviceDetail"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        
        colDate.setCellFactory(column -> new TableCell<Service, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(DateUtil.formatDate(item));
                }
            }
        });
        
        
        
        colCost.setCellFactory(column -> new TableCell<Service, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
    }
    

    private void setupFilters() {
        statusFilter.getItems().addAll("All", "Pending", "In Progress", "Completed");
        statusFilter.setValue("All");
        
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusDays(30));
        
        statusFilter.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters());
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters());
        endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters());
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(service -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                return service.getCustomerName().toLowerCase().contains(lowerCaseFilter) ||
                       service.getVehicleName().toLowerCase().contains(lowerCaseFilter) ||
                       service.getServiceDetail().toLowerCase().contains(lowerCaseFilter) ||
                       service.getStatus().toLowerCase().contains(lowerCaseFilter) ||
                       String.valueOf(service.getId()).contains(newValue);
            });
        });
    }

    private void loadServices() {
        serviceList = FXCollections.observableArrayList(serviceDAO.getAllServices());
        filteredData = new FilteredList<>(serviceList, p -> true);
        
        SortedList<Service> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(serviceTable.comparatorProperty());
        serviceTable.setItems(sortedData);
        
        updateFilters();
    }

    private void updateFilters() {
        filteredData.setPredicate(service -> {
            if (!statusFilter.getValue().equals("All")) {
                if (!service.getStatus().equals(statusFilter.getValue())) {
                    return false;
                }
            }
            
            if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
                LocalDate serviceDate = DateUtil.convertToLocalDate(service.getDate());
                if (serviceDate.isBefore(startDatePicker.getValue()) || 
                    serviceDate.isAfter(endDatePicker.getValue())) {
                    return false;
                }
            }
            
            return true;
        });
    }

    @FXML
    private void handleResetFilters() {
        statusFilter.setValue("All");
        startDatePicker.setValue(LocalDate.now().minusDays(30));
        endDatePicker.setValue(LocalDate.now());
        searchField.clear();
    }

    @FXML
    private void handleExportToCSV() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV File");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            
            fileChooser.setInitialFileName("service_report_" + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv");
            
            File file = fileChooser.showSaveDialog(serviceTable.getScene().getWindow());
            
            if (file != null) {
                List<Service> services = serviceDAO.getAllServices();
                
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("ID,Customer Name,Vehicle Name,Service Detail,Status,Date,Cost");
                    writer.newLine();
                    
                    for (Service service : services) {
                        writer.write(String.format("\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%.2f\"",
                            service.getId(),
                            service.getCustomerName(),
                            service.getVehicleName(),
                            service.getServiceDetail(),
                            service.getStatus(),
                            DateUtil.formatDate(service.getDate()),
                            service.getCost()));
                        writer.newLine();
                    }
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Export Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Service records exported successfully to:\n" + file.getAbsolutePath());
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to export service records:\n" + e.getMessage());
            alert.showAndWait();
        }
    }

    

  
}