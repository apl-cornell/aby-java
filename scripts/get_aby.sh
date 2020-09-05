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
git submodule update --init --depth 1 2> /dev/null
(cd extern/ENCRYPTO_utils && git submodule update --init --depth 1 extern/relic 2> /dev/null)
git submodule foreach --recursive git reset --quiet --hard
