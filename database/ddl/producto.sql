CREATE TABLE IF NOT EXISTS public.producto
(
    id integer NOT NULL DEFAULT nextval('producto_id_seq'::regclass),
    productid character varying(10) COLLATE pg_catalog."default" NOT NULL,
    ubicacion character varying(10) COLLATE pg_catalog."default",
    sku character varying(10) COLLATE pg_catalog."default",
    cantidad double precision,
    precio numeric(10,2),
    fechamodi timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT producto_pkey PRIMARY KEY (id),
    CONSTRAINT uk_producto_productid UNIQUE (productid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.producto
    OWNER to postgres;