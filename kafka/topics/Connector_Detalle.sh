
#!/bin/bash


cat <<EOF | curl -X POST http://localhost:8084/connectors \
-H "Content-Type: application/json" \
-d @-
{
  "name": "detailproduct-connector-$BRANCH_ID",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "database.hostname": "host.docker.internal",
    "database.port": "5432",
    "database.user": "$DB_USER",
    "database.password": "$DB_PASSWORD",
    "database.dbname": "postgres",

    "topic.prefix": "detailproduct-$BRANCH_ID",
    "table.include.list": "public.detailproduct",
    "database.server.name": "$BRANCH_ID",
    "slot.name": "detailproduct_slot",
    "plugin.name": "pgoutput",

    "transforms": "route",
    "transforms.route.type": "org.apache.kafka.connect.transforms.RegexRouter",
    "transforms.route.regex": ".*public.detailproduct",
    "transforms.route.replacement": "detailproduct-topic-$BRANCH_ID",

    "snapshot.mode": "initial",
    "include.schema.changes": "false"
  }
}
EOF


