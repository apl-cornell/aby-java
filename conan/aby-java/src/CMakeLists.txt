cmake_minimum_required(VERSION 3.1)
project(abyjava LANGUAGES CXX)

include(${CMAKE_SOURCE_DIR}/../conanbuildinfo.cmake)
conan_basic_setup()

add_library(${PROJECT_NAME} SHARED wrapper.cpp)
target_include_directories(${PROJECT_NAME} PRIVATE ${CONAN_INCLUDE_DIRS})
target_link_libraries(${PROJECT_NAME} PRIVATE ${CONAN_LIBS})
set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_STANDARD 11)

# Statically link the C++ standard library.
if(UNIX AND NOT APPLE)
    target_link_libraries(${PROJECT_NAME} PRIVATE -static-libstdc++)
endif()

install(TARGETS ${PROJECT_NAME})
