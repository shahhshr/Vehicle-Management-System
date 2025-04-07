CREATE TABLE PAYMENTS (
    INVOICEID NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    CUSTOMERID NUMBER NOT NULL,
    CUSTOMERNAME VARCHAR2(100) NOT NULL,
    SERVICEID NUMBER NOT NULL,
    AMOUNT NUMBER(10,2) NOT NULL,
    PAYMENTDATE DATE NOT NULL,
    PAYMENTMETHOD VARCHAR2(50) NOT NULL,
    STATUS VARCHAR2(20) NOT NULL,
    CONSTRAINT FK_PAYMENT_CUSTOMER FOREIGN KEY (CUSTOMERID) REFERENCES CUSTOMERS(CUSTOMERID),
    CONSTRAINT FK_PAYMENT_SERVICE FOREIGN KEY (SERVICEID) REFERENCES SERVICE(SERVICEID)
);


select * from payments;

--Insert 
CREATE OR REPLACE PROCEDURE pp_insert_payment_sh (
    vp_customerid_sh NUMBER,
    vp_customername_sh VARCHAR2,
    vp_serviceid_sh NUMBER,
    vp_amount_sh NUMBER,
    vp_paymentdate_sh DATE,
    vp_paymentmethod_sh VARCHAR2,
    vp_status_sh VARCHAR2
) AS
    v_count NUMBER;
BEGIN
    -- Check if CUSTOMERID exists
    SELECT COUNT(*) INTO v_count FROM CUSTOMERS WHERE CUSTOMERID = vp_customerid_sh;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Customer ID does not exist.');
    END IF;

    -- Check if SERVICEID exists
    SELECT COUNT(*) INTO v_count FROM SERVICE WHERE SERVICEID = vp_serviceid_sh;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Service ID does not exist.');
    END IF;

    -- Validate amount (should be positive)
    IF vp_amount_sh <= 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Amount should be greater than zero.');
    END IF;

    -- Insert data into PAYMENTS table
    INSERT INTO PAYMENTS (CUSTOMERID, CUSTOMERNAME, SERVICEID, AMOUNT, PAYMENTDATE, PAYMENTMETHOD, STATUS)
    VALUES (vp_customerid_sh, vp_customername_sh, vp_serviceid_sh, vp_amount_sh, vp_paymentdate_sh, vp_paymentmethod_sh, vp_status_sh);

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Payment record inserted successfully.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END pp_insert_payment_sh;
/

--Update
CREATE OR REPLACE PROCEDURE pp_update_payment_sh (
    vp_invoiceid_sh NUMBER,
    vp_customerid_sh NUMBER,
    vp_customername_sh VARCHAR2,
    vp_serviceid_sh NUMBER,
    vp_amount_sh NUMBER,
    vp_paymentdate_sh DATE,
    vp_paymentmethod_sh VARCHAR2,
    vp_status_sh VARCHAR2
) AS
    v_count NUMBER;
BEGIN
    -- Check if INVOICEID exists
    SELECT COUNT(*) INTO v_count FROM PAYMENTS WHERE INVOICEID = vp_invoiceid_sh;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20004, 'Invoice ID does not exist.');
    END IF;

    -- Check if CUSTOMERID exists
    SELECT COUNT(*) INTO v_count FROM CUSTOMERS WHERE CUSTOMERID = vp_customerid_sh;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Customer ID does not exist.');
    END IF;

    -- Check if SERVICEID exists
    SELECT COUNT(*) INTO v_count FROM SERVICE WHERE SERVICEID = vp_serviceid_sh;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Service ID does not exist.');
    END IF;

    -- Validate AMOUNT (should be positive)
    IF vp_amount_sh <= 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Amount should be greater than zero.');
    END IF;

    -- Update payment record
    UPDATE PAYMENTS
    SET CUSTOMERID = vp_customerid_sh,
        CUSTOMERNAME = vp_customername_sh,
        SERVICEID = vp_serviceid_sh,
        AMOUNT = vp_amount_sh,
        PAYMENTDATE = vp_paymentdate_sh,
        PAYMENTMETHOD = vp_paymentmethod_sh,
        STATUS = vp_status_sh
    WHERE INVOICEID = vp_invoiceid_sh;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Payment record updated successfully.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END pp_update_payment_sh;
/


--Delete
CREATE OR REPLACE PROCEDURE pp_delete_payment_sh (
    vp_invoiceid_sh NUMBER
) AS
    v_count NUMBER;
BEGIN
    -- Check if INVOICEID exists
    SELECT COUNT(*) INTO v_count FROM PAYMENTS WHERE INVOICEID = vp_invoiceid_sh;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20005, 'Invoice ID does not exist.');
    END IF;

    -- Delete the record
    DELETE FROM PAYMENTS WHERE INVOICEID = vp_invoiceid_sh;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Payment record deleted successfully.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END pp_delete_payment_sh;
/




