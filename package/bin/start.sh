#!/bin/bash
#
# Critter service startup script
#
# ernest.micklei@philemonworks.com

# where am i
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [[ $# == 1 && $1 == "--no-daemon" ]]; then
    java \
        -Dlog4j.configuration="file://$DIR/../conf/log4j.properties" \
        $JAVA_OPTS \
        -classpath "$DIR/../lib/*" \
        com.philemonworks.critter.Launcher "$DIR/../conf/critter.properties"
    exit $?
fi

# start the engines
java \
    -Dlog4j.configuration="file://$DIR/../conf/log4j.properties" \
    $JAVA_OPTS \
    -classpath "$DIR/../lib/*" \
    com.philemonworks.critter.Launcher "$DIR/../conf/critter.properties" > boot.log 2>&1 &

# store the java process id for stop.sh
LASTPID=$(echo $!)
echo $LASTPID > "main.pid"
