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
FROM ubuntu:20.04 AS builder
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=America/New_York

WORKDIR /root

# Install dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    cmake \
    g++ \
    m4 \
    make \
    perl \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Download and build source dependencies
COPY external external
RUN make -C external build-boost clean-source clean-build
RUN make -C external build-gmp clean-source clean-build
RUN make -C external build-openssl clean-source clean-build
RUN make -C external build-openjdk clean-source clean-build

# Copy ABY source code
COPY --from=swig /root/ABY ABY

# Copy generated Java interface and wrappper code
COPY --from=swig /root/src src

# Build for Linux
COPY CMakeLists.txt .
COPY gradle.properties .
COPY scripts/variables.sh scripts/build_java_wrapper.sh scripts/
RUN scripts/build_java_wrapper.sh
RUN mkdir -p src/main/resources/natives/linux_64 && cp build/cmake/install/lib/libabyjava.so "$_"/


# Test built Linux binary
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
