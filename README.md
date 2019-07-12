# Framework Jmx Command Client

[![Build Status](https://travis-ci.org/CJSCommonPlatform/catchup-and-shuttering-manager.svg?branch=master)](https://travis-ci.org/CJSCommonPlatform/catchup-and-shuttering-manager) [![Coverage Status](https://coveralls.io/repos/github/CJSCommonPlatform/catchup-and-shuttering-manager/badge.svg?branch=master)](https://coveralls.io/github/CJSCommonPlatform/catchup-and-shuttering-manager?branch=master)

## Usage

catchup-shuttering-manager
 -c,--command <arg>     Framework command to execute.
 -h,--help              Show help.
 -ho,--host <arg>       Hostname or IP address of the Wildfly server. Defaults to localhost
 -p,--port <arg>        Wildfly management port. Defaults to 9990 (the default for Wildfly)
 -u, --username         Optional username for Wildfly management security
 -pw, --password        Optional password for Wildfly management security
 -l, --list             List of all framework commands
 
 
 java -jar catchup-shuttering-manager.jar -c "PING"
 
 Note: If you are running wildfly on your local machine and running as the same user as the one you are
 logged in as, then username/password are not required. (However, if running wildfly in a virtual machine
 like vagrant, username and password will be required)
 
 The default username/password for Wildfly is admin/admin  
