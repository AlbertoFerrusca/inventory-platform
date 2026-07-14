
CREATE OR REPLACE FUNCTION public.fn_delete_item(
	porderid uuid,
	pproductid character varying)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    numrows INT;
BEGIN

    -- 🔹 borrar detalle solo si la orden está en PENDING
    DELETE FROM public.detailproduct d
    WHERE d.orderid = porderid
      AND d.productid = pproductid
      AND EXISTS (
          SELECT 1
          FROM public.headproduct h
          WHERE h.orderid = d.orderid
            AND h.estatus = 'DRAFTED'
      );

    -- 🔹 si no borró nada
    IF NOT FOUND THEN
        RETURN FALSE;
    END IF;

    -- 🔹 revisar si ya no tiene más detalles
    SELECT COUNT(*)
    INTO numrows
    FROM public.detailproduct d
    WHERE d.orderid = porderid;

    -- 🔹 si ya no hay detalles → borrar head
    IF numrows = 0 THEN
        DELETE FROM public.headproduct
        WHERE orderid = porderid
        AND estatus = 'DRAFTED';
    END IF;

    RETURN TRUE;

END;
$BODY$;

ALTER FUNCTION public.fn_delete_item(uuid, character varying)
    OWNER TO postgres;

