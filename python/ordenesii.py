import pika
import json
import psycopg2
import multiprocessing
import argparse
import os
import sys
sys.stdout.reconfigure(line_buffering=True)

# ✅ CONFIG DB
DB_CONFIG = {
    "host":os.getenv('DB_HOST_PY','localhost'),
    "database":os.getenv('DB_DATABASE','postgres'),
    "user":os.getenv('DB_USER'),
    "password":os.getenv('DB_PASSWORD')
}

# ✅ Variable global por proceso
conn = None


# ✅ Crear conexión DB
def get_db():
    return psycopg2.connect(**DB_CONFIG)


# ✅ Obtener cursor (con reconexión)
def get_cursor():
    global conn
    if conn is None or conn.closed != 0:
        print(f"[PID {os.getpid()}] 🔄 Reconectando DB...")
        conn = get_db()
    return conn.cursor()


# ✅ Guardar orden
def save_order(order):
    global conn

    if not order.get("items"):
        raise ValueError("Orden sin items")

    
    cursor=None

    if order.get("cliente") is None:
       raise ValueError("Cliente Requerido")
    if order.get("employId") is None:
        raise ValueError("Empleado Requerido")
    try:
        cursor = get_cursor()  
        print(f"[PID {os.getpid()}] Procesando orden...")

        # ✅ INSERT HEAD
        cursor.execute("""
            INSERT INTO public.headproduct(EmployId, Estatus, ClienteID, Total, external_id)
            VALUES (%s, %s, %s, %s, %s)
            RETURNING OrderID, fecha
        """, (
            order.get("employId"),
            "DRAFTED",
            order.get("cliente"),
            order.get("totalCompra", 0),
            order.get("external_id")
        ))

        result = cursor.fetchone()
        order_id = result[0]
        fecha = result[1]

        # ✅ INSERT DETAILS
        items = order.get("items", [])

        for item in items:
            cursor.execute("""
                INSERT INTO public.detailproduct(OrderId, ProductID, Quantity, Price)
                VALUES (%s, %s, %s, %s)
            """, (
                order_id,
                item.get("productoId"),
                item.get("cantidad", 0),
                item.get("precio", 0)
            ))

        # ✅ COMMIT DB
        conn.commit()

        print(f"[PID {os.getpid()}] ✅ Orden procesada {order_id}")

    except Exception as e:
        if conn and conn.closed == 0:
         conn.rollback()
        print(f"[PID {os.getpid()}] ❌ Error DB: {e}")
        raise  # 🔥 importante para que haga nack

    finally:
        if cursor:
         cursor.close()


# ✅ Callback RabbitMQ
def callback(ch, method, properties, body):
    try:
        order = json.loads(body)
        save_order(order)
        ch.basic_ack(delivery_tag=method.delivery_tag)

    except Exception as e:
        print(f"[PID {os.getpid()}] ❌ Error general: {e}")
        ch.basic_nack(delivery_tag=method.delivery_tag, requeue=True)


# ✅ Worker RabbitMQ
def start_worker():
    global conn
    queuename=os.getenv('RABBIT_QUEUE_NAME_ORDERS')
    rabbithost=os.getenv('RABBIT_HOST', 'localhost')
    print(f"[PID {os.getpid()}] worker listo ",flush=True)
    # 🔥 1 conexión por proceso
    conn = get_db()

    print(f"[PID {os.getpid()}] DB OK")
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(rabbithost)
    )
    print(f"[PID {os.getpid()}] Rabbit OK")
    channel = connection.channel()

    channel.queue_declare(queue=queuename, durable=True)

    channel.basic_qos(prefetch_count=1)

    channel.basic_consume(
        queue=queuename,
        on_message_callback=callback
    )

    print(f"[PID {os.getpid()}] 🟢 Worker listo")
    try: 
     channel.start_consuming()
    finally:
        if conn and conn.closed == 0:
            conn.close()
        connection.close()    

# ✅ Ejecutar en paralelo
def run_parallel(n):
    processes = []

    for i in range(n):
        p = multiprocessing.Process(target=start_worker)
        p.start()
        processes.append(p)

    for p in processes:
        p.join()


# ✅ MAIN
if __name__ == "__main__":
    parser = argparse.ArgumentParser()

    parser.add_argument(
        "--sessions",
        type=int,
        default=1,
        help="Número de workers paralelos"
    )

    args = parser.parse_args()

    print(f"🚀 Iniciando {args.sessions} sesiones...")

    run_parallel(args.sessions)
