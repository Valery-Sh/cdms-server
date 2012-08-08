SET SERVEROUTPUT ON;

CONNECT / AS SYSDBA;
ALTER USER hr IDENTIFIED BY hr ACCOUNT UNLOCK;
DISCONNECT;
CONNECT hr/hr;
--
-- First drop lnked by foreign key table
--
DROP TABLE cdms_Customers;
DROP SEQUENCE cdms_Customers_seq; 

CREATE TABLE cdms_Customers (
    id NUMBER PRIMARY KEY, 		-- must be incremented by cdms_Customers_seq
    version NUMBER DEFAULT 0,	
    firstName VARCHAR2(16) NOT NULL,
    lastName  VARCHAR2(16) NOT NULL,
    email  VARCHAR2(64) NOT NULL,
    phone  VARCHAR2(12) NOT NULL,
   createdAt DATE DEFAULT SYSDATE NOT NULL,
   createdBy  NUMBER  NOT NULL,                          -- The id of the user who inserted the record
 CONSTRAINT FK_CUSTOMERS_ON_USERS
	FOREIGN KEY (createdBy) 
	REFERENCES cdms_Users(id)
);

CREATE SEQUENCE cdms_Customers_seq 
    MINVALUE 10
    START WITH 10
    INCREMENT BY 10
    CACHE 50;


INSERT INTO cdms_Customers (id,firstName,lastName,email,phone,createdAt,createdBy) VALUES (
		cdms_Customers_seq.nextval,'Bob','Scott','b.scott@gmail.com','489617', TO_DATE('20120620','YYYYMMDD'),10		
	);

--
-- Now Print all users
--
COLUMN id HEADING 'id';
COLUMN version HEADING 'V.M';
COLUMN firstName HEADING 'First Name';
COLUMN lastName  HEADING 'Last Name';
COLUMN email  HEADING 'Email';
COLUMN phone  HEADING 'Phone';
COLUMN createdAt  HEADING 'Create Date';
COLUMN createdBy  HEADING 'Created By';

SELECT id,firstName,LastName,email,phone,createdAt, createdBy FROM cdms_Customers ORDER BY firstName,lastName;

DISCONNECT;
