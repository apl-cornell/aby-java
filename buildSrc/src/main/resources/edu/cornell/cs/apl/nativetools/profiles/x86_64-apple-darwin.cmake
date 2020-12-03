set(CMAKE_SYSTEM_NAME Darwin)
set(CMAKE_SYSTEM_PROCESSOR x86_64)

macro(osxcross_getconf VAR)
  if(NOT ${VAR})
    set(${VAR} "$ENV{${VAR}}")
    if(${VAR})
      set(${VAR} "${${VAR}}" CACHE STRING "${VAR}")
      message(STATUS "Found ${VAR}: ${${VAR}}")
    else()
      message(FATAL_ERROR "Cannot determine \"${VAR}\"")
    endif()
  endif()
endmacro()

osxcross_getconf(CROSS_TOOLCHAIN)
osxcross_getconf(OSXCROSS_TARGET_DIR)
osxcross_getconf(OSXCROSS_SDK)

set(CMAKE_AR ${CROSS_TOOLCHAIN}-ar)
set(CMAKE_C_COMPILER ${CROSS_TOOLCHAIN}-cc)
set(CMAKE_CXX_COMPILER ${CROSS_TOOLCHAIN}-c++)
set(CMAKE_ASM_COMPILER ${CMAKE_C_COMPILER})
set(CMAKE_RANLIB ${CROSS_TOOLCHAIN}-ranlib)
set(CMAKE_INSTALL_NAME_TOOL ${CROSS_TOOLCHAIN}-install_name_tool)

set(CMAKE_FIND_ROOT_PATH
  "${OSXCROSS_SDK}"
  "${OSXCROSS_TARGET_DIR}/macports/pkgs/opt/local")

# Search for programs in the build host directories.
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
# Search for libraries and headers in the target directories.
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY BOTH)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE BOTH)
set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE BOTH)
