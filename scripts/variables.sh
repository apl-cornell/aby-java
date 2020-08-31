# Directory containing this file
DIR=$(cd "$(dirname "$0")" && pwd -P)

GRADLE_PROPERTIES=$DIR/../gradle.properties
ABY_VERSION=$(grep abyVersion < "$GRADLE_PROPERTIES" | cut -d'=' -f2)
ABY_GROUP=$(grep abyGroup < "$GRADLE_PROPERTIES" | cut -d'=' -f2)

INSTALL_DIR=$(pwd -P)/ABY/build/install
HEADERS_DIR=$INSTALL_DIR/include

export ABY_VERSION ABY_GROUP INSTALL_DIR HEADERS_DIR
