CREATE OR REPLACE FUNCTION public.fn_delete_head(
	porderid uuid)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN

    -- 🔹 borrar detalles solo si el head está en PENDING
    DELETE FROM public.detailproduct d
    WHERE d.orderid = porderid
      AND EXISTS (
          SELECT 1
          FROM public.headproduct h
          WHERE h.orderid = d.orderid
            AND h.estatus = 'DRAFTED'
      );

    -- 🔹 borrar encabezado solo si ya no hay detalles
    DELETE FROM public.headproduct
    WHERE orderid = porderid
      AND estatus = 'DRAFTED'
      AND NOT EXISTS (
          SELECT 1
          FROM public.detailproduct
          WHERE orderid = porderid
      );

    -- 🔹 validar si se borró el head
    IF NOT FOUND THEN
        RETURN FALSE;
    END IF;

    RETURN TRUE;

END;
$BODY$;

ALTER FUNCTION public.fn_delete_head(uuid)
    OWNER TO postgres;

