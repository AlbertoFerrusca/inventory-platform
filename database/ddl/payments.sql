
CREATE TABLE IF NOT EXISTS public.payments
(
    payment_id uuid NOT NULL DEFAULT gen_random_uuid(),
    order_id uuid NOT NULL,
    amount numeric(12,2) NOT NULL,
    currency character varying(10) COLLATE pg_catalog."default" DEFAULT 'MXN'::character varying,
    status character varying(20) COLLATE pg_catalog."default" NOT NULL,
    payment_method character varying(50) COLLATE pg_catalog."default",
    branch_id character varying(50) COLLATE pg_catalog."default" NOT NULL DEFAULT 'JAL001'::character varying,
    estado character varying(50) COLLATE pg_catalog."default",
    bank_folio character varying(20) COLLATE pg_catalog."default",
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    deleted boolean DEFAULT false,
    installments integer DEFAULT 1,
    CONSTRAINT payments_pkey PRIMARY KEY (payment_id),
    CONSTRAINT uq_payment_order UNIQUE (order_id),
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id)
        REFERENCES public.headproduct (orderid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.payments
    OWNER to postgres;