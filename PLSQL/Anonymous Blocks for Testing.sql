-- Test Insert_Customer
DECLARE
BEGIN
    Insert_Customer('John', 'Doe', 'john.doe@example.com', '1234567890', '123 Main St');
    DBMS_OUTPUT.PUT_LINE('Customer inserted successfully');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error inserting customer: ' || SQLERRM);
END;
/

-- Test Insert_Vehicle
DECLARE
    v_customer_id NUMBER;
BEGIN
    -- Get a customer ID to use
    SELECT CustomerID INTO v_customer_id FROM Customers WHERE ROWNUM = 1;
    
    Insert_Vehicle(v_customer_id, 'Toyota', 'Camry', 2020, '1HGCM82633A123456');
    DBMS_OUTPUT.PUT_LINE('Vehicle inserted successfully');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error inserting vehicle: ' || SQLERRM);
END;
/

-- Test Low Stock Trigger
DECLARE
BEGIN
    -- Insert an item with low stock
    Insert_Inventory('Engine Oil', '5W-30 Synthetic', 5, 29.99, 'Fluids', 10, 'OilCo');
    DBMS_OUTPUT.PUT_LINE('Inventory item inserted - should trigger low stock alert');
    
    -- Update to make stock even lower
    UPDATE Inventory SET StockQuantity = 2 WHERE ItemName = 'Engine Oil';
    DBMS_OUTPUT.PUT_LINE('Inventory updated - should trigger low stock alert again');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error testing low stock trigger: ' || SQLERRM);
END;
/

-- Test Get_Low_Stock_Items
DECLARE
    v_cursor SYS_REFCURSOR;
    v_item_name VARCHAR2(100);
    v_stock NUMBER;
    v_threshold NUMBER;
BEGIN
    Get_Low_Stock_Items(v_cursor);
    
    DBMS_OUTPUT.PUT_LINE('Low Stock Items:');
    DBMS_OUTPUT.PUT_LINE('---------------------------------');
    
    LOOP
        FETCH v_cursor INTO v_item_name, v_stock, v_threshold;
        EXIT WHEN v_cursor%NOTFOUND;
        
        DBMS_OUTPUT.PUT_LINE('Item: ' || v_item_name || 
                            ', Stock: ' || v_stock || 
                            ', Threshold: ' || v_threshold);
    END LOOP;
    
    CLOSE v_cursor;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error getting low stock items: ' || SQLERRM);
        IF v_cursor%ISOPEN THEN
            CLOSE v_cursor;
        END IF;
END;
/

-- Test Process_Payment
DECLARE
    v_customer_id NUMBER;
    v_service_id NUMBER;
BEGIN
    -- Get a customer ID and service ID to use
    SELECT CustomerID INTO v_customer_id FROM Customers WHERE ROWNUM = 1;
    
    -- Create a service first
    Insert_Service(v_customer_id, 'Toyota Camry', 'Oil Change', SYSDATE, 49.99);
    SELECT ServiceID INTO v_service_id FROM Service WHERE ROWNUM = 1 ORDER BY ServiceID DESC;
    
    -- Process payment
    Process_Payment(v_customer_id, v_service_id, 49.99, 'Credit Card');
    DBMS_OUTPUT.PUT_LINE('Payment processed successfully');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error processing payment: ' || SQLERRM);
END;
/