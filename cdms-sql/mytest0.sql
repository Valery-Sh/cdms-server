connect cdms_user/user;

set serveroutput on;
begin
  dbms_output.put_line('Hello, Valery');
end;
/
drop table cdms_user.mytesttable0;
drop sequence cdms_user.mytesttable0_seq;

create table cdms_user.mytesttable0 (
  id number primary key,
  lastName varchar2(16)
);
/*
CREATE SEQUENCE mytesttable0_seq
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1
    CACHE 20;
insert into mytesttable0 (id,lastName) values (mytesttable0_seq.nextval,'Sirotkina');
select * from mytesttable0;
describe mytesttable0;
column lastName heading 'SURNAME';
select lastname from mytesttable0;
*/
disconnect;
