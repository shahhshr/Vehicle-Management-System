CREATE TABLE Service (
    ServiceID INT,
    CustomerName VARCHAR2(100),
    VehicleID INT,
    ServiceType VARCHAR2(100),
    ServiceDate DATE,
    ServiceTime VARCHAR2(10),
    Mechanic VARCHAR2(100)
);

--procedure for Add service
CREATE OR REPLACE PROCEDURE AddService (
    p_ServiceID IN INT,
    p_CustomerName IN VARCHAR2,
    p_VehicleID IN INT,
    p_ServiceType IN VARCHAR2,
    p_ServiceDate IN DATE,
    p_ServiceTime IN VARCHAR2,
    p_Mechanic IN VARCHAR2
) AS
BEGIN
    -- Insert a new service record into the Service table
    INSERT INTO Service (ServiceID, CustomerName, VehicleID, ServiceType, ServiceDate, ServiceTime, Mechanic)
    VALUES (p_ServiceID, p_CustomerName, p_VehicleID, p_ServiceType, p_ServiceDate, p_ServiceTime, p_Mechanic);

    -- Commit the transaction
    COMMIT;
END AddService;
/

BEGIN
    AddService(
        p_ServiceID => &ServiceID,
        p_CustomerName => '&CustomerName',
        p_VehicleID => &VehicleID,
        p_ServiceType => '&ServiceType',
        p_ServiceDate => TO_DATE('&ServiceDate', 'YYYY-MM-DD'),
        p_ServiceTime => '&ServiceTime',
        p_Mechanic => '&Mechanic'
    );
END;
/


-- procedure for read service
CREATE OR REPLACE PROCEDURE ReadService (
    p_ServiceID IN INT DEFAULT NULL,
    p_CustomerName IN VARCHAR2 DEFAULT NULL,
    p_VehicleID IN INT DEFAULT NULL,
    p_ServiceType IN VARCHAR2 DEFAULT NULL,
    p_ServiceDate IN DATE DEFAULT NULL,
    p_Mechanic IN VARCHAR2 DEFAULT NULL
) AS
    v_cursor SYS_REFCURSOR;
BEGIN
    -- Open a cursor to fetch service records based on the provided filters
    OPEN v_cursor FOR
        SELECT ServiceID,
               SUBSTR(CustomerName, 1, 20) AS CustomerName, -- Limit to 20 characters
               VehicleID,
               SUBSTR(ServiceType, 1, 15) AS ServiceType,   -- Limit to 15 characters
               TO_CHAR(ServiceDate, 'YYYY-MM-DD') AS ServiceDate, 
               SUBSTR(ServiceTime, 1, 8) AS ServiceTime,    -- Limit to 8 characters (e.g., "10:00 AM")
               SUBSTR(Mechanic, 1, 15) AS Mechanic          -- Limit to 15 characters
        FROM Service
        WHERE (ServiceID = p_ServiceID OR p_ServiceID IS NULL)
          AND (CustomerName = p_CustomerName OR p_CustomerName IS NULL)
          AND (VehicleID = p_VehicleID OR p_VehicleID IS NULL)
          AND (ServiceType = p_ServiceType OR p_ServiceType IS NULL)
          AND (ServiceDate = p_ServiceDate OR p_ServiceDate IS NULL)
          AND (Mechanic = p_Mechanic OR p_Mechanic IS NULL);

    -- Return the result set
    DBMS_SQL.RETURN_RESULT(v_cursor);
END ReadService;
/


BEGIN
    ReadService(p_ServiceID => &ServiceID);
END;
/
