package com.zodiacgroup.model;

import javax.persistence.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

@Entity
@Table(name = "INVENTORY")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVENTORYID")
    private int inventoryId;
    
    @Column(name = "ITEMNAME", nullable = false, length = 100)
    private String itemName;
    
    @Column(name = "DESCRIPTION", length = 255)
    private String description;
    
    @Column(name = "STOCKQUANTITY", nullable = false)
    private int stockQuantity;
    
    @Column(name = "UNITPRICE", nullable = false)
    private double unitPrice;
    
    @Column(name = "CATEGORY", nullable = false, length = 50)
    private String category;
    
    @Column(name = "MINIMUMSTOCKTHRESHOLD")
    private int minimumStockThreshold;
    
    @Column(name = "SUPPLIER", length = 100)
    private String supplier;

    public Inventory() {}

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMinimumStockThreshold() {
        return minimumStockThreshold;
    }

    public void setMinimumStockThreshold(int minimumStockThreshold) {
        this.minimumStockThreshold = minimumStockThreshold;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public IntegerProperty inventoryIdProperty() {
        return new SimpleIntegerProperty(inventoryId);
    }

    public StringProperty itemNameProperty() {
        return new SimpleStringProperty(itemName);
    }

    public StringProperty categoryProperty() {
        return new SimpleStringProperty(category);
    }

    public IntegerProperty stockQuantityProperty() {
        return new SimpleIntegerProperty(stockQuantity);
    }

    public DoubleProperty unitPriceProperty() {
        return new SimpleDoubleProperty(unitPrice);
    }

    public StringProperty supplierProperty() {
        return new SimpleStringProperty(supplier);
    }

    public boolean isLowStock() {
        return stockQuantity < minimumStockThreshold;
    }

    public String getFormattedPrice() {
        return String.format("$%.2f", unitPrice);
    }

    @Override
    public String toString() {
        return "Inventory [id=" + inventoryId + ", name=" + itemName + ", stock=" + stockQuantity + "]";
    }
    
    public boolean matchesSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return true;
        }
        
        String lowerSearch = searchTerm.toLowerCase();
        return String.valueOf(inventoryId).contains(searchTerm) ||
               itemName.toLowerCase().contains(lowerSearch) ||
               (description != null && description.toLowerCase().contains(lowerSearch)) ||
               category.toLowerCase().contains(lowerSearch) ||
               (supplier != null && supplier.toLowerCase().contains(lowerSearch)) ||
               String.valueOf(stockQuantity).contains(searchTerm) ||
               String.format("%.2f", unitPrice).contains(searchTerm);
    }
}