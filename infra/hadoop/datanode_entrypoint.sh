#!/bin/bash
set -e

mkdir -p /usr/local/hadoop/hdfs/datanode
chmod -R 777 /usr/local/hadoop/hdfs/datanode

hdfs dfs -mkdir -p /data/orders
hdfs dfs -mkdir -p /data/analytics
hdfs dfs -chmod -R 777 /data

hdfs dfs -mkdir -p /checkpoints/data/orders
hdfs dfs -mkdir -p /checkpoints/data/analytics
hdfs dfs -chmod -R 777 /checkpoints

exec "$@"