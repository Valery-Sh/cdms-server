-- Create a CDMS user with full rights to create objects and save data:

DROP USER cdms_user CASCADE;
DROP ROLE cdms_role;

CREATE USER cdms_user IDENTIFIED BY user;

CREATE ROLE cdms_role;

GRANT CREATE session, CREATE table, CREATE view, 
      CREATE procedure,CREATE synonym
--      ALTER table, ALTER view, ALTER procedure,ALTER synonym,
--      DROP table, DROP view, DROP procedure,DROP synonym
      TO cdms_role;

GRANT cdms_role TO cdms_user;