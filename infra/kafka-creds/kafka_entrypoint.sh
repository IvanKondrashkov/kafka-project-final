#!/bin/bash
set -e

kafka-acls --bootstrap-server kafka-0:9092 \
  --command-config /etc/kafka/secrets/admin.properties \
  --add Write, Describe \
  --allow-principal User:producer \
  --producer \
  --topic orders

kafka-acls --bootstrap-server kafka-0:9092 \
  --command-config /etc/kafka/secrets/admin.properties \
  --add Write, Describe \
  --allow-principal User:producer \
  --producer \
  --topic blocked-products

kafka-acls --bootstrap-server kafka-0:9092 \
  --command-config /etc/kafka/secrets/admin.properties \
  --add Write, Describe \
  --allow-principal User:producer \
  --producer \
  --topic filtered-orders

kafka-acls --bootstrap-server kafka-0:9092 \
  --command-config /etc/kafka/secrets/admin.properties \
  --add Write, Describe \
  --allow-principal User:producer \
  --producer \
  --topic user-analytics

kafka-acls --bootstrap-server kafka-0:9092 \
  --command-config /etc/kafka/secrets/admin.properties \
  --add Read, Describe \
  --allow-principal User:consumer \
  --consumer \
  --topic orders \
  --group filter-processor-id

kafka-acls --bootstrap-server kafka-0:9092 \
  --command-config /etc/kafka/secrets/admin.properties \
  --add Read, Describe \
  --allow-principal User:consumer \
  --consumer \
  --topic blocked-products \
  --group filter-processor-id

exec "$@"