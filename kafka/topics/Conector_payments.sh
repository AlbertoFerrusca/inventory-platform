
#!/bin/bash

cat <<EOF | curl -X POST http://localhost:8084/connectors \
-H "Content-Type: application/json" \
-d @-
{
  "name": "payments-connector-$BRANCH_ID",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "database.hostname": "$DB_HOST",
    "database.port": "5432",
    "database.user": "$DB_USER",
    "database.password": "$DB_PASSWORD",
    "database.dbname": "postgres",
    "slot.name": "payments_slot",

    "topic.prefix": "payments-$BRANCH_ID",
    "table.include.list": "public.payments",
    "database.server.name": "$BRANCH_ID",
   
    "plugin.name": "pgoutput",
    "decimal.handling.mode": "double",
    
    "transforms": "route",
    "transforms.route.type": "org.apache.kafka.connect.transforms.RegexRouter",
    "transforms.route.regex": ".*public.payments",
    "transforms.route.replacement": "payments-topic-$BRANCH_ID",

    "snapshot.mode": "initial",
    "include.schema.changes": "false"
  }
}
EOF

