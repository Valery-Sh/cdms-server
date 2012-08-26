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
	ON DELETE CASCADE,
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
-- -------------------------------------------------GENERATE INVOICES -----------------------------------------------------------------------------------


DECLARE
	n INTEGER;
	r INTEGER;
	c INTEGER;

	v_seq_id NUMBER;
	v_userId INTEGER;
	v_customerId INTEGER;
	v_customerCreatedAt DATE;

        v_committed NUMBER;

	v_count INTEGER;
	v_min INTEGER;
	v_max INTEGER;

        v_createdAt  DATE;

	 Cursor  customers  IS SELECT id,createdAt
                       FROM cdms_customers;
        
BEGIN
	   v_min := 1;
           v_max := 10;
	   v_committed  :=  0;

    	   v_count := TRUNC(DBMS_RANDOM.VALUE(v_min,v_max));			

	   OPEN customers;

           n := 0;
	   c := 0;
	   LOOP
	  	FETCH customers INTO v_customerId,v_customerCreatedAt;
	   	EXIT WHEN customers%NOTFOUND;
               v_committed  :=   v_committed + 1;


		r := TRUNC(DBMS_RANDOM.VALUE(v_min,v_max));
		v_count := n + r;		
			
			FOR i IN 1..v_count LOOP
  	        		v_userId := TRUNC(DBMS_RANDOM.VALUE(1,10));	
				v_userId := v_userId * 10;      
				v_seq_id  :=  cdms_Invoices_seq.nextval;         

				v_createdAt := getRandomDate(v_customerCreatedAt, TO_DATE('20120801','YYYYMMDD') );		

				INSERT INTO cdms_Invoices (id, customerId,
								   	createdAt,createdBy ) VALUES (
									v_seq_id, v_customerId,
								        v_createdAt ,v_userId		
	        		);
				c := c + 1;
				IF c = 2000 THEN
					COMMIT;
					c := 0;
--					DBMS_OUTPUT.PUT_LINE('GEN 50,000 INVOICES COMMITTED:  ' ||  v_committed );
				END IF;
          		END LOOP;
			IF r < 10 THEN
				n := 10-r;
			ELSE
				n := 0;
			END IF;
          END LOOP;
	
	COMMIT;

END;
/
