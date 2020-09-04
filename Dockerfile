# Generate the Java interface using SWIG
FROM bitnami/minideb:buster AS swig
WORKDIR /root

# Install dependencies
RUN install_packages \
    ca-certificates \
    git \
    swig

# Copy configuration
COPY gradle.properties .
COPY scripts/variables.sh scripts/

# Download ABY source code
COPY scripts/get_aby.sh scripts/
RUN scripts/get_aby.sh

# Apply our patch to ABY sources
COPY ABY.patch .
COPY scripts/patch_aby.sh scripts/
RUN scripts/patch_aby.sh

# Generate the Java interface
COPY aby.i .
COPY scripts/generate_java_interface.sh scripts/
RUN scripts/generate_java_interface.sh


# Build the Linux binary
FROM phusion/holy-build-box-64:latest AS builder
WORKDIR /root

# Install dependencies
RUN yum -y install \
    wget \
    && yum clean all

# Download and build source dependencies
COPY external external
RUN /hbb_shlib/activate-exec make -C external build-boost clean-source clean-build
RUN /hbb_shlib/activate-exec make -C external build-gmp clean-source clean-build
RUN /hbb_shlib/activate-exec make -C external build-openssl clean-source clean-build
RUN /hbb_shlib/activate-exec make -C external build-openjdk clean-source clean-build

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

FROM ubuntu:20.04 as tester
RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-11-jdk-headless

CMD ["/bin/bash"]
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
