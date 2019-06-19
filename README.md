# CatchUp And Shuttering Manager

[![Build Status](https://travis-ci.org/CJSCommonPlatform/catchup-and-shuttering-manager.svg?branch=master)](https://travis-ci.org/CJSCommonPlatform/catchup-and-shuttering-manager) [![Coverage Status](https://coveralls.io/repos/github/CJSCommonPlatform/catchup-and-shuttering-manager/badge.svg?branch=master)](https://coveralls.io/github/CJSCommonPlatform/catchup-and-shuttering-manager?branch=master)

## Usage

catchup-shuttering-manager
 -c,--command <arg>   Framework command to execute, for example
                      CATCHUP,SHUTTER, UNSHUTTER & REBUILD.
 -h,--help            show help.
 -ho,--host <arg>     Host Remote or Localhost
 -p,--port <arg>      Wildfly management port
 
 java -jar catchup-shuttering-manager.jar -c "SHUTTER" -ho "localhost" -p "9999"
