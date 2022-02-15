package io.github.apl_cornell.nativetools.templates

/** Makefile for building the library. */
internal val buildMakefile = Makefile("build.mk") {
    """
    CMAKE_FLAGS := -Wno-dev -DCMAKE_TOOLCHAIN_FILE=$(CMAKE_TOOLCHAIN_FILE)

    .PHONY: build
    build: $patchedSourceDirectory $swigGeneratedCppFile $(CONAN_PATHS_FILE)
        cmake $(CMAKE_FLAGS) -DCMAKE_INSTALL_PREFIX=. -B $cmakeBuildDirectory
        cmake --build $cmakeBuildDirectory --target install/strip

    -include ${swigMakefile.name}
    -include ${dependenciesMakefile.name}
    """
}
