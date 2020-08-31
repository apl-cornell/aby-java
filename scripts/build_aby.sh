#!/usr/bin/env sh

set -e

# shellcheck source=./variables.sh
. "$(dirname "$0")"/variables.sh

# TODO: remove, since these only apply to MacOS
OPENSSL_ROOT_DIR=$(brew --prefix openssl)
export OPENSSL_ROOT_DIR

cd ABY
cmake -B build
cmake --build build
cmake --install build --prefix "$INSTALL_DIR"

# Remove C++ attributes since SWIG doesn't support them yet
sed -i.bak 's/\[\[maybe_unused\]\]//g' "$INSTALL_DIR/include/abycore/circuit/circuit.h"
