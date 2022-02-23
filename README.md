# ABY Java

[ABY](https://github.com/encryptogroup/ABY) is a cryptographic framework for
secure two-party computation implemented in C++. This library provides a
painless way to use ABY from Java. It bundles a Java interface along with a
native binary for each [supported platform](#supported-platforms). At run time,
the library extracts the correct binary based on the system. This allows you to
use ABY as any other Maven dependency, completely ignoring the fact that the
framework is implemented in C++.

## Installation

This library is distributed through [Maven Central][maven-central].

### Gradle

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.apl-cornell:aby-java:<version>")
}
```

[maven-central]: https://search.maven.org/artifact/io.github.apl-cornell/aby-java

## Supported Platforms

We currently build native libraries for macOS and Linux. ABY (transitively)
depends on [GMP](https://gmplib.org/), which doesn't work on Windows.

## Building

You need to have [Java](https://www.oracle.com/java/technologies/downloads/)
and [Docker](https://docs.docker.com/get-docker/) installed. Then, simply run:

```shell
./gradlew build
```

This command uses Docker to generate the Java interface and build the native
binaries. It then copies the generated files from Docker to your machine,
assembles the library JAR, and runs units tests.

Using Docker ensures reliable and portable builds. For example, it allows
building a macOS binary on a Linux machine without any setup, and ensures the
generated binary is the same no matter where it is generated.

We generate the Java interface from the C++ interface
using [SWIG](http://www.swig.org/), and
use [dockcross](https://github.com/dockcross/dockcross) to build the native
binaries.

## Development

ABY Java does not expose all features of ABY. You can extend the SWIG interface
file [ABY.i](ABY.i) to include more features. Refer to
the [SWIG documentation](http://www.swig.org/Doc4.0/) and especially
the [section on Java](http://www.swig.org/Doc4.0/Java.html) for more
information.

ABY Java is based on the
fork [apl-cornell/ABY](https://github.com/apl-cornell/ABY). You can contribute
to that repository if you need to make changes to ABY source code. You need to
update the version of ABY specified in [build.gradle.kts](build.gradle.kts)
if you do.

After making changes, create a new release on GitHub. This will build and
publish a new version of the library.
