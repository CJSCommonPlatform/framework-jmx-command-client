# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]
### Added
- New command '--list' which lists the commands available on your Wildfly instance.
- Authentication to a remote Wildfly instance with commands '--username' and '--password'
### Changed
- Application now run in a Weld container to allow for dependency injection

## [1.0.0] - 2019-06-19

### Added
- Initial release of the catchup-shuttering-manager CLI tool, supporting invocation of the event sourcing frameworks SHUTTER, UNSHUTTER, CATCHUP, REBUILD operations.

