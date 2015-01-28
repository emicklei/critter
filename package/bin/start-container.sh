#!/bin/bash
#
# Critter service startup script that can be used inside a Docker container.
#
# uses environment properties to configure Critter

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

java \
    -Dlog4j.configuration="file://$DIR/../conf/log4j.properties" \
    $JAVA_OPTS \
    -classpath "$DIR/../lib/*" \
    com.philemonworks.critter.Launcher
