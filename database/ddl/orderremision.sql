
CREATE TABLE IF NOT EXISTS public.orderremision
(
    id integer NOT NULL DEFAULT nextval('orderremision_id_seq'::regclass),
    productid character varying(10) COLLATE pg_catalog."default" NOT NULL,
    cantidad numeric(10,2),
    precio numeric(10,2),
    estatus character varying(20) COLLATE pg_catalog."default" NOT NULL DEFAULT 'ACTIVO'::character varying,
    fechai timestamp without time zone,
    fechaf timestamp without time zone,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    orderid uuid NOT NULL,
    CONSTRAINT pk_orderremision PRIMARY KEY (orderid, productid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.orderremision
    OWNER to postgres;