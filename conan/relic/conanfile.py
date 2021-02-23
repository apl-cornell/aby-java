import glob
import os
from conans import ConanFile, CMake, tools


class RelicConan(ConanFile):
    name = "relic"
    license = ("Apache-2.0", "LGPL-2.1")
    description = "RELIC is a modern cryptographic meta-toolkit with emphasis on efficiency and flexibility."
    topics = ("conan", "security")
    homepage = "https://github.com/relic-toolkit/relic"
    url = "https://github.com/conan-io/conan-center-index"
    settings = "os", "compiler", "build_type", "arch"
    options = {
        "shared": [True, False],
        "fPIC": [True, False]
    }
    default_options = {
        "shared": False,
        "fPIC": True
    }
    generators = "cmake"
    exports_sources = ["CMakeLists.txt", "patches/*"]

    @property
    def _source_subfolder(self):
        return "source_subfolder"

    @property
    def _build_subfolder(self):
        return "build_subfolder"

    def config_options(self):
        if self.settings.os == "Windows":
            del self.options.fPIC

    def configure(self):
        del self.settings.compiler.libcxx
        del self.settings.compiler.cppstd

    def source(self):
        tools.get(f"https://github.com/relic-toolkit/relic/archive/{self.version}.tar.gz")
        extracted_dir = glob.glob(f"{self.name}-*")[0]
        os.rename(extracted_dir, self._source_subfolder)

    def _configure_cmake(self):
        if not hasattr(self, "_cmake"):
            cmake = CMake(self)

            cmake.definitions["DOCUM"] = False
            if self.options.shared:
                cmake.definitions["SHLIB"] = True
                cmake.definitions["STLIB"] = False
            else:
                cmake.definitions["SHLIB"] = False
                cmake.definitions["STLIB"] = True

            cmake.definitions["TESTS"] = 0
            cmake.definitions["BENCH"] = 0

            cmake.configure(build_folder=self._build_subfolder)
            self._cmake = cmake
        return self._cmake

    def build(self):
        cmake = self._configure_cmake()
        cmake.build()

    def package(self):
        self.copy("LICENSE*", dst="licenses", src=self._source_subfolder)
        cmake = self._configure_cmake()
        cmake.install()
        tools.rmdir(os.path.join(self.package_folder, "cmake"))

    def package_info(self):
        self.cpp_info.names["cmake_find_package"] = "RELIC"
        self.cpp_info.names["cmake_find_package_multi"] = "RELIC"
        self.cpp_info.includedirs = [
            os.path.join("include", self.name),
            os.path.join("include", self.name, "low"),
        ]
        self.cpp_info.libs = tools.collect_libs(self)
