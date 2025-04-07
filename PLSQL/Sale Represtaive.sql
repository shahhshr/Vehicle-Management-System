CREATE TABLE Sale_Represent (
    employee_id NUMBER GENERATED ALWAYS AS IDENTITY START WITH 1000 INCREMENT BY 1 PRIMARY KEY,
    employee_name VARCHAR2(255) NOT NULL,
    position VARCHAR2(100) NOT NULL,
    email VARCHAR2(255) UNIQUE NOT NULL,
    phone_number VARCHAR2(15) UNIQUE NOT NULL
);

Drop table Sale_Represent;
--INSERT
CREATE OR REPLACE PROCEDURE pp_insert_sale_represent (
  p_sale_id IN NUMBER,
  p_name IN VARCHAR2,
  p_email IN VARCHAR2,
  p_phone_number IN VARCHAR2
) IS
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM SALE_REPRESENT
  WHERE PHONE_NUMBER = p_phone_number;

  IF v_count = 0 THEN
    INSERT INTO SALE_REPRESENT (SALE_ID, NAME, EMAIL, PHONE_NUMBER)
    VALUES (p_sale_id, p_name, p_email, p_phone_number);
  ELSE
    DBMS_OUTPUT.PUT_LINE('Phone number already exists!');
  END IF;
END;
/




--Update
CREATE OR REPLACE PROCEDURE pp_update_sale_represent (
    vp_employee_id     IN NUMBER,
    vp_employee_name   IN VARCHAR2,
    vp_position        IN VARCHAR2,
    vp_email           IN VARCHAR2,
    vp_phone_number    IN VARCHAR2
) AS
BEGIN
    UPDATE Sale_Represent
    SET
        employee_name = vp_employee_name,
        position = vp_position,
        email = vp_email,
        phone_number = vp_phone_number
    WHERE employee_id = vp_employee_id;

    IF SQL%ROWCOUNT = 0 THEN
        DBMS_OUTPUT.PUT_LINE('Error: No employee found with ID ' || vp_employee_id);
    ELSE
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Employee with ID ' || vp_employee_id || ' updated successfully.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END pp_update_sale_represent;
/


--Delete
CREATE OR REPLACE PROCEDURE pp_delete_sale_represent (
    vp_employee_id IN INT
) AS 
BEGIN
    DELETE FROM Sale_Represent
    WHERE employee_id = vp_employee_id;

    IF SQL%ROWCOUNT = 0 THEN
        DBMS_OUTPUT.PUT_LINE('Error: No employee found with ID ' || vp_employee_id);
    ELSE
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Employee with ID ' || vp_employee_id || ' deleted successfully.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END pp_delete_sale_represent;
/


--view
CREATE OR REPLACE PROCEDURE pp_view_sale_represent (
    p_Result OUT SYS_REFCURSOR
) AS 
BEGIN
    OPEN p_Result FOR
        SELECT employee_id, employee_name, position, email, phone_number
        FROM Sale_Represent;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END pp_view_sale_represent;
/


SELECT * FROM Sale_Represent;


SELECT table_name, column_name
FROM all_cons_columns
WHERE constraint_name = 'SYS_C001153414';


