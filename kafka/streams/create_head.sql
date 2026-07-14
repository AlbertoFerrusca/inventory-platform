CREATE STREAM orders_stream_raw(
payload STRUCT<
    after STRUCT<
      orderid STRING,
      employid STRING,
      clienteid STRING,
      estatus STRING,
      total DOUBLE,
      fecha STRING,
      external_id STRING
    >
  >
)
WITH (
  KAFKA_TOPIC='headproduct-topic-JAL',
  VALUE_FORMAT='JSON'
);


CREATE STREAM orders_stream AS
SELECT
  payload->after->orderid      AS orderid,
  payload->after->employid     AS employid,
  payload->after->clienteid    AS cliente_id,
  payload->after->estatus      AS estatus,
  payload->after->total        AS total,
  payload->after->fecha        AS fecha,
  payload->after->external_id  AS external_id
FROM orders_stream_raw
WHERE payload->after IS NOT NULL;

CREATE TABLE total_by_cliente_mes 
WITH (
  KEY_FORMAT='JSON'
)
AS
SELECT
  cliente_id,
  WINDOWSTART AS inicio_mes,
  SUM(total) AS total_mes
FROM orders_stream
WINDOW TUMBLING (SIZE 30 DAYS)
GROUP BY cliente_id
EMIT CHANGES;

CREATE TABLE total_by_cliente_mes_simple 
WITH (
  KEY_FORMAT='JSON'
)
AS
SELECT
  SUBSTRING(fecha, 1, 7) AS mes,
  cliente_id,
  SUM(total) AS total_mes
FROM orders_stream
GROUP BY SUBSTRING(fecha, 1, 7), cliente_id
EMIT CHANGES;

CREATE TABLE total_by_employee 
WITH (
  KEY_FORMAT='JSON'
)
AS
SELECT
  employid,
  SUM(total) AS total_vendido
FROM orders_stream
GROUP BY employid
EMIT CHANGES;

CREATE TABLE total_by_cliente_employee 
WITH (
  KEY_FORMAT='JSON'
)
AS
SELECT
  cliente_id,
  employid,
  SUM(total) AS total
FROM orders_stream
GROUP BY cliente_id, employid
EMIT CHANGES;

CREATE TABLE total_by_employee_mes 
WITH (
  KEY_FORMAT='JSON'
)
AS
SELECT
  SUBSTRING(fecha, 1, 7) AS mes,
  employid,
  SUM(total) AS total,
  COUNT(*) AS total_ordenes
FROM orders_stream
GROUP BY SUBSTRING(fecha, 1, 7), employid
EMIT CHANGES;


/*No iria */

CREATE TABLE orders_count AS
SELECT 'TOTAL' AS k,
       COUNT(*) AS total
FROM orders_stream
GROUP BY 'TOTAL' EMIT CHANGES;