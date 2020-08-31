#!/usr/bin/env sh

# Downloads the ABY source code from GitHub

set -e

# shellcheck source=./variables.sh
. "$(dirname "$0")"/variables.sh

echo "ABY Version: $ABY_VERSION"

# Clone the ABY Git repository
mkdir -p ABY && cd ABY
git init
git remote add origin https://github.com/encryptogroup/ABY
git fetch --quiet --depth 1 origin "$ABY_VERSION"
git checkout --quiet "$ABY_VERSION"
git submodule update --init --recursive --depth 1 2> /dev/null
