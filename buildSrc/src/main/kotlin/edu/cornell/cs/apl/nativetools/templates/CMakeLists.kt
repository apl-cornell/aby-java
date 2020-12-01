package edu.cornell.cs.apl.nativetools.templates

internal val cmakeLists = Template("CMakeLists.txt") {
    """
    cmake_minimum_required(VERSION 3.12)
    project(${library.name}Java LANGUAGES CXX)

    # Use Conan to resolve dependencies.
    include(${'$'}{CMAKE_BINARY_DIR}/conanbuildinfo.cmake)
    conan_basic_setup()

    # Link dependencies statically to generate a self contained binary.
    # if(WIN32)
    #     list(INSERT CMAKE_FIND_LIBRARY_SUFFIXES 0 .lib .a)
    # else()
    #     set(CMAKE_FIND_LIBRARY_SUFFIXES .a)
    # endif()

    # Generate relocatable code since we are statically linking.
    set(CMAKE_POSITION_INDEPENDENT_CODE ON)


    add_library(${sharedLibraryName} SHARED
        $swigGeneratedCppFile
    )

    include($cmakeFile)

    add_subdirectory(${patchedSourceDirectory})

    # Add Java Native Interface header files.
    set(JAVA_INCLUDE_PATH $jniDirectory)
    if(WIN32)
        set(JAVA_INCLUDE_PATH2 $jniDirectory/windows)
    else()
        set(JAVA_INCLUDE_PATH2 $jniDirectory/unix)
    endif()
    set(JNI_INCLUDE_DIRS ${'$'}{JAVA_INCLUDE_PATH} ${'$'}{JAVA_INCLUDE_PATH2})

    target_include_directories(${sharedLibraryName}
        PRIVATE ${'$'}{JNI_INCLUDE_DIRS}
    )

    install(TARGETS $sharedLibraryName
        EXPORT "${'$'}{PROJECT_NAME}Targets"
        ARCHIVE DESTINATION lib
        LIBRARY DESTINATION lib
        INCLUDES DESTINATION lib
    )
    """
}
