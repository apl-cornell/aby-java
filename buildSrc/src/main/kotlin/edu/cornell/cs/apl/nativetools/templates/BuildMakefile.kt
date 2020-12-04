package edu.cornell.cs.apl.nativetools.templates

/** Makefile for building the library. */
internal val buildMakefile = Makefile("build.mk") {
    """
    CMAKE_FLAGS := -Wno-dev -DCMAKE_TOOLCHAIN_FILE=$(CMAKE_TOOLCHAIN_FILE)
    CONAN_PATHS_FILE := $cmakeBuildDirectory/conan_paths.cmake

    .PHONY: build
    build: $patchedSourceDirectory $swigGeneratedCppFile $(CONAN_PATHS_FILE)
        cmake $(CMAKE_FLAGS) -DCMAKE_INSTALL_PREFIX=. -B $cmakeBuildDirectory
        cmake --build $cmakeBuildDirectory --target install/strip

    $(CONAN_PATHS_FILE): conanfile.*
        conan install . --generator=cmake_paths --install-folder=$cmakeBuildDirectory --build=missing
        touch $@

    -include ${swigMakefile.name}
    """
}
