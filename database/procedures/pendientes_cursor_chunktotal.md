CREATE OR REPLACE PROCEDURE public.pendientes_cursor_chunktotal()
 LANGUAGE plpgsql
AS $procedure$
DECLARE
    v_orderid UUID;
    rec RECORD;

    v_ids UUID[];   -- 🔥 arreglo de órdenes (chunk)
BEGIN

    LOOP
        -- 🔥 1. TOMAR 1000 ÓRDENES
        SELECT ARRAY(
            SELECT h.orderid
            FROM headproduct h
            inner join payments p on 
            p.order_id=h.orderid
            WHERE p.status ='PENDING'
			FOR UPDATE OF h SKIP LOCKED
            LIMIT 1000
        )
        INTO v_ids;

        -- salir si ya no hay registros
        IF COALESCE(array_length(v_ids, 1),0) = 0 THEN
            EXIT;
        END IF;

        -- 🔥 2. PROCESAR ORDEN POR ORDEN (CONTROL FINO)
        FOREACH v_orderid IN ARRAY v_ids
        LOOP
            BEGIN
			    --RAISE NOTICE 'order  %',v_orderid;      
                -- 🔹 procesar detalles de la orden
                FOR rec IN
                    SELECT d.productid, d.quantity, d.price
                    FROM detailproduct d
                    WHERE d.orderid = v_orderid
                LOOP

                    UPDATE producto p
                    SET cantidad = p.cantidad - rec.quantity
                    WHERE p.productid = rec.productid
                      AND p.cantidad >= rec.quantity;

                    IF NOT FOUND THEN
                        -- ❌ sin stock
                        INSERT INTO public.orderremision (
                            orderid,
                            productid,
                            cantidad,
                            precio,
                            estatus
                        )
                        VALUES (
                            v_orderid,
                            rec.productid,
                            rec.quantity,
                            rec.price,
                            'PENDIENTE'
                        )
                        ON CONFLICT (orderid, productid) DO NOTHING;
                    END IF;

                END LOOP; --  Eata mal //vericar 

                -- 🔹 determinar estatus final
                IF EXISTS (
                    SELECT 1
                    FROM orderremision r
                    WHERE r.orderid = v_orderid
                ) THEN

                    UPDATE headproduct
                    SET estatus = 'REMISION'
                    WHERE orderid = v_orderid;


                   UPDATE payments
                   set  status='REMISON/ERROR'
                   where order_id=v_orderid;

                ELSE

                    UPDATE headproduct
                    SET estatus = 'COMPLETADO'
                    WHERE orderid = v_orderid;

                   UPDATE payments
                   set  status='PROCESADO'
                   where order_id=v_orderid;

                END IF;


            

                
                EXCEPTION WHEN OTHERS THEN
                -- 🔥 manejo de error por orden
                UPDATE headproduct
                SET estatus = 'ERROR'
                WHERE orderid = v_orderid;
                RAISE NOTICE 'Error en orden %', v_orderid;

            END;
           END LOOP;
    END LOOP;

END;
$procedure$
