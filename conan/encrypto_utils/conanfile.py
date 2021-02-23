import glob
import os
from conans import ConanFile, CMake, tools


class EncryptoUtilsConan(ConanFile):
    name = "encrypto_utils"
    license = "LGPL-3.0"
    description = "Crypto and networking utils used for ABY and OTExtension."
    topics = ("conan", "security")
    homepage = "https://github.com/encryptogroup/ENCRYPTO_utils"
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

    def requirements(self):
        self.requires("boost/1.72.0")
        self.requires("gmp/6.2.1")
        self.requires("openssl/1.1.1j")
        self.requires("relic/c25edf6520e5e4b39663f0d0594db7186440be5c")

    def source(self):
        tools.get(f"https://github.com/encryptogroup/ENCRYPTO_utils/archive/{self.version}.tar.gz")
        extracted_dir = glob.glob(f"ENCRYPTO_utils-*")[0]
        os.rename(extracted_dir, self._source_subfolder)

    def _configure_cmake(self):
        if not hasattr(self, "_cmake"):
            cmake = CMake(self)
            cmake.configure(build_folder=self._build_subfolder)
            self._cmake = cmake
        return self._cmake

    def build(self):
        cmake = self._configure_cmake()
        cmake.build()

    def package(self):
        self.copy("LICENSE", dst="licenses", src=self._source_subfolder)
        cmake = self._configure_cmake()
        cmake.install()

    def package_info(self):
        self.cpp_info.names["cmake_find_package"] = "ENCRYPTO_utils"
        self.cpp_info.names["cmake_find_package_multi"] = "ENCRYPTO_utils"
        self.cpp_info.libs = tools.collect_libs(self)
