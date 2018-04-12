#!/usr/bin/env bash

PORT=$1
result=' '
result=$(lsof -i:$PORT|grep 'LISTEN')
if [ -z "${result}" ]; then
  echo "0"
else
  echo "1"
fi
