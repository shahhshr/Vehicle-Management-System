--Customer 

--Create Customers Table
CREATE TABLE Customers (
    CustomerID NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    FirstName VARCHAR2(50) NOT NULL,
    LastName VARCHAR2(50) NOT NULL,
    Email VARCHAR2(100) UNIQUE NOT NULL,
    Phone VARCHAR2(15),
    Address VARCHAR2(255)
);

-- Procedures

-- Insert_Customer procedure
CREATE OR REPLACE PROCEDURE Insert_Customer(
    p_FirstName IN VARCHAR2,
    p_LastName IN VARCHAR2,
    p_Email IN VARCHAR2,
    p_Phone IN VARCHAR2,
    p_Address IN VARCHAR2
) AS
BEGIN
    INSERT INTO Customers (FirstName, LastName, Email, Phone, Address)
    VALUES (p_FirstName, p_LastName, p_Email, p_Phone, p_Address);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END Insert_Customer;
/

-- Update_Customer procedure
CREATE OR REPLACE PROCEDURE Update_Customer(
    p_CustomerID IN NUMBER,
    p_FirstName IN VARCHAR2,
    p_LastName IN VARCHAR2,
    p_Email IN VARCHAR2,
    p_Phone IN VARCHAR2,
    p_Address IN VARCHAR2
) AS
BEGIN
    UPDATE Customers
    SET FirstName = p_FirstName,
        LastName = p_LastName,
        Email = p_Email,
        Phone = p_Phone,
        Address = p_Address
    WHERE CustomerID = p_CustomerID;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END Update_Customer;
/

-- Delete_Customer procedure
CREATE OR REPLACE PROCEDURE Delete_Customer(
    p_CustomerID IN NUMBER
) AS
BEGIN
    DELETE FROM Customers WHERE CustomerID = p_CustomerID;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END Delete_Customer;
/

-- Get_Customer procedure with REF CURSOR
CREATE OR REPLACE PROCEDURE Get_Customer(
    p_CustomerID IN NUMBER,
    p_Result OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_Result FOR
    SELECT * FROM Customers WHERE CustomerID = p_CustomerID;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END Get_Customer;
/

-- Get_All_Customers procedure with REF CURSOR
CREATE OR REPLACE PROCEDURE Get_All_Customers(
    p_Result OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_Result FOR
    SELECT * FROM Customers ORDER BY LastName, FirstName;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END Get_All_Customers;
/

--Function

-- Function to check if customer exists
CREATE OR REPLACE FUNCTION Customer_Exists(
    p_CustomerID IN NUMBER
) RETURN BOOLEAN AS
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM Customers
    WHERE CustomerID = p_CustomerID;
    
    RETURN (v_count > 0);
EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END Customer_Exists;
/

--Cursor
-- Cursor to get customers with vehicles
CREATE OR REPLACE PROCEDURE Get_Customers_With_Vehicles(
    p_Result OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_Result FOR
    SELECT c.*, v.Make, v.Model, v.VIN
    FROM Customers c
    LEFT JOIN Vehicles v ON c.CustomerID = v.CustomerID
    ORDER BY c.LastName, c.FirstName;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END Get_Customers_With_Vehicles;
/