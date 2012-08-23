CONNECT / AS SYSDBA;


ALTER USER hr IDENTIFIED BY hr ACCOUNT UNLOCK;
DISCONNECT;


CONNECT hr/hr;

SET SERVEROUTPUT ON;  
--
-- First drop lnked by foreign key table
--
DROP TABLE cdms_Items;
DROP SEQUENCE cdms_Items_seq; 

DROP FUNCTION getRandomItemName;
DROP FUNCTION getRandomPrice;

CREATE TABLE cdms_Items (
    id NUMBER PRIMARY KEY, 		-- must be incremented by cdms_Items_seq
    version NUMBER DEFAULT 0,	
    price NUMBER(15,2)  NOT NULL ,                    -- may be 15.4 ?
    barcode VARCHAR2(12) NOT NULL,
    itemName VARCHAR2(16) NOT NULL,
   createdAt DATE DEFAULT SYSDATE NOT NULL,
   createdBy  NUMBER  NOT NULL,                          -- The id of the user who inserted the record
   CONSTRAINT itemName_unique UNIQUE (itemName),
   CONSTRAINT barCode_unique UNIQUE (barCode),
   CONSTRAINT FK_ITEMS_ON_USERS
	FOREIGN KEY (createdBy) 
	REFERENCES cdms_Users(id)
);

CREATE SEQUENCE cdms_Items_seq 
    MINVALUE 10
    START WITH 10
    INCREMENT BY 10
    CACHE 50;


CREATE FUNCTION getRandomItemName
RETURN VARCHAR2
AS
  idx Number;
  s2 VARCHAR2(2);
  itemNames DBMS_SQL.VARCHAR2S;
BEGIN
	itemNames(1) := 'TV';
	itemNames(2) := 'Camera';
	itemNames(3) := 'Phone';
	itemNames(4) := 'Player';
	itemNames(5) := 'Lamp';
	itemNames(6) := 'iPad';
	itemNames(7) := 'iPhone';
	itemNames(8) := 'Kettle';
	itemNames(9) := 'Iron';
	itemNames(10) := 'Mixer';
	itemNames(11) := 'Toster';
	itemNames(12) := 'Pen';

	idx := DBMS_RANDOM.VALUE(1,12);	
  	s2 := DBMS_RANDOM.STRING('U', 2);

   RETURN itemNames(idx) || '-' || s2 || '-';
END;
/

CREATE FUNCTION getRandomPrice
RETURN NUMBER
AS
BEGIN
   RETURN TRUNC(DBMS_RANDOM.VALUE(50,250),2) ;
END;
/


--------------  GENERATE  5000000 ITEMS  (createdDate in [2001-01-01,2011-06-30']  ---------------------------


DECLARE
	
	v_ItemName VARCHAR2(16);
	userId INTEGER;
        v_barcode VARCHAR2(12);
        v_commit_counter NUMBER;
        v_committed NUMBER;
        v_createdAt  DATE;
BEGIN
	DBMS_OUTPUT.PUT_LINE('--------------  GENERATE  500,000 ITEMS ---------------------------');
	v_commit_counter := 0;
	v_committed := 0;
	FOR i IN 1..500000 LOOP
		v_commit_counter := v_commit_counter + 1;
		v_committed := v_committed + 1;
		IF   v_commit_counter = 10000 THEN
			COMMIT;
			v_commit_counter := 0;
			DBMS_OUTPUT.PUT_LINE('GEN 500,000 ITEMS COMMITTED:  ' ||  v_committed );

		END IF;
                v_itemName := getRandomItemName || i;
  	        userId := TRUNC(DBMS_RANDOM.VALUE(1,10));	
		userId := userId * 10;
		v_barcode :=  TO_CHAR( TRUNC(DBMS_RANDOM.VALUE(127493, 999999)) ) || LPAD(TO_CHAR(i),6);

		v_createdAt := getRandomDate(TO_DATE('20110101','YYYYMMDD'),TO_DATE('20110630','YYYYMMDD'));

		INSERT INTO cdms_Items (id, price,barcode,itemName,
								   createdAt,createdBy ) VALUES (
				cdms_Items_seq.nextval,
			        getRandomPrice, v_barcode,v_itemName,v_createdAt ,userId		
	        );
		
	END LOOP;     
	COMMIT;

END;
/

DROP FUNCTION getRandomItemName;
DROP FUNCTION getRandomPrice;

DISCONNECT;

