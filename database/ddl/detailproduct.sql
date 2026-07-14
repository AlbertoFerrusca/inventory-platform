CREATE TABLE IF NOT EXISTS public.detailproduct
(
    orderid uuid NOT NULL,
    productid character varying(10) COLLATE pg_catalog."default" NOT NULL,
    quantity double precision,
    price double precision,
    CONSTRAINT detailproduct_pkey PRIMARY KEY (orderid, productid),
    CONSTRAINT fk_order FOREIGN KEY (orderid)
        REFERENCES public.headproduct (orderid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.detailproduct
    OWNER to postgres;