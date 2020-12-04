package edu.cornell.cs.apl.nativetools.templates

internal val swigDockerfile = Template("swig.dockerfile") {
    """
    # Generate the Java interface using SWIG
    FROM ubuntu:20.04 AS swig

    ## Install dependencies
    RUN apt-get update && apt-get install -y --no-install-recommends \
        ca-certificates \
        git \
        make \
        rsync \
        swig \
        && rm -rf /var/lib/apt/lists/*

    WORKDIR $dockerWorkDirectory

    ## Download library
    COPY ${getMakefile.name} .
    RUN make -f ${getMakefile.name}

    ## Generate the Java interface
    COPY ${swigMakefile.name} $patchFile $swigFile ./
    RUN make -f ${swigMakefile.name}
    """
}

internal val linuxDockerfile = Platform.LINUX_64.let { platform ->
    Template("${platform.safeName}.dockerfile") {
        """
    ${swigDockerfile.include(this)}

    # Build for Linux
    FROM dockcross/manylinux2014-x64 as ${platform.safeName}
    CMD ["/bin/bash"]

    ## Configure Conan
    ENV PATH="/opt/python/cp35-cp35m/bin:${'$'}{PATH}"
    RUN conan profile new --detect default \
        && conan profile update settings.compiler.libcxx=libstdc++11 default

    WORKDIR $dockerWorkDirectory

    ## Install dependencies
    COPY conanfile.* .
    RUN conan install . --install-folder=$cmakeBuildDirectory --build=missing

    ${build.include(this)}
    """
    }
}

// TODO: do we need to worry about MACOSX_DEPLOYMENT_TARGET?
internal val macosDockerfile = Platform.MACOS_64.let { platform ->
    Template("${platform.safeName}.dockerfile") {
        """
    ${swigDockerfile.include(this)}

    # Build for macOS
    FROM liushuyu/osxcross as ${platform.safeName}

    ## Install dependencies
    RUN apt-get update && apt-get install -y --no-install-recommends \
        gettext-base \
        m4 \
        && rm -rf /var/lib/apt/lists/*

    ## Install Conan
    RUN wget --quiet https://dl.bintray.com/conan/installers/conan-ubuntu-64_1_31_4.deb -O conan.deb \
        && dpkg -i conan.deb \
        && rm conan.deb

    ## Set environment variables
    SHELL ["/bin/sh", "-l", "-c"]
    ARG OSXCROSS_ENV=/root/.profile
    RUN osxcross-conf > ${'$'}OSXCROSS_ENV
    RUN echo "export CROSS_TRIPLE=x86_64-apple-${'$'}OSXCROSS_TARGET" >> ${'$'}OSXCROSS_ENV
    RUN echo "export CROSS_TOOLCHAIN=${'$'}OSXCROSS_CCTOOLS_PATH/${'$'}CROSS_TRIPLE" >> ${'$'}OSXCROSS_ENV
    RUN echo ". ${'$'}OSXCROSS_ENV" > /root/.bashrc

    ## Configure Conan
    ARG CONAN_PROFILE=/root/.conan/profiles/default
    COPY profiles/x86_64-apple-darwin.conan ${'$'}CONAN_PROFILE-source
    RUN envsubst < ${'$'}CONAN_PROFILE-source > ${'$'}CONAN_PROFILE \
        && rm ${'$'}CONAN_PROFILE-source

    ## Configure CMake
    COPY profiles/x86_64-apple-darwin.cmake /root/Toolchain.cmake
    ENV CMAKE_TOOLCHAIN_FILE=/root/Toolchain.cmake

    WORKDIR $dockerWorkDirectory
    # ENV MACOSX_DEPLOYMENT_TARGET=10.6

    ## Install dependencies
    COPY conanfile.* .
    RUN conan install . --install-folder=$cmakeBuildDirectory --build=b2 --build=missing

    ${build.include(this)}
    """
    }
}

private val build = Template("build") {
    """
    ## Copy source code
    COPY --from=swig $dockerWorkDirectory/$patchedSourceDirectory/ $patchedSourceDirectory

    ## Build the base library using a dummy wrapper
    COPY ${buildMakefile.name} ${cmakeLists.name} $cmakeFile ./
    RUN mkdir -p $(dirname $swigGeneratedCppFile) && touch $swigGeneratedCppFile
    RUN make -f ${buildMakefile.name}

    ## Build the wrapper
    COPY $jniDirectory $jniDirectory
    COPY --from=swig $dockerWorkDirectory/$swigGeneratedCppFile $swigGeneratedCppFile
    RUN touch $swigGeneratedCppFile
    RUN make -f ${buildMakefile.name}
    """
}
