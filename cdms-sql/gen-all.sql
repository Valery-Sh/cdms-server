CONNECT / AS SYSDBA;
ALTER USER hr IDENTIFIED BY hr ACCOUNT UNLOCK;
DISCONNECT;
CONNECT hr/hr;


@sub-sql/drop-all;
@sub-sql/create-fun-getRandomDate

@sub-sql/gen-10-users;
@sub-sql/gen-permissions.sql 

@sub-sql/gen-500000-items;
@sub-sql/gen-5000-customers;
@sub-sql/gen-50000-invoices;
@sub-sql/gen-invoiceItems;

DROP FUNCTION getRandomDate

DISCONNECT