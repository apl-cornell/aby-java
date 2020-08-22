#!/usr/bin/env sh

set -e

# TODO: remove, since these only apply to MacOS
OPENSSL_ROOT_DIR=$(brew --prefix openssl)
export OPENSSL_ROOT_DIR

cd ABY
cmake -B build
cmake --build build
cmake --install build --prefix "$(pwd)/build/install"
