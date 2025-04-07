
CREATE TABLE Service (
    ServiceID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    CustomerID NUMBER NOT NULL,
    CustomerName VARCHAR2(100) NOT NULL,
    VehicleName VARCHAR2(100) NOT NULL,
    ServiceDetail VARCHAR2(500) NOT NULL,
    Status VARCHAR2(20) DEFAULT 'Pending' CHECK (Status IN ('Pending', 'In Progress', 'Completed')),
    ServiceDate DATE NOT NULL,
    Cost NUMBER(10,2),
    CONSTRAINT fk_service_customer FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);
 ALTER TABLE Service ADD MechanicName VARCHAR2(100);
 
CREATE OR REPLACE PROCEDURE pp_insert_service_sr (
    p_CustomerID      IN Service.CustomerID%TYPE,
    p_CustomerName    IN Service.CustomerName%TYPE,
    p_VehicleName     IN Service.VehicleName%TYPE,
    p_ServiceDetail   IN Service.ServiceDetail%TYPE,
    p_Status          IN Service.Status%TYPE DEFAULT 'Pending',
    p_ServiceDate     IN Service.ServiceDate%TYPE,
    p_Cost            IN Service.Cost%TYPE,
    p_MechanicName    IN Service.MechanicName%TYPE
) AS
BEGIN
    INSERT INTO Service (
        CustomerID,
        CustomerName,
        VehicleName,
        ServiceDetail,
        Status,
        ServiceDate,
        Cost,
        MechanicName
    ) VALUES (
        p_CustomerID,
        p_CustomerName,
        p_VehicleName,
        p_ServiceDetail,
        p_Status,
        p_ServiceDate,
        p_Cost,
        p_MechanicName
    );
    
    DBMS_OUTPUT.PUT_LINE('Service record inserted successfully.');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error inserting service record: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE pp_update_service_sr (
    p_ServiceID       IN Service.ServiceID%TYPE,
    p_CustomerID      IN Service.CustomerID%TYPE,
    p_CustomerName    IN Service.CustomerName%TYPE,
    p_VehicleName     IN Service.VehicleName%TYPE,
    p_ServiceDetail   IN Service.ServiceDetail%TYPE,
    p_Status          IN Service.Status%TYPE,
    p_ServiceDate     IN Service.ServiceDate%TYPE,
    p_Cost            IN Service.Cost%TYPE,
    p_MechanicName    IN Service.MechanicName%TYPE
) AS
    v_count INTEGER;
BEGIN
    -- Check if the ServiceID exists
    SELECT COUNT(*) INTO v_count FROM Service WHERE ServiceID = p_ServiceID;

    IF v_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('ServiceID ' || p_ServiceID || ' does not exist.');
    ELSE
        UPDATE Service
        SET
            CustomerID    = p_CustomerID,
            CustomerName  = p_CustomerName,
            VehicleName   = p_VehicleName,
            ServiceDetail = p_ServiceDetail,
            Status        = p_Status,
            ServiceDate   = p_ServiceDate,
            Cost          = p_Cost,
            MechanicName  = p_MechanicName
        WHERE ServiceID = p_ServiceID;

        DBMS_OUTPUT.PUT_LINE('Service record with ID ' || p_ServiceID || ' updated successfully.');
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error updating service record: ' || SQLERRM);
END;
/


CREATE OR REPLACE PROCEDURE pp_delete_service_sr (
    p_ServiceID IN Service.ServiceID%TYPE
) AS
    v_count INTEGER;
BEGIN
    -- Check if the ServiceID exists
    SELECT COUNT(*) INTO v_count
    FROM Service
    WHERE ServiceID = p_ServiceID;

    IF v_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('ServiceID ' || p_ServiceID || ' does not exist.');
    ELSE
        DELETE FROM Service
        WHERE ServiceID = p_ServiceID;

        DBMS_OUTPUT.PUT_LINE('Service record with ID ' || p_ServiceID || ' deleted successfully.');
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error deleting service record: ' || SQLERRM);
END;
/


CREATE OR REPLACE PROCEDURE pp_view_service_sr (
    p_ServiceID IN Service.ServiceID%TYPE,
    p_ResultSet OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_ResultSet FOR
        SELECT 
            ServiceID, 
            CustomerID, 
            CustomerName, 
            VehicleName, 
            ServiceDetail, 
            Status, 
            ServiceDate, 
            Cost, 
            MechanicName
        FROM Service
        WHERE ServiceID = p_ServiceID;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('No service found with ID ' || p_ServiceID);
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error retrieving service: ' || SQLERRM);
END;
/

DECLARE
    v_cursor SYS_REFCURSOR;
    v_serviceid NUMBER;
    v_mechanicname VARCHAR2(100);
BEGIN
    pp_view_service_sr(1, v_cursor);
    LOOP
        FETCH v_cursor INTO v_serviceid, /* other fields */, v_mechanicname;
        EXIT WHEN v_cursor%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('Mechanic: ' || v_mechanicname);
    END LOOP;
    CLOSE v_cursor;
END;
/




CREATE OR REPLACE PROCEDURE pp_get_services_by_customerid_sr (
    p_CustomerID IN Service.CustomerID%TYPE,
    p_ResultSet OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_ResultSet FOR
        SELECT ServiceID, CustomerName, VehicleName, ServiceDetail, 
               Status, ServiceDate, Cost, MechanicName
        FROM Service
        WHERE CustomerID = p_CustomerID;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error retrieving services: ' || SQLERRM);
        RAISE;
END;
/



CREATE OR REPLACE PROCEDURE pp_get_services_by_vehicle_sr (
    p_VehicleName IN Service.VehicleName%TYPE
) AS
BEGIN
    -- Display Header
    DBMS_OUTPUT.PUT_LINE('Services for Vehicle Name: ' || p_VehicleName);
    DBMS_OUTPUT.PUT_LINE('--------------------------------------------------------');

    -- Loop through and fetch all service records for the given VehicleName
    FOR rec IN (
        SELECT s.ServiceID, s.CustomerID, s.CustomerName, v.VehicleID, v.Make, v.Model, v.Year, v.VIN, 
               s.ServiceDetail, s.Status, s.ServiceDate, s.Cost
        FROM Service s
        JOIN vehicles v ON s.VehicleName = v.Make  -- Ensure this matches your actual schema
        WHERE s.VehicleName = p_VehicleName
    ) 
    LOOP
        DBMS_OUTPUT.PUT_LINE('ServiceID      : ' || rec.ServiceID);
        DBMS_OUTPUT.PUT_LINE('CustomerID     : ' || rec.CustomerID);
        DBMS_OUTPUT.PUT_LINE('Customer Name  : ' || rec.CustomerName);
        DBMS_OUTPUT.PUT_LINE('VehicleID      : ' || rec.VehicleID);
        DBMS_OUTPUT.PUT_LINE('Make           : ' || rec.Make);
        DBMS_OUTPUT.PUT_LINE('Model          : ' || rec.Model);
        DBMS_OUTPUT.PUT_LINE('Year           : ' || rec.Year);
        DBMS_OUTPUT.PUT_LINE('VIN            : ' || rec.VIN);
        DBMS_OUTPUT.PUT_LINE('Service Detail : ' || rec.ServiceDetail);
        DBMS_OUTPUT.PUT_LINE('Status         : ' || rec.Status);
        DBMS_OUTPUT.PUT_LINE('Service Date   : ' || TO_CHAR(rec.ServiceDate, 'YYYY-MM-DD'));
        DBMS_OUTPUT.PUT_LINE('Cost           : ' || TO_CHAR(rec.Cost, '9999.99'));
        DBMS_OUTPUT.PUT_LINE('--------------------------------------------------------');
    END LOOP;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('No services found for Vehicle Name: ' || p_VehicleName);
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error retrieving services: ' || SQLERRM);
END;
/









CREATE OR REPLACE PROCEDURE pp_get_services_by_customerid_sr (
    p_CustomerID IN Service.CustomerID%TYPE,
    p_ResultSet OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_ResultSet FOR
        SELECT ServiceID, CustomerName, VehicleName, ServiceDetail, Status, ServiceDate, Cost
        FROM Service
        WHERE CustomerID = p_CustomerID;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error retrieving services: ' || SQLERRM);
        RAISE;
END;
/

-- Step 1: Create a ref cursor type (do this once in your schema if not already done)
CREATE OR REPLACE PACKAGE service_pkg AS
    TYPE service_ref_cursor IS REF CURSOR;
END service_pkg;
/

-- Step 2: Use that ref cursor in the procedures

-- Procedure 1: Search all services by search term
CREATE OR REPLACE PROCEDURE pp_search_services_sr (
    p_SearchTerm IN VARCHAR2,
    p_ResultSet OUT service_pkg.service_ref_cursor
) AS
BEGIN
    OPEN p_ResultSet FOR
        SELECT s.ServiceID, s.CustomerID, s.CustomerName, s.VehicleName, 
               s.ServiceDetail, s.Status, s.ServiceDate, s.Cost
        FROM Service s
        WHERE UPPER(s.CustomerName) LIKE UPPER(p_SearchTerm)
           OR UPPER(s.VehicleName) LIKE UPPER(p_SearchTerm)
           OR UPPER(s.ServiceDetail) LIKE UPPER(p_SearchTerm)
           OR UPPER(s.Status) LIKE UPPER(p_SearchTerm)
        ORDER BY s.ServiceDate DESC;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error searching services: ' || SQLERRM);
        RAISE;
END;
/

-- Procedure 2: Search services by customer ID and term
-- Updated search procedures
CREATE OR REPLACE PROCEDURE pp_search_services_sr (
    p_SearchTerm IN VARCHAR2,
    p_ResultSet OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_ResultSet FOR
        SELECT s.ServiceID, s.CustomerID, s.CustomerName, s.VehicleName, 
               s.ServiceDetail, s.Status, s.ServiceDate, s.Cost, s.MechanicName
        FROM Service s
        WHERE UPPER(s.CustomerName) LIKE '%' || UPPER(p_SearchTerm) || '%'
           OR UPPER(s.VehicleName) LIKE '%' || UPPER(p_SearchTerm) || '%'
           OR UPPER(s.ServiceDetail) LIKE '%' || UPPER(p_SearchTerm) || '%'
           OR UPPER(s.Status) LIKE '%' || UPPER(p_SearchTerm) || '%'
           OR UPPER(s.MechanicName) LIKE '%' || UPPER(p_SearchTerm) || '%'
        ORDER BY s.ServiceDate DESC;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error searching services: ' || SQLERRM);
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE pp_get_services_by_vehicleid_sr (
    p_VehicleID IN NUMBER,
    p_ResultSet OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_ResultSet FOR
        SELECT s.ServiceID, s.CustomerID, s.CustomerName, s.VehicleName, 
               s.ServiceDetail, s.Status, s.ServiceDate, s.Cost, s.MechanicName
        FROM Service s
        JOIN Vehicles v ON s.VehicleName = v.Make || ' ' || v.Model
        WHERE v.VehicleID = p_VehicleID
        ORDER BY s.ServiceDate DESC;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error retrieving services: ' || SQLERRM);
        RAISE;
END;
/


CREATE OR REPLACE PROCEDURE pp_search_services_by_customer_sr (
    p_CustomerID IN Service.CustomerID%TYPE,
    p_SearchTerm IN VARCHAR2,
    p_ResultSet OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_ResultSet FOR
        SELECT s.ServiceID, s.CustomerID, s.CustomerName, s.VehicleName, 
               s.ServiceDetail, s.Status, s.ServiceDate, s.Cost, s.MechanicName
        FROM Service s
        WHERE s.CustomerID = p_CustomerID
          AND (UPPER(s.CustomerName) LIKE '%' || UPPER(p_SearchTerm) || '%'
               OR UPPER(s.VehicleName) LIKE '%' || UPPER(p_SearchTerm) || '%'
               OR UPPER(s.ServiceDetail) LIKE '%' || UPPER(p_SearchTerm) || '%'
               OR UPPER(s.Status) LIKE '%' || UPPER(p_SearchTerm) || '%'
               OR UPPER(s.MechanicName) LIKE '%' || UPPER(p_SearchTerm) || '%')
        ORDER BY s.ServiceDate DESC;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error searching services: ' || SQLERRM);
        RAISE;
END;
/
