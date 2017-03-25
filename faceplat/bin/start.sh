#!/bin/sh 
rm -f tpid

nohup java -jar faceplat-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &

echo $ > tpid
