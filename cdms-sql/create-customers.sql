CONNECT / AS SYSDBA;
ALTER USER hr IDENTIFIED BY hr ACCOUNT UNLOCK;
DISCONNECT;
CONNECT hr/hr;

SET SERVEROUTPUT ON;
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



DISCONNECT;


