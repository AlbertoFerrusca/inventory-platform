#!/bin/bash


export PGPASSWORD="$DB_PASSWORD"
DB_HOST_LOCAL="localhost"
DB_PORT="5432"

echo "Inicio: $(date)"

location="JAL001"
/Library/PostgreSQL/17/bin/psql \
-h "$DB_HOST_LOCAL" \
-p "$DB_PORT" \
-U "$DB_USER" \
-d "$DB_DATABSE" \
-c "call pendientes_cursor_chunkTotal();"

unset PGPASSWORD

echo "FIN: $(date)"
