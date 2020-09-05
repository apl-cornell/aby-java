#!/usr/bin/env sh

# Copies a file or a folder from a Docker image to the host.

set -e

# Build the Docker image and remember its ID.
# shellcheck disable=SC2086
IMAGE_ID=$(docker build $1 . | tee /dev/tty | tail -n1 | cut -d' ' -f3)

mkdir -p "$(dirname "$3")"

CONTAINER_ID=$(docker create "$IMAGE_ID")
docker cp "$CONTAINER_ID:$2" "$3"
docker rm "$CONTAINER_ID"
