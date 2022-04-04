cmake_minimum_required(VERSION 3.1)
project(abyjava LANGUAGES CXX)

find_package(ABY REQUIRED)
add_library(RELIC::relic STATIC IMPORTED)
set_target_properties(RELIC::relic PROPERTIES
    IMPORTED_LOCATION "${ABY_CMAKE_DIR}/../../librelic_s.a"
)
#target_link_libraries(RELIC::relic INTERFACE relic_s)

add_library(${PROJECT_NAME} SHARED wrapper.cpp)
target_link_libraries(${PROJECT_NAME} PRIVATE ABY::aby)
set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_STANDARD 11)

# Statically link the C++ standard library.
if(UNIX AND NOT APPLE)
    target_link_libraries(${PROJECT_NAME} PRIVATE -static-libstdc++)
endif()

install(TARGETS ${PROJECT_NAME})
