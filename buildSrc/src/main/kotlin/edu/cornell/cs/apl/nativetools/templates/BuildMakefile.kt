package edu.cornell.cs.apl.nativetools.templates

/** Makefile for building the library. */
internal val buildMakefile = Makefile("build.mk") {
    """
    CMAKE_FLAGS := -Wno-dev -DCMAKE_TOOLCHAIN_FILE=$(CMAKE_TOOLCHAIN_FILE)
    CONAN_PATHS_FILE := $cmakeSourceBuildDirectory/conan_paths.cmake

    .PHONY: build-wrapper
    build-wrapper: $swigGeneratedCppFile $cmakeSourceInstallDirectory
        cmake $(CMAKE_FLAGS) \
            -DCMAKE_PROJECT_${cmakeWrapperProjectName}_INCLUDE=$(abspath $(CONAN_PATHS_FILE)) \
            -DCMAKE_INSTALL_PREFIX=. -B $cmakeWrapperBuildDirectory
        cmake --build $cmakeWrapperBuildDirectory --target install/strip

    $cmakeSourceInstallDirectory: $patchedSourceDirectory $(CONAN_PATHS_FILE)
        rm -rf $@
        cmake $(CMAKE_FLAGS) \
            -DCMAKE_PROJECT_${library.name}_INCLUDE=$(abspath $(CONAN_PATHS_FILE)) \
            -DCMAKE_INSTALL_PREFIX=$@ -S $< -B $cmakeSourceBuildDirectory
        cmake --build $cmakeSourceBuildDirectory --target install
        touch $@

    $(CONAN_PATHS_FILE): conanfile.*
        conan install . --generator=cmake_paths --install-folder=$cmakeSourceBuildDirectory --build=missing
        touch $@

    -include ${swigMakefile.name}
    """
}
