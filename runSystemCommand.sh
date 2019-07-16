#!/usr/bin/env bash

FRAMEWORK_JMX_COMMAND_CLIENT_VERSION=1.1.0-SNAPSHOT

#fail script on error
set -e
echo "Running Framework System Command Client $1 $2 $3 $4 $5"
echo
java -jar target/framework-jmx-command-client-${FRAMEWORK_JMX_COMMAND_CLIENT_VERSION}-jar-with-dependencies.jar $1 $2 $3 $4 $5
