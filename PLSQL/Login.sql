--1. Create the Users Table


CREATE TABLE login (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR2(50) NOT NULL UNIQUE,
    password VARCHAR2(100) NOT NULL,
    role VARCHAR2(20) NOT NULL CHECK (role IN ('ADMIN', 'SALES_REP'))
);

--2. Insert Initial Admin User

INSERT INTO login (username, password, role) 
VALUES ('admin', 'admin123', 'ADMIN');

--3. Insert Sample Sales Representative

INSERT INTO login (username, password, role) 
VALUES ('sales2', 'sales123', 'SALES_REP');

select * from login;
-- Connect as techtrek user and run:
SELECT * FROM login WHERE username = 'admin';
COMMIT;