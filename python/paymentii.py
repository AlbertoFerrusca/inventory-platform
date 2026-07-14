import pika
import json
import psycopg2
import os

DB_CONFIG = {
    "host":os.getenv('DB_HOST_PY','localhost'),
    "database":os.getenv('DB_DATABASE','postgres') ,
    "user":os.getenv('DB_USER'),
    "password":os.getenv('DB_PASSWORD')
}


def get_db():
    return psycopg2.connect(**DB_CONFIG)

def save_error(cursor,payment,conn,error):
        cursor.callproc("fn_error_payment",[
         str(payment.get("order_id")),
            payment.get("amount"),
            payment.get("status"),
            payment.get("payment_method"),
            payment.get("installments"),
            payment.get("estado"),
            payment.get("bank_folio"),
            error  
           ])
        conn.commit()
        print(f"[Worker {os.getpid()}] ✅ Error Payment OK order_id={payment.get('order_id')}")    
   
       

def save_payment(payment):
    cursor = None
    conn = None
    conn = get_db()
    cursor = conn.cursor()
    try:
        cursor.callproc("fn_insert_payment", [
            str(payment.get("order_id")),
            payment.get("amount"),
            payment.get("status"),
            payment.get("payment_method"),
            payment.get("installments"),
            payment.get("estado"),
            payment.get("bank_folio")
        ])

        conn.commit()
        return True

    except Exception as e:
        if conn and conn.closed == 0:
         conn.rollback()
        try:
           save_error(cursor,payment,conn,str(e)) 
           return False 
        except Exception as err:
         print(f"[Worker {os.getpid()}] ❌ DB Error: {e}")
         return False

    finally:
        if cursor:
            cursor.close()
        if conn and conn.closed == 0:
           conn.close()





def callback(ch, method, properties, body):
    try:
        payment = json.loads(body)

        if not payment.get("order_id"):
            raise Exception("order_id faltante")

        print(f"[Worker {os.getpid()}] 📥 Procesando: {payment.get('order_id')}")

        result=save_payment(payment)
        if result:
            print("Pago procesado")
        else:
            print("pago enviado a la tabla de errores")    

        # ✅ Ack solo si todo salió bien
        ch.basic_ack(delivery_tag=method.delivery_tag)

    except Exception as e:
        print(f"[Worker {os.getpid()}] ❌ Error general: {e}")

        # ✅ Reintento (importante en producción)
        ch.basic_nack(delivery_tag=method.delivery_tag, requeue=True)


def start():

    queuename=os.getenv('RABBIT_QUEUE_NAME_PAYMENT')
    rabbithost=os.getenv('RABBIT_HOST', 'localhost')
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(rabbithost)
    )

    channel = connection.channel()

    
    channel.queue_declare(
        queue=queuename,
        durable=True
    )


    

    # ✅ Control de carga (muy importante)
    channel.basic_qos(prefetch_count=1)

    channel.basic_consume(
        queue=queuename,
        on_message_callback=callback
    )

    print(f"[Worker {os.getpid()}] 💰 Esperando payments...")

    channel.start_consuming()


if __name__ == "__main__":
    start()
