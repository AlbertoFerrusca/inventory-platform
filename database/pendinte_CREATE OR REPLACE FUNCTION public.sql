CREATE OR REPLACE FUNCTION public.fn_restock_by_location(
	p_location character varying,
	p_batch_size integer)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN

DROP TABLE IF EXISTS tmp_pendientes;

CREATE TEMP TABLE tmp_pendientes ON COMMIT DROP as Select id,productid,ubicacion,cantidad,precio
                                      FROM productos_kafka_test
									  Where ubicacion=p_location and procesado=false
									  ORDER BY id
									  LIMIT p_batch_size; 

IF not EXISTS (
	SElect 1 from tmp_pendientes )
then return ;
end if;	 

INSERT INTO producto (productid, cantidad, precio)
   SELECT productid,cantidad,precio
   FROM tmp_pendientes
	ON CONFLICT (productid)
	DO UPDATE SET 
	      cantidad = producto.cantidad + EXCLUDED.cantidad,
		  precio = EXCLUDED.precio;

    UPDATE productos_kafka_test
    SET procesado = true,
       fecha_procesado = now()
        WHERE id in (SElect id from tmp_pendientes);
		
     


END;
$BODY$;


ALTER FUNCTION public.fn_restock_by_location(character varying)
    OWNER TO postgres;