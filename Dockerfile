# Build the Linux binary
FROM phusion/holy-build-box-64:latest AS builder
WORKDIR /root

# Install dependencies
RUN yum -y install \
    wget \
    && yum clean all

# Holy Build Box provided libstdc++.a has broken std::thread support.
# See https://github.com/phusion/holy-build-box/issues/19.
# This is causing missing symbol errors on CI. We remove this library
# to force GCC to pickup its own libstdc++ which seems to be portable.
RUN rm -f /hbb_shlib/lib/libstdc++.a

# Copy ABY source code
COPY --from=swig /root/ABY ABY

# Copy generated Java interface and wrappper code
COPY --from=swig /root/src src

# Build the wrapper library
COPY CMakeLists.txt .
COPY gradle.properties .
COPY scripts/variables.sh scripts/build_java_wrapper.sh scripts/
RUN /hbb_shlib/activate-exec scripts/build_java_wrapper.sh

# Copy the wrapper library into resources
ENV ABY_JAVA_INSTALL_DIR=src/main/resources/natives/linux_64
RUN mkdir -p $ABY_JAVA_INSTALL_DIR && cp build/cmake/install/lib/libabyjava.so "$_"/
RUN strip --strip-all $ABY_JAVA_INSTALL_DIR/libabyjava.so

# Check that the generated libraries are portable
RUN /hbb_shlib/activate-exec libcheck $ABY_JAVA_INSTALL_DIR/libabyjava.so


# Test built Linux binary
# FROM openjdk:11-jdk-slim as tester
# CMD ["/bin/bash"]

FROM ubuntu:20.04 as tester
RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-11-jdk-headless

WORKDIR /root

## Have Gradle Wrapper download the Gradle binary
COPY gradlew .
COPY gradle gradle
RUN ./gradlew --version

## Have Gradle download all dependencies
COPY gradle.properties *.gradle.kts ./
RUN ./gradlew --no-daemon build || return 0

## Copy wrapper code and the ABY binary
COPY --from=builder /root/src src

## Build and test the app
COPY src/test src/test
RUN ./gradlew --no-daemon build
