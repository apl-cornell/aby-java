#include "relic.h"

int main() {
   	/* Initialize library with default configuration. */
    int code = core_init() == RLC_OK ? 0 : 1;
    core_clean();
    return code;
}
