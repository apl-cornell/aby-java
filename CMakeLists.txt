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

add_subdirectory(ABY)
find_package(JNI REQUIRED)

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
