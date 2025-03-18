--create Table 
CREATE TABLE Appointments (
    AppointmentID INT,
    VehicleID INT,
    CustomerName VARCHAR2(255),
    AppointmentDate DATE,
    ServiceType VARCHAR2(255),
    Status VARCHAR2(50)
);

--Procedure for Add appointment
CREATE OR REPLACE PROCEDURE AddAppointment (
    p_AppointmentID IN INT,
    p_VehicleID IN INT,
    p_CustomerName IN VARCHAR2,
    p_AppointmentDate IN DATE,
    p_ServiceType IN VARCHAR2,
    p_Status IN VARCHAR2
) AS
BEGIN
    INSERT INTO Appointments (AppointmentID, VehicleID, CustomerName, AppointmentDate, ServiceType, Status)
    VALUES (p_AppointmentID, p_VehicleID, p_CustomerName, p_AppointmentDate, p_ServiceType, p_Status);
    
    COMMIT;
END AddAppointment;
/

--call the prodcure
BEGIN
    AddAppointment(
        p_AppointmentID => &AppointmentID,
        p_VehicleID => &VehicleID,
        p_CustomerName => '&CustomerName',
        p_AppointmentDate => TO_DATE('&AppointmentDate', 'YYYY-MM-DD'),
        p_ServiceType => '&ServiceType',
        p_Status => '&Status'
    );
END;
/

--Read Appointment
CREATE OR REPLACE PROCEDURE ReadAppointment (
    p_AppointmentID IN INT DEFAULT NULL,
    p_CustomerName IN VARCHAR2 DEFAULT NULL,
    p_VehicleID IN INT DEFAULT NULL,
    p_Status IN VARCHAR2 DEFAULT NULL
) AS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT AppointmentID,
               VehicleID,
               SUBSTR(CustomerName, 1, 20) AS CustomerName, -- Limit to 20 characters
               TO_CHAR(AppointmentDate, 'YYYY-MM-DD') AS AppointmentDate, -- Format date
               SUBSTR(NVL(ServiceType, 'Not Specified'), 1, 15) AS ServiceType, -- Limit to 15 characters
               SUBSTR(NVL(Status, 'Not Specified'), 1, 10) AS Status -- Limit to 10 characters
        FROM Appointments
        WHERE (AppointmentID = p_AppointmentID OR p_AppointmentID IS NULL)
          AND (CustomerName = p_CustomerName OR p_CustomerName IS NULL)
          AND (VehicleID = p_VehicleID OR p_VehicleID IS NULL)
          AND (Status = p_Status OR p_Status IS NULL);

    DBMS_SQL.RETURN_RESULT(v_cursor);
END ReadAppointment;
/

--call the procedure
BEGIN
    ReadAppointment(p_AppointmentID => &AppointmentID);
END;
/

    
--procedure for UpdateAppointment
CREATE OR REPLACE PROCEDURE UpdateAppointment (
    p_AppointmentID IN INT,
    p_VehicleID IN INT,
    p_CustomerName IN VARCHAR2,
    p_AppointmentDate IN DATE,
    p_ServiceType IN VARCHAR2,
    p_Status IN VARCHAR2
) AS
    v_RowCount INT;
BEGIN
    -- Check if the appointment exists
    SELECT COUNT(*)
    INTO v_RowCount
    FROM Appointments
    WHERE AppointmentID = p_AppointmentID;

    -- If the appointment does not exist, raise an error
    IF v_RowCount = 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Appointment with ID ' || p_AppointmentID || ' does not exist.');
    ELSE
        -- Update the appointment
        UPDATE Appointments
        SET
            VehicleID = p_VehicleID,
            CustomerName = p_CustomerName,
            AppointmentDate = p_AppointmentDate,
            ServiceType = p_ServiceType,
            Status = p_Status
        WHERE
            AppointmentID = p_AppointmentID;

        COMMIT;
    END IF;
END UpdateAppointment;
/

--call the procedure
BEGIN
    UpdateAppointment(
        p_AppointmentID => &p_AppointmentID,
        p_VehicleID => &p_VehicleID,
        p_CustomerName => '&p_CustomerName',
        p_AppointmentDate => TO_DATE('&p_AppointmentDate', 'YYYY-MM-DD'),
        p_ServiceType => '&p_ServiceType',
        p_Status => '&p_Status'
    );
END;
/

-- procedure for Delete Appointment
CREATE OR REPLACE PROCEDURE DeleteAppointment (
    p_AppointmentID IN INT
) AS
    v_RowCount INT;
BEGIN
    -- Check if the appointment exists
    SELECT COUNT(*)
    INTO v_RowCount
    FROM Appointments
    WHERE AppointmentID = p_AppointmentID;

    -- If the appointment does not exist, raise an error
    IF v_RowCount = 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Appointment with ID ' || p_AppointmentID || ' does not exist.');
    ELSE
        -- Delete the appointment
        DELETE FROM Appointments
        WHERE AppointmentID = p_AppointmentID;

        COMMIT;
    END IF;
END DeleteAppointment;
/


-- Prompt the user for input
ACCEPT p_AppointmentID NUMBER PROMPT 'Enter the Appointment ID to delete: '

-- Call the procedure with the user input
BEGIN
    DeleteAppointment(&p_AppointmentID);
END;
/

SELECT * FROM Appointments;
