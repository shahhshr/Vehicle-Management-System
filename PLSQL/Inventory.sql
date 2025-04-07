--Create Inventory Table
CREATE TABLE Inventory (
    InventoryID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ItemName VARCHAR2(100) NOT NULL,
    Description VARCHAR2(255),
    StockQuantity NUMBER NOT NULL,
    UnitPrice NUMBER(10, 2) NOT NULL,
    Category VARCHAR2(50) NOT NULL,  
    MinimumStockThreshold NUMBER,    
    Supplier VARCHAR2(100)
);

-- Procedures

-- Insert_Inventory procedure
CREATE OR REPLACE PROCEDURE Insert_Inventory(
    p_ItemName IN VARCHAR2,
    p_Description IN VARCHAR2,
    p_StockQuantity IN NUMBER,
    p_UnitPrice IN NUMBER,
    p_Category IN VARCHAR2,
    p_MinimumStockThreshold IN NUMBER,
    p_Supplier IN VARCHAR2
) AS
BEGIN
    INSERT INTO Inventory (ItemName, Description, StockQuantity, UnitPrice, Category, MinimumStockThreshold, Supplier)
    VALUES (p_ItemName, p_Description, p_StockQuantity, p_UnitPrice, p_Category, p_MinimumStockThreshold, p_Supplier);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END Insert_Inventory;
/

-- Update_Inventory procedure
CREATE OR REPLACE PROCEDURE Update_Inventory(
    p_InventoryID IN NUMBER,
    p_ItemName IN VARCHAR2,
    p_Description IN VARCHAR2,
    p_StockQuantity IN NUMBER,
    p_UnitPrice IN NUMBER,
    p_Category IN VARCHAR2,
    p_MinimumStockThreshold IN NUMBER,
    p_Supplier IN VARCHAR2
) AS
BEGIN
    UPDATE Inventory
    SET ItemName = p_ItemName,
        Description = p_Description,
        StockQuantity = p_StockQuantity,
        UnitPrice = p_UnitPrice,
        Category = p_Category,
        MinimumStockThreshold = p_MinimumStockThreshold,
        Supplier = p_Supplier
    WHERE InventoryID = p_InventoryID;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END Update_Inventory;
/

-- Delete_Inventory procedure
CREATE OR REPLACE PROCEDURE Delete_Inventory(
    p_InventoryID IN NUMBER
) AS
BEGIN
    DELETE FROM Inventory WHERE InventoryID = p_InventoryID;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END Delete_Inventory;
/

-- Get_Inventory procedure with REF CURSOR
CREATE OR REPLACE PROCEDURE Get_Inventory(
    p_InventoryID IN NUMBER,
    p_Result OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_Result FOR
    SELECT * FROM Inventory WHERE InventoryID = p_InventoryID;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END Get_Inventory;
/

-- Get_Low_Stock_Items procedure
CREATE OR REPLACE PROCEDURE Get_Low_Stock_Items(
    p_Result OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_Result FOR
    SELECT * FROM Inventory 
    WHERE StockQuantity < MinimumStockThreshold
    ORDER BY StockQuantity ASC;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END Get_Low_Stock_Items;
/
--Function

-- Function to check if inventory item exists
CREATE OR REPLACE FUNCTION Inventory_Exists(
    p_InventoryID IN NUMBER
) RETURN BOOLEAN AS
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM Inventory
    WHERE InventoryID = p_InventoryID;
    
    RETURN (v_count > 0);
EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END Inventory_Exists;
/
-- Trigger for Low Stock Alert

-- Trigger for low stock alert
CREATE OR REPLACE TRIGGER Low_Stock_Alert
AFTER INSERT OR UPDATE OF StockQuantity ON Inventory
FOR EACH ROW
DECLARE
    v_notification_text VARCHAR2(500);
BEGIN
    -- Check if stock is below threshold
    IF :NEW.StockQuantity < :NEW.MinimumStockThreshold THEN
        v_notification_text := 'Low stock alert for ' || :NEW.ItemName || 
                              ' (Current: ' || :NEW.StockQuantity || 
                              ', Threshold: ' || :NEW.MinimumStockThreshold || ')';
        
        -- Here you would typically insert into a notifications table
        -- For example:
        -- INSERT INTO Notifications (Message, Type, Timestamp)
        -- VALUES (v_notification_text, 'STOCK', SYSTIMESTAMP);
        
        -- For demonstration, we'll just output the message
        DBMS_OUTPUT.PUT_LINE('NOTIFICATION: ' || v_notification_text);
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        -- Log error but don't stop the operation
        DBMS_OUTPUT.PUT_LINE('Error in Low_Stock_Alert trigger: ' || SQLERRM);
END Low_Stock_Alert;
/

--Cursor Example

-- Cursor to search inventory items
CREATE OR REPLACE PROCEDURE Search_Inventory(
    p_SearchText IN VARCHAR2,
    p_Result OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_Result FOR
    SELECT * FROM Inventory
    WHERE UPPER(ItemName) LIKE '%' || UPPER(p_SearchText) || '%'
       OR UPPER(Description) LIKE '%' || UPPER(p_SearchText) || '%'
       OR UPPER(Category) LIKE '%' || UPPER(p_SearchText) || '%'
       OR UPPER(Supplier) LIKE '%' || UPPER(p_SearchText) || '%';
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END Search_Inventory;
/