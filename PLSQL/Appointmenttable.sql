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

BEGIN
    ReadAppointment(p_AppointmentID => &AppointmentID);
END;
/

SELECT * FROM Appointments;