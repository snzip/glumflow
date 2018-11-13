#!/bin/bash
dirsArray=("./haproxy/certs.d" "./haproxy/letsencrypt" "./tb-node/postgres" "./tb-node/cassandra" "./tb-node/log/tb1" "./tb-node/log/tb2")

for dir in ${dirsArray[@]}
do
    if [ ! -d "$dir" ]; then
        echo creating dir $dir
        mkdir -p $dir
    fi
done
