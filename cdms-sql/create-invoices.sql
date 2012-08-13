CONNECT / AS SYSDBA;


ALTER USER hr IDENTIFIED BY hr ACCOUNT UNLOCK;
DISCONNECT;


CONNECT hr/hr;

SET SERVEROUTPUT ON;  
--
-- First drop lnked by foreign key table
--

DROP TABLE cdms_invoiceItems;
DROP SEQUENCE cdms_invoiceItems_seq; 

DROP TABLE cdms_invoices;
DROP SEQUENCE cdms_invoices_seq; 


CREATE TABLE cdms_Invoices (
    id NUMBER PRIMARY KEY, 		-- must be incremented by cdms_invoices_seq
    version NUMBER DEFAULT 0,	
    customerId Number NOT NULL,
   createdAt DATE DEFAULT SYSDATE NOT NULL,
   createdBy  NUMBER  NOT NULL,                          -- The id of the user who inserted the record
   CONSTRAINT FK_INVOICES_ON_CUSTOMER
	FOREIGN KEY (customerId) 
	REFERENCES cdms_Customers(id)
         ON DELETE CASCADE
   CONSTRAINT FK_INVOICES_ON_USERS
	FOREIGN KEY (createdBy) 
	REFERENCES cdms_Users(id)
);

CREATE SEQUENCE cdms_Invoices_seq 
    MINVALUE 10
    START WITH 10
    INCREMENT BY 10
    CACHE 50;

CREATE TABLE cdms_InvoiceItems (
    id NUMBER PRIMARY KEY, 		-- must be incremented by cdms_invoiceItems_seq
    version NUMBER DEFAULT 0,	
    itemCount NUMBER NOT NULL,    
    invoiceId Number NOT NULL,
    itemId         Number NOT NULL,
   CONSTRAINT FK_INVOICEITEMS_ON_INVOICES
	FOREIGN KEY (invoiceId) 
	REFERENCES cdms_Invoices(id)
	ON DELETE CASCADE,
   CONSTRAINT FK_INVOICEITEMS_ON_ITEMS
	FOREIGN KEY (itemId) 
	REFERENCES cdms_Items(id)
);

CREATE SEQUENCE cdms_InvoiceItems_seq 
    MINVALUE 10
    START WITH 10
    INCREMENT BY 10
    CACHE 50;


DISCONNECT;
