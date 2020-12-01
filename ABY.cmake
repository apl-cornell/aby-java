find_package(Boost 1.74.0 EXACT REQUIRED COMPONENTS filesystem thread system)

target_link_libraries(abyjava
    PRIVATE ABY::aby
)
