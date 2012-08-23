CONNECT / AS SYSDBA;


ALTER USER hr IDENTIFIED BY hr ACCOUNT UNLOCK;
DISCONNECT;


CONNECT hr/hr;

SET SERVEROUTPUT ON;  
DROP FUNCTION getRandomDate;


CREATE FUNCTION getRandomDate(pDate IN DATE) RETURN DATE 
AS
  v_randomDate DATE;
  v_startNumber  NUMBER;
  v_endNumber    NUMBER;
  v_endDate DATE;  
BEGIN
        v_endDate := TO_DATE('20120801','YYYYMMDD');

        --  Convert dates to Julian date numbers
        
        v_startNumber := TO_NUMBER(TO_CHAR(pDate, 'J'));
        v_endNumber := TO_NUMBER(TO_CHAR(v_endDate, 'J'));

 

        -- 2. Using the DBMS_RANDOM function to get the random date

        v_randomDate := TO_DATE(TRUNC(DBMS_RANDOM.VALUE(v_startNumber, v_endNumber)), 'J');
        -- Test output
        dbms_output.put_line(   'Random date between '
                                || TO_CHAR(pDate, 'DD.MM.YYYY')
                                || ' and '
                                || TO_CHAR(v_endDate, 'DD.MM.YYYY')
                                || ' is: '
                                || TO_CHAR(v_randomDate, 'DD.MM.YYYY') );
 
        RETURN v_randomDate;
END;
/

DECLARE
  v_d DATE;
BEGIN
    v_d := getRandomDate(TO_DATE('20120107','YYYYMMDD'));
END;
/
DROP FUNCTION getRandomDate;

DISCONNECT;

