#!/bin/bash


export PGPASSWORD="$DB_PASSWORD"
DB_HOST_LOCAL="localhost"
DB_PORT="5432"

location="JAL001"
/Library/PostgreSQL/17/bin/psql \
-h "$DB_HOST_LOCAL" \
-p "$DB_PORT" \
-U "$DB_USER" \
-d "$DB_DATABSE" \
-c "select fn_restock_by_location('$location');"

unset PGPASSWORD
