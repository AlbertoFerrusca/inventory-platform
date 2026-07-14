
CREATE OR REPLACE FUNCTION public.fn_restock_by_location(
	p_location character varying)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN

INSERT INTO producto (productid, cantidad, precio)
   SELECT productid,cantidad,precio
    FROM productos_kafka_test
    WHERE ubicacion = p_location
	
	ON CONFLICT (productid)
	DO UPDATE SET 
	      cantidad = producto.cantidad + EXCLUDED.cantidad,
		  precio = EXCLUDED.precio;
END;
$BODY$;

ALTER FUNCTION public.fn_restock_by_location(character varying)
    OWNER TO postgres;

