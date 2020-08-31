#!/usr/bin/env sh

# Generates a dynamic library by linking ABY with the wrapper code

set -e

# TODO: remove, since these only apply to MacOS
OPENSSL_ROOT_DIR=$(brew --prefix openssl)
export OPENSSL_ROOT_DIR

BUILD_DIR=build/cmake
cmake -B $BUILD_DIR -Wno-dev
cmake --build $BUILD_DIR
cmake --install $BUILD_DIR --prefix $BUILD_DIR/install
