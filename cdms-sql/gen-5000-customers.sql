CONNECT / AS SYSDBA;

SET SERVEROUTPUT ON;  -- MUST BE AFTER COONECT
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



CREATE FUNCTION getRandomName
RETURN VARCHAR2
AS
  idx Number;
  names DBMS_SQL.VARCHAR2S;
BEGIN
	names(1) := 'Jim';
	names(2) := 'Bill';
	names(3) := 'Paul';
	names(4) := 'David';
	names(5) := 'Mark';
	names(6) := 'Jacob';
	names(7) := 'Mason';
	names(8) := 'William';
	names(9) := 'Jayden';
	names(10) := 'Noah';
	names(11) := 'Michael';
	names(12) := 'Ethan';
	names(13) := 'Alexander';
	names(14) := 'Aiden';
	names(15) := 'Daniel';
	names(16) := 'Anthony';
	names(17) := 'Matthew';
	names(18) := 'Elijah';
	names(19) := 'Joshua';
	names(20) := 'Andrew';

   idx := DBMS_RANDOM.VALUE(1,20);	
   RETURN names(idx);
END;
/

DECLARE
	lastNames DBMS_SQL.VARCHAR2S;
      	names DBMS_SQL.VARCHAR2S;
        idx INTEGER;
        firstNamev VARCHAR2(16);
        lastNamev VARCHAR2(16);
        userId INTEGER;
        emailv VARCHAR2(64);
        phonev VARCHAR2(12);
        nphone INTEGER;

        v_createdAt  DATE;


BEGIN


DBMS_OUTPUT.PUT_LINE('===============================================================');
	names(1) := 'Jim';
	names(2) := 'Bill';
	names(3) := 'Paul';
	names(4) := 'David';
	names(5) := 'Mark';
	names(6) := 'Jacob';
	names(7) := 'Mason';
	names(8) := 'William';
	names(9) := 'Jayden';
	names(10) := 'Noah';
	names(11) := 'Michael';
	names(12) := 'Ethan';
	names(13) := 'Alexander';
	names(14) := 'Aiden';
	names(15) := 'Daniel';
	names(16) := 'Anthony';
	names(17) := 'Matthew';
	names(18) := 'Elijah';
	names(19) := 'Joshua';
	names(20) := 'Andrew';

	lastNames(1) := 'Scott';
	lastNames(2) := 'Gates';
	lastNames(3) := 'Smith';
	lastNames(4) := 'Miller';
	lastNames(5) := 'Clark';
	lastNames(6) := 'Taylor';
	lastNames(7) := 'Anderson';
	lastNames(8) := 'Thomas';
	lastNames(9) := 'Jeckson';

	lastNames(10) := 'Harris'; 
	lastNames(11) := 'Martin'; 
	lastNames(12) := 'Thompson';
	lastNames(13) := 'Garcia'; 
	lastNames(14) := 'Martinez'; 
	lastNames(15) := 'Robinson'; 

	lastNames(16) := 'Rodriguez'; 
	lastNames(17) := 'Lewis';  
	lastNames(18) := 'Lee';  
	lastNames(19) := 'Walker';  
	lastNames(20) := 'Hall';  
	FOR n IN 1..25 LOOP
		FOR i IN 1..20 LOOP
			FOR j IN 1..10 LOOP
			    idx := DBMS_RANDOM.VALUE(1,20);	
			    userId := DBMS_RANDOM.VALUE(1,10);	
			    userId := userId * 10;	
			    nphone := DBMS_RANDOM.VALUE(222222,90000000);			   
	                    phonev := to_char(nphone);
	                    lastNamev := lastNames(i) ;
			    firstNamev	:=  getRandomName;
		            emailv := firstNamev || n ||  '.' || lastNamev || i || j || '@gmail.com';		    	
--		    DBMS_OUTPUT.PUT_LINE('Array element' ||  i || ',' ||  j   ||  ' = [' || lastNames(i)  ||  ']'   ||  '  NAME  ' || getRandomName || ' else=' || names(idx)  || '; userId=' || userId);
--		    DBMS_OUTPUT.PUT_LINE('EMAIL = ' ||  emailv  || ' ; phone = ' || phonev || '; j=' || j || '; i=' || i );

             		   v_createdAt := getRandomDate( TO_DATE('20110701','YYYYMMDD'), TO_DATE('20120801','YYYYMMDD'));

			    INSERT INTO cdms_Customers (id,firstName,lastName,email,phone,createdAt,createdBy) VALUES (
   			             cdms_Customers_seq.nextval,firstNamev,lastNamev,LOWER(emailv),phonev, v_createdAt,userId
	        	     );
	                END LOOP;     
		END LOOP;     
	END LOOP;     


	COMMIT;
END;
/
DROP FUNCTION getRandomName;

DISCONNECT;
