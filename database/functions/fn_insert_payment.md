
CREATE OR REPLACE FUNCTION public.fn_insert_payment(
	p_order_id uuid,
	p_amount numeric,
	p_status character varying,
	p_payment_method character varying,
	p_installments integer,
	p_estado character varying,
	p_bank_folio character varying)
    RETURNS uuid
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    head_total NUMERIC;
    detail_total NUMERIC;
    v_payment_id UUID;
BEGIN

    -- ✅ Validar HEAD
    SELECT total INTO head_total
    FROM headproduct
    WHERE orderid = p_order_id;

    IF head_total IS NULL THEN
        RAISE EXCEPTION 'Order not found';
    END IF;

    -- ✅ Validar DETAIL
    SELECT COALESCE(SUM(quantity * price), 0)
    INTO detail_total
    FROM detailproduct
    WHERE orderid = p_order_id;

    IF head_total <> detail_total THEN
        RAISE EXCEPTION 
        'Mismatch HEAD vs DETAIL';
    END IF;

    -- ✅ Validar monto

RAISE NOTICE 'p_amount=%', p_amount;
RAISE NOTICE 'head_total=%', head_total;
RAISE NOTICE 'iguales=%', (p_amount = head_total);
RAISE NOTICE 'diferencia=%', (p_amount - head_total);

IF p_amount = head_total THEN
    RAISE NOTICE 'SON IGUALES';
ELSE
    RAISE NOTICE 'SON DIFERENTES';
END IF;

    IF p_amount <> head_total THEN
        RAISE EXCEPTION 'Invalid amount';
    END IF;

    -- ✅ Insert con RETURNING
    INSERT INTO payments (
        order_id,
        amount,
        status,
        payment_method,
        installments,
        estado,
        bank_folio
    )
    VALUES (
        p_order_id,
        p_amount,
        p_status,
        p_payment_method,
        p_installments,
        p_estado,
        p_bank_folio
    )
    RETURNING payment_id INTO v_payment_id;

    -- ✅ regresar ID
    RETURN v_payment_id;

END;
$BODY$;

ALTER FUNCTION public.fn_insert_payment(uuid, numeric, character varying, character varying, integer, character varying, character varying)
    OWNER TO postgres;

