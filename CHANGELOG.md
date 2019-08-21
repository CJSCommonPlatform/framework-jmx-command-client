# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]

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



