SET SERVEROUTPUT ON;
CONNECT hr/hr;

DROP TABLE cdms_Permissions;
DROP SEQUENCE cdms_Permissions_seq; 

CREATE TABLE cdms_Permissions (
    id NUMBER PRIMARY KEY, 		-- must be incremented by cdms_Users_seq
    version NUMBER DEFAULT 0,
    permissions VARCHAR2(16) NOT NULL,
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
-- users with id in (1,3,6) permission: 'view'
--
INSERT INTO cdms_Permissions (id,permissions,userId) VALUES (
		cdms_Permissions_seq.nextval,'view',10		
	);
INSERT INTO cdms_Permissions (id,permissions,userId) VALUES (
		cdms_Permissions_seq.nextval,'view',30		
	);
INSERT INTO cdms_Permissions (id,permissions,userId) VALUES (
		cdms_Permissions_seq.nextval,'view',60		
	);

--
-- users with id in (2,4,10) permission: 'edit'
--
INSERT INTO cdms_Permissions (id,permissions,userId) VALUES (
		cdms_Permissions_seq.nextval,'edit',20	
	);
INSERT INTO cdms_Permissions (id,permissions,userId) VALUES (
		cdms_Permissions_seq.nextval,'edit',40	
	);
INSERT INTO cdms_Permissions (id,permissions,userId) VALUES (
		cdms_Permissions_seq.nextval,'edit',100		
	);

--
-- users with id in (5,7,8,9) permission: 'view statistics'
--
INSERT INTO cdms_Permissions (id,permissions,userId) VALUES (
		cdms_Permissions_seq.nextval,'view statistics',50		
	);
INSERT INTO cdms_Permissions (id,permissions,userId) VALUES (
		cdms_Permissions_seq.nextval,'view statistics',70		
	);
INSERT INTO cdms_Permissions (id,permissions,userId) VALUES (
		cdms_Permissions_seq.nextval,'view statistics',80		
	);
INSERT INTO cdms_Permissions (id,permissions,userId) VALUES (
		cdms_Permissions_seq.nextval,'view statistics',90		
	);

--
-- Now Print all permissions
--
COLUMN id HEADING 'id';
COLUMN permissions HEADING 'Permissions';
COLUMN userId  HEADING 'User Id';
SELECT userId,id, permissions FROM cdms_Permissions ORDER BY userId;

DISCONNECT;