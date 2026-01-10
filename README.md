# System theme detector

*Note: This project is work in progress. It should mostly work for Linux and Windows, but MacOS is not supported yet. Bugs 'n' stuff are also expected.*

The purpose of this library is to provide utilities for detecting the current system theme on all major platforms and to register callbacks that can listen for theme changes.

It has been inspired by [jsystemthemedetector](https://github.com/Dansoftowner/jSystemThemeDetector) but differs in some major respects:

* Detects Appearance (the name used for light/dark mode in this library), Accent Color and Font (mostly relevant for Linux).
* Uses `ProcessBuilder` and does not require any foreign function interfaces or third-party dependencies (except [slf4j](https://www.slf4j.org/manual.html) for optional logging).
* Uses (mostly) desktop-agnostic utilities on Linux.

## Usage

[Work in progress]

## Installation

[Work in progress]