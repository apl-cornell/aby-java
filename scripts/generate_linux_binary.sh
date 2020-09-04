#!/usr/bin/env sh

# Builds the Linux binary using Docker and copies it into the correct directory.

set -e

# Build the Docker image and remember its ID.
IMAGE_ID=$(docker build --target builder . | tee /dev/tty | tail -n1 | cut -d' ' -f3)

# Copy the linux binary out.
OUTPUT_DIR=src/main/resources/natives/linux_64
mkdir -p $OUTPUT_DIR

CONTAINER_ID=$(docker create "$IMAGE_ID")
docker cp "$CONTAINER_ID:/root/$OUTPUT_DIR/libabyjava.so" $OUTPUT_DIR/
docker rm "$CONTAINER_ID"
