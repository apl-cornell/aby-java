#!/usr/bin/env sh

# Generates a dynamic library by linking ABY with the wrapper code

set -e

# shellcheck source=./variables.sh
. "$(dirname "$0")"/variables.sh

JAVA_HOME=$(java -XshowSettings:properties -version 2>&1 > /dev/null | grep 'java.home' | awk '{print $3}')
echo "JAVA_HOME: $JAVA_HOME"

# Compile the wrapper
g++ -std=c++11 \
    -I"$HEADERS_DIR" -I"$JAVA_HOME"/include -I"$JAVA_HOME"/include/darwin \
    -c aby_wrap.cpp

# Link everything into a dynamic library
