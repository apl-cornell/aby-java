import os

from conans import ConanFile, CMake, tools


class AbyJavaConan(ConanFile):
    name = "aby-java"
    license = "LGPL-3.0"
    homepage = "https://github.com/apl-cornell/aby-java"
    url = "https://github.com/conan-io/conan-center-index"
    description = "Java bindings for the ABY Framework."
    topics = ("conan", "security")
    settings = "os", "compiler", "build_type", "arch"
    generators = "cmake"
    exports_sources = ["src/*"]
    tool_requires = "swig/4.0.2"

    @property
    def _build_subfolder(self):
        return "build_subfolder"

    def requirements(self):
        self.requires(f"aby/{self.version}")
        self.requires("jni/jdk-11+28")

    def _configure_cmake(self):
        if not hasattr(self, "_cmake"):
            cmake = CMake(self)
            cmake.configure(source_folder="src", build_folder=self._build_subfolder)
            self._cmake = cmake
        return self._cmake

    def _swig(self):
        """Generate C++ wrapper and Java classes."""
        options = "-Wall -Werror -macroerrors"
        includes = " ".join([f"-I{path}" for path in self.deps_cpp_info['aby'].include_paths])
        output = f"-o {self._build_subfolder}/wrapper.cpp -outdir {self._build_subfolder}/java"
        tools.mkdir(os.path.join(self._build_subfolder, "java"))
        self.run(f"swig {options} -c++ -java {includes} {output} src/ABY.i")

    def build(self):
        # Fix Swig library directory on MacOS.
        if self.settings.os == "Macos":
            swig_lib = os.path.join(self.deps_cpp_info["swig"].bin_paths[0], "swiglib")
            with tools.environment_append({"SWIG_LIB": swig_lib}):
                self._swig()
        else:
            self._swig()

        cmake = self._configure_cmake()
        cmake.build()

    def package(self):
        self.copy("*", dst="licenses", src=os.path.join(self.deps_cpp_info["aby"].rootpath, "licenses"))
        cmake = self._configure_cmake()
        cmake.install()

    def package_info(self):
        self.cpp_info.libs = tools.collect_libs(self)
