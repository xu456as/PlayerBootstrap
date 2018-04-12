#!/usr/bin/env bash

PORT=$1
PID=$(lsof -i:$PORT|grep 'LISTEN'|awk '{print $2}')
if [ -z $PID ];then
  echo "0;PID not found"
else
  kill $PID
  echo "1;finish"
fi
