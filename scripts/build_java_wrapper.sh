#!/usr/bin/env sh

# Generates a dynamic library by linking ABY with the wrapper code

set -e

if command -v brew > /dev/null
then
    # TODO: remove, since these only apply to MacOS
    OPENSSL_ROOT_DIR=$(brew --prefix openssl)
    export OPENSSL_ROOT_DIR
fi

BUILD_DIR=build/cmake
cmake -B $BUILD_DIR -Wno-dev
cmake --build $BUILD_DIR
cmake --install $BUILD_DIR --prefix $BUILD_DIR/install
