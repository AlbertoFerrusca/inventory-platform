#!/bin/bash

source venv/bin/activate



nohup python -u ordenesii.py --sessions 2  > orders.log 2>&1 &
nohup python -u paymentii.py --sessions 4 > payment.log 2>&1 &
#nohup python -u paymentii.py --sessions  > payment.log 2>&1 &
nohup python eventos.py --sessions 1 > eventos.log 2>&1 &

find . -name "*.log" -size +10M -exec truncate -s 0 {} \;

echo "Servicio de ordenes y pyament activados" 
