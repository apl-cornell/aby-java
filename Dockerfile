# Stage 1: Build binaries
FROM ubuntu:20.04 AS builder
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=America/New_York

WORKDIR /root

# Install dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    cmake \
    g++ \
    git \
    lzip \
    m4 \
    make \
    openjdk-11-jdk-headless \
    swig \
    wget
    # && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

# Copy configuration
COPY gradle.properties .
COPY scripts/variables.sh scripts/

# Download and build source dependencies
COPY external external
RUN make -C external build-all clean-source clean-build

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

# Build for Linux
COPY CMakeLists.txt .
COPY scripts/build_java_wrapper.sh scripts/
RUN scripts/build_java_wrapper.sh


# Stage 2: test built Linux binary
FROM openjdk:11-jdk-slim as tester
CMD ["/bin/bash"]
WORKDIR /root

## Have Gradle Wrapper download the Gradle binary
COPY gradlew .
COPY gradle gradle
RUN ./gradlew --version

## Have Gradle download all dependencies
COPY gradle.properties *.gradle.kts ./
RUN ./gradlew --no-daemon assemble || return 0

## Copy the ABY binary
COPY --from=builder /root/src/main/java src/main/java
COPY --from=builder /root/build/cmake/install/lib/libabyjava.so src/main/resources/natives/linux_64/

## Build and test the app
COPY src/test src/test
RUN ./gradlew --no-daemon build
