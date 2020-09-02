cmake_minimum_required(VERSION 3.12)
project(AbyJava LANGUAGES CXX)

# Write built executables and libraries to bin/ and lib/, respectively.
if(NOT CMAKE_RUNTIME_OUTPUT_DIRECTORY)
    set(CMAKE_RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}/bin")
endif()
if(NOT CMAKE_LIBRARY_OUTPUT_DIRECTORY)
    set(CMAKE_LIBRARY_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}/lib")
endif()
if(NOT CMAKE_ARCHIVE_OUTPUT_DIRECTORY)
    set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}/lib")
endif()

# Prefer locally built dependencies over system dependencies.
set(CMAKE_PREFIX_PATH "${CMAKE_CURRENT_SOURCE_DIR}/external/install")

set(JAVA_AWT_LIBRARY NotNeeded)
set(JAVA_AWT_INCLUDE_PATH NotNeeded)
find_package(JNI REQUIRED)

# Link dependencies statically to generate a self contained binary.
set(Boost_USE_STATIC_LIBS ON)
set(OPENSSL_USE_STATIC_LIBS ON)
if(WIN32)
    list(INSERT CMAKE_FIND_LIBRARY_SUFFIXES 0 .lib .a)
else()
    set(CMAKE_FIND_LIBRARY_SUFFIXES .a)
endif()

# Generate relocatable code since we are statically linking.
set(CMAKE_POSITION_INDEPENDENT_CODE ON)


add_subdirectory(ABY)


add_library(abyjava SHARED
    src/main/cpp/aby_wrap.cpp
)

target_include_directories(abyjava
    PRIVATE ${JNI_INCLUDE_DIRS}
)

target_link_libraries(abyjava
    PRIVATE ABY::aby
)

install(TARGETS abyjava
    EXPORT "${PROJECT_NAME}Targets"
    ARCHIVE DESTINATION lib
    LIBRARY DESTINATION lib
    INCLUDES DESTINATION lib
)
