CREATE STREAM order_items_raw (
payload STRUCT<
    after STRUCT<
     orderid VARCHAR,
     productid VARCHAR,
     quantity DOUBLE,
     price DOUBLE
   >
> 
)
WITH (
  KAFKA_TOPIC='detailproduct-topic-JAL',
  VALUE_FORMAT='JSON'
);



CREATE STREAM detail_stream AS
SELECT
  payload->after->orderid   AS orderid,
  payload->after->productid AS productoId,
  payload->after->quantity  AS cantidad,
  payload->after->price     AS precio
FROM order_items_raw
WHERE payload->after IS NOT NULL;



CREATE TABLE top_products 
WITH (
  KEY_FORMAT='JSON'
)
AS
SELECT
  productoid,
  SUM(cantidad * precio) AS total_vendido,
  SUM(cantidad) AS productos
FROM detail_stream
GROUP BY productoId
EMIT CHANGES;



CREATE TABLE product_totals 
WITH (
  KEY_FORMAT='JSON'
)
AS
SELECT
  productoId,
  SUM(cantidad) AS productos,
  SUM(cantidad * precio) AS total
FROM detail_stream
GROUP BY productoId
EMIT CHANGES;



/*CREATE STREAM detail_with_head AS
SELECT
  d.productoId,
  d.cantidad,
  d.precio,
  o.fecha,
  o.cliente_id,
  o.estatus
FROM detail_stream d
JOIN orders_stream o
  WITHIN 1 HOUR
  ON d.orderid = o.orderid
EMIT CHANGES;
*/

CREATE STREAM detail_with_head AS
SELECT
  d.orderid,
  d.productoId,
  d.cantidad,
  d.precio,
  o.fecha,
  o.cliente_id,
  o.estatus
FROM detail_stream d
JOIN orders_stream o
  WITHIN 1 HOUR
  ON d.orderid = o.orderid
EMIT CHANGES;



CREATE TABLE product_by_day 
WITH (
  KEY_FORMAT='JSON'
) as 
SELECT
  fecha,
  productoId,
  SUM(cantidad) AS productos,
  SUM(cantidad * precio) AS total
FROM detail_with_head
GROUP BY fecha, productoId
EMIT CHANGES;




