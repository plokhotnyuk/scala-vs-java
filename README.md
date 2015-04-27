Performance of Scala compared to Java

Main goal of this project is to show that it is possible to write idiomatic Scala code which run on par with Java in terms of execution speed and memory consumption.

[![Travis CI Build Status](https://secure.travis-ci.org/plokhotnyuk/scala-vs-java.png)](http://travis-ci.org/plokhotnyuk/scala-vs-java)

[![Shippable Build Status](https://api.shippable.com/projects/550ee5965ab6cc1352a767a9/badge?branchName=master)](https://app.shippable.com/projects/550ee5965ab6cc1352a767a9/builds)

[![Wercker Build Status](https://app.wercker.com/status/a73f239e6cf3a305c53182e4c99b83d9/s "wercker status")](https://app.wercker.com/project/bykey/a73f239e6cf3a305c53182e4c99b83d9)

## Hardware required
- CPU: 2 cores or more
- RAM: 2Gb or greater

## Software installed required
- JDK: 1.7.0_x or newer
- sbt: 0.13.x

## Building & running benchmarks
Use following command-line instructions to build from sources and run all benchmarks:
```sh
sbt -Dsbt.log.noformat=true "run .*" >outX.txt
```
For printing format of mask, other options & list of available profilers use following commands:
```sh
sbt -Dsbt.log.noformat=true "run -h" > help.txt
sbt -Dsbt.log.noformat=true "run -lprof" > profilers.txt
```
NOTE: First time compiling on JDK 7 can require bigger size of thread stack:
```
sbt -J-Xss1m compile
```

## Test result descriptions
Results of running on different environments:

#### out_8u40.txt
Intel(R) Core(TM) i7-2640M CPU @ 2.80GHz (max 3.50GHz), RAM 12Gb DDR3-1333, Ubuntu 14.10, Oracle JDK 1.8.0_40-b25 64-bit

#### out_8u31.txt
Intel(R) Core(TM) i7-2640M CPU @ 2.80GHz (max 3.50GHz), RAM 12Gb DDR3-1333, Ubuntu 14.10, Oracle JDK 1.8.0_31-b13 64-bit

#### out_7u76.txt
Intel(R) Core(TM) i7-2640M CPU @ 2.80GHz (max 3.50GHz), RAM 12Gb DDR3-1333, Ubuntu 14.10, Oracle JDK 1.7.0_76-b13 64-bit
