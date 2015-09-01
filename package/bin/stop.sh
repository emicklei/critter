#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "$0" )" && pwd )"

read LASTPID < $SCRIPT_DIR/main.pid
kill -9 $LASTPID
rm -f main.pid
