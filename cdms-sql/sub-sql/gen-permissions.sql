SET SERVEROUTPUT ON;

DROP TABLE cdms_Permissions;
DROP SEQUENCE cdms_Permissions_seq; 

CREATE TABLE cdms_Permissions (
    id NUMBER PRIMARY KEY, 		-- must be incremented by cdms_Users_seq
    version NUMBER DEFAULT 0,
    permission VARCHAR2(16) NOT NULL,
    userId NUMBER,
    CONSTRAINT FK_ON_USERS
	FOREIGN KEY (userId) 
	REFERENCES cdms_Users(id)
);

CREATE SEQUENCE cdms_Permissions_seq 
    MINVALUE 10
    START WITH 10
    INCREMENT BY 10
    CACHE 50;

--
-- users with id =10 permission: 'edit' and 'view statistics' userName = ALL, psw=all
--
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'edit',10		
	);
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'view statistics',10		
	);
--
-- users with id =20 permission: 'view' and 'view statistics' userName = VS, psw=vs
--
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'view',20		
	);
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'view statistics',20		
	);
--
-- users with id =30 permission: 'edit' cannot  'view statistics'  userName = E, psw=e
--
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'edit',30		
	);
--
-- users with id =40 permission: 'view' cannot  'view statistics'  userName = V, psw=v
--
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'view',40		
	);
--
-- users with id =50 permission: 'view statistics'  Can onle view statistice. Cannot  'view or edit'' Customer and Invoice.  userName = S, psw=s
--
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'view statistics',50		
	);

--
-- users with id in (60) permission: 'view'
--
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'view',60		
	);

--
-- users with id in (100) permission: 'edit'
--
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'edit',100		
	);
--
-- users with id in (70,80,90) permission: 'view statistics'
--
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'view statistics',70		
	);
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'view statistics',80		
	);
INSERT INTO cdms_Permissions (id,permission,userId) VALUES (
		cdms_Permissions_seq.nextval,'view statistics',90		
	);

--
-- Print all permissions
--
COLUMN id HEADING 'id';
COLUMN permission HEADING 'Permission';
COLUMN userId  HEADING 'User Id';
SELECT userId,id, permission FROM cdms_Permissions ORDER BY userId;
