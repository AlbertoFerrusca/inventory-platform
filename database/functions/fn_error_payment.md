
CREATE OR REPLACE FUNCTION public.fn_error_payment(
	porder_id uuid,
	pamount numeric,
	pstatus character varying,
	ppayment_method character varying,
	pinstallments integer,
	pestado character varying,
	pbank_folio character varying,
	perror_message text)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN

    INSERT INTO payments_retry (
        order_id,
        amount,
        status,
        payment_method,
        installments,
        estado,
        bank_folio,
        error_message
    )
    VALUES (
        porder_id,
        pamount,
        pstatus,
        ppayment_method,
        pinstallments,
        pestado,
        pbank_folio,
        perror_message
    );

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END;
$BODY$;

ALTER FUNCTION public.fn_error_payment(uuid, numeric, character varying, character varying, integer, character varying, character varying, text)
    OWNER TO postgres;
