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

WORKDIR /work

## Download library
COPY get.mk .
RUN make -f get.mk

## Generate the Java interface
COPY swig.mk lib.patch lib.i ./
RUN make -f swig.mk



# Build for Linux
FROM dockcross/manylinux2014-x64 as linux
CMD ["/bin/bash"]

## Configure Conan
ENV PATH="/opt/python/cp35-cp35m/bin:${PATH}"
RUN conan profile new --detect default \
    && conan profile update settings.compiler.libcxx=libstdc++11 default

## Install dependencies
COPY conanfile.* .
RUN conan install . --install-folder=build/cmake --build=missing

## Copy source code
COPY --from=swig /work .

## Build
COPY build.mk CMakeLists.txt lib.cmake ./
COPY jni jni
RUN make -f build.mk


# Build for Windows
FROM dockcross/windows-static-x64-posix as windows


# Build for macOS
FROM cacay/crossbuild as macos

## Update CMake

## Install Conan
RUN wget --quiet https://dl.bintray.com/conan/installers/conan-ubuntu-64_1_31_4.deb -O conan.deb \
    && dpkg -i conan.deb \
    && rm conan.deb
COPY profiles/conan.x86_64-apple-darwin14 /root/.conan/profiles/default

WORKDIR /work
ENV CROSS_TRIPLE=x86_64-apple-darwin

## Install dependencies
COPY conanfile.* .
RUN conan install . --install-folder=build/cmake --build=missing

## Copy source code
COPY --from=swig /work .

## Build
COPY build.mk CMakeLists.txt lib.cmake ./
COPY jni jni
# RUN crossbuild make -f build.mk

