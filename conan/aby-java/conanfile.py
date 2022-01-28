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

    def build(self):
        cmake = self._configure_cmake()
        cmake.build()

    def package(self):
        cmake = self._configure_cmake()
        cmake.install()

    def package_info(self):
        self.cpp_info.libs = tools.collect_libs(self)
