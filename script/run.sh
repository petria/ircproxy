#!/bin/bash

CLASSPATH="out/production/ircproxy/"
START_CLASS="com.freakz.ircproxy.IrcProxy"

#for i in lib/*.jar
#do
#  CLASSPATH=${CLASSPATH}:$i
#done

echo "CLASSPATH = $CLASSPATH"
echo "ARGUMENTS = $@"

#while true; do

  java -Xms512m -Xmx768m -Xdebug -classpath $CLASSPATH $START_CLASS $@

  EXIT_CODE=$?

  echo "EXIT_CODE: $EXIT_CODE"
  if [ "$EXIT_CODE" -eq 0 ]; then
    exit 0
  fi

#  echo "... cycling"
#  sleep 2

#done

