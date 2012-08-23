
CONNECT / AS SYSDBA;
ALTER USER hr IDENTIFIED BY hr ACCOUNT UNLOCK;
DISCONNECT;


CONNECT hr/hr;

SET SERVEROUTPUT ON;

--
-- First drop lnked by foreign key table
--
DROP TABLE cdms_Permissions;
DROP SEQUENCE cdms_Permissions_seq; 

DROP TABLE cdms_Users;
DROP SEQUENCE cdms_Users_seq; 

CREATE TABLE cdms_Users (
    id NUMBER PRIMARY KEY, 		-- must be incremented by cdms_Users_seq
    version NUMBER DEFAULT 0,	
    firstName VARCHAR2(16) NOT NULL,
    lastName  VARCHAR2(16) NOT NULL,
    userName  VARCHAR2(16) NOT NULL,
    password  VARCHAR2(16) NOT NULL
);

CREATE SEQUENCE cdms_Users_seq 
    MINVALUE 10
    START WITH 10
    INCREMENT BY 10
    CACHE 50;


INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Bill','Gates','MSWIN','msbg'		
	);
INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Ann','Brown','Ann B.','abpsw'		
	);
INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Jone','Smith','J.S.','jspsw'		
	);
INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Jone','Smith','J.S.2','js2psw'		
	);
INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Paul','Miller','P.M.','pmpsw'		
	);
INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Kat','Thomas','K.T.','ktpsw'		
	);
INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Ann','Tailor','A.T.','atpsw'		
	);
INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Ann','Tailor','A.T.','atpsw'		
	);
INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Fill','Tailor','F.T.','ftpsw'		
	);
INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Roy','Harris','R.H.','rhpsw'		
	);

-- 
-- Now Print all users
--
COLUMN id HEADING 'id';
COLUMN version HEADING 'V.M';
COLUMN firstName HEADING 'First Name';
COLUMN lastName  HEADING 'Last Name';
COLUMN userName  HEADING 'User Name';
COLUMN password  HEADING 'PSW';
SELECT id,firstName,LastName,userName,password FROM cdms_Users ORDER BY firstName,lastName;

DISCONNECT;

--
-- Create user permissions
--
@create-permissions.sql  -- invoke script to create permissions

