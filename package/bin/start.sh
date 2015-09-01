#!/bin/bash
#
# Critter service startup script
#
# ernest.micklei@philemonworks.com

# where am i
SCRIPT_DIR="$( cd "$( dirname "$0" )" && pwd )"
CRITTER_HOME="$( dirname "$SCRIPT_DIR" )"
LOG_DIR=$CRITTER_HOME/log

cd $CRITTER_HOME

if [ ! -d $LOG_DIR ]; then
  mkdir $LOG_DIR
fi

if [[ $# == 1 && $1 == "--no-daemon" ]]; then
    java \
        -Dlog4j.configuration="file://$CRITTER_HOME/conf/log4j.properties" \
        $JAVA_OPTS \
        -classpath "$CRITTER_HOME/lib/*" \
        com.philemonworks.critter.Launcher "$CRITTER_HOME/conf/critter.properties"
    exit $?
fi

# start the engines
java \
    -Dlog4j.configuration="file://$CRITTER_HOME/conf/log4j.properties" \
    $JAVA_OPTS \
    -classpath "$CRITTER_HOME/lib/*" \
    com.philemonworks.critter.Launcher "$CRITTER_HOME/conf/critter.properties" > $LOG_DIR/boot.log 2>&1 &

# store the java process id for stop.sh
LASTPID=$(echo $!)
echo $LASTPID > $SCRIPT_DIR/main.pid
