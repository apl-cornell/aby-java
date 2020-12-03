package edu.cornell.cs.apl.nativetools.templates

// TODO: stop CMake from picking up system Boost
//   https://cmake.org/cmake/help/git-stage/variable/CMAKE_FIND_PACKAGE_PREFER_CONFIG.html
//   https://docs.conan.io/en/latest/integrations/build_system/cmake/cmake_paths_generator.html
// TODO: silence CMake complaining about too new Boost
internal val cmakeLists = Template("CMakeLists.txt") {
    """
    cmake_minimum_required(VERSION 3.12)
    project($cmakeWrapperProjectName LANGUAGES CXX)

    set(CMAKE_PREFIX_PATH $cmakeSourceInstallDirectory ${'$'}{CMAKE_PREFIX_PATH})

    add_library($sharedLibraryName SHARED
        $swigGeneratedCppFile
    )

    include($cmakeFile)

    # Add Java Native Interface header files.
    set(JAVA_INCLUDE_PATH $jniDirectory)
    if(WIN32)
        set(JAVA_INCLUDE_PATH2 $jniDirectory/windows)
    else()
        set(JAVA_INCLUDE_PATH2 $jniDirectory/unix)
    endif()
    set(JNI_INCLUDE_DIRS ${'$'}{JAVA_INCLUDE_PATH} ${'$'}{JAVA_INCLUDE_PATH2})

    target_include_directories($sharedLibraryName
        PRIVATE ${'$'}{JNI_INCLUDE_DIRS}
    )

    # Statically link the C++ standard library.
    if(UNIX AND NOT APPLE)
        target_link_libraries($sharedLibraryName PRIVATE -static-libstdc++)
    endif()

    # Install directory depends on the OS.
    if(WIN32)
        set(INSTALL_DIRECTORY ${nativeBinaryDirectory(Platform.WINDOWS_64)})
    elseif(APPLE)
        set(INSTALL_DIRECTORY ${nativeBinaryDirectory(Platform.MACOS_64)})
    elseif(UNIX)
        set(INSTALL_DIRECTORY ${nativeBinaryDirectory(Platform.LINUX_64)})
    endif()
    install(TARGETS $sharedLibraryName
        LIBRARY DESTINATION ${'$'}{INSTALL_DIRECTORY}
        RUNTIME DESTINATION ${'$'}{INSTALL_DIRECTORY}
    )
    """
}
