CREATE STREAM payments_raw (
    payload STRUCT<    
    before STRUCT<       
    order_id VARCHAR    
    >,   
     after STRUCT<
           payment_id VARCHAR,
           order_id VARCHAR,
           amount DOUBLE,
           status VARCHAR,
           payment_method VARCHAR,
           branch_id VARCHAR,
           estado VARCHAR,      
           installments INTEGER   
           >  
        >
) WITH (
  KAFKA_TOPIC='payments-topic-JAL',
  VALUE_FORMAT='JSON');

CREATE STREAM payments_stream AS 
SELECT  
  payload->after->order_id      AS order_id,
  payload->after->status        AS status,
  payload->after->payment_method AS payment_method,
  payload->after->installments  AS installments,  
  payload->after->estado        AS estado,
  payload->after->branch_id     AS branch_id, 
  payload->after->amount     AS amount  
  FROM payments_raw 
  EMIT CHANGES;




CREATE TABLE PAYMENTS_CASH_BY_ESTADO  
WITH (KEY_FORMAT='JSON')
AS 
SELECT   ESTADO,   
    SUM(AMOUNT) as TOTAL_AMOUNT 
FROM PAYMENTS_STREAM 
WHERE (PAYMENT_METHOD = 'CASH') 
GROUP by ESTADO 
EMIT CHANGES;

CREATE TABLE PAYMENTS_CASH_BY_ESTADO_windows
WITH (KEY_FORMAT='JSON') as
SELECT 
  estado,
  SUM(amount) AS total_amount
FROM payments_stream
WINDOW SESSION (30 SECONDS)
WHERE payment_method = 'CASH'
GROUP BY estado
EMIT CHANGES;


CREATE TABLE PAYMENTS_CASH_BY_BRANCH
 WITH (KEY_FORMAT='JSON') AS 
SELECT   ESTADO, 
        BRANCH_ID,   
       SUM(AMOUNT) TOTAL_AMOUNT
FROM PAYMENTS_STREAM 
WHERE (PAYMENT_METHOD = 'CASH') 
GROUP by ESTADO, BRANCH_ID 
EMIT CHANGES;

CREATE TABLE payments_cash_by_branch_windows 
WITH (KEY_FORMAT='JSON') AS
SELECT 
  estado,
  branch_id,
  SUM(amount) AS total_amount
FROM payments_stream
WINDOW SESSION (30 SECONDS)
WHERE payment_method = 'CASH'
GROUP BY estado, branch_id
EMIT CHANGES;


CREATE TABLE PAYMENTS_CREDIT_BY_ESTADO WITH (KEY_FORMAT='JSON') 
AS 
SELECT    ESTADO,  
INSTALLMENTS,   
SUM(AMOUNT) TOTAL_AMOUNT 
FROM PAYMENTS_STREAM 
WHERE (PAYMENT_METHOD = 'CARD') 
GROUP BY ESTADO,INSTALLMENTS 
EMIT CHANGES;


CREATE TABLE payments_credit_by_estado_windows 
WITH (KEY_FORMAT='JSON') AS
SELECT 
  estado,
  installments,
  SUM(amount) AS total_amount
FROM payments_stream
WINDOW SESSION (30 SECONDS)
WHERE payment_method = 'CARD'
GROUP BY estado,installments
EMIT CHANGES;


CREATE TABLE PAYMENTS_CREDIT_BY_BRANCH WITH (KEY_FORMAT='JSON') 
AS 
SELECT   ESTADO,  
BRANCH_ID,  
INSTALLMENTS,   
SUM(AMOUNT) TOTAL_AMOUNT 
FROM PAYMENTS_STREAM 
WHERE (PAYMENT_METHOD = 'CARD') 
GROUP BY ESTADO, BRANCH_ID,INSTALLMENTS 
EMIT CHANGES;


CREATE TABLE payments_credit_by_branch_windows 
WITH (KEY_FORMAT='JSON') AS
SELECT
  estado,
  branch_id,
  installments,
  SUM(amount) AS total_amount
FROM payments_stream
WINDOW SESSION (30 SECONDS)
WHERE payment_method = 'CARD'
GROUP BY estado, branch_id, installments
EMIT CHANGES;


