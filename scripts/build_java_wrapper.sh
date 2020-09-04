#!/usr/bin/env sh

# Generates a dynamic library by linking ABY with the wrapper code

set -e

BUILD_DIR=build/cmake
INSTALL_DIR=$BUILD_DIR/install
mkdir -p $INSTALL_DIR

cmake -Wno-dev -DCMAKE_INSTALL_PREFIX=$INSTALL_DIR -B $BUILD_DIR
cmake --build $BUILD_DIR
cmake --build $BUILD_DIR --target install
