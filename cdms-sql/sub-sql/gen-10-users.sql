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


-- 1). can view, edit and view statistics

INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Bill','Gates','ALL','all'		
	);
-- 2). can only view, and view statistics. Cannot edit Customers and Invoices

INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Ann','Brown','VS','vs'		
	);

-- 3). can edit, and cannot view statistics. 

INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Jone','Smith','E','e'		
	);
-- 4). can view and cannot view statistics. Cannot edit .

INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Jone','Smith','V','v'		
	);

-- 5). can only  view statistics. Cannot view or edit  Customer and Invoice.

INSERT INTO cdms_Users (id,firstName,lastName,userName,password) VALUES (
		cdms_Users_seq.nextval,'Paul','Miller','S','s'		
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



