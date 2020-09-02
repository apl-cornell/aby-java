FROM bitnami/minideb:buster
WORKDIR /root

# Install dependencies
RUN install_packages \
    ca-certificates \
    cmake \
    g++ \
    git \
    libboost-system-dev \
    libboost-thread-dev \
    libgmp-dev \
    libssl-dev \
    make \
    openjdk-11-jdk-headless \
    swig

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

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
