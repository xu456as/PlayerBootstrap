#!/usr/bin/env bash

PROJECT_DIR=$(cd `dirname $0`;pwd)
MAIN_CLASS=me.fetonxu.ServerMain
CLASS_PATH="${PROJECT_DIR}/target/classes/:${PROJECT_DIR}/target/dependency/*"
java -server -classpath $CLASS_PATH $MAIN_CLASS
