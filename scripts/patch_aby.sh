#!/usr/bin/env sh

# Applies a custom patch to ABY that:
#   - Removes C++ attributes from (some) header files since SWIG doesn't support them yet
#   - Stops ABY/abycore from trying to link against stdc++fs when using Clang
#   - Fixes the ABY/ENCRYPTO_utils/relic CMake file so it exports itself as a library
#   - Fixes the ABY/ENCRYPTO_utils CMake file so it imports Relic as a library

set -e

# shellcheck source=./variables.sh
. "$(dirname "$0")"/variables.sh

PATCH_FILE=$(cd "$DIR/.." && pwd -P)/ABY.patch
echo "Applying patch: $PATCH_FILE"
(cd "$ABY_DIR" && git apply "$PATCH_FILE")
