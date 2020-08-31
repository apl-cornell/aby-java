#!/usr/bin/env sh

# Generates Java wrappers using SWIG

set -e

# shellcheck source=./variables.sh
. "$(dirname "$0")"/variables.sh

echo "ABY Group: $ABY_GROUP"

PACKAGE=$ABY_GROUP.aby
OUTPUT_DIR=src/main/java/$(echo "$PACKAGE" | tr -s "." "/")

mkdir -p "$OUTPUT_DIR"
swig -Wall -Werror -macroerrors \
    -c++ \
    -java -package "$PACKAGE" \
    -I"$ABY_DIR/src" -I"$ABY_DIR/extern/ENCRYPTO_utils/src" \
    -outdir "$OUTPUT_DIR" -cppext cpp \
    aby.i
