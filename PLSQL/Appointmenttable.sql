CREATE TABLE Appointments (
    AppointmentID NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 100 INCREMENT BY 1) PRIMARY KEY,
    CustomerID NUMBER NOT NULL,
    VehicleID NUMBER NOT NULL,
    CustomerName VARCHAR2(255),
    AppointmentDate DATE,
    ServiceType VARCHAR2(255),
    Status VARCHAR2(50),
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE CASCADE,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID) ON DELETE CASCADE
);

-- Appointments CRUD Operations

-- (A) Insert an Appointment
CREATE OR REPLACE PROCEDURE Insert_Appointment(
    p_CustomerID IN NUMBER,
    p_VehicleID IN NUMBER,
    p_CustomerName IN VARCHAR2,
    p_AppointmentDate IN DATE,
    p_ServiceType IN VARCHAR2,
    p_Status IN VARCHAR2
) AS
BEGIN
    INSERT INTO Appointments (CustomerID, VehicleID, CustomerName, AppointmentDate, ServiceType, Status)
    VALUES (p_CustomerID, p_VehicleID, p_CustomerName, p_AppointmentDate, p_ServiceType, p_Status);
    COMMIT;
END;
/

-- For Execution:

BEGIN
    Insert_Appointment(1, 1, 'Poorva Patel', TO_DATE('2023-12-15 10:00', 'YYYY-MM-DD HH24:MI'), 'Oil Change', 'Scheduled');
END;
/

SELECT * FROM APPOINTMENTS;
-- (B) Get Appointment
CREATE OR REPLACE PROCEDURE Get_Appointment(
    p_AppointmentID IN NUMBER
) AS
    v_CustomerName Appointments.CustomerName%TYPE;
    v_AppointmentDate Appointments.AppointmentDate%TYPE;
    v_ServiceType Appointments.ServiceType%TYPE;
    v_Status Appointments.Status%TYPE;
BEGIN
    SELECT CustomerName, AppointmentDate, ServiceType, Status
    INTO v_CustomerName, v_AppointmentDate, v_ServiceType, v_Status
    FROM Appointments
    WHERE AppointmentID = p_AppointmentID;

    DBMS_OUTPUT.PUT_LINE('Appointment ID: ' || p_AppointmentID);
    DBMS_OUTPUT.PUT_LINE('Customer: ' || v_CustomerName);
    DBMS_OUTPUT.PUT_LINE('Date: ' || TO_CHAR(v_AppointmentDate, 'YYYY-MM-DD HH24:MI'));
    DBMS_OUTPUT.PUT_LINE('Service Type: ' || v_ServiceType);
    DBMS_OUTPUT.PUT_LINE('Status: ' || v_Status);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Appointment not found with ID: ' || p_AppointmentID);
END;
/

-- For Execution:

BEGIN
    Get_Appointment(100);
END;
/


-- (C) Update Appointment
CREATE OR REPLACE PROCEDURE Update_Appointment(
    p_AppointmentID IN NUMBER,
    p_CustomerID IN NUMBER,
    p_VehicleID IN NUMBER,
    p_CustomerName IN VARCHAR2,
    p_AppointmentDate IN DATE,
    p_ServiceType IN VARCHAR2,
    p_Status IN VARCHAR2
) AS
BEGIN
    UPDATE Appointments
    SET CustomerID = p_CustomerID,
        VehicleID = p_VehicleID,
        CustomerName = p_CustomerName,
        AppointmentDate = p_AppointmentDate,
        ServiceType = p_ServiceType,
        Status = p_Status
    WHERE AppointmentID = p_AppointmentID;

    COMMIT;
END;
/

-- For Execution:

BEGIN
    Update_Appointment(100, 1, 1, 'Poorva Patel', TO_DATE('2023-12-16 11:00', 'YYYY-MM-DD HH24:MI'), 'Oil Change and Tire Rotation', 'Rescheduled');
END;
/


-- (D) Delete Appointment
CREATE OR REPLACE PROCEDURE Delete_Appointment(
    p_AppointmentID IN NUMBER
) AS
BEGIN
    DELETE FROM Appointments WHERE AppointmentID = p_AppointmentID;
    COMMIT;
END;
/

-- For Execution:

BEGIN
    Delete_Appointment(100);
END;
/


-- (E) Get All Appointments for a Customer
CREATE OR REPLACE PROCEDURE Get_Customer_Appointments(
    p_CustomerID IN NUMBER
) AS
    CURSOR c_appointments IS
        SELECT AppointmentID, CustomerName, AppointmentDate, ServiceType, Status
        FROM Appointments
        WHERE CustomerID = p_CustomerID
        ORDER BY AppointmentDate;
    
    v_appointment c_appointments%ROWTYPE;
    v_count NUMBER := 0;
BEGIN
    DBMS_OUTPUT.PUT_LINE('Appointments for Customer ID: ' || p_CustomerID);
    DBMS_OUTPUT.PUT_LINE('----------------------------------');
    
    OPEN c_appointments;
    LOOP
        FETCH c_appointments INTO v_appointment;
        EXIT WHEN c_appointments%NOTFOUND;
        
        v_count := v_count + 1;
        
        DBMS_OUTPUT.PUT_LINE('Appointment ID: ' || v_appointment.AppointmentID);
        DBMS_OUTPUT.PUT_LINE('Customer: ' || v_appointment.CustomerName);
        DBMS_OUTPUT.PUT_LINE('Date: ' || TO_CHAR(v_appointment.AppointmentDate, 'YYYY-MM-DD HH24:MI'));
        DBMS_OUTPUT.PUT_LINE('Service: ' || v_appointment.ServiceType);
        DBMS_OUTPUT.PUT_LINE('Status: ' || v_appointment.Status);
        DBMS_OUTPUT.PUT_LINE('----------------------------------');
    END LOOP;
    CLOSE c_appointments;
    
    IF v_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No appointments found for this customer.');
    END IF;
END;
/

-- For Execution:

BEGIN
    Get_Customer_Appointments(1);
END;
/
