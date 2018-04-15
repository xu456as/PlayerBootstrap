#!/usr/bin/env bash

port=$1
PROJECT_DIR=$(cd `dirname $0`;pwd)
MAIN_CLASS=org.aitank.entry.Main
CLASS_PATH="${PROJECT_DIR}/target/lib/*:${PROJECT_DIR}/target/classes/"
java -server -classpath $CLASS_PATH $MAIN_CLASS $port
