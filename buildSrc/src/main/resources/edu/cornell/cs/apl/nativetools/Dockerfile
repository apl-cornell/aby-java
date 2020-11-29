# Generate the Java interface using SWIG
FROM ubuntu:20.04 AS swig
WORKDIR /root

# Install dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    git \
    make \
    rsync \
    swig \
    && rm -rf /var/lib/apt/lists/*

# Download library
COPY lib.mk get.mk ./
RUN make -f get.mk

# Generate the Java interface
COPY *.mk lib.patch lib.i ./
RUN make -f build.mk swig
