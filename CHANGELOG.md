# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]

## [2.4.3] - 2020-04-23
### Changed
- microservice-framework -> 6.4.2

## [2.4.2] - 2020-04-09
### Changed
- microservice-framework -> 6.4.1

## [2.4.1] - 2019-11-20
### Fixed
- Successful runs of a command now logs the success message from the server

## [2.4.0] - 2019-11-13
### Changed
- Update to framework 6.4.0

## [2.3.0] - 2019-11-07
### Changed
- Update to framework 6.3.0
- All commands are now called using their String command name to allow for new commands to be
added without needing to update the dependency on framework

## [2.2.4] - 2019-10-31
### Changed
- Update to framework 6.2.3    

## [2.2.3] - 2019-10-24
### Changed
- Update to framework 6.2.2    

## [2.2.2] - 2019-10-18
### Changed
- Changed return codes to include code for command failed. New codes are
    - 0: Success
    - 1: Authentication Failed
    - 2: Connection Failed
    - 3: Command Failed
    - 4: Exception invoking System Command/Anything else
- Update to framework 6.2.1    

## [2.2.1] - 2019-10-16
### Changed
- Made local copies of UtcClock and Sleeper, as the dependency on utilities that
these classes require confuses Weld on startup, and causes odd Class loading errors
in the container 
### Added
- Main method to Bootstrapper to aid debugging

## [2.2.0] - 2019-10-15
### Changed
- Update to framework 6.2.0

## [2.1.0] - 2019-10-01
### Changed
- Update to framework 6.1.1

## [2.0.10] - 2019-09-26
### Added
- Extra logging on exceptions, to output stack trace of other failure exceptions

## [2.0.9] - 2019-09-23
### Added
- Result code returned : 0 - completed, 1 - authentication failed, 2 - connection failed, 3 - other failure

## [2.0.8] - 2019-09-19
### Changed
- Update to framework 6.0.15
### Added
- New SystemCommands ADD_TRIGGER and REMOVE_TRIGGER

## [2.0.7] - 2019-09-11
- Update to framework 6.0.14

## [2.0.6] 2019-09-08
### Changed
- Update to framework 6.0.12
- Improved server error handling
- Now prints the stack trace from the server on any server error

## [2.0.5] 2019-08-30
### Changed
- Update to framework 6.0.11

## [2.0.4] 2019-08-29
### Changed
- Update to framework 6.0.10

## [2.0.3] 2019-08-23
### Changed
- Added the description to the listing of System Commands

## [2.0.2] 2019-08-21
### Changed
- Update to framework 6.0.9

## [2.0.1] 2019-08-19
### Changed
- Update to framework 6.0.6

## [2.0.0] 2019-08-15
### Changed
- Update to framework 6.0.2

## [1.0.2] 2019-07-18
### Added
- Added a mandatory command line switch '--context-name' to allow to connect to a specific context

## [1.0.1] 2019-07-17
### Fixed
- Removed all dependencies on server side code to allow fat jar to be run 
as standalone correctly

## [1.0.0] 2019-07-16
### Added
- Initial release of the catchup-shuttering-manager CLI tool, supporting invocation of the event sourcing frameworks SHUTTER, UNSHUTTER, CATCHUP, REBUILD operations.
- New command '--list' which lists the commands available on your Wildfly instance.
- Authentication to a remote Wildfly instance with commands '--username' and '--password'
- Application runs in a Weld container to allow for dependency injection



