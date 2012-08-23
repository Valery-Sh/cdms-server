CONNECT / AS SYSDBA;


ALTER USER hr IDENTIFIED BY hr ACCOUNT UNLOCK;
DISCONNECT;


CONNECT hr/hr;

SET SERVEROUTPUT ON;  
--
-- First drop lnked by foreign key table
--

-- -------------------------------------------------GENERATE INVOICEITEMS -----------------------------------------------------------------------------------

DECLARE
	n INTEGER;

	v_itemId INTEGER;
	v_invoiceId INTEGER;
	v_count INTEGER;
	v_min INTEGER;
	v_max INTEGER;
	v_Items_row_count INTEGER;
	c NUMBER;

	 Cursor  invoices  IS SELECT id 
                       FROM cdms_Invoices;
        
BEGIN
	DBMS_OUTPUT.PUT_LINE('--------------  GENERATE  INVOICEITEMS ---------------------------');
	   v_min := 1;
           v_max := 10;

	   

	   SELECT COUNT(*) INTO v_Items_row_count FROM cdms_Items ;

	   OPEN invoices;

           n := 0;

	   LOOP
	  	FETCH invoices INTO v_invoiceId;
	   	EXIT WHEN invoices%NOTFOUND;

		v_count := TRUNC(DBMS_RANDOM.VALUE(v_min,v_max));			
			
		FOR i IN 1..v_count LOOP
			v_itemId := TRUNC(DBMS_RANDOM.VALUE(1, v_items_row_count));
			v_itemId := v_itemId * 10;
			c := TRUNC(DBMS_RANDOM.VALUE(1,20));			
		
			INSERT INTO cdms_InvoiceItems (id, invoiceId, itemId, itemCount)
				   	 VALUES (cdms_InvoiceItems_seq.nextval, v_invoiceid,v_itemId,c) ;
			n := n + 1;
			IF n = 2000 THEN
				COMMIT;
				n := 0;
			END IF;
		END LOOP;

          END LOOP;
	
	COMMIT;

END;
/



DISCONNECT;
