# Version 1.0.3
---
Minor release (Fix release)

Replace spock testing framework with junit5+jmockit

## Changes
* Changed Stack sensors internal ThreadLocal by InheritableThreadLocal in order to make compatible with reactive programming
* Fixed Stack sensors name closing parent before child, now purge the childs
* Fixed some static unchecked warnings
