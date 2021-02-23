package edu.cornell.cs.apl.nativetools.templates

/** Makefile for downloading and installing the dependencies. */
internal val dependenciesMakefile = Makefile("dependencies.mk") {
    """
    CONAN_PATHS_FILE := $cmakeBuildDirectory/conan_paths.cmake

    CONAN_HAS_HOST_PROFILE := $(shell conan profile show $(CROSS_TRIPLE))
    ifdef CONAN_HAS_HOST_PROFILE
    CONAN_PROFILE_FLAGS := --profile:build=default --profile:host=$(CROSS_TRIPLE)
    else
    CONAN_PROFILE_FLAGS :=
    endif

    $(CONAN_PATHS_FILE): conanfile.*
        conan install . $(CONAN_PROFILE_FLAGS) --build=missing \
            --generator=cmake_paths --install-folder=$cmakeBuildDirectory
        touch $@
    """
}
