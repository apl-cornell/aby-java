package edu.cornell.cs.apl.nativetools.templates

/** Makefile for building the library. */
internal val buildMakefile = Makefile("build.mk") {
    """
    .PHONY: build
    build: $patchedSourceDirectory $swigGeneratedCppFile
        conan install . --install-folder=$cmakeBuildDirectory --build=missing
        cmake -Wno-dev -DCMAKE_INSTALL_PREFIX=$nativeBinaryBaseDirectory -B $cmakeBuildDirectory
        # cmake --build $cmakeBuildDirectory
        cmake --build $cmakeBuildDirectory --target install

    include ${swigMakefile.name}
    """
}
