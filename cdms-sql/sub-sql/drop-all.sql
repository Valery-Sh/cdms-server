SET SERVEROUTPUT ON;  
--
-- First drop lnked by foreign key table
--

DROP TABLE cdms_invoiceItems;
DROP SEQUENCE cdms_invoiceItems_seq; 

DROP TABLE cdms_invoices;
DROP SEQUENCE cdms_invoices_seq; 

DROP TABLE cdms_Items;
DROP SEQUENCE cdms_iItems_seq; 

DROP FUNCTION getRandomItemName;
DROP FUNCTION getRandomPrice;
DROP FUNCTION getRandomDate;

DROP TABLE cdms_customers;
DROP SEQUENCE cdms_customers_seq; 
DROP FUNCTION getRandomName;
