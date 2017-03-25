#!/bin/sh 
tpid=`cat tpid | awk '{print $1}'`
tpid=`ps -aef | grep $tpid | awk '{print $2}' |grep $tpid`
if [ ${tpid} ]; then
        echo faceplat Service is running.
else
        echo faceplat Service is NOT running.
fi
