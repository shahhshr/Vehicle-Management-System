-- login_table.sql: Create the Login table
CREATE TABLE Login (
    user_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    role VARCHAR2(20) CHECK (role IN ('admin', 'mechanic', 'customer')) NOT NULL
);

-- mechanic_table.sql: Create the Mechanic table
CREATE TABLE Mechanic (
    mechanic_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    phone VARCHAR2(15) NOT NULL,
    specialty VARCHAR2(50),
    experience NUMBER(2) CHECK (experience >= 0)
);

-- login_authentication_proc.sql: PL/SQL procedure for login authentication
CREATE OR REPLACE PROCEDURE AuthenticateUser(
    p_username IN VARCHAR2,
    p_password IN VARCHAR2,
    p_role OUT VARCHAR2
) AS
    v_password_hash VARCHAR2(255);
BEGIN
    SELECT password_hash, role INTO v_password_hash, p_role
    FROM Login
    WHERE username = p_username;
    
    IF v_password_hash = p_password THEN
        DBMS_OUTPUT.PUT_LINE('Login Successful');
    ELSE
        p_role := NULL;
        DBMS_OUTPUT.PUT_LINE('Invalid Credentials');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_role := NULL;
        DBMS_OUTPUT.PUT_LINE('User not found');
END AuthenticateUser;
/