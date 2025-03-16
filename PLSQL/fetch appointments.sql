
CREATE OR REPLACE FUNCTION GetUpcomingAppointments
RETURN SYS_REFCURSOR
AS
    v_cursor SYS_REFCURSOR;
BEGIN
    -- Open a cursor to fetch upcoming appointments
    OPEN v_cursor FOR
        SELECT AppointmentID,
               CustomerName,
               VehicleID,
               ServiceType,
               TO_CHAR(AppointmentDate, 'YYYY-MM-DD HH24:MI:SS') AS AppointmentDate,
               Status
        FROM Appointments
        WHERE AppointmentDate > SYSDATE 
        ORDER BY AppointmentDate; 

    -- Return the cursor
    RETURN v_cursor;
END GetUpcomingAppointments;
/



DECLARE
    v_cursor SYS_REFCURSOR;
    v_appointmentID INT;
    v_customerName VARCHAR2(100);
    v_vehicleID INT;
    v_serviceType VARCHAR2(100);
    v_appointmentDate VARCHAR2(20);
    v_status VARCHAR2(50);
BEGIN
    -- Call the function to get the cursor
    v_cursor := GetUpcomingAppointments;

    -- Fetch and display the results
    LOOP
        FETCH v_cursor INTO v_appointmentID, v_customerName, v_vehicleID, v_serviceType, v_appointmentDate, v_status;
        EXIT WHEN v_cursor%NOTFOUND;

        -- Display the appointment details
        DBMS_OUTPUT.PUT_LINE(
            'AppointmentID: ' || v_appointmentID || ', ' ||
            'CustomerName: ' || v_customerName || ', ' ||
            'VehicleID: ' || v_vehicleID || ', ' ||
            'ServiceType: ' || v_serviceType || ', ' ||
            'AppointmentDate: ' || v_appointmentDate || ', ' ||
            'Status: ' || v_status
        );
    END LOOP;

    -- Close the cursor
    CLOSE v_cursor;
END;
/
