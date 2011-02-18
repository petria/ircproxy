#!/bin/bash

getent passwd `netstat -4ne | grep $@ |  cut -c 81-85 -` | cut -d':' -f1

