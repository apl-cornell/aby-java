# The directory containing this file
DIR=$(cd "$(dirname "$0")" && pwd -P)

GRADLE_PROPERTIES=$DIR/../gradle.properties
ABY_VERSION=$(grep abyVersion < "$GRADLE_PROPERTIES" | cut -d'=' -f2)
ABY_GROUP=$(grep abyGroup < "$GRADLE_PROPERTIES" | cut -d'=' -f2)

ABY_DIR=$(pwd -P)/ABY
INSTALL_DIR=$ABY_DIR/build/install

export ABY_VERSION ABY_GROUP ABY_DIR INSTALL_DIR
