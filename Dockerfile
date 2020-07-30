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
  make

# Download ABY
COPY gradle.properties .
COPY scripts/get_aby.sh scripts/
RUN scripts/get_aby.sh

# Build ABY
RUN cd ABY && mkdir build && cd build && cmake .. && make
