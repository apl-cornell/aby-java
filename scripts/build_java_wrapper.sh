#!/usr/bin/env sh

# Generates a dynamic library by linking ABY with the wrapper code

set -e

BUILD_DIR=build/cmake
INSTALL_DIR=$BUILD_DIR/install
mkdir -p $INSTALL_DIR

cmake -B $BUILD_DIR -Wno-dev
cmake --build $BUILD_DIR
cmake --install $BUILD_DIR --prefix $INSTALL_DIR
