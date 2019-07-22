# Framework Jmx Command Client

[![Build Status](https://travis-ci.org/CJSCommonPlatform/framework-jmx-command-client.svg?branch=master)](https://travis-ci.org/CJSCommonPlatform/framework-jmx-command-client) [![Coverage Status](https://coveralls.io/repos/github/CJSCommonPlatform/framework-jmx-command-client/badge.svg?branch=master)](https://coveralls.io/github/CJSCommonPlatform/framework-jmx-command-client?branch=master)

Command line client tool for running Framework System Commands against any framework based context.

From Framework 6.0.0 and above

## Usage

**java -jar framework-jmx-command-client.jar _\<options\>_**

- **-cn,--context-name** <arg>  _The name of the context on which to run the command. Required_
- **-c,--command** <arg>        _The name of the System Command to run_
- **-h,--host** <arg>           _Hostname or IP address of the Wildfly server. Defaults to localhost_
- **-p,--port** <arg>           _Wildfly management port. Defaults to 9990 (the default for Wildfly)_
- **-u, --username**            _Optional username for Wildfly management security_
- **-pw, --password**           _Optional password for Wildfly management security_
- **-l, --list**                _Shows a list of all framework System Commands and exits_
- **--help**                    _Show help message and exits._
 
### For example 
_java -jar framework-jmx-command-client.jar --context-name example-single --command PING_

Would run the command 'PING' against a local wildfly instance with no authentication with a example-single.war deployed
 
###### Note: 
 If you are running wildfly on your local machine and running as the same user as the one you are
 logged in as, then username/password are not required. (However, if running wildfly in a virtual machine
 like vagrant, username and password will be required)
 
 The default username/password for Wildfly is admin/admin  
