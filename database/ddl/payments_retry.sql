CREATE TABLE IF NOT EXISTS public.payments_retry
(
    id bigint NOT NULL DEFAULT nextval('payments_retry_id_seq'::regclass),
    order_id uuid NOT NULL,
    amount numeric(18,2),
    status character varying(50) COLLATE pg_catalog."default",
    payment_method character varying(50) COLLATE pg_catalog."default",
    installments integer,
    estado character varying(50) COLLATE pg_catalog."default",
    bank_folio character varying(100) COLLATE pg_catalog."default",
    error_message text COLLATE pg_catalog."default",
    retry_count integer DEFAULT 0,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT payments_retry_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.payments_retry
    OWNER to postgres;