#!/usr/bin/env sh

# Downloads the ABY source code from GitHub

set -e

# shellcheck source=./variables.sh
. "$(dirname "$0")"/variables.sh

ABY_URL=https://github.com/encryptogroup/ABY
echo "ABY Version: $ABY_VERSION"

# Clone the ABY Git repository
mkdir -p "$ABY_DIR"
cd "$ABY_DIR"
git init
git fetch --quiet --depth 1 $ABY_URL "$ABY_VERSION"
git checkout --quiet "$ABY_VERSION"
git reset --quiet --hard "$ABY_VERSION"
git submodule update --init --recursive --depth 1 2> /dev/null

# Remove C++ attributes since SWIG doesn't support them yet
FILE=src/abycore/circuit/circuit.h
echo "Removing C++ attributes in $FILE"
sed -i.bak 's/\[\[maybe_unused\]\]//g' "$ABY_DIR/$FILE" && rm "$ABY_DIR/$FILE.bak"
