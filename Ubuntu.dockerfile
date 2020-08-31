FROM ubuntu:20.04
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=America/New_York

WORKDIR /root

# Install dependencies
RUN apt-get update && apt-get install -y \
  cmake \
  g++ \
  git \
  libboost-all-dev \
  libgmp-dev \
  libssl-dev \
  make

# Download ABY
COPY gradle.properties .
COPY scripts/get_aby.sh scripts/
RUN scripts/get_aby.sh

# Build ABY
RUN cd ABY && mkdir build && cd build && cmake .. && make
