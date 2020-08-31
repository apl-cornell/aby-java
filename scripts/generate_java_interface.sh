#!/usr/bin/env sh

# Generates Java wrappers using SWIG

set -e

HEADERS_DIR=ABY/build/install/include

ABY_GROUP=$(grep abyGroup < gradle.properties | cut -d'=' -f2)
echo "ABY Group: $ABY_GROUP"

PACKAGE=$ABY_GROUP.aby
OUTPUT_DIR=src/main/java/$(echo "$PACKAGE" | tr -s "." "/")

mkdir -p "$OUTPUT_DIR"
swig -Wall -Werror -macroerrors \
    -c++ \
    -java -package "$PACKAGE" \
    -I"$HEADERS_DIR" \
    -outdir "$OUTPUT_DIR" -cppext cpp \
    aby.i
