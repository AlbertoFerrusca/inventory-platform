CREATE OR REPLACE PROCEDURE public.proc_remision_chunk(
	)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
    rec RECORD;
BEGIN

LOOP

    -- 🔹 recorrer chunk de 1000 registros pendientes
    FOR rec IN
        SELECT orderid, productid, cantidad
        FROM public.orderremision
        WHERE estatus = 'PENDING'
        FOR UPDATE SKIP LOCKED
        LIMIT 1000
    LOOP

        -- 🔹 intentar descontar inventario
        UPDATE public.producto
        SET cantidad = cantidad - rec.cantidad
        WHERE productid = rec.productid
          AND cantidad >= rec.cantidad;

        -- 🔹 si no se pudo descontar → siguiente
        IF NOT FOUND THEN
            CONTINUE;
        END IF;

        -- 🔹 marcar remisión como enviada
        UPDATE public.orderremision
        SET estatus = 'BYDELIB'
        WHERE orderid = rec.orderid
          AND productid = rec.productid;

    END LOOP;

    -- 🔹 salir si no procesó nada
    IF NOT FOUND THEN
        EXIT;
    END IF;

END LOOP;

END;
$BODY$;
ALTER PROCEDURE public.proc_remision_chunk()
    OWNER TO postgres;

