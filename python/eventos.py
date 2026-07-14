import pika
import json
import psycopg2
import multiprocessing
import argparse
import os

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
def save_order(event):
    global conn

    if not event.get("type"):
        raise ValueError("Not Exist statement!!")
    

    

    action=event.get("type")
    items=event.get("items",[])


   


    if action == "DELETE_ORDER" and event.get("orderid") is None:
      raise ValueError("orderid requerido")
    if action == "DELETE_DETAIL" and (event.get("orderid") is None  and  not items ):
      raise ValueError("either orderid or productid missing")  
    cursor = None
    try:
        cursor = get_cursor()
        print(f"[PID {os.getpid()}] Procesando orden...")
        results = []
        if action == "DELETE_ORDER":

        # ✅ INSERT HEAD
         cursor.execute("""
            select fn_delete_head(%s)
        """, (
            event.get("orderid"),
            ))
         results.append(cursor.fetchone())

        elif action == "DELETE_DETAIL":
          
          for item in items:
           cursor.execute("""
            select fn_delete_item(%s,%s)
        """, (
            event.get("orderid"),
            item
            ))
           results.append(cursor.fetchone())
        else: 
            raise ValueError(f"Unknown action {action}")
        
       
        conn.commit()

        print(f"[PID {os.getpid()}] ✅ Borrado {results}")

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
        event = json.loads(body)
        save_order(event)
        ch.basic_ack(delivery_tag=method.delivery_tag)

    except Exception as e:
        print(f"[PID {os.getpid()}] ❌ Error general: {e}")
        ch.basic_nack(delivery_tag=method.delivery_tag, requeue=True)


# ✅ Worker RabbitMQ
def start_worker():
    global conn
    queuename=os.getenv('RABBIT_QUEUE_NAME_EVENT')
    rabbithost=os.getenv('RABBIT_HOST', 'localhost')
    # 🔥 1 conexión por proceso
    conn = get_db()

    connection = pika.BlockingConnection(
        pika.ConnectionParameters(rabbithost)
    )

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
