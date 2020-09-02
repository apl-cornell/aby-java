FROM alpine:3.12
WORKDIR /root

# Install dependencies
RUN apk add --no-cache \
    boost-dev \
    cmake \
    g++ \
    git \
    gmp-dev \
    libressl-dev \
    make \
    openjdk11-jdk \
    swig

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk

# Download ABY source code
COPY gradle.properties .
COPY scripts/variables.sh scripts/get_aby.sh scripts/
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
# RUN scripts/build_java_wrapper.sh
