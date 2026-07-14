
CREATE TABLE IF NOT EXISTS public.productos_kafka_test
(
    product_key character varying(100) COLLATE pg_catalog."default",
    productid character varying(50) COLLATE pg_catalog."default",
    ubicacion character varying(50) COLLATE pg_catalog."default",
    cantidad numeric,
    precio numeric,
    procesado boolean DEFAULT false,
    fecha_procesado timestamp without time zone
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.productos_kafka_test
    OWNER to postgres;