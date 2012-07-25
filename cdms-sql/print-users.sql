SET SERVEROUTPUT ON;

CONNECT hr/hr;

--
-- Print all users
--
COLUMN id HEADING 'user.id';
COLUMN firstName HEADING 'First Name';
COLUMN lastName  HEADING 'Last Name';
COLUMN userName  HEADING 'UserName';
COLUMN password  HEADING 'PSW';
COLUMN perm HEADING 'Perms'
COLUMN pId HEADING 'perm.Id'
--
-- Print Users only
--
SELECT u.id,u.firstName,u.lastName,u.userName,u.password FROM cdms_Users u
   ORDER BY lastName,firstName;

--
-- Print User's Permissions
--
SELECT u.id,p.Id as pId,u.firstName,u.lastName,p.permissions as perm FROM cdms_Users u
   LEFT JOIN cdms_Permissions p ON (u.id = p.userId) 
   ORDER BY lastName,firstName;

DISCONNECT;