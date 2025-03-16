-- mechanic_crud_proc.sql: PL/SQL procedures for Mechanic CRUD operations
CREATE OR REPLACE PROCEDURE AddMechanic(
    p_name IN VARCHAR2,
    p_email IN VARCHAR2,
    p_phone IN VARCHAR2,
    p_specialty IN VARCHAR2,
    p_experience IN NUMBER
) AS
BEGIN
    INSERT INTO Mechanic (name, email, phone, specialty, experience)
    VALUES (p_name, p_email, p_phone, p_specialty, p_experience);
    COMMIT;
END AddMechanic;
/

CREATE OR REPLACE PROCEDURE UpdateMechanic(
    p_mechanic_id IN NUMBER,
    p_name IN VARCHAR2,
    p_email IN VARCHAR2,
    p_phone IN VARCHAR2,
    p_specialty IN VARCHAR2,
    p_experience IN NUMBER
) AS
BEGIN
    UPDATE Mechanic
    SET name = p_name, email = p_email, phone = p_phone, specialty = p_specialty, experience = p_experience
    WHERE mechanic_id = p_mechanic_id;
    COMMIT;
END UpdateMechanic;
/

CREATE OR REPLACE PROCEDURE DeleteMechanic(p_mechanic_id IN NUMBER) AS
BEGIN
    DELETE FROM Mechanic WHERE mechanic_id = p_mechanic_id;
    COMMIT;
END DeleteMechanic;
/

-- mechanic_trigger.sql: PL/SQL trigger to notify admins when a new mechanic is added
CREATE OR REPLACE TRIGGER NotifyAdminOnNewMechanic
AFTER INSERT ON Mechanic
FOR EACH ROW
BEGIN
    DBMS_OUTPUT.PUT_LINE('New mechanic added: ' || :NEW.name || ' (Email: ' || :NEW.email || ')');
END NotifyAdminOnNewMechanic;
/
