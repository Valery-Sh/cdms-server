CONNECT / AS SYSDBA;


ALTER USER hr IDENTIFIED BY hr ACCOUNT UNLOCK;
DISCONNECT;


CONNECT hr/hr;

SET SERVEROUTPUT ON;  

DROP FUNCTION getRandomDate
-----------------------------------------------------------------------------------------------------------
-- The FUNCTION getRandomDate:  Genereates a RANDOM DATE that is greater or equal to the given parameter
-- and less or equal to 2012-08-01
-----------------------------------------------------------------------------------------------------------

CREATE FUNCTION getRandomDate(pStartDate IN DATE,pEndDate IN DATE) RETURN DATE 
AS
  v_randomDate DATE;
  v_startNumber  NUMBER;
  v_endNumber    NUMBER;

BEGIN

        --  Convert dates to Julian date numbers
        
        v_startNumber := TO_NUMBER(TO_CHAR(pStartDate, 'J'));
        v_endNumber := TO_NUMBER(TO_CHAR(pEndDate, 'J'));

 

        -- 2. Using the DBMS_RANDOM function to get the random date

        v_randomDate := TO_DATE(TRUNC(DBMS_RANDOM.VALUE(v_startNumber, v_endNumber)), 'J');
        -- Test output
       --    dbms_output.put_line(   'Random date between '   || TO_CHAR(pStartDate, 'DD.MM.YYYY')    || ' and '   || TO_CHAR(pEndDate, 'DD.MM.YYYY')    || ' is: '   || TO_CHAR(v_randomDate, 'DD.MM.YYYY') );
 
        RETURN v_randomDate;
END;
/


DISCONNECT;

