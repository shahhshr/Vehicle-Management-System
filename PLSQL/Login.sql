-- login_table.sql - Create Login Table
CREATE TABLE Login (
    user_id NUMBER PRIMARY KEY,
    email VARCHAR2(100) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL,
    role VARCHAR2(50) CHECK (role IN ('Admin', 'Sales Representative'))
);
-- login_authentication_proc.sql - Login Authentication Procedure
CREATE OR REPLACE PROCEDURE authenticate_user (
    p_email IN Login.email%TYPE,
    p_password IN Login.password%TYPE,
    p_role OUT Login.role%TYPE
) AS
BEGIN
    SELECT role INTO p_role
    FROM Login
    WHERE email = p_email AND password = p_password;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_role := NULL;
END;
/
