package edu.cornell.cs.apl.nativetools.templates

/** Makefile for building the library. */
internal val buildMakefile = Makefile("build.mk") {
    """
    .PHONY: build
    build: $patchedSourceDirectory $swigGeneratedCppFile
        conan install . --install-folder=$cmakeBuildDirectory --build=missing
        cmake -Wno-dev -DCMAKE_TOOLCHAIN_FILE=${'$'}(CMAKE_TOOLCHAIN_FILE) -DCMAKE_INSTALL_PREFIX=. -B $cmakeBuildDirectory
        cmake --build $cmakeBuildDirectory --target install/strip

    include ${swigMakefile.name}
    """
}
