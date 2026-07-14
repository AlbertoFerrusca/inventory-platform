CREATE TABLE IF NOT EXISTS public.headproduct
(
    employid character varying(10) COLLATE pg_catalog."default" NOT NULL,
    fecha timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estatus character varying(10) COLLATE pg_catalog."default",
    clienteid character varying(10) COLLATE pg_catalog."default",
    total double precision,
    fechacambio timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    external_id character varying(50) COLLATE pg_catalog."default",
    orderid uuid NOT NULL DEFAULT gen_random_uuid(),
    CONSTRAINT headproduct_pkey PRIMARY KEY (orderid)
)