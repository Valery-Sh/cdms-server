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
	REFERENCES cdms_Customers(id),
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
);

CREATE SEQUENCE cdms_InvoiceItems_seq 
    MINVALUE 10
    START WITH 10
    INCREMENT BY 10
    CACHE 50;
-- -------------------------------------------------GENERATE INVOICES -----------------------------------------------------------------------------------


DECLARE
--        v_userId INTEGER;
--	v_customerId INTEGER;
--	v_invoiceId INTEGER;
	v_minInvoiceItems INTEGER;
	v_maxInvoiceItems INTEGER;
	
	v_Items_row_count INTEGER;
	v_seq_id NUMBER;
	v_userId INTEGER;
	v_customerId INTEGER;
	v_count INTEGER;
	v_min INTEGER;
	v_max INTEGER;

	 Cursor  customers  IS SELECT id 
                       FROM cdms_customers;
        
BEGIN
	   v_min := 10;
           v_max := 30;
	   v_minInvoiceItems := 1 ;
           v_maxInvoiceItems := 20;

	   SELECT COUNT(*) INTO v_Items_row_count FROM cdms_Items ;

    	   v_count := TRUNC(DBMS_RANDOM.VALUE(v_min,v_max));			

	   OPEN customers;
	   LOOP
	  	FETCH customers INTO v_customerId;
	   	EXIT WHEN customers%NOTFOUND;
		v_count := TRUNC(DBMS_RANDOM.VALUE(v_min,v_max));		
                IF v_count > 0 THEN 
			FOR i IN 1..v_count LOOP
  	        		v_userId := TRUNC(DBMS_RANDOM.VALUE(1,10));	
				v_userId := v_userId * 10;      
				v_seq_id  :=  cdms_Invoices_seq.nextval;         
				INSERT INTO cdms_Invoices (id, customerId,
								   	createdAt,createdBy ) VALUES (
									v_seq_id, v_customerId,
								        TO_DATE('20120620','YYYYMMDD') ,v_userId		
	        		);
				-- ----------- For each Invoice generate a range of InvoiceItems  -------------------------
  			        v_count := TRUNC(DBMS_RANDOM.VALUE( v_minInvoiceItems, v_maxInvoiceItems));		
				
				FOR j IN 1..v_count LOOP

					INSERT INTO cdms_InvoiceItems (id, invoiceId,itemId,itemCount)
								   	 VALUES (
										cdms_InvoiceItems_seq.nextval, v_seq_id,20, j
		       			);
				END LOOP;
					
--          			DBMS_OUTPUT.PUT_LINE(v_customerId);
          		END LOOP;
		END IF;
          END LOOP;

	COMMIT;

END;
/



DISCONNECT;
