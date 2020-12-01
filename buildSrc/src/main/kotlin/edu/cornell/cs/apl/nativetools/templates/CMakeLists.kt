package edu.cornell.cs.apl.nativetools.templates

internal val cmakeLists = Template("CMakeLists.txt") {
    """
    cmake_minimum_required(VERSION 3.12)
    project(${library.name}Java LANGUAGES CXX)

    # Use Conan to resolve dependencies.
    include(${'$'}{CMAKE_BINARY_DIR}/conanbuildinfo.cmake)
    conan_basic_setup()

    # Generate relocatable code since we are statically linking.
    set(CMAKE_POSITION_INDEPENDENT_CODE ON)


    add_library($sharedLibraryName SHARED
        $swigGeneratedCppFile
    )

    include($cmakeFile)

    add_subdirectory($patchedSourceDirectory EXCLUDE_FROM_ALL)

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
        set(INSTALL_DIRECTORY $windowsBinaryDirectory)
    elseif(APPLE)
        set(INSTALL_DIRECTORY $macosBinaryDirectory)
    elseif(UNIX)
        set(INSTALL_DIRECTORY $linuxBinaryDirectory)
    endif()
    install(TARGETS $sharedLibraryName
        LIBRARY DESTINATION ${'$'}{INSTALL_DIRECTORY}
        RUNTIME DESTINATION ${'$'}{INSTALL_DIRECTORY}
    )
    """
}
