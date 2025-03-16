-- mechanic_table.sql - Create Mechanic Table
CREATE TABLE Mechanic (
    mechanic_id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    phone VARCHAR2(15) NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    expertise VARCHAR2(255) NOT NULL
);
-- mechanic_crud_proc.sql - Mechanic CRUD Operations
CREATE OR REPLACE PROCEDURE add_mechanic (
    p_name IN Mechanic.name%TYPE,
    p_phone IN Mechanic.phone%TYPE,
    p_email IN Mechanic.email%TYPE,
    p_expertise IN Mechanic.expertise%TYPE
) AS
BEGIN
    INSERT INTO Mechanic (mechanic_id, name, phone, email, expertise)
    VALUES (mechanic_seq.NEXTVAL, p_name, p_phone, p_email, p_expertise);
END;
/

CREATE OR REPLACE PROCEDURE delete_mechanic (
    p_mechanic_id IN Mechanic.mechanic_id%TYPE
) AS
BEGIN
    DELETE FROM Mechanic WHERE mechanic_id = p_mechanic_id;
END;
/

-- mechanic_trigger.sql - Notify Admins when a new Mechanic is added
CREATE OR REPLACE TRIGGER notify_admin_mechanic
AFTER INSERT ON Mechanic
FOR EACH ROW
BEGIN
    INSERT INTO Admin_Notifications (message)
    VALUES ('New mechanic ' || :NEW.name || ' has been added.');
END;
/
